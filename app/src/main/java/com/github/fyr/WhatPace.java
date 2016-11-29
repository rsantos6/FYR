package com.github.fyr;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class WhatPace extends AppCompatActivity {

    private DataToday data = new DataToday();
    private RadioGroup radioPaceGroup;
    private RadioButton radioPaceButton;
    public FirebaseAuth firebaseAuth;

    /*
    This class lets the user select what pace they wish to run
    on today. They select their choice by clicking a radio button
    and clicking the next button. Their choice is save in a data
    object which will eventually be used to match them against
    another user's proifle.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_what_pace);
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
                    intent = new Intent(WhatPace.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==2){
                   intent = new Intent(WhatPace.this, HomePage.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               if (i==3){
                   intent = new Intent(WhatPace.this, ChatList.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
                if (i==4){
                    intent = new Intent(WhatPace.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(WhatPace.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(WhatPace.this, "NothingSelected", Toast.LENGTH_SHORT).show();
            }
        });


    }
    /*
    This method stores the information chosen
    by the user (depends on what radio button
    they clicked) and puts it in a data object
    which is passed on and will eventually be used
    to find a match
     */
    public void setPace(View view) {
        // get selected radio button from radioGroup
        this.radioPaceGroup = (RadioGroup) findViewById(R.id.paceGroup);
        if (this.radioPaceGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(WhatPace.this, "Please Select a Pace", Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = this.radioPaceGroup.getCheckedRadioButtonId();
            this.radioPaceButton = (RadioButton) findViewById(selectedId);
            this.data.setPace(this.radioPaceButton.getText().toString());
            Intent intent = new Intent(WhatPace.this, WhatSurface.class).putExtra("obj", this.data);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    public void backToHome(View view){
        Intent intent = new Intent(WhatPace.this, HomePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }


}


