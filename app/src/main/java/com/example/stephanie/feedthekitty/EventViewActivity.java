package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventViewActivity extends AppCompatActivity {

    Button createEventButton;

    static ListView attendingListView;
    static ListView hostingListView;

    static ArrayList<String> hostingEventArrayList = new ArrayList<String>();
    static ArrayList<String> attendingEventArrayList = new ArrayList<String>();

    static ArrayAdapter<String> hostingAdapter;
    static ArrayAdapter<String> attendingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);


        createEventButton = (Button) findViewById(R.id.createButton);
        attendingListView = (ListView) findViewById(R.id.attendingList);
        hostingListView = (ListView) findViewById(R.id.hostingList);

        hostingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, hostingEventArrayList);
        attendingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, attendingEventArrayList);

        hostingListView.setAdapter(hostingAdapter);
        attendingListView.setAdapter(attendingAdapter);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventViewActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void addHostingEvent(String eventName) {
        hostingEventArrayList.add(eventName);
        hostingAdapter.notifyDataSetChanged();
    }

    public static void addAttendingEvent(String eventName) {
        attendingEventArrayList.add(eventName);
        attendingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        WePay.updatePendingCheckouts(getApplicationContext());
    }

}

