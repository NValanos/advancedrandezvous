package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.*;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Event;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.LatLong;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Participant;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

public class CreateEvent extends AppCompatActivity implements OnMapReadyCallback{
    private Button createEventButton;
    private EditText titleEditText;
    private EditText dateInputText;
    private EditText timeInputText;
    private GoogleMap mMap;
    private static LatLng location;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat dateFormatter;
    private FirebaseAuth auth;
    private User owner;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Calendar newCalendar = Calendar.getInstance();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.create_event_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.event_details);


        //Init Firebase
        auth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        Query ownerQuery = mDatabase.orderByChild("id").equalTo(auth.getCurrentUser().getUid());
        ownerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.getChildren()!=null && dataSnapshot.getChildren().iterator().hasNext()){

                    for (DataSnapshot ownerSnapshot: dataSnapshot.getChildren()){
                        owner = ownerSnapshot.getValue(User.class);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        titleEditText = (EditText)findViewById(R.id.title_edit_text);

        dateInputText = (EditText)findViewById(R.id.date_input_text);
        dateInputText.setInputType(InputType.TYPE_NULL);
        dateInputText.requestFocus();

        dateInputText.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                datePickerDialog.show();
            }
        });

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateInputText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        timeInputText = (EditText)findViewById(R.id.time_input_text);
        timeInputText.setInputType(InputType.TYPE_NULL);
        timeInputText.requestFocus();

        timeInputText.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                timePickerDialog.show();
            }
        });

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeInputText.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.create_event_map);
        mapFragment.getMapAsync(this);

        createEventButton = (Button)findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(titleEditText.getText().toString())){
                    Toast.makeText(CreateEvent.this, "PLEASE ADD TITLE", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(dateInputText.getText().toString())){
                    Toast.makeText(CreateEvent.this, "PLEASE ADD DATE", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(timeInputText.getText().toString())){
                    Toast.makeText(CreateEvent.this, "PLEASE ADD TIME", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (location == null){
                    Toast.makeText(CreateEvent.this, "PLEASE ADD LOCATION", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringTokenizer dateTokenizer =  new StringTokenizer(dateInputText.getText().toString(), "-");
                int day = Integer.parseInt(dateTokenizer.nextToken());
                int month = Integer.parseInt(dateTokenizer.nextToken());
                int year = Integer.parseInt(dateTokenizer.nextToken());

                StringTokenizer timeTokenizer = new StringTokenizer(timeInputText.getText().toString(),":");
                int hour = Integer.parseInt(timeTokenizer.nextToken());
                int min = Integer.parseInt(timeTokenizer.nextToken());

                Calendar date = Calendar.getInstance();
                date.set(year, month -1, day, hour, min);
                System.out.println("Date before method: " + date.getTime().toString());
                String id = createEvent(titleEditText.getText().toString(), location, date.getTime());
                if (id != null){
                    Toast.makeText(CreateEvent.this, "Event created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateEvent.this, EventActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CreateEvent.this, "Failed to create event", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    protected String createEvent(String title, LatLng location, Date date){
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setTitle(title);
        LatLong latLong = new LatLong(location.latitude, location.longitude);
        event.setLocation(latLong);
        System.out.println("Creating date: " + date.toString());
        event.setDate(date.getTime());
        event.setActive(true);
        event.setOwnerId(owner.getId());

        Participant participant = new Participant();
        participant.setName(owner.getUsername());
        participant.setId(owner.getId());
        participant.setTag("Accepted");

        List<Participant> participantsList = new ArrayList<>();
        participantsList.add(participant);
        event.setParticipantsIdList(participantsList);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("events").child(event.getId()).setValue(event);

        return event.getId();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(CreateEvent.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
//                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        // Add a marker in Sydney and move the camera
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                location = latLng;
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
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
