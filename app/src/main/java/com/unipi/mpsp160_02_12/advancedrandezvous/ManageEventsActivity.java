package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.google.firebase.database.DatabaseReference;

import java.util.Locale;

/**
 * Created by Nick on 9/9/2017.
 */

public class ManageEventsActivity extends TabActivity {

    private static final String ACTIVE_SPEC = "Active";
    private static final String COMPLETED_SPEC = "Completed";
    private DatabaseReference databaseReference;
    private DatabaseReference ref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
        String language = languagepref.getString("languageToLoad", "novalue");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.manage_events);

        TabHost tabHost = getTabHost();

        // Active Tab
        TabSpec activeSpec = tabHost.newTabSpec(getResources().getString(R.string.active_spec));
        // Tab Icon
        activeSpec.setIndicator(getResources().getString(R.string.active_spec));
        Intent activeEventsIntent = new Intent(this, ActiveEventsActivity.class);
        // Tab Content
        activeSpec.setContent(activeEventsIntent);

        // Cleared Tab
        TabSpec completedSpec = tabHost.newTabSpec(getResources().getString(R.string.completed_spec));
        completedSpec.setIndicator(getResources().getString(R.string.completed_spec));
        Intent completedEventsIntent = new Intent(this, CompletedEventsActivity.class);
        completedSpec.setContent(completedEventsIntent);


        // Adding all TabSpec to TabHost
        tabHost.addTab(activeSpec); // Adding Inbox tab
        tabHost.addTab(completedSpec); // Adding Outbox tab
    }

    @Override
    protected void onResume(){

        SharedPreferences languagepref = getSharedPreferences("language",MODE_PRIVATE);
        String language = languagepref.getString("languageToLoad", "novalue");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        super.onResume();

    }
}
