package com.github.fyr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class MatchMakingActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface  {


    public FirebaseAuth firebaseAuth;

    private static final String TAG = "UserList";
    private DatabaseReference userlistReference;

    public static MatchCardAdapter cardAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<UserProfile> potentialMatches;
    private ValueEventListener mUserListListener;
    private ArrayList<String> rejectedMatches;
    private SwipeFlingAdapterView flingContainer;

    private String dist;
    private String pace;
    private String terrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        pace = intent.getStringExtra("pace");
        terrain = intent.getStringExtra("terrain");
        dist = intent.getStringExtra("dist");

        Firebase.setAndroidContext(this);
        this.firebaseAuth = FirebaseAuth.getInstance();
        potentialMatches = new ArrayList<>();
        UserProfile test = new UserProfile();
        test.setName("Thanks for Using FYR");
        test.setBio(("Swipe to view your matches!"));
        test.setImage("");
        potentialMatches.add(test);
        //

        userlistReference = FirebaseDatabase.getInstance().getReference();//.child("users");
        //userlistReference.o



        userlistReference.child("users").addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> test = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                ArrayList<UserProfile> list = convertMapToList(test);
                potentialMatches = list;

                cardAdapter.notifyDataSetChanged();

                filterMatches();

                // ideally rejected Matches would be saved in the db
                // putting it in the method that would access it.
                // db is going to save names of those who have been rejected in the
                // recent past
                rejectedMatches = new ArrayList<>();
                cardAdapter = new MatchCardAdapter(potentialMatches, MatchMakingActivity.this);
                flingContainer.setAdapter(cardAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "onCancelled: ",databaseError.toException());

            }

            public ArrayList<UserProfile> convertMapToList(HashMap<String, HashMap<String, String>> map) {
                // This is a time sucking method that would not scale. If android / firebase had an
                // ActiveRecord subsitute this method would be unnecesary
                ArrayList<UserProfile> list = new ArrayList<>();

                for (String key : map.keySet()) {
                    UserProfile temp = new UserProfile();
                    temp.setName(map.get(key).get("name"));
                    temp.setBio(map.get(key).get("bio"));
                    temp.setImage(map.get(key).get("image"));
                    temp.setPace(map.get(key).get("pace"));
                    temp.setTerrain(map.get(key).get("terrain"));
                    // Distance isn't in db yet, come back to this
                    list.add(temp);
                }
                return list;
            }
        });

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        cardAdapter = new MatchCardAdapter(potentialMatches, MatchMakingActivity.this);
        flingContainer.setAdapter(cardAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                UserProfile loser = potentialMatches.get(0);
                potentialMatches.remove(0);
                cardAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //rejectedMatches.add(loser.getName());
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                potentialMatches.remove(0);
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

    private void filterMatches() {
        int step = 0;
        // PROBLEM! users are going to be in a standard order, need to randomize it
        // but what is an efficient way to do that...
        while(potentialMatches.size() > 15 && step != 4){
            switch(step){
                case 0: filterLocation();
                    break;
                case 1: filterPace();
                    break;
                case 2: filterTerrain();
                    break;
                case 3: filterDistance();
                    break;
            }
            step++;
        }
    }

    private void filterDistance(){
        /**
         * Commenting out until dist actually saves correctly
         int index = 0;
         while(potentialMatches.size() > 15){
         for(UserProfile u : potentialMatches){
         if(u.getDistance() != dist){
         potentialMatches.remove(index);
         }
         index++;
         }
         }**/
    }

    private void filterTerrain(){
        int index = 0;
        while(potentialMatches.size() > 15){
            for(UserProfile u : potentialMatches){
                if(u.getTerrain() != terrain){
                    potentialMatches.remove(index);
                }
                index++;
            }
        }
    }

    private void filterPace(){
        int index = 0;
        while(potentialMatches.size() > 15){
            for(UserProfile u : potentialMatches){
                if(u.getPace() != pace){
                    potentialMatches.remove(index);
                }
                index++;
            }
        }
    }

    private void filterLocation(){
        int index = 0;
        while(potentialMatches.size() > 15){
            for(UserProfile u : potentialMatches){
                if(u.getDistance() != dist){
                    potentialMatches.remove(index);
                }
                index++;
            }
        }
    }

    protected  void onStart(){
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        // add the rejected list to the User's database
        // do we want to add the full list or just the ID's?
        // yes! because then you don't have to convert the
        // array list from a HashMap to a user list when
        // you pull it from the db.

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

    private static class ViewHolder {
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

            if(imageString == null){
                imageString = userlistReference.child("users").child("rsan@brandeisedu").child("image").toString();
            }
            byte[] decodedImage = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            viewHolder.cardImage.setImageBitmap(imageBitmap);


            return rowView;
        }
    }

}
