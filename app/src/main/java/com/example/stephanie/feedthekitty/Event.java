package com.example.stephanie.feedthekitty;

import android.content.Intent;

/**
 * Created by Stephanie on 11/29/17.
 */

public class Event {

    private String eventTitle; //event name
    private String eventID;

    private static final String TAG = " Event Class";


    public Event(){

    }

    public Event(Intent intent){
        this.eventID = intent.getStringExtra(eventID);
        this.eventTitle = "name goes here";

    }

    public Intent packageToIntent(){
        Intent intent = new Intent();
        intent.putExtra("eventTitle", eventTitle);
        return intent;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventId() {
        return eventID;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

}
