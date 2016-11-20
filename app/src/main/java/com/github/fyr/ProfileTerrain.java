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

public class ProfileTerrain extends AppCompatActivity implements View.OnClickListener{

    public RadioGroup radioTerrainGroup;
    public RadioButton radioTerrainButton;
    public FirebaseAuth firebaseAuth;
    public TextView textViewWelcome;
    public DatabaseReference databaseReference;
    public Button buttonSave;
    public UserProfile data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_terrain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.data = getIntent().getExtras().getParcelable("obj");
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.textViewWelcome = (TextView) findViewById(R.id.textView);
        this.textViewWelcome.setText("Please select your prefered terrain:");
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.buttonSave = (Button) findViewById(R.id.button);
        buttonSave.setOnClickListener(this);


    }


    public void setTerrain() {
        // get selected radio button from radioGroup
        this.radioTerrainGroup = (RadioGroup) findViewById(R.id.terrainGroup);
        if (this.radioTerrainGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(ProfileTerrain.this, "Please Select a Terrain", Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = this.radioTerrainGroup.getCheckedRadioButtonId();
            this.radioTerrainButton = (RadioButton) findViewById(selectedId);
            String x = this.radioTerrainButton.getText().toString();
            this.data.setTerrain(x);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            this.databaseReference.child("users").child(user.getEmail().replace(".","")).setValue(this.data);
            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfileTerrain.this, ProfileDistance.class).putExtra("obj", data);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }

    @Override
    public void onClick(View view) {
        if(view==buttonSave){
            this.setTerrain();
        }
    }

}


