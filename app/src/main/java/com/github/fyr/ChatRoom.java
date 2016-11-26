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



    //push messageObject to firebase
    //set context would just be getting messageObject from firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        this.user = firebaseAuth.getCurrentUser();
        this.messageList = new ArrayList<MessageObject>();

        this.message = (EditText) findViewById(R.id.etMessage);
        this.listView = (ListView) findViewById(R.id.listview_chat);
        this.sendMessageButton = (Button) findViewById(R.id.buttonSend);
        setContent();

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
                databaseReference.child("chat").child(user.getEmail().replace(".","")).setValue(messageList);
                ListView listView = (ListView) findViewById(R.id.listview_chat);
                listView.setAdapter(new MessageAdapter(this,messageList));
            }
        }



    public void setContent() {
        final ArrayList<MessageObject> mList = new ArrayList<MessageObject>();
        this.databaseReference.child("chat").child(user.getEmail().replace(".", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) postSnapshot.getValue();
                    String name = hashMap.get("name").toString();
                    String message = hashMap.get("message").toString();
                    MessageObject m = new MessageObject();

                    m.setMessage(message);
                    m.setName(name);
                    mList.add(m);
                    actualContent(mList);
                }
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
