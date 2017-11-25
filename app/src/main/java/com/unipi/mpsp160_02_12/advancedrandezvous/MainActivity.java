package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.unipi.mpsp160_02_12.advancedrandezvous.Auth.DashBoard;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button manageEventsButton;
    private Button createEventButton;
    private Button btnfriends;
    private static final String TAG_RETAINED_FRAGMENT = "retained_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
        String language = languagepref.getString("languageToLoad", "novalue");
        System.out.println("language:" + language);
        Locale locale = null;
        if (language == null || "novalue".equals(language)){
            locale = Locale.getDefault();
            System.out.println("language: " + locale.getLanguage());
            SharedPreferences.Editor editor = languagepref.edit();
            editor.putString("languageToLoad", locale.getLanguage());
            editor.commit();
        }
        else{
            locale = new Locale(language);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

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
        Intent intent;
        switch (item.getItemId()) {

            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_account:
                intent = new Intent(this, DashBoard.class);
                startActivity(intent);
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
