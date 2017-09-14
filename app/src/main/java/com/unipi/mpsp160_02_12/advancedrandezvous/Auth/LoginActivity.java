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
import com.unipi.mpsp160_02_12.advancedrandezvous.MainActivity;
import com.unipi.mpsp160_02_12.advancedrandezvous.R;


public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText input_email, input_password;
    TextView btnSignup, btnForgotPass;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check already session, if ok-> Dashboard
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this, DashBoard.class));

        //View
        input_email = (EditText)findViewById(R.id.login_input_email);
        input_password = (EditText)findViewById(R.id.login_input_password);

        btnLogin = (Button)findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if ("".equals(input_email.getText().toString())){
                    Toast.makeText(LoginActivity.this, "PLEASE INSERT EMAIL ADDRESS", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(input_password.getText().toString())){
                    Toast.makeText(LoginActivity.this, "PLEASE INSERT PASSWORD ", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(input_email.getText().toString(), input_password.getText().toString());
            }
        });

        btnSignup = (TextView)findViewById(R.id.login_btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
                startActivity(intent);
            }
        });

        btnForgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


    }


    private void loginUser(String email, final String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            if (password.length() < 6){

                                Toast.makeText(getApplicationContext(),"Password length must be over 6",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(new Intent(LoginActivity.this, DashBoard.class));
                        }
                    }
                });
    }
}
