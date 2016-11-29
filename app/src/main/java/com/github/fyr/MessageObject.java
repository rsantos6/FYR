package com.github.fyr;

/**
 * Created by RussBuss on 11/22/2016.
 */

/*
This class represents an individual message that a person
sends, containing the actual message and the person's name
which will both show in the chat
 */
public class MessageObject {
    public String message;
    public String name;

    public MessageObject(){

    }

    public void setName(String userName){

        this.name = userName;
    }

    public void setMessage(String userMessage){

        this.message = userMessage;
    }

    public String getName(){

        return this.name;
    }

    public String getMessage() {

        return this.message;
    }
}
