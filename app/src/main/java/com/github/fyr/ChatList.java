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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

/*
This class is the list of conversations between the user and the
person that they matched to. The user can click on person and
go to the conversation.

How the chat was implemented in on our firebase database a hashmap of
all of the chats are being stored as an arraylist full of message objects
These message objects contain the senders name and the actual message
(both in the form of a string). In the hashmap the arraylist is the value
and the key is a randomly generated key.

The random String key is only generated when the user first matches to
another person. The random String is placed in a hashmap, also stored
in the firebase database. Unlike the first hashmap, this hashmap is stored
in the user's profile and the matched person's profile. The random String is
the value and the key is the matched person's name (for the matched person's
hashmap the key is the user's name because for them that is the matched person).
This way both people are able to access the hashmap that contains the conversation.

The next issue to be solved is now how to get this random key out of the hashmap?
The name of the matched person has to be stored for both people. Each person has
another arraylist in their firebase database profile. This arraylist contains a
ListViewObject of all of their matches. These ListViewObjects contain the matched
person's name and email. These ListViewObjects are used to populate the list view
in this class. Then when the user clicks on that person (with the intention of
going to the chat) the name of that person is pulled out of the textview, then
plugged into the hashmap to retrieve the previously randomly generated String, and
finally this string is used to retrieve the arraylist of the message objects from
the other hashmap. These message objects are used to populate the arraylist in the
chatroom class.

When a user sends a message, a new messageobject is merely placed in the arraylist which is put
back into the hashmap and firebase is updated.


 */

public class ChatList extends AppCompatActivity {
    public DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    public FirebaseUser user;
    public ArrayList<ListViewObjects> matches = new ArrayList<ListViewObjects>();
    public HashMap<String, String> userHashMap = new HashMap<String, String>();
    public String key;
    public String nameOfUser;
    public String matchesEmail;




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


