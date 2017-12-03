package com.example.stephanie.feedthekitty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
    Created by Stephanie Verduguez on 11/29/2017
 */
public class EventDetailsActivity extends AppCompatActivity {

    TextView eventName;
    TextView fundTotal;
    TextView fundGoal;

    Button attend_button;
    Button contri_button;

    private static final String TAG = " Event Details Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        final String name = getIntent().getStringExtra("EVENT_NAME");
        final String goal = getIntent().getStringExtra("FUND_GOAL");

        fundGoal = (TextView) findViewById(R.id.fund_goal);
        fundGoal.setText("Fund Goal: $" + goal);

        eventName = (TextView) findViewById(R.id.detail_eventTitle_text);
        eventName.setText(name + "\n");

        fundTotal = (TextView) findViewById(R.id.total_collected);
        fundTotal.setText("Total Collected: $" + goal);

        attend_button = (Button) findViewById(R.id.detail_attend_button);
        contri_button = (Button) findViewById(R.id.detail_contri_button);

        attend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO: need firebase for this

            }
        });

        contri_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: need WePay for this
            }
        });

        //TODO: what else is needed to finsh class...

        //TODO: need to have list of people attending at the bottom of screen

    }

}
