package com.github.fyr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (i==1){
                    intent = new Intent(AboutPage.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==2){
                   intent = new Intent(AboutPage.this, HomePage.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               /*if (i==3){
                   intent = new Intent(AboutPage.this, MatchPage.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
                /*if (i==4){
                    intent = new Intent(AboutPage.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }*/
              /* if (i==5) {
                   intent = new Intent(HomePage.this, LogOut.class);//This will log the user out
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AboutPage.this, "NothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
