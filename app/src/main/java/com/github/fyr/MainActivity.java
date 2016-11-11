package com.github.fyr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//gets rid of title bar
        setContentView(R.layout.activity_main);
    }

    public void toLogin(View view){
        Intent intent = new Intent(MainActivity.this, LoginUser.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }

    public void toRegister(View view){
        Intent intent = new Intent(MainActivity.this, RegisterUser.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }

}
