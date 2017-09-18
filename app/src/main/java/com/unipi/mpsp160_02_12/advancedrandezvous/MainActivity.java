package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unipi.mpsp160_02_12.advancedrandezvous.Auth.DashBoard;

public class MainActivity extends AppCompatActivity {

    private Button manageEventsButton;
    private Button createEventButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createEventButton = (Button)findViewById(R.id.create_event_activity_button);
        createEventButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        manageEventsButton = (Button)findViewById(R.id.manage_events_button);
        manageEventsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ManageEventsActivity.class);
                startActivity(intent);
            }
        });

        settingsButton = (Button)findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DashBoard.class);
                startActivity(intent);
            }
        });
    }
}
