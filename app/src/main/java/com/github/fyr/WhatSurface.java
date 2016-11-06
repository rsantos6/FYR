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

    public void setSurface(View view) {
        // get selected radio button from radioGroup
        this.radioSurfaceGroup = (RadioGroup) findViewById(R.id.surfaceGroup);
        int selectedId = this.radioSurfaceGroup.getCheckedRadioButtonId();
        this.radioSurfaceButton = (RadioButton) findViewById(selectedId);
        this.data.setTerrain(this.radioSurfaceButton.getText().toString());
        Intent intent = new Intent(WhatSurface.this, WhatDistance.class).putExtra("obj",this.data);
        startActivity(intent);
    }

    public void backToHome(View view){
        Intent intent = new Intent(WhatSurface.this, HomePage.class);
        startActivity(intent);
    }

}
