package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.unipi.mpsp160_02_12.advancedrandezvous.Auth.DashBoard;

public class MainActivity extends AppCompatActivity {

    private Button manageEventsButton;
    private Button createEventButton;
    private Button settingsButton;
    private Button btnfriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.amber));
        setSupportActionBar(myToolbar);

//        ActivityCompat.requestPermissions(this,
//                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},123);

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

//        settingsButton = (Button)findViewById(R.id.settings);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, DashBoard.class);
//                startActivity(intent);
//            }
//        });

        btnfriends = (Button)findViewById(R.id.friends_button);
        btnfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent  intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());

    }
}
