package com.example.stephanie.feedthekitty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

   private Button createEventButton;

   private DatabaseReference mRef;

    private static final String TAG = " Create Event Activity";

    //TODO: need to create account for WePay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com/");

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

                    String eventName = eventTitle.getText().toString();

                    String fundGoal = fundTotal.getText().toString();

                    DatabaseReference eventNameRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");

                   DatabaseReference eventNameChildRef = eventNameRef.child(eventName);

                   DatabaseReference setEventName = eventNameChildRef.child("Fund Goal");

                    setEventName.setValue("$" + fundGoal);


                    event.setEventTitle(eventTitle.getText().toString());
                    event.setFundTotal(Integer.parseInt(fundTotal.getText().toString()));

                    setResult(RESULT_OK, event.packageToIntent());
                    finish();

                }

            }
        });





    }
}
