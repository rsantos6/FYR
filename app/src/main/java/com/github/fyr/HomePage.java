package com.github.fyr;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


//need to consier if i want an overflow menu of just to use three dots as a menu item
public class HomePage extends AppCompatActivity  { //implements AdapterView.OnItemSelectedListener {
    protected ActionBar actionBar;
    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_home_page);
        this.firebaseAuth = FirebaseAuth.getInstance();
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent;
               if (i==1){
                   intent = new Intent(HomePage.this, ProfilePage.class);
                   startActivity(intent);
                   overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               if (i==2){
                   Toast.makeText(HomePage.this, "Already on Home page", Toast.LENGTH_SHORT).show();//if clicks home page and on home
               }
               /*if (i==3){
                   intent = new Intent(HomePage.this, MatchPage.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
               if (i==4){
                   intent = new Intent(HomePage.this, AboutPage.class);
                   startActivity(intent);
                   overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               if (i==5) {
                   firebaseAuth.signOut();
                   finish();
                   startActivity(new Intent(HomePage.this, MainActivity.class));
                   overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(HomePage.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toPace(View view){
        Intent intent = new Intent(HomePage.this, WhatPace.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void toProfilePage(View view) {
        Intent i = new Intent(this, ProfilePage.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }
}
