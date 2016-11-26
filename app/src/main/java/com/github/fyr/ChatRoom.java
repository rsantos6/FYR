package com.github.fyr;


import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


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
        this.matchesName = getIntent().getExtras().getParcelable("obj");
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        this.user = firebaseAuth.getCurrentUser();

        this.messageList = new ArrayList<MessageObject>();

        this.message = (EditText) findViewById(R.id.etMessage);
        this.listView = (ListView) findViewById(R.id.listview_chat);
        this.sendMessageButton = (Button) findViewById(R.id.buttonSend);

        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/hashMap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> hashMap = (HashMap<String, String>) dataSnapshot.getValue();
                key = hashMap.get(matchesName);//use this to find the right conversation in the hashmap in firebase, will pull out an arraylist of message objects
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        setContent(key);

        ListView lv = (ListView) findViewById(R.id.listview_chat);
        lv.setAdapter(new MessageAdapter(this, messageList));



        this.messageText = this.message.getText().toString();


        this.databaseReference.child("users").child(user.getEmail().replace(".", "") + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void Add(View view) {
            if (!message.getText().toString().equals("")){
                MessageObject msg = new MessageObject();
                msg.setName(name);
                message = (EditText) findViewById(R.id.etMessage);
                messageText = message.getText().toString();
                msg.setMessage(messageText);
                messageList.add(msg);
                HashMap<String, ArrayList<MessageObject>> hash = new HashMap<>();
                hash.put(this.key, messageList);
                databaseReference.child("chat").child("chatHashMap").setValue(hash);
                ListView listView = (ListView) findViewById(R.id.listview_chat);
                listView.setAdapter(new MessageAdapter(this,messageList));
            }
        }



    public void setContent(String key) {
        final String k = key;
        this.databaseReference.child("chat").child("chatHashMap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<MessageObject> mList = new ArrayList<MessageObject>();
                HashMap<String, ArrayList<MessageObject>> hashMap = (HashMap<String, ArrayList<MessageObject>>) dataSnapshot.getValue();
                mList = hashMap.get(k);
                actualContent(mList);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    public void actualContent(ArrayList<MessageObject> list){
        this.messageList = list;
        ListView listView = (ListView) findViewById(R.id.listview_chat);
        listView.setAdapter(new MessageAdapter(this, messageList));
    }


















    /*public Button buttonSend;
    public EditText inputMessage;
    public TextView chatConversation;

    public ArrayList<String> conversation = new ArrayList<String>();


    this.firebaseAuth = FirebaseAuth.getInstance();
    this.database = FirebaseDatabase.getInstance();
    this.databaseReference = this.database.getReference();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    this.otherUserName = "Praveen";//this would be passed in
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(user.getEmail().replace(".","")+this.otherUserName, this.conversation);
    databaseReference.child("chats").setValue(map);



    //this.roomName =
    //need to set the roomName depending on who the user is chatting with
    //this'll be done in the ListView of matches and when they click on the match


    this.buttonSend = (Button) findViewById(R.id.send);
    this.inputMessage = (EditText) findViewById(R.id.editText);
    this.chatConversation = (TextView) findViewById(R.id.message);
       /* this.databaseReference = FirebaseDatabase.getInstance().getReference().child(roomName);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                key = databaseReference.push().getKey();
                databaseReference.updateChildren(map);

                DatabaseReference messageRoot = databaseReference.child(key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put()
            }
        });*/

}
