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
    ListView attendingListView;
    ListView hostingListView;

    static ArrayList<String> eventArrayList = new ArrayList<String>();
    static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);


        createEventButton = (Button) findViewById(R.id.createButton);
        attendingListView = (ListView) findViewById(R.id.attendingList);
        hostingListView = (ListView) findViewById(R.id.hostingList);

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, eventArrayList);
        hostingListView.setAdapter(adapter);

        hostingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(EventViewActivity.this, EventDetailsActivity.class);




            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventViewActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });

        WePay.updatePendingCheckouts(getApplicationContext());
    }

    public static void addEvent(Context context, String eventName) {
        eventArrayList.add(eventName);
        adapter.notifyDataSetChanged();
    }

}
