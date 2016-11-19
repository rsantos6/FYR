package com.github.fyr;

import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;


public class MatchMakingActivity extends AppCompatActivity {


    public FirebaseAuth firebaseAuth;
    private Firebase mFirebaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String pace = intent.getStringExtra("pace");
        String terrain = intent.getStringExtra("terrain");
        String dist = intent.getStringExtra("dist");

        System.out.println(pace + " " + terrain + " " + dist);

        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com/");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
