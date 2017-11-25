package com.unipi.mpsp160_02_12.advancedrandezvous.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.R;

public class DashBoard extends AppCompatActivity {

    private TextView txtWelcome;
    private EditText input_new_password;
    private Button btnChangePass, btnLogout;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //View
        txtWelcome = (TextView)findViewById(R.id.dashboard_welcome);
        input_new_password = (EditText)findViewById(R.id.dashboard_input_new_password);


        btnChangePass = (Button)findViewById(R.id.dashboard_btn_change_pass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(input_new_password.getText().toString())){
                    Toast.makeText(getApplicationContext(),R.string.insertPass, Toast.LENGTH_SHORT).show();
                    return;
                }
                changePassword(input_new_password.getText().toString());
            }
        });


        btnLogout = (Button)findViewById(R.id.dashboard_btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        //Session Check
        if (auth.getCurrentUser() != null){
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txtWelcome.setText(R.string.welcomeUser + "" +dataSnapshot.child(auth.getCurrentUser().getUid()).child("username").getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("onCancelled", databaseError.toException());
                }
            });
        }

    }

    private void logoutUser(){
        auth.signOut();
        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(DashBoard.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),R.string.pasChange,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
