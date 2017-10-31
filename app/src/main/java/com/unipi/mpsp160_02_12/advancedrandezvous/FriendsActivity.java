package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Friend;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.FriendListAdapter;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Participant;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FriendsActivity extends AppCompatActivity {

    private ListView list;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Friend friend;
    FriendListAdapter mAdapter;
    private Button listBtn;
    private FirebaseAuth auth;
    private String eventId;
    private List<Friend> friendsArrayList = new ArrayList<>();
    public static Boolean isFromEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        myToolbar.setBackgroundColor(Color.parseColor("#000000"));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.event_details);
        listBtn = (Button)findViewById(R.id.btnListAction);
        loadList();
        if (getIntent().hasExtra("eventId")){
            eventId = getIntent().getStringExtra("eventId");
            isFromEvent = true;
        }
        else{
            isFromEvent = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu1, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_add_friend:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a New Friend")
                        .setMessage("Insert friend's email: ")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                //Add friend to Database
                                createFriend(task);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void createFriend(String email){

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Query usersQuery = databaseReference.orderByChild("email").equalTo(email);

        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null && dataSnapshot.getChildren().iterator().hasNext()){
                    //User exists to database
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        String uemail = userSnapshot.child("email").getValue().toString();
                        String uid = userSnapshot.child("id").getValue().toString();
                        String uname = userSnapshot.child("username").getValue().toString();

                        Friend friend = new Friend();
                        friend.setId(uid);
                        friend.setEmail(uemail);
                        friend.setName(uname);

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("friends").child(friend.getId()).setValue(friend);
                        Toast.makeText(FriendsActivity.this,"Friend added Successfully.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //User does not exist to database
                    Toast.makeText(FriendsActivity.this,"The user does not have the application", Toast.LENGTH_SHORT).show();
                }
                loadList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void chooseAction(View view){
        if (eventId == null){
            deleteFriend(view);
        }
        else{
            addParticipant(view);
        }


    }

    public void deleteFriend(View view){
        View parent = (View)view.getParent();
        Friend friendToDelete = friendsArrayList.get(list.getPositionForView(parent));


        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid()).child("friends");
        Query removeQuery = databaseReference.orderByChild("email").equalTo(friendToDelete.getEmail());

        removeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null && dataSnapshot.getChildren().iterator().hasNext()){

                    for (DataSnapshot removeSnapshot: dataSnapshot.getChildren()){
                        String fid = removeSnapshot.child("id").getValue().toString();
                        dataSnapshot.getRef().child(fid).removeValue();
                    }
                }
                loadList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void addParticipant(View view){
        View parent = (View)view.getParent();
        final Friend friendToParticipate = friendsArrayList.get(list.getPositionForView(parent));


        databaseReference = FirebaseDatabase.getInstance().getReference("events").child(eventId).child("participantsIdList");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                boolean duplicate = false;
                for(DataSnapshot participantSnapshot : dataSnapshot.getChildren()){
                    Participant partSnap = participantSnapshot.getValue(Participant.class);
                    if (partSnap.getId().equals(friendToParticipate.getId())){
                        duplicate = true;
                    }
                    count++;
                }
                if (duplicate){
                    Toast.makeText(FriendsActivity.this, "Already Participant", Toast.LENGTH_SHORT).show();
                }
                else{
                    Participant participant = new Participant();
                    participant.setId(friendToParticipate.getId());
                    participant.setName(friendToParticipate.getName());
                    participant.setTag("Pending");

                    databaseReference.child(String.valueOf(count)).setValue(participant);
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    private void loadList(){

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        list = (ListView)findViewById(R.id.friendlist);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ref = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("friends");
        Query friendsQuery = ref;

        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> friendList = new ArrayList<String>();
                friendsArrayList = new ArrayList<Friend>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    friend = singleSnapshot.getValue(Friend.class);

                    System.err.println("after db read");

                    if (friend != null){
                        friendList.add(friend.getEmail());
                        friendsArrayList.add(friend);
                        System.out.println("The FRIEND ADDED");
                    }
                    else{
                        System.out.println("The FRIEND IS NULL");
                    }
                }

                if(mAdapter == null){
                    mAdapter = new FriendListAdapter(FriendsActivity.this, R.layout.row, friendsArrayList);
                    list.setAdapter(mAdapter);

                } else {
                    mAdapter.clear();
                    mAdapter.addAll(friendsArrayList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}


