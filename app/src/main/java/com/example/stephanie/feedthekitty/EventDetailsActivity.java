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

    Button attend_button;
    Button contri_button;

    private static final String TAG = " Event Details Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        final String name = getIntent().getStringExtra("EVENT_NAME");

        Event event = new Event(getIntent());

        eventName = (TextView) findViewById(R.id.detail_eventTitle_text);
        eventName.setText(name);

        fundTotal = (TextView) findViewById(R.id.detail_total_text);
        fundTotal.setText("$" + event.getFundTotal().toString());

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
