package com.github.fyr;

import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Iterator;


import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class MatchMakingActivity extends AppCompatActivity {


    public FirebaseAuth firebaseAuth;
    private Firebase mFirebaseRef;

    private static final String TAG = "UserList" ;
    private DatabaseReference userlistReference;
    private FirebaseDatabase db;
    private ValueEventListener mUserListListener;
    ArrayList<String> usernamelist;
    ArrayAdapter arrayAdapter;
    public ListView UserList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String pace = intent.getStringExtra("pace");
        String terrain = intent.getStringExtra("terrain");
        String dist = intent.getStringExtra("dist");

        System.out.println(pace + " " + terrain + " " + dist);

        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://new-fyr.firebaseio.com/");
        this.firebaseAuth = FirebaseAuth.getInstance();

        userlistReference = FirebaseDatabase.getInstance().getReference().child("users");
        System.out.println("LOOK HERE!!!!!!!!!");
        System.out.println(userlistReference);
        onStart();
        UserList = (ListView) findViewById(R.id.UserList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        final ValueEventListener userListener = new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println("Look Here");
                HashMap<String, Object> map = (HashMap) dataSnapshot.getValue();
                //System.out.println(dataSnapshot.getValue());
                //System.out.println(map.keySet());

                usernamelist = new ArrayList<>(map.keySet());
                arrayAdapter = new ArrayAdapter(MatchMakingActivity.this,android.R.layout.simple_list_item_1,usernamelist);
                UserList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "onCancelled: ",databaseError.toException());

            }
        };
        userlistReference.addValueEventListener(userListener);

        mUserListListener = userListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mUserListListener != null) {
            userlistReference.removeEventListener(mUserListListener);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**switch(item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }**/
        return false;
    }

}
