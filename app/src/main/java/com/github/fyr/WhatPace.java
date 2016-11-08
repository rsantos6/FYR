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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WhatPace extends AppCompatActivity {

    private DataToday data = new DataToday();
    private RadioGroup radioPaceGroup;
    private RadioButton radioPaceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_what_pace);


    }





    public void setPace(View view) {
        // get selected radio button from radioGroup
        this.radioPaceGroup = (RadioGroup) findViewById(R.id.paceGroup);
        int selectedId = this.radioPaceGroup.getCheckedRadioButtonId();
        this.radioPaceButton = (RadioButton) findViewById(selectedId);
        this.data.setPace(this.radioPaceButton.getText().toString());
        Intent intent = new Intent(WhatPace.this, WhatSurface.class).putExtra("obj",this.data);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void backToHome(View view){
        Intent intent = new Intent(WhatPace.this, HomePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }


}


