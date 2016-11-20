package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileName extends AppCompatActivity implements View.OnClickListener{
    public DatabaseReference databaseReference;
    public Button buttonSave;
    public UserProfile user;
    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.user = new UserProfile();
        this.firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.buttonSave = (Button) findViewById(R.id.button);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==buttonSave){
            this.setNameAndBio();
        }
    }

    private void setNameAndBio() {
        EditText editName = (EditText) findViewById(R.id.editTextName);
        String name = editName.getText().toString();
        EditText editBio = (EditText) findViewById(R.id.editTextBio);
        String bio = editBio.getText().toString();
        this.user.setName(name);
        this.user.setBio(bio);
        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
        this.databaseReference.child("users").child(name).setValue(this.user);
        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ProfileName.this, ProfilePace.class).putExtra("obj", user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //Firebase userRef = ref.child("users").child("alanisawesome");

    }

}
