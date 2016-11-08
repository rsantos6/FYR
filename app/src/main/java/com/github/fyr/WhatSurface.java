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

public class WhatSurface extends AppCompatActivity {

    private DataToday data;
    private RadioGroup radioSurfaceGroup;
    private RadioButton radioSurfaceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_surface);
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

    public void setSurface() {
        // get selected radio button from radioGroup
        int selectedId = this.radioSurfaceGroup.getCheckedRadioButtonId();
        this.radioSurfaceButton = (RadioButton) findViewById(selectedId);
        this.data.setTerrain(this.radioSurfaceButton.getText().toString());
    }

    public void backToPace(View view){
        this.radioSurfaceGroup = (RadioGroup) findViewById(R.id.surfaceGroup);//get radio group which contains all the radio buttons
        if (this.radioSurfaceGroup.getCheckedRadioButtonId() != -1){//if a radio button within the radio group is clicked
            this.setSurface();//set the surface field in the data object
        }//if no radio button is clicked don't try to set the surface field because there's nothing to set
        Intent intent = new Intent(WhatSurface.this, WhatPace.class).putExtra("obj", this.data);//create intent with data object
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);//transition to go back to pace page

    }

    public void toTerrain(View view){
        this.radioSurfaceGroup = (RadioGroup) findViewById(R.id.surfaceGroup);
        this.setSurface();
        Intent intent = new Intent(WhatSurface.this, WhatDistance.class).putExtra("obj",this.data);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
