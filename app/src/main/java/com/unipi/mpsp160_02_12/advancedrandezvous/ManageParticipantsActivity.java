package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Participant;


import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Dimitris on 25/9/2017.
 */

public class ManageParticipantsActivity extends AppCompatActivity {

    private ListView list;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Participant participant;
    ArrayAdapter<String> mAdapter;
    private String eventId;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_participants_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.participants_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.amber));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.event_details);

        eventId = getIntent().getStringExtra("eventId");
        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu2, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_participant:
                createParticipant();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void createParticipant(){

        Intent intent = new Intent(ManageParticipantsActivity.this, FriendsActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        loadList();
    }

    private void loadList(){

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        list = (ListView)findViewById(R.id.participantlist);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ref = databaseReference.child("events").child(eventId).child("participantsIdList");
        Query friendsQuery = ref;

        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> participantList = new ArrayList<String>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    participant = singleSnapshot.getValue(Participant.class);

                    System.err.println("after db read");

                    if (participant != null){
                        participantList.add(participant.getName() + ":" + participant.getTag());
                        System.out.println("The FRIEND ADDED");
                    }
                    else{
                        System.out.println("The FRIEND IS NULL");
                    }
                }

                if(mAdapter == null){
                    mAdapter = new ArrayAdapter<String>(ManageParticipantsActivity.this ,R.layout.row2, R.id.participant, participantList);
                    list.setAdapter(mAdapter);
                } else {
                    mAdapter.clear();
                    mAdapter.addAll(participantList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }
}