        /*
        This creates the functionality for the spinner on the top right of pretty much every
        page. If the user clicks the arrow a menu scrolls down with certain pages the user
        can access if they click on it. If the user tries to click on a page they're already on
        a Toast will appear stating that they are already on that page.
         */
        Spinner Spinner = (Spinner) findViewById(R.id.spinner);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (i == 1) {
                    intent = new Intent(ChatList.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i == 2) {
                    intent = new Intent(ChatList.this, HomePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i==3){
                    Toast.makeText(ChatList.this, "Already in the Chat Room", Toast.LENGTH_SHORT).show();//if clicks home page and on home
                }
                if (i == 4) {
                    intent = new Intent(ChatList.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i == 5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ChatList.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ChatList.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = getIntent();
        // If we are coming from the matchmaking activity
        //then there will be extras
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("obj")) {
                UserProfile matched = extras.getParcelable("obj");
                //create a new listviewobject for this matched person
                //as we want to add this to the arraylist of
                //matched people so we can populate the listview
                ListViewObjects lv = new ListViewObjects();
                lv.setEmail(matched.getEmail().toLowerCase());
                this.matchesEmail = matched.getEmail().toLowerCase();
                lv.setName(matched.getName());
                this.matches.add(lv);//insert the matched person into the arraylist

                /*
                This section of code grabs the current user's name from firebase
                in order to create a listviewobject to place in the other person's
                arraylist of matches
                 */
                this.databaseReference.child("users").child(this.user.getEmail().replace(".", "") + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = (String) dataSnapshot.getValue();
                        setUserName(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*
                This section of code updates the other person's matches arraylist
                 */

                this.databaseReference.child("users").child(matched.getEmail().replace(".", "") + "/matches").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<HashMap> match = (ArrayList<HashMap>) dataSnapshot.getValue();//get the arraylist of matches
                        //firebase stores things as hashmap unfortunately so I have
                        //to pull out the arraylist from multiple hashmaps
                        ArrayList<ListViewObjects> mlo = new ArrayList<ListViewObjects>();
                        //firebase doesn't actually store an arrarylist so
                        //I have to remake the arraylist
                        if (match != null){//if there is an existing arraylist of matches
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


                        setMatches(mlo);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*
                This section of code updates the users match arraylist
                 */
                this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/matches").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<HashMap> match = (ArrayList<HashMap>) dataSnapshot.getValue();//get the arraylist of matches
                        //firebase stores things as hashmap unfortunately so I have
                        //to pull out the arraylist from multiple hashmaps
                        ArrayList<ListViewObjects> mlo = new ArrayList<ListViewObjects>();
                        //firebase doesn't actually store an arrarylist so
                        //I have to remake the arraylist
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
                //randomly generate a string which will be used as the key
                char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                this.key = sb.toString();
                this.userHashMap.put(matched.getName(),key);


                /*
                This section of code updates the user's hashmap
                 */
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

                /*
                This section of code updates the other person's hashmap
                 */
                this.databaseReference.child("users").child(this.matchesEmail.replace(".", "") + "/hashMap").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> hash = (HashMap<String, String>) dataSnapshot.getValue();//get the arraylist of matches
                        setOtherHashContent(hash);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*
                Since these people just matched this section of code creates
                a new hashmap with a new conversation
                 */
                this.databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String, ArrayList<MessageObject>> hashMap = (HashMap<String, ArrayList<MessageObject>>) dataSnapshot.getValue();
                        setHash(hashMap);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }else{
            /*
            If the user does not access this page after just matching with
            someone, using the spinner for instance
             */

            /*
            This section of code retrieves the user's arraylist of matches in order to populate
            the listview
             */
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
        }
    }

    /*
    This method sets the other person's hashmap
    by retrieving their existing hashmap (if it
    does actually exist) and adding the new value
    and key.
     */
    private void setOtherHashContent(HashMap<String, String> hash) {
        HashMap<String, String> temp = new HashMap<String, String>();
        if(hash != null){
            temp.putAll(hash);
        }
        HashMap<String, String> newHash = new HashMap<String, String>();
        newHash.put(this.nameOfUser, this.key);
        temp.putAll(newHash);
        this.databaseReference.child("users").child(this.matchesEmail.replace(".", "") + "/hashMap").setValue(temp);
    }

    /*
    This method updates the other person's
    arraylist of matches by adding a listviewobject
    of this user's info
     */
    private void setMatches(ArrayList<ListViewObjects> mlo) {
        ListViewObjects userProfile = new ListViewObjects();
        userProfile.setName(this.nameOfUser);
        userProfile.setEmail(this.user.getEmail());
        mlo.add(userProfile);
        this.databaseReference.child("users").child(this.matchesEmail.replace(".","") + "/matches").setValue(mlo);
    }

    private void setUserName(String name) {
        this.nameOfUser = name;
    }

    /*
    This method creates a new chat by initializing a
    new arraylist with a welcome messageobject.
    This is put into the hashmap with the random
    String key and updated in firebase
     */
    public void setHash(HashMap<String, ArrayList<MessageObject>> hashing){
        MessageObject ms = new MessageObject();//for testing create a messsage
        ms.setMessage("Welcome to the chat room!");
        ms.setName("FYR");
        ArrayList<MessageObject> ml = new ArrayList<>();//this represents the actual conversation
        ml.add(ms);
        HashMap<String, ArrayList<MessageObject>> hash = new HashMap<>();
        hash.put(this.key, ml);//hashmap with the messagelist as the value, access it by using randomly generated String
        HashMap<String, ArrayList<MessageObject>> temp = new HashMap<String, ArrayList<MessageObject>>();
        if(hashing != null){
            temp.putAll(hashing);
        }
        temp.putAll(hash);
        this.databaseReference.child("chat").setValue(temp);
    }

    /*
    This method updates the user's hashmap
    retrieving the existing hashmap and
    adding to it and updating firebase
     */
    public void setHashContent(HashMap<String,String> hash){
        HashMap<String, String> temp = new HashMap<String, String>();
        if(hash != null){
            temp.putAll(hash);
        }
        temp.putAll(this.userHashMap);
        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/hashMap").setValue(temp);
    }

    /*
    This method retrieves all existing matches
    from firebase (complicated by firebase's love
    of hashmaps) and uses them to populate the list view
     */
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

    /*
    This method goes to the chat room, depending on which
    matched person the user clicked on
     */
    public void goToConversation(View view){
        View parentView = (View) view.getParent();
        TextView textView = (TextView) parentView.findViewById(R.id.match_name);//whichever person the user clicks on, get their name
        final String nameOfText = textView.getText().toString();
        this.databaseReference.child("users").child(user.getEmail().replace(".", "")).child("hashMap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();//gets the randomly generated String using the match's name
                String k = hashMap.get(nameOfText);//use this to find the right conversation in the hashmap in firebase, will pull out an arraylist of message objects
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