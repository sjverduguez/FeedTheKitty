package com.example.stephanie.feedthekitty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class EventViewActivity extends AppCompatActivity {

    Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        createEventButton = findViewById(R.id.createButton);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventViewActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });

        WePay.updatePendingCheckouts(getApplicationContext());

    }

}
