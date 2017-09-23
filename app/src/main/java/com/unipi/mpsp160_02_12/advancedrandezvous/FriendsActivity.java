package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Friend;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FriendsActivity extends AppCompatActivity {

    private ListView list;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Friend friend;
    ArrayAdapter<String> mAdapter;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        loadList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

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

                        Friend friend = new Friend();
                        friend.setId(uid);
                        friend.setEmail(uemail);

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

    public void deleteFriend(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.friend);
        String task = String.valueOf(taskTextView.getText());


        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid()).child("friends");
        Query removeQuery = databaseReference.orderByChild("email").equalTo(task);

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
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    friend = singleSnapshot.getValue(Friend.class);

                    System.err.println("after db read");

                    if (friend != null){
                        friendList.add(friend.getEmail());
                        System.out.println("The FRIEND ADDED");
                    }
                    else{
                        System.out.println("The FRIEND IS NULL");
                    }
                }

                if(mAdapter == null){
                    mAdapter = new ArrayAdapter<String>(FriendsActivity.this ,R.layout.row, R.id.friend, friendList);
                    list.setAdapter(mAdapter);
                } else {
                    mAdapter.clear();
                    mAdapter.addAll(friendList);
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


