package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfilePace extends AppCompatActivity implements View.OnClickListener{
    public RadioGroup radioPaceGroup;
    public RadioButton radioPaceButton;
    public FirebaseAuth firebaseAuth;
    public TextView textViewWelcome;
    public DatabaseReference databaseReference;
    public Button buttonSave;
    public UserProfile data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pace);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = new UserProfile();
        this.firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        this.textViewWelcome = (TextView) findViewById(R.id.textView);
        this.textViewWelcome.setText("Welcome " + user.getEmail() + "! Select your usual pace:");
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.buttonSave = (Button) findViewById(R.id.button);
        buttonSave.setOnClickListener(this);


    }


    public void setPace() {
        // get selected radio button from radioGroup
        this.radioPaceGroup = (RadioGroup) findViewById(R.id.paceGroup);
        if (this.radioPaceGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(ProfilePace.this, "Please Select a Pace", Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = this.radioPaceGroup.getCheckedRadioButtonId();
            this.radioPaceButton = (RadioButton) findViewById(selectedId);
            String x = this.radioPaceButton.getText().toString();
            this.data.setPace(x);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            this.databaseReference.child(user.getUid()).setValue(this.data);
            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
           /* Intent intent = new Intent(ProfilePace.this, WhatSurface.class).putExtra("obj", data);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
        }

    }

    @Override
    public void onClick(View view) {
        if(view==buttonSave){
            this.setPace();
        }
    }

}

