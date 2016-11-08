package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ReviewRun extends AppCompatActivity {
    private DataToday data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_review_run);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = getIntent().getExtras().getParcelable("obj");

        TextView tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setText("Pace: " + data.getPace());
        TextView tv2 = (TextView) findViewById(R.id.textView4);
        tv2.setText("Terrain: " + data.getTerrain());
        TextView tv3 = (TextView) findViewById(R.id.textView5);
        tv3.setText("Distance: " + data.getDistance());

    }

    public void backToPace(View view){
        Intent intent = new Intent(ReviewRun.this, WhatPace.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

}
