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

        /*
        This creates the functionality for the spinner on the top right of pretty much every
        page. If the user clicks the arrow a menu scrolls down with certain pages the user
        can access if they click on it. If the user tries to click on a page they're already on
        a Toast will appear stating that they are already on that page.
         */
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent;
               if (i==1){
                   intent = new Intent(HomePage.this, ProfilePage.class);
                   startActivity(intent);
                   overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);//animated transitions between activities
               }
               if (i==2){
                   Toast.makeText(HomePage.this, "Already on Home page", Toast.LENGTH_SHORT).show();//if clicks home page and on home
               }
               if (i==3){
                   intent = new Intent(HomePage.this, ChatList.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);//animated transitions between activities
               }
               if (i==4){
                   intent = new Intent(HomePage.this, AboutPage.class);
                   startActivity(intent);
                   overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);//animated transitions between activities
               }
               if (i==5) {
                   firebaseAuth.signOut();
                   finish();
                   startActivity(new Intent(HomePage.this, MainActivity.class));
                   overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);//animated transitions between activities
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(HomePage.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    This method brings the user to the page where they can
    select what pace they feel like running today. This is the
    first step in the matching process
     */
    public void toPace(View view){
        Intent intent = new Intent(HomePage.this, WhatPace.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);//animated transitions between activities
    }

    /*
    This method brings the user to their profile page which contains all of
    their stats, bio, and profile picture. They can edit it if they choose
     */
    public void toProfilePage(View view) {
        Intent i = new Intent(this, ProfilePage.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);//animated transitions between activities
    }
}
