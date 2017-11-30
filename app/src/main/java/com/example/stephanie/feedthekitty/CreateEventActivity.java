package com.example.stephanie.feedthekitty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Stephanie Verduguez 11/29/2017
 */

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventTitle;
    private EditText fundTotal;

   private Button createEventButton;

    private static final String TAG = " Create Event Activity";

    //TODO: need to create account for WePay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventTitle = (EditText) findViewById(R.id.eventTitle);
        fundTotal = (EditText) findViewById(R.id.eventFund_total);

        createEventButton = (Button) findViewById(R.id.createEvent_button);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Event event = new Event();


                /*Check if all input fields are initialized, otherwise package
                * intent and set Result */

                if(eventTitle.getText().toString().matches("")||
                        fundTotal.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Must initialize all fields",Toast.LENGTH_SHORT).show();

                }else{

                    event.setEventTitle(eventTitle.getText().toString());
                    event.setFundTotal(Integer.parseInt(fundTotal.getText().toString()));

                    setResult(RESULT_OK, event.packageToIntent());
                    finish();

                }

            }
        });





    }
}
