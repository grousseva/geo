package edu.ucsb.cs.cs184.grousseva.groussevageopictures;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static edu.ucsb.cs.cs184.grousseva.groussevageopictures.FirebaseHelper.fba;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{

    private GoogleMap mMap;
    public static FirebaseDatabase db;
    public int RequestReadExternalStorageID;
    public static FirebaseHelper.Post x;
    HashMap<String, Marker> markerHashMap = new HashMap<>();
    HashMap<String, String> titTime = new HashMap<>();
    public static HashMap<String, DataSnapshot> lines = new HashMap<>();
    MarkerDialogue frag;
    String[] pictures = {"1573537538000", "1573537538001", "1573537538002", "1573537538003", "1573537538004", "1573537538005", "1573537538006", "1573537538007", "1573537538008", "1573537538009"};
    int i = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]
            {Manifest.permission.ACCESS_FINE_LOCATION},RequestReadExternalStorageID);
        }

        if(ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE},RequestReadExternalStorageID);
        }


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });


        mapFragment.getMapAsync(this);

        FirebaseHelper.Initialize(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ucsb = new LatLng(34.412936, -119.847863);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ucsb));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
        mMap.setOnMapLoadedCallback(this);


        db = FirebaseDatabase.getInstance(fba);

        DatabaseReference postsL = db.getReference("posts");


        postsL.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                x = dataSnapshot.getValue(FirebaseHelper.Post.class);
                addNewMarker(x.latitude, x.longitude, x.title.toString(), x.timestamp.toString(), dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DataSnapshot old_snapshot = lines.get(dataSnapshot.getKey());

                FirebaseHelper.Post temp = old_snapshot.getValue(FirebaseHelper.Post.class);

                x = dataSnapshot.getValue(FirebaseHelper.Post.class);

                Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(temp.latitude, temp.longitude),
                                        new LatLng(x.latitude, x.longitude))
                                .width(5)
                                .color(Color.BLACK));
                removeMarker(x.title.toString());
                addNewMarker(x.latitude, x.longitude, x.title.toString(), x.timestamp.toString(), dataSnapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                x = dataSnapshot.getValue(FirebaseHelper.Post.class);

                if(markerHashMap.get(x.title.toString()) != null)
                {
                    removeMarker(x.title.toString());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                x = dataSnapshot.getValue(FirebaseHelper.Post.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(pictures[i %10]);
                i++;
                titTime.put(markerOptions.getTitle(), String.valueOf(System.currentTimeMillis()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                viewMarker(marker);
                return false;
            }
        });

    }


    @Override
    public void onMapLoaded()
    {
        LatLng ucsb = new LatLng(34.412936, -119.847863);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ucsb));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
        mMap.setOnMapLoadedCallback(this);


    }

    public void viewMarker(Marker m)
    {
        FragmentManager fm = getSupportFragmentManager();
        frag = MarkerDialogue.newInstance(m.getTitle(), titTime.get(m.getTitle()));
        frag.show(fm, "frag");

    }


    public void clear()
    {
        mMap.clear();
        LatLng ucsb = new LatLng(34.412936, -119.847863);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ucsb));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
        i = 1;
        onMapReady(mMap);

    }

    public void addNewMarker(Double lat, Double lon, String title, String timestamp, DataSnapshot ds)
    {
        LatLng place = new LatLng(lat, lon);
        Marker marker = mMap.addMarker(new MarkerOptions().position(place).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        markerHashMap.put(title, marker);
        titTime.put(title, timestamp);
        if(marker != null)
        {
            lines.put(ds.getKey(), ds);
        }
    }

    public void removeMarker(String title)
    {
        Marker marker = markerHashMap.get(title);
        marker.remove();
        markerHashMap.remove(title);
    }
}
