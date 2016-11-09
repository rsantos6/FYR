package com.github.fyr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                /*if (i==1){
                    intent = new Intent(ProfilePage.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }*/
                if (i==2){
                   intent = new Intent(ProfilePage.this, HomePage.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               /*if (i==3){
                   intent = new Intent(ProfilePage.this, MatchPage.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
                if (i==4){
                    intent = new Intent(ProfilePage.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
              /* if (i==5) {
                   intent = new Intent(ProfilePage.this, LogOut.class);//This will log the user out
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ProfilePage.this, "NothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editBio(View view) {
        AlertDialog.Builder editBioAlert = new AlertDialog.Builder(this);

        editBioAlert.setTitle("Edit Biography");
        editBioAlert.setMessage("250 character limit");

        final EditText input = new EditText(this);
        int maxLength = 250;
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        editBioAlert.setView(input);

        editBioAlert.setPositiveButton("Submit", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                TextView bioText = (TextView) findViewById(R.id.bioText);
                bioText.setText(value);
            }
        });

        editBioAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        editBioAlert.show();
    }

    public void editProfilePicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        int pickImageRequest = 1;

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.profilePicture);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
