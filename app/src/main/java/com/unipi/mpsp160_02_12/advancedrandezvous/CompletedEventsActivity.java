package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.CompletedListAdapter;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Event;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.CompletedListAdapter;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;
import static android.content.ContentValues.TAG;

/**
 * Created by Nick on 9/9/2017.
 */

public class CompletedEventsActivity extends ListActivity {
    private ListView completedList;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Event event;

    private FirebaseAuth auth;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
            String language = languagepref.getString("languageToLoad", "novalue");
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            setContentView(R.layout.completed_events_list);

            completedList = (ListView)findViewById(list);

            auth = FirebaseAuth.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            ref = databaseReference.child("events");
            Query completedEventsQuery = ref.orderByChild("active").equalTo(false);


            System.err.println("before listener");
            completedEventsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Event> completedEventList = new ArrayList<Event>();
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        event = singleSnapshot.getValue(Event.class);
                        System.err.println("after db read");

                        if (event != null){
                            List<Participant> partList;
                            partList = event.getParticipantsIdList();

                            for (Participant participant: partList){
                                if (participant.getId().equals(auth.getCurrentUser().getUid())){
                                    completedEventList.add(event);
                                    break;
                                }
                            }
                        }
                        else{
                            System.out.println("The EVENT IS NULL");
                        }
                    }
                    CompletedListAdapter listAdapter = new CompletedListAdapter(CompletedEventsActivity.this, android.R.layout.activity_list_item, completedEventList );
                    completedList.setAdapter(listAdapter);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        }

}
