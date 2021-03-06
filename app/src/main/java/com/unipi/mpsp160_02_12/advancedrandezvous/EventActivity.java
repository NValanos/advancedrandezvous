package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.Auth.DashBoard;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Event;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.LatLong;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Participant;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.WorkaroundMapFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private Date date;
    private TextView titleTextView;
    private TextView dateTextView;
    private GoogleMap mMap;
    private ScrollView myScrollView;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Event event;
    private LatLong latLong;
    private LatLng mapsLatLng = new LatLng(0,0);
    private Button managePartitipantsButton;
    private Button checkInButton;
    private LocationManager locationManager;

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

        setContentView(R.layout.event_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.event_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.amber));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.event_details);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        titleTextView = (TextView)findViewById(R.id.eventName);
        dateTextView = (TextView)findViewById(R.id.eventDate);
        checkInButton = (Button)findViewById(R.id.checkInButton);

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCurrentLocation();
            }
        });

        managePartitipantsButton = (Button)findViewById(R.id.managePartitipantsButton);
        managePartitipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ManageParticipantsActivity.class);
                if(event!= null) {
                    intent.putExtra("eventId", event.getId());
                    startActivity(intent);
                }
            }
        });


        //+=+=+=+= Custom SupportMapFragment so that we can override its touch event =+=+=+=+
        myScrollView = (ScrollView) findViewById(R.id.event_ScrollView);

        WorkaroundMapFragment mapFragment = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        myScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });
        //+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ref = databaseReference.child("events");

        Query eventQuery = ref.orderByChild("id").equalTo(this.getIntent().getStringExtra("id"));
        System.err.println("before listener");
        eventQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    event = singleSnapshot.getValue(Event.class);
                    latLong = event.getLocation();
                    mapsLatLng = new LatLng(latLong.getLatitude(), latLong.getLongitude());
                    System.err.println("after db read");

                    if (event != null){
                        titleTextView.setText(event.getTitle());
                        date = new Date(event.getDate());
                        dateTextView.setText(date.toString());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(mapsLatLng).title("Marker"));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(mapsLatLng.latitude, mapsLatLng.longitude))      // Sets the center of the map to location user
                                .zoom(17)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                    else{
                        System.out.println("The EVENT IS NULL");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }

        });
        System.err.println("after listener");


        //+=+=+=+=+=Spinner=+=+=+=+=+
        final String eventId = getIntent().getStringExtra("id");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("events").child(eventId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        event = dataSnapshot.getValue(Event.class);

                        List<Participant> partList;
                        partList = event.getParticipantsIdList();

                        if (event.isActive()){
                            // Event is Active spinner is Enabled
                            for (Participant participant: partList){
                                if (participant.getId().equals(auth.getCurrentUser().getUid())){

                                    Spinner spinner = (Spinner)findViewById(R.id.spinnerState);


                                    ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(EventActivity.this, R.array.state, android.R.layout.simple_spinner_item);
                                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(stateAdapter);
                                    int index = 0;
                                    for (int i=0; i<spinner.getCount(); i++){
                                        if (spinner.getItemAtPosition(i).equals(participant.getTag())){
                                            index = i;
                                        }
                                    }
                                    spinner.setSelection(index);
                                    spinner.setOnItemSelectedListener(EventActivity.this);
                                }
                            }
                        } else {
                            // Event is Completed spinner is Disabled
                            for (Participant participant: partList){
                                if (participant.getId().equals(auth.getCurrentUser().getUid())){

                                    Spinner spinner = (Spinner)findViewById(R.id.spinnerState);

                                    spinner.setEnabled(false);


                                    ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(EventActivity.this, R.array.complete_state, android.R.layout.simple_spinner_item);
                                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(stateAdapter);
                                    int index = 0;
                                    for (int i=0; i<spinner.getCount(); i++){
                                        if (spinner.getItemAtPosition(i).equals(participant.getTag())){
                                            index = i;
                                        }
                                    }
                                    spinner.setSelection(index);
                                    spinner.setOnItemSelectedListener(EventActivity.this);
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String stateSelected = parent.getItemAtPosition(position).toString();
        if ("Accept".equals(stateSelected) && event.isActive()){
            checkInButton.setVisibility(View.VISIBLE);
        }
        else{
            checkInButton.setVisibility(View.INVISIBLE);
        }

        final String eventKey = getIntent().getStringExtra("id");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("events").child(eventKey).child("participantsIdList").orderByChild("id").equalTo(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot keySnapShot: dataSnapshot.getChildren()){
                            String participantKey = keySnapShot.getKey();

                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("events").child(eventKey).child("participantsIdList").child(participantKey).child("tag").setValue(stateSelected);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void checkCurrentLocation(){

        if (ActivityCompat.checkSelfPermission(EventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {

                float distance[] = new float [3];

                Location.distanceBetween(location.getLatitude(), location.getLongitude(), event.getLocation().getLatitude(), event.getLocation().getLongitude(), distance);
                if (distance[0] <= 100){
                    Date eventDate = new Date(event.getDate());
                    Date currentTime = Calendar.getInstance().getTime();
                    Calendar afterEventDate = Calendar.getInstance();
                    afterEventDate.setTime(eventDate);
                    afterEventDate.add(Calendar.MINUTE, 15);
                    System.out.println(currentTime.toString());
                    System.out.println(eventDate.toString());
                    if (currentTime.after(eventDate) && currentTime.before(afterEventDate.getTime())){
                        Toast.makeText(this, R.string.welcome, Toast.LENGTH_SHORT).show();
                        final String eventKey = getIntent().getStringExtra("id");

                        auth = FirebaseAuth.getInstance();
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("events").child(eventKey).child("participantsIdList").orderByChild("id").equalTo(auth.getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot keySnapShot: dataSnapshot.getChildren()){
                                            String participantKey = keySnapShot.getKey();

                                            databaseReference = FirebaseDatabase.getInstance().getReference();
                                            databaseReference.child("events").child(eventKey).child("participantsIdList").child(participantKey).child("tag").setValue("Attended");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                    else if (currentTime.after(afterEventDate.getTime())){
                        Toast.makeText(this, R.string.passed, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, R.string.early, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, R.string.away, Toast.LENGTH_SHORT).show();
                }

            }
        }
        else{
            Toast.makeText(EventActivity.this, R.string.localtionErr ,Toast.LENGTH_LONG).show();
        }

    }

    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(EventActivity.this, message, Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(EventActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String s) {
            Toast.makeText(EventActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String s) {
            Toast.makeText(EventActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }
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
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            System.out.println(message);
        }
        public void onStatusChanged(String s, int i, Bundle b) {
            System.out.println("Provider status changed");
        }
        public void onProviderDisabled(String s) {
            System.out.println("Provider disabled by the user. GPS turned off");
        }
        public void onProviderEnabled(String s) {
            System.out.println("Provider enabled by the user. GPS turned on");
        }
    };

}
