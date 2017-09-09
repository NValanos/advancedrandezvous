package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Created by Nick on 9/9/2017.
 */

public class ManageEventsActivity extends TabActivity {

    private static final String ACTIVE_SPEC = "Active";
    private static final String COMPLETED_SPEC = "Completed";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_events);

        TabHost tabHost = getTabHost();

        // Inbox Tab
        TabSpec activeSpec = tabHost.newTabSpec(ACTIVE_SPEC);
        // Tab Icon
        activeSpec.setIndicator(ACTIVE_SPEC);
        Intent activeEventsIntent = new Intent(this, ActiveEventsActivity.class);
        // Tab Content
        activeSpec.setContent(activeEventsIntent);

        // Outbox Tab
        TabSpec completedSpec = tabHost.newTabSpec(COMPLETED_SPEC);
        completedSpec.setIndicator(COMPLETED_SPEC);
        Intent completedEventsIntent = new Intent(this, CompletedEventsActivity.class);
        completedSpec.setContent(completedEventsIntent);


        // Adding all TabSpec to TabHost
        tabHost.addTab(activeSpec); // Adding Inbox tab
        tabHost.addTab(completedSpec); // Adding Outbox tab
    }
}
