package com.github.fyr;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class WhatPace extends Activity {

    private DataToday data = new DataToday();
    private RadioGroup radioPaceGroup;
    private RadioButton radioPaceButton;
    private Button btnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_pace);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }





    public void setPace(View view) {
        // get selected radio button from radioGroup
        radioPaceGroup = (RadioGroup) findViewById(R.id.paceGroup);
        int selectedId = radioPaceGroup.getCheckedRadioButtonId();
        radioPaceButton = (RadioButton) findViewById(selectedId);
        data.setPace(radioPaceButton.getText().toString());
        TextView v = (TextView) findViewById(R.id.textView3);
        v.setText(data.getPace().toString());
    }

}
