package com.github.fyr;

/**
 * Created by RussBuss on 11/22/2016.
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
