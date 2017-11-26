package com.unipi.mpsp160_02_12.advancedrandezvous.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
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
import com.google.firebase.auth.FirebaseAuth;
import com.unipi.mpsp160_02_12.advancedrandezvous.R;

import java.util.Locale;

public class ForgotPassword extends AppCompatActivity {

    private EditText input_email;
    private Button btnResetPass;
    private TextView btnBack;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
        String language = languagepref.getString("languageToLoad", "novalue");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        //View
        input_email = (EditText)findViewById(R.id.forgot_input_email);

        btnResetPass = (Button)findViewById(R.id.forgot_btn_reset);
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(input_email.getText().toString())){
                    Toast.makeText(ForgotPassword.this, R.string.forgotpass_toast_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                resetPassword(input_email.getText().toString());
            }
        });


        btnBack = (TextView)findViewById(R.id.forgot_btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), R.string.forgotpass_toast_email_success +email,Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.forgotpass_toast_email_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
