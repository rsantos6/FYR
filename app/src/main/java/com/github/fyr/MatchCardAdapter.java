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
//import com.github.fyr.MatchMakingActivity.ViewHolder;


import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Brad on 11/23/16.
 */


public class MatchCardAdapter { //  extends BaseAdapter {

    public List<UserProfile> userList;
    public Context context;

    public MatchCardAdapter(List<UserProfile> users, Context context) {
        this.userList = users;
        this.context = context;
    }

/**
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
        ViewHolder vh = new ViewHolder();

        if (rowView == null) {

            LayoutInflater inflater = getLayoutInflater();
            rowView = inflater.inflate(R.layout.match_card, parent, false);
            // configure view holder

            //ViewHolder = new MatchMakingActivity.ViewHolder();
            vh.nameText = (TextView) rowView.findViewById(R.id.nameText);
            vh.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
            vh.bioText = (TextView) rowView.findViewById(R.id.bioText);
            rowView.setTag(vh);

        } else {
            vh = (MatchMakingActivity.ViewHolder) convertView.getTag();
        }

        vh.nameText.setText(userList.get(position).getName() + "");
        vh.bioText.setText(userList.get(position).getBio() + "");
        String imageString = userList.get(position).getImage();

        byte[] decodedImage = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
        vh.cardImage.setImageBitmap(imageBitmap);


        return rowView;
    }
    **/
}