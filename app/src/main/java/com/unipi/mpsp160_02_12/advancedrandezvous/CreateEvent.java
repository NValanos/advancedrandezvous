package com.unipi.mpsp160_02_12.advancedrandezvous;

import android.app.Activity;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Event;

import java.util.Date;

public class CreateEvent extends Activity implements OnMapReadyCallback {
    private Button createEventButton;
    private EditText titleEditText;
    private EditText dateEditText;
    private GoogleMap mMap;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        titleEditText = (EditText)findViewById(R.id.title_edit_text);
        dateEditText = (EditText)findViewById(R.id.date_edit_text);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.create_event_map);
        mapFragment.getMapAsync(this);

        createEventButton = (Button)findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for now
                Location loc  = new Location("random location");
                loc.setLatitude(0.0d);
                loc.setLongitude(0.0d);

                if ("".equals(titleEditText.getText().toString())){
                    Toast.makeText(CreateEvent.this, "PLEASE ADD TITLE", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(dateEditText.getText().toString())){
                    Toast.makeText(CreateEvent.this, "PLEASE ADD DATE", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                }


                createEvent(titleEditText.getText().toString(), loc, dateEditText.getText().toString());

            }
        });


    }

    protected Boolean createEvent(String title, Location location, String date){
        Event event = new Event();
        event.setTitle(title);
        event.setLocation(location);
        event.setDate(date);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("events").setValue(event);

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
            }
        });
    }


}
