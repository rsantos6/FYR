package com.github.fyr;

import android.app.Activity;
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

public class WhatPace extends Activity {

    private DataToday data;

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


    public void addPace(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.ten_nine:
                if(checked) {
                    this.data.setPace("10-9");
                }
                break;
            case R.id.nine_eight:
                if(checked) {
                    this.data.setPace("9-8");
                }
                break;
            case R.id.eight_seven:
                if(checked) {
                    this.data.setPace("8-7");
                }
                break;
            case R.id.seven_six:
                if(checked) {
                    this.data.setPace("7-6");
                }
                break;
        }
        TextView t = (TextView) findViewById(R.id.textView3);
        t.setText(data.getPace());


    }

}
