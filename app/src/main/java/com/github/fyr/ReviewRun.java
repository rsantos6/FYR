package com.github.fyr;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewRun extends AppCompatActivity implements LocationListener {
    private DataToday data;
    public FirebaseAuth firebaseAuth;

    private String pace;
    private String terrain;
    private String dist;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String gpsText;
    protected Location gps;

    public DatabaseReference databaseReference;
    protected boolean gps_enabled, network_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_review_run);
        this.firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = getIntent().getExtras().getParcelable("obj");

        /*
        This section of code sets the textviews to the values
        in the data object so the user can review the info
        they entered before trying to find a match
         */

        FirebaseUser curr = firebaseAuth.getCurrentUser();
        gpsText = "not set yet";
        //this.databaseReference.child("users").child(curr.getEmail().replace(".","")+"/location").setValue("test");

        TextView tv1 = (TextView) findViewById(R.id.paceText);
        pace = data.getPace();
        tv1.setText("  " + pace);
        TextView tv2 = (TextView) findViewById(R.id.terrainText);
        terrain = data.getTerrain();
        tv2.setText("  " + terrain);
        TextView tv3 = (TextView) findViewById(R.id.distanceText);
        dist = data.getDistance();
        tv3.setText("  " + dist);


         /*
        This creates the functionality for the spinner on the top right of pretty much every
        page. If the user clicks the arrow a menu scrolls down with certain pages the user
        can access if they click on it. If the user tries to click on a page they're already on
        a Toast will appear stating that they are already on that page.
         */
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (i == 1) {
                    intent = new Intent(ReviewRun.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i == 2) {
                    intent = new Intent(ReviewRun.this, HomePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
               if (i==3){
                   intent = new Intent(ReviewRun.this, ChatList.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
                if (i == 4) {
                    intent = new Intent(ReviewRun.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i == 5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ReviewRun.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ReviewRun.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gpsText = location.getLatitude() + " " + location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                // meh
            }

            @Override
            public void onProviderEnabled(String s) {
                // meh
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 50, 0, locationListener);
        }
    }

    public void backToPace(View view) {
        Intent intent = new Intent(ReviewRun.this, WhatPace.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

    public void findMatches(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Something went wrong....");
            return;
        }
        locationManager.requestLocationUpdates("gps", 50, 0, locationListener);
        System.out.println("\n LOOK HERE \n" + gpsText);
        FirebaseUser curr = firebaseAuth.getCurrentUser();
        System.out.println(curr.getEmail());

        Intent intent = new Intent(ReviewRun.this, MatchMakingActivity.class);
        intent.putExtra("pace", pace);
        intent.putExtra("terrain", terrain);
        intent.putExtra("dist", dist);

        if(!gpsText.equals("not set yet")){
            intent.putExtra("gpsText", gpsText);
        }else{
            String deft = getDefaultLocation();
            intent.putExtra("gpsText", deft);
        }

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // locationManager.requestLocationUpdates("gps", 50, 0, locationListener);;
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // this is a joke
                        return;
                    }
                    locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
                }
        }
    }

    public void onLocationChanged(Location location) {
        // This method ONLY gets called when the location gets changed.
        // Therefore, when starting/restarting the app in one sitting it does NOT re-run this method
        // Need to save a location as a default in the db and access that when needed
        gpsText = location.getLatitude() + " " + location.getLongitude();
        gps = location;
        addLocationToGeoFire(location, 0);
    }

    private void addLocationToGeoFire(Location location, int message){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
        GeoFire geoFire = new GeoFire(ref);
        FirebaseUser curr = firebaseAuth.getCurrentUser();
        String current_user = firebaseAuth.getCurrentUser().getEmail().replace(".", "");
        if(message == 0){
            geoFire.setLocation(current_user, new GeoLocation(location.getLatitude(), location.getLongitude()));
            databaseReference.child("users").child(current_user +"/location").setValue(location.toString());
        }else{
            // Add Default location of the Brandeis Library, the main hot spot for young runners of Brandeis
            geoFire.setLocation(current_user, new GeoLocation(37.421998333333335, -120.084000000000002));
            String defaultGPS = "[37.421998333333335, -120.084000000000002]";
            System.out.println("kashfjashdfjahsdbfjb " + current_user);
            databaseReference.child("users").child(current_user+"/location").setValue("test");
            gpsText = defaultGPS;
        }
    }

    public String getDefaultLocation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        FirebaseUser curr = firebaseAuth.getCurrentUser();
        Location l = new Location("");addLocationToGeoFire(l, 1);
        return "[37.421998333333335, -120.084000000000002]";
        /**
        try {
            String location = this.databaseReference.child("users").child(curr.getEmail().replace(".", "") + "/location").getKey();
            System.out.println("This is the default: " + location);
            gpsText = location;
            return location;
        } catch (Exception e) {
            Location l = new Location("");
            addLocationToGeoFire(l, 1);
            return "[37.421998333333335, -120.084000000000002]";
        }**/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // meh
    }

    @Override
    public void onProviderEnabled(String s) {
        // meh
    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
