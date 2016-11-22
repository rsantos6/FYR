package com.github.fyr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.util.Base64;

import java.io.IOException;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.bumptech.glide.Glide;

public class MatchMakingActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface{


    public FirebaseAuth firebaseAuth;
    private Firebase mFirebaseRef;

    private static final String TAG = "UserList" ;
    private DatabaseReference userlistReference;
    private FirebaseDatabase db;
    private ValueEventListener mUserListListener;
    ArrayList<String> potentialMatches;
    public ListView UserList;

    public static MatchCardAdapter cardAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<UserProfile> al;
    private SwipeFlingAdapterView flingContainer;

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
        onStart();

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        // These lines of code define the ArrayList that is going to be used
        // to store the card data, and then the ArrayList is set as the adapter.
        // Populate this ArrayList to create the different cards.
        al = new ArrayList<>();
        cardAdapter = new MatchCardAdapter(al, MatchMakingActivity.this);
        flingContainer.setAdapter(cardAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                al.remove(0);
                cardAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                al.remove(0);
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();

                cardAdapter.notifyDataSetChanged();
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

                potentialMatches = new ArrayList<>(map.keySet());
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

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

/*============================================================
ViewHolder CLASS

Stores all the fields that are going to be on the cards,
along with the background.
============================================================*/

    public static class ViewHolder {
        public TextView nameText;
        public ImageView cardImage;
        public TextView bioText;
    }

/*=============================================================
MatchCardAdapter CLASS

Formats the cards that are going to be displayed
within the view
==============================================================*/

    public class MatchCardAdapter extends BaseAdapter {

        public List<UserProfile> userList;
        public Context context;

        private MatchCardAdapter(List<UserProfile> users, Context context) {
            this.userList = users;
            this.context = context;
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.match_card, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.nameText = (TextView) rowView.findViewById(R.id.nameText);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                viewHolder.bioText = (TextView) rowView.findViewById(R.id.bioText);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameText.setText(userList.get(position).getName() + "");
            viewHolder.bioText.setText(userList.get(position).getBio() + "");
            String imageString = userList.get(position).getImage();

            byte[] decodedImage = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            viewHolder.cardImage.setImageBitmap(imageBitmap);



            return rowView;
        }
    }
}
