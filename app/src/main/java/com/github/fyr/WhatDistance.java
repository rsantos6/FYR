package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class WhatDistance extends AppCompatActivity {
    private DataToday data;
    private RadioGroup radioDistanceGroup;
    private RadioButton radioDistanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_distance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = getIntent().getExtras().getParcelable("obj");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void toReview(View view) {
        // get selected radio button from radioGroup
        this.radioDistanceGroup = (RadioGroup) findViewById(R.id.distanceGroup);
        int selectedId = this.radioDistanceGroup.getCheckedRadioButtonId();
        this.radioDistanceButton = (RadioButton) findViewById(selectedId);
        this.data.setDistance(this.radioDistanceButton.getText().toString());
        Intent intent = new Intent(WhatDistance.this, ReviewRun.class).putExtra("obj",this.data);
        startActivity(intent);
    }

    public void backToHome(View view){
        Intent intent = new Intent(WhatDistance.this, HomePage.class);
        startActivity(intent);
    }

}
