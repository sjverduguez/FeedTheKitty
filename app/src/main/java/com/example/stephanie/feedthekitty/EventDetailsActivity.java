package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
    Created by Stephanie Verduguez on 11/29/2017
 */
public class EventDetailsActivity extends AppCompatActivity {

    TextView eventName;
    TextView fundTotal;
    TextView fundGoal;
    TextView description;

    EditText contributeAmount;


    String event_id;
    String accessToken;

    Button attend_button;
    Button contri_button;

    private static final String TAG = "Event Details Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        contributeAmount = (EditText) findViewById(R.id.detail_contribute_amount);
        fundGoal = (TextView) findViewById(R.id.fund_goal);

        eventName = (TextView) findViewById(R.id.eventName);

        fundTotal = (TextView) findViewById(R.id.total_collected);

        description = (TextView) findViewById(R.id.description);

        attend_button = (Button) findViewById(R.id.detail_attend_button);
        contri_button = (Button) findViewById(R.id.detail_contri_button);

        event_id = getIntent().getStringExtra("EVENT_ID");
        DatabaseReference events = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");

        events.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot eventDetails = dataSnapshot.child(event_id);
                eventName.setText(eventDetails.child("Name").getValue().toString());
                fundGoal.setText("Fund Goal: $" + eventDetails.child("Fund Goal").getValue().toString());
                description.setText(eventDetails.child("Description").getValue().toString());
                accessToken = eventDetails.child("AccessToken").getValue().toString();
                // fundTotal.setText("Total Collected: $" + goal);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventName.setText("Error loading event");
            }
        });

        attend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO: need firebase for this

            }
        });

        contri_button.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.example.stephanie.FeedTheKitty", Context.MODE_PRIVATE);
            String name = sharedPref.getString("UserName", "Anonymous");

            @Override
            public void onClick(View view) {
                WePay.checkout(getApplicationContext(), Integer.parseInt(event_id), name, Float.parseFloat(contributeAmount.getText().toString()), accessToken);
            }
        });

        //TODO: what else is needed to finsh class...

        //TODO: need to have list of people attending at the bottom of screen

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
                Intent intent = new Intent(EventDetailsActivity.this, EventViewActivity.class);
                startActivity(intent);
        }
        return true;
    }
}
