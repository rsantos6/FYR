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
            this.key = extras.getString("obj");
        }

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference();
        this.user = firebaseAuth.getCurrentUser();


        this.message = (EditText) findViewById(R.id.etMessage);
        this.listView = (ListView) findViewById(R.id.listview_chat);
        this.sendMessageButton = (Button) findViewById(R.id.buttonSend);


        this.databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                hashMap = (HashMap<String, ArrayList<HashMap>>) dataSnapshot.getValue();
                ArrayList<HashMap> mList = (ArrayList<HashMap>) hashMap.get(key);

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



        //  ListView lv = (ListView) findViewById(R.id.listview_chat);//populate the listview with existing conversation between users
        //lv.setAdapter(new MessageAdapter(this, messageList));



        this.messageText = this.message.getText().toString();


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

    private void setContent(ArrayList<MessageObject> m) {
        this.messageList = m;
        ListView listView = (ListView) findViewById(R.id.listview_chat);
        listView.setAdapter(new MessageAdapter(this, m));
    }


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