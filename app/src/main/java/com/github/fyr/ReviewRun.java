package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ReviewRun extends AppCompatActivity {
    private DataToday data;
    public FirebaseAuth firebaseAuth;

    private String pace;
    private String terrain;
    private String dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_review_run);
        this.firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = getIntent().getExtras().getParcelable("obj");

        TextView tv1 = (TextView) findViewById(R.id.paceText);
        pace = data.getPace();
        tv1.setText("  " + pace);
        TextView tv2 = (TextView) findViewById(R.id.terrainText);
        terrain = data.getTerrain();
        tv2.setText("  " + terrain);
        TextView tv3 = (TextView) findViewById(R.id.distanceText);
        dist = data.getDistance();
        tv3.setText("  " + dist);

        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (i==1){
                    intent = new Intent(ReviewRun.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==2){
                   intent = new Intent(ReviewRun.this, HomePage.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               /*if (i==3){
                   intent = new Intent(ReviewRun.this, MatchPage.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
                if (i==4){
                    intent = new Intent(ReviewRun.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ReviewRun.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ReviewRun.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void backToPace(View view){
        Intent intent = new Intent(ReviewRun.this, WhatPace.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

    public void findMatches(View view){
        Intent intent = new Intent(ReviewRun.this, MatchMakingActivity.class);
        intent.putExtra("pace", pace);
        intent.putExtra("terrain", terrain);
        intent.putExtra("dist", dist);

        startActivity(intent);
    }

}
