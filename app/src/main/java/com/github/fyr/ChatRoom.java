package com.github.fyr;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.Contacts.SettingsColumns.KEY;

/*
This class is the individual chat between the user and the matched
person. Conversations are saved on firebase so when the user
goes back into the chat room (after leaving) all of their messages
are still here.

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

public class ChatRoom extends AppCompatActivity {
    public EditText message;
    public String messageText;
    public String name;
    public Button sendMessageButton;

    public ListView listView;
    public ArrayList<MessageObject> messageList;
    public Handler handler = new Handler();
    public DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    public FirebaseUser user;
    public String matchesName;
    public String key;
    public HashMap hashMap;



    //put conversations in hashmap with a random generated key
    //userprofile object will have a hashmap
    //the value of the hashmap will be these randomly generated key
    //the key to this hashmap will be the other persons name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                    intent = new Intent(ChatRoom.this, ProfilePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i == 2) {
                    intent = new Intent(ChatRoom.this, HomePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i==3){
                    intent = new Intent(ChatRoom.this, ChatList.class);//This will be the MatchesPage
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
                }
                if (i == 4) {
                    intent = new Intent(ChatRoom.this, AboutPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
                if (i == 5) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(ChatRoom.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ChatRoom.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            //retrieve the random key
            this.key = extras.getString("obj");
        }

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        this.user = firebaseAuth.getCurrentUser();


        this.message = (EditText) findViewById(R.id.etMessage);
        this.listView = (ListView) findViewById(R.id.listview_chat);
        this.sendMessageButton = (Button) findViewById(R.id.buttonSend);


        /*
        This section of code retrieves the hashmap
        that contains the conversation between the two
        people
         */
        this.databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                hashMap = (HashMap<String, ArrayList<HashMap>>) dataSnapshot.getValue();
                ArrayList<HashMap> mList = (ArrayList<HashMap>) hashMap.get(key);

                /*
                This section of code creates a list of messageobjects
                from the existing conversation in order to populate
                the listview
                 */
                ArrayList<MessageObject> messageTempList = new ArrayList<MessageObject>();
                for(int i = 0; i<mList.size();i++){
                    HashMap bigHash =mList.get(i);
                    String name = (String) bigHash.get("name");
                    String message = (String) bigHash.get("message");
                    MessageObject newMessage = new MessageObject();
                    newMessage.setName(name);
                    newMessage.setMessage(message);
                    messageTempList.add(newMessage);

                }
                setContent(messageTempList);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        //message that the user typed
        this.messageText = this.message.getText().toString();


        /*
        This section of code gets the user's name in order to create the
        messageobject
         */
        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.getValue();//gets the current user's name used for the messageobject
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    This section of code populates the listview
    with the existing conversation
     */
    private void setContent(ArrayList<MessageObject> m) {
        this.messageList = m;
        ListView listView = (ListView) findViewById(R.id.listview_chat);
        listView.setAdapter(new MessageAdapter(this, m));
    }

/*
This method sends the message in the form of a message object
The hashmap on firebase is updated and the listview is re-populated
to include the new message
 */
    public void Add(View view) {
        if (!message.getText().toString().equals("")){
            MessageObject msg = new MessageObject();
            msg.setName(name);
            message = (EditText) findViewById(R.id.etMessage);
            messageText = message.getText().toString();
            message.setText("");
            msg.setMessage(messageText);
            messageList.add(msg);
            //HashMap<String, ArrayList<MessageObject>> hash = new HashMap<>();
            hashMap.put(key, messageList);
            databaseReference.child("chat").setValue(hashMap);
            ListView listView = (ListView) findViewById(R.id.listview_chat);
            listView.setAdapter(new MessageAdapter(this,messageList));
        }
    }




}