package com.github.fyr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ChatList extends AppCompatActivity {
    public DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    public FirebaseUser user;
    public ArrayList<ListViewObjects> matches = new ArrayList<ListViewObjects>();
    public HashMap<String, String> userHashMap = new HashMap<String, String>();




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

        Intent intent = getIntent();
        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("obj")) {
                UserProfile matched = extras.getParcelable("obj");
                ListViewObjects lv = new ListViewObjects();
                lv.setEmail(matched.getEmail());
                lv.setName(matched.getName());
                this.matches.add(lv);

                this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/matches").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<HashMap> match = (ArrayList<HashMap>) dataSnapshot.getValue();//get the arraylist of matches
                        ArrayList<ListViewObjects> mlo = new ArrayList<ListViewObjects>();
                        if (match != null){
                            for(int i =0; i<match.size();i++) {
                                HashMap bigHash = match.get(i);
                                String name = (String) bigHash.get("name");
                                String email = (String) bigHash.get("email");
                                ListViewObjects lvo = new ListViewObjects();
                                lvo.setName(name);
                                lvo.setEmail(email);
                                mlo.add(lvo);
                            }
                        }


                        setContent(mlo);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();//randomly generate a string which will be used as the key
                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                String key = sb.toString();
                this.userHashMap.put(matched.getName(),key);



                this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/hashMap").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> hash = (HashMap<String, String>) dataSnapshot.getValue();//get the arraylist of matches
                        setHashContent(hash);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                MessageObject ms = new MessageObject();//for testing create a messsage
                ms.setMessage("Welcome to the chat room!");
                ms.setName("FYR");
                ArrayList<MessageObject> ml = new ArrayList<>();//this represents the actual conversation
                ml.add(ms);
                HashMap<String, ArrayList<MessageObject>> hash = new HashMap<>();
                hash.put(key, ml);//hashmap with the messagelist as the value, access it by using randomly generated String
                this.databaseReference.child("chat").setValue(hash);




            }
        }
    }

    public void setHashContent(HashMap<String,String> hash){
        HashMap<String, String> temp = new HashMap<String, String>();
        if(hash != null){
            temp.putAll(hash);
        }
        temp.putAll(this.userHashMap);
        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/hashMap").setValue(temp);
    }
    public void setContent(ArrayList<ListViewObjects>mlo){
        if (!mlo.isEmpty()){
            for(int i=0;i<mlo.size();i++){
                ListViewObjects lv = mlo.get(i);
                this.matches.add(lv);
            }
        }

        this.databaseReference.child("users").child(user.getEmail().replace(".","") + "/matches").setValue(this.matches);
        ListView lv = (ListView) findViewById(R.id.chatList);//populate the listview with their name
        lv.setAdapter(new ChatListAdapter(ChatList.this,this.matches));

    }

    public void goToConversation(View view){
        TextView textView = (TextView) findViewById(R.id.match_name);//whichever person the user clicks on, get their name
        final String name = textView.getText().toString();
        this.databaseReference.child("users").child(user.getEmail().replace(".", "")).child("hashMap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();//gets the randomly generated String using the match's name
                String k = hashMap.get(name);//use this to find the right conversation in the hashmap in firebase, will pull out an arraylist of message objects
                Intent intent = new Intent(ChatList.this, ChatRoom.class).putExtra("obj", k);
                //as a key to retrieve the randomly generated String which is used to get the arraylist representing the conversation
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}