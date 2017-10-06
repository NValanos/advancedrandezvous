package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import java.util.Date;

import static android.content.ContentValues.TAG;

public class EventActivity extends Activity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private Date date;
    private TextView titleTextView;
    private TextView dateTextView;
    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private DatabaseReference ref;
    private Event event;
    private LatLong latLong;
    private LatLng mapsLatLng = new LatLng(0,0);
    private Button managePartitipantsButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        titleTextView = (TextView)findViewById(R.id.eventName);
        dateTextView = (TextView)findViewById(R.id.eventDate);

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


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapsLatLng));
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

        //Spinner
        Spinner spinner = (Spinner)findViewById(R.id.spinnerState);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stateAdapter);
        spinner.setOnItemSelectedListener(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        System.err.println("when creating map");

        mMap.addMarker(new MarkerOptions().position(mapsLatLng).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapsLatLng));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String stateSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, stateSelected, Toast.LENGTH_SHORT).show();

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
}
