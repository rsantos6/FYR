package com.github.fyr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileName extends AppCompatActivity implements View.OnClickListener{
    public DatabaseReference databaseReference;
    public Button buttonSave;
    public UserProfile user;
    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.user = getIntent().getExtras().getParcelable("obj");
        this.firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.buttonSave = (Button) findViewById(R.id.button);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==buttonSave){
            this.setNameAndBio();
        }
    }

    private void setNameAndBio() {
        EditText editName = (EditText) findViewById(R.id.editTextName);
        String name = editName.getText().toString();
        EditText editBio = (EditText) findViewById(R.id.editTextBio);
        String bio = editBio.getText().toString();
        this.user.setName(name);
        this.user.setBio(bio);
        FirebaseUser fireUser = firebaseAuth.getCurrentUser();
        this.databaseReference.child("users").child(fireUser.getEmail().replace(".","")).setValue(this.user);
        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ProfileName.this, ProfilePace.class).putExtra("obj", user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//your image
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                bitmap.recycle();
                byte[] byteArray = bYtE.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                this.user.setImage(imageFile);

                ImageView imageView = (ImageView) findViewById(R.id.profilePicture);
                //imageView.setImageBitmap(bitmap);

                byte[] decodedString = Base64.decode(this.user.getImage(), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
