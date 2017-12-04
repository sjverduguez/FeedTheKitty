package com.example.stephanie.feedthekitty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.util.ArrayList;

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
    Button share_button;

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
        share_button = (Button) findViewById(R.id.share);

        event_id = getIntent().getStringExtra("EVENT_ID");
        DatabaseReference events = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");
        DatabaseReference eventDetails = events.child(event_id);


        eventDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventName.setText(dataSnapshot.child("Name").getValue().toString());
                fundGoal.setText("Fund Goal: $" + dataSnapshot.child("Fund Goal").getValue().toString());
                description.setText(dataSnapshot.child("Description").getValue().toString());
                // fundTotal.setText("Total Collected: $" + goal);

                accessToken = dataSnapshot.child("AccessToken").getValue().toString();

                Iterable<DataSnapshot> checkouts = dataSnapshot.child("Checkout").getChildren();

                ArrayList<String> contributionList = new ArrayList<String>();

                float totalContributed = 0;

                for (DataSnapshot snapshot : checkouts){
                    Object payer = snapshot.child("Payer").getValue();
                    Object amount = snapshot.child("Amount").getValue();
                    if (payer != null && amount != null){
                        float amt = Float.parseFloat(amount.toString());
                        totalContributed = totalContributed + amt;
                        contributionList.add(payer.toString() + " contributed $" + String.format("%.2f", amt));
                    }
                }

                fundTotal.setText("Total Collected: $" + String.format("%.2f", totalContributed));

                String[] contributions = new String[contributionList.size()];

                contributions = contributionList.toArray(contributions);

                ArrayAdapter adapter = new ArrayAdapter<String>(EventDetailsActivity.this,R.layout.contribution_list_view,contributions);

                ListView listView = (ListView) findViewById(R.id.contributions);
                listView.setAdapter(adapter);
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

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // Creates a Uri based on a base Uri and a record ID based on the contact's last name
                // Declares the base URI string
                String link = "http://feedthekittyapp.com/" + event_id;

                // Declares a path string for URIs that you use to copy data
                // Declares the Uri to paste to the clipboard
                Uri copyUri = Uri.parse(link);


                // Creates a new URI clip object. The system uses the anonymous getContentResolver() object to
                // get MIME types from provider. The clip object's label is "URI", and its data is
                // the Uri previously created.
                ClipData clip = ClipData.newUri(getContentResolver(), "URI", copyUri);
                clipboard.setPrimaryClip(clip);


                Toast.makeText(getApplicationContext(), "Event copied to clipboard", Toast.LENGTH_LONG).show();

            }
        });

        //TODO: what else is needed to finsh class...

        //TODO: need to have list of people attending at the bottom of screen

    }

    @Override
    public void onResume() {
        super.onResume();

        WePay.updatePendingCheckouts(getApplicationContext());
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
