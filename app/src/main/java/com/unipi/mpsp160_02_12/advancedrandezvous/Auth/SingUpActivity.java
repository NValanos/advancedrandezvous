package com.unipi.mpsp160_02_12.advancedrandezvous.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unipi.mpsp160_02_12.advancedrandezvous.MainActivity;
import com.unipi.mpsp160_02_12.advancedrandezvous.R;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.User;

public class SingUpActivity extends AppCompatActivity {

    Button btnSignup;
    TextView btnLogin, btnForgotPass;
    EditText input_email, input_pass, input_username;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        //View
        input_username = (EditText)findViewById(R.id.signup_input_username);
        input_email = (EditText)findViewById(R.id.signup_input_email);
        input_pass = (EditText)findViewById(R.id.signup_input_password);

        btnSignup = (Button)findViewById(R.id.signup_btn_register);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(input_email.getText().toString())){
                    Toast.makeText(SingUpActivity.this, "PLEASE INSERT EMAIL ADDRESS", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(input_pass.getText().toString())){
                    Toast.makeText(SingUpActivity.this, "PLEASE INSERT PASSWORD ", Toast.LENGTH_SHORT).show();
                    return;
                }
                singUpUser(input_email.getText().toString(), input_pass.getText().toString());
            }
        });

        btnLogin = (TextView)findViewById(R.id.signup_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnForgotPass = (TextView)findViewById(R.id.signup_btn_forgot_password);
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingUpActivity.this, ForgotPassword.class));
                finish();
            }
        });
    }

    private void singUpUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Error: " + task.getException(), Toast.LENGTH_SHORT).show();

                        } else{
                            Toast.makeText(getApplicationContext(),"Register Success ", Toast.LENGTH_SHORT).show();
                            //Create User to Database
                            createNewUser(input_username.getText().toString(), input_email.getText().toString(), auth.getCurrentUser().getUid());
                            startActivity(new Intent(SingUpActivity.this, MainActivity.class));
                        }
                    }
                });
    }

    private void createNewUser(String username, String email, String userId){

        User user = new User(username, email, userId);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(userId).setValue(user);
    }
}
