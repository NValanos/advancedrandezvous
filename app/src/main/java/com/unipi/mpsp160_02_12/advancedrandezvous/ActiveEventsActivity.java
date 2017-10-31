package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.ListActivity;
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
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Event;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.ActiveListAdapter;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Participant;


import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.content.ContentValues.TAG;

public class ActiveEventsActivity extends ListActivity {
    private ListView activeList;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Event event;

    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_events_list);


        activeList = (ListView)findViewById(list);

        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ref = databaseReference.child("events");
        Query activeEventsQuery = ref.orderByChild("active").equalTo(true);


        System.err.println("before listener");
        activeEventsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> activeEventList = new ArrayList<Event>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    event = singleSnapshot.getValue(Event.class);
                    System.err.println("after db read");

                    if (event != null){
                        List<Participant> partList;
                        partList = event.getParticipantsIdList();

                        for (Participant participant: partList){
                            if (participant.getId().equals(auth.getCurrentUser().getUid())){
                                activeEventList.add(event);
                                break;
                            }
                        }
                    }
                    else{
                        System.out.println("The EVENT IS NULL");
                    }
                }
                ActiveListAdapter listAdapter = new ActiveListAdapter(ActiveEventsActivity.this, android.R.layout.activity_list_item, activeEventList );
                activeList.setAdapter(listAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }

        });
    }
}