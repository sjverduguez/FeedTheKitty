package com.example.stephanie.feedthekitty;

import android.content.Intent;

/**
 * Created by Stephanie on 11/29/17.
 */

public class Event {

    private String eventTitle; //event name
    private Integer fundTotal;  //fund amount raised

    private static final String TAG = " Event Class";


    public Event(){

    }

    public Event(Intent intent){

        this.eventTitle = intent.getStringExtra("eventTitle");
        this.fundTotal = intent.getIntExtra("fundTotal", 0);

    }

    public Intent packageToIntent(){
        Intent intent = new Intent();

        intent.putExtra("eventTitle", eventTitle);
        intent.putExtra("fundTotal", fundTotal);

        return intent;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getFundTotal() {
        return fundTotal;
    }

    public void setFundTotal(Integer fundTotal) {
        this.fundTotal = fundTotal;
    }

    public void addFunds(Integer amt){
        fundTotal += amt;
    }

}
