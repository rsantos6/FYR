package com.github.fyr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
        this.radioPaceGroup = (RadioGroup) findViewById(R.id.paceGroup);
        int selectedId = this.radioPaceGroup.getCheckedRadioButtonId();
        this.radioPaceButton = (RadioButton) findViewById(selectedId);
        this.data.setPace(this.radioPaceButton.getText().toString());
        Intent intent = new Intent(WhatPace.this, WhatSurface.class).putExtra("obj",this.data);
        startActivity(intent);
    }

    public void backToHome(View view){
        Intent intent = new Intent(WhatPace.this, HomePage.class);
        startActivity(intent);
    }

}
