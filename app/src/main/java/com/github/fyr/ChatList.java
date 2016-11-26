package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ChatList extends AppCompatActivity {
    public DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    public FirebaseUser user;
    public ArrayList<String> matches = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        this.user = firebaseAuth.getCurrentUser();
        matches.add("Russ");
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String key = sb.toString();
        HashMap<String, String> hashing = new HashMap<>();
        hashing.put("Russ",key);
        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/hashMap").setValue(hashing);
        MessageObject ms = new MessageObject();
        ms.setMessage("hey");
        ms.setName("Russ");
        ArrayList<MessageObject> ml = new ArrayList<>();
        ml.add(ms);
        HashMap<String, ArrayList<MessageObject>> hash = new HashMap<>();
        hash.put(key, ml);
        databaseReference.child("chat").child("chatHashMap").setValue(hash);
        databaseReference.child("users").child(user.getEmail().replace(".","") + "/matches").setValue(matches);



        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/matches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matches = (ArrayList<String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ListView listView = (ListView) findViewById(R.id.chatList);
        listView.setAdapter(new ChatListAdapter(this,matches));


    }

    public void goToConversation(View view){
        TextView textView = (TextView) findViewById(R.id.match_name);
        String name = textView.getText().toString();
        Intent i = new Intent(ChatList.this, ChatRoom.class).putExtra("obj", name);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }

}
