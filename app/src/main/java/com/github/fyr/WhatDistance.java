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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class WhatDistance extends AppCompatActivity {
    private DataToday data;
    private RadioGroup radioDistanceGroup;
    private RadioButton radioDistanceButton;
    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_what_distance);
        this.firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = getIntent().getExtras().getParcelable("obj");

        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (i==1){
                    intent = new Intent(WhatDistance.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==2){
                   intent = new Intent(WhatDistance.this, HomePage.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               /*if (i==3){
                   intent = new Intent(WhatDistance.this, MatchPage.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
                if (i==4){
                    intent = new Intent(WhatDistance.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(WhatDistance.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(WhatDistance.this, "NothingSelected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setDistance() {
        // get selected radio button from radioGroup
        int selectedId = this.radioDistanceGroup.getCheckedRadioButtonId();
        this.radioDistanceButton = (RadioButton) findViewById(selectedId);
        this.data.setDistance(this.radioDistanceButton.getText().toString());
    }

    public void backToTerrain(View view){
        this.radioDistanceGroup = (RadioGroup) findViewById(R.id.distanceGroup);//get radio group which contains all the radio buttons
        if (this.radioDistanceGroup.getCheckedRadioButtonId() != -1){//if a radio button within the radio group is clicked
            this.setDistance();//set the surface field in the data object
        }//if no radio button is clicked don't try to set the surface field because there's nothing to set
        Intent intent = new Intent(WhatDistance.this, WhatSurface.class).putExtra("obj", this.data);//create intent with data object
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);//transition to go back to pace page

    }

    public void toReview(View view){
        this.radioDistanceGroup = (RadioGroup) findViewById(R.id.distanceGroup);
        if (this.radioDistanceGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(WhatDistance.this, "Please Select a Distance", Toast.LENGTH_SHORT).show();
        } else {
            this.setDistance();
            Intent intent = new Intent(WhatDistance.this, ReviewRun.class).putExtra("obj", this.data);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

}
