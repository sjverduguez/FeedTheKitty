package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Stephanie Verduguez 11/29/2017
 */

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventTitle;
    private EditText fundTotal;
    private EditText description;

    private Button createEventButton;

    private static final String TAG = " Create Event Activity";

    //TODO: need to create account for WePay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventTitle = (EditText) findViewById(R.id.eventTitle);
        fundTotal = (EditText) findViewById(R.id.eventFund_total);
        description = (EditText) findViewById(R.id.eventDescription);

        createEventButton = (Button) findViewById(R.id.createEvent_button);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Event event = new Event();


                /*Check if all input fields are initialized, otherwise package
                * intent and set Result */

                if(eventTitle.getText().toString().matches("")||
                        fundTotal.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Must initialize all fields",Toast.LENGTH_SHORT).show();

                }else{

                    /*Add Event name to firebase and add fund goal as a child of event name.
                    * We can change fund goal to be funds raised once we integrate with WePay*/

                    String eventName = eventTitle.getText().toString();
                    String fundGoal = fundTotal.getText().toString();
                    String eventDescription = description.getText().toString();

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.example.stephanie.FeedTheKitty", Context.MODE_PRIVATE);
                    String accessToken = sharedPref.getString("AccessToken", null);
                    Log.i(TAG, "AccessToken: " + accessToken);
                    WePay.createAccount(getApplicationContext(), eventName, eventDescription, 10, null, accessToken, fundGoal);


                    /*DatabaseReference eventNameRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");

                    DatabaseReference eventNameChildRef = eventNameRef.child(eventName);


                    DatabaseReference setEventName = eventNameChildRef.child("Fund Goal");

                    setEventName.setValue("$" + fundGoal); */



                    /*event.setEventTitle(eventTitle.getText().toString());
                    event.setFundTotal(Integer.parseInt(fundTotal.getText().toString()));

                    setResult(RESULT_OK, event.packageToIntent());

                    Intent intent = new Intent(CreateEventActivity.this, EventDetailsActivity.class);
                    intent.putExtra("EVENT_NAME", eventName);
                    startActivity(intent); */

                    //finish();

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent intent = new Intent(CreateEventActivity.this, EventViewActivity.class);
                startActivity(intent);
        }
        return true;
    }

}
