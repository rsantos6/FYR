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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileEdit extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;
    public RadioGroup radioDistanceGroup;
    public RadioButton radioDistanceButton;
    public RadioGroup radioTerrainGroup;
    public RadioButton radioTerrainButton;
    public RadioGroup radioPaceGroup;
    public RadioButton radioPaceButton;
    public Button buttonDistance;
    public Button buttonTerrain;
    public Button buttonPace;
    public Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.buttonPace = (Button) findViewById(R.id.button);
        this.buttonTerrain = (Button) findViewById(R.id.button2);
        this.buttonDistance = (Button) findViewById(R.id.button3);
        this.back = (Button) findViewById(R.id.button4);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        this.buttonPace.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setPace();
                    }
                });
        this.buttonTerrain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTerrain();
                    }
                });
        this.buttonDistance.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        setDistance();
                    }
                });
        this.back.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        toProfile();
                    }
                });
    }

    public void setTerrain() {
        // get selected radio button from radioGroup
        this.radioTerrainGroup = (RadioGroup) findViewById(R.id.terrainGroup);
        if (this.radioTerrainGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(ProfileEdit.this, "Please Select a Terrain", Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = this.radioTerrainGroup.getCheckedRadioButtonId();
            this.radioTerrainButton = (RadioButton) findViewById(selectedId);
            String value = this.radioTerrainButton.getText().toString();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String userUid = user.getUid();
            databaseReference.child("users").child(user.getEmail().replace(".","") + "/terrain").setValue(value);
            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
        }
    }

    public void setDistance() {
        // get selected radio button from radioGroup
        this.radioDistanceGroup = (RadioGroup) findViewById(R.id.distanceGroup);
        if (this.radioDistanceGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(ProfileEdit.this, "Please Select a Distance", Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = this.radioDistanceGroup.getCheckedRadioButtonId();
            this.radioDistanceButton = (RadioButton) findViewById(selectedId);
            String value = this.radioDistanceButton.getText().toString();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String userUid = user.getUid();
            databaseReference.child("users").child(user.getEmail().replace(".","") + "/distance").setValue(value);
            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
        }

    }

    public void setPace() {
        // get selected radio button from radioGroup
        this.radioPaceGroup = (RadioGroup) findViewById(R.id.paceGroup);
        if (this.radioPaceGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(ProfileEdit.this, "Please Select a Pace", Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = this.radioPaceGroup.getCheckedRadioButtonId();
            this.radioPaceButton = (RadioButton) findViewById(selectedId);
            String value = this.radioPaceButton.getText().toString();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String userUid = user.getUid();
            databaseReference.child("users").child(user.getEmail().replace(".","") + "/pace").setValue(value);
            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
        }
    }

    public void toProfile(){
        Intent intent = new Intent(ProfileEdit.this, ProfilePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }
}
