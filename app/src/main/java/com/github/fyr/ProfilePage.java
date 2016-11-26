package com.github.fyr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfilePage extends AppCompatActivity {
    public TextView totalRunsText;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase database;
    public DatabaseReference databaseReference;

    String userName;
    String userBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        FirebaseUser user = this.firebaseAuth.getCurrentUser();


        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = (String) dataSnapshot.getValue();
                byte[] decodedString = Base64.decode(String.valueOf(image), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ImageView imageView = (ImageView) findViewById(R.id.profilePicture);
                imageView.setImageBitmap(bm);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/distance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String distance = (String) dataSnapshot.getValue();
                totalRunsText = (TextView) findViewById(R.id.usualDistance);
                totalRunsText.setText("Usual Distance: "+distance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/terrain").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String terrain = (String) dataSnapshot.getValue();
                totalRunsText = (TextView) findViewById(R.id.preferedTerrain);
                totalRunsText.setText("Prefered Terrain: "+terrain);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/pace").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pace = (String) dataSnapshot.getValue();
                totalRunsText = (TextView) findViewById(R.id.usualPace);
                totalRunsText.setText("Usual Pace: "+pace);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.getValue();
                userName = name;
                totalRunsText = (TextView) findViewById(R.id.nameText);
                totalRunsText.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/bio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bio = (String) dataSnapshot.getValue();
                userBio = bio;
                totalRunsText = (TextView) findViewById(R.id.bioText);
                totalRunsText.setText(bio);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (i==1){
                    Toast.makeText(ProfilePage.this, "Already on Profile page", Toast.LENGTH_SHORT).show();//if clicks home page and on home
                }
                if (i==2){
                   intent = new Intent(ProfilePage.this, HomePage.class);
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
               if (i==3){
                   intent = new Intent(ProfilePage.this, ChatRoom.class);//This will be the MatchesPage
                   startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
               }
                if (i==4){
                    intent = new Intent(ProfilePage.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i==5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ProfilePage.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ProfilePage.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editBio(View view) {
        AlertDialog.Builder editBioAlert = new AlertDialog.Builder(this);

        editBioAlert.setTitle("Edit Biography");
        editBioAlert.setMessage("250 character limit");


        final EditText input = new EditText(this);
        int maxLength = 250;
        input.setText(userBio);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        editBioAlert.setView(input);

        editBioAlert.setPositiveButton("Submit", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                TextView bioText = (TextView) findViewById(R.id.bioText);
                bioText.setText(value);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String userUid = user.getUid();
                databaseReference.child("users").child(user.getEmail().replace(".","") + "/bio").setValue(value);
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

    public void editStats(View view) {
        /*UserProfile data = new UserProfile();
        data.setName(userName);
        data.setBio(userBio);
        Intent intent = new Intent(ProfilePage.this, ProfilePace.class).putExtra("obj", data);
        startActivity(intent);*/
        Intent intent = new Intent(ProfilePage.this, ProfileEdit.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//your image
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                bitmap.recycle();
                byte[] byteArray = bYtE.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                
                FirebaseUser user = this.firebaseAuth.getCurrentUser();
                this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/image").setValue(imageFile);


                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.profilePicture);
                //imageView.setImageBitmap(bitmap);

                byte[] decodedString = Base64.decode(imageFile, Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
