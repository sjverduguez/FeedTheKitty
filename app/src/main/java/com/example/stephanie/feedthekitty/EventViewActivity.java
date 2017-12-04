package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.Set;

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


        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);


        setContentView(R.layout.activity_view_events);



        Set<String> tempSet = prefs.getStringSet("host_set", null);
        if (tempSet != null)
            hostingEventArrayList.addAll(tempSet);

        tempSet = prefs.getStringSet("attend_set", null);
        if (tempSet != null)
            attendingEventArrayList.addAll(tempSet);

        if (savedInstanceState != null) {
            hostingEventArrayList = savedInstanceState.getStringArrayList("host_list");
            attendingEventArrayList = savedInstanceState.getStringArrayList("attend_list");
        }

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
    public void onRestart() {
        super.onRestart();
        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        Set<String> tempSet = prefs.getStringSet("host_set", null);
        if (tempSet != null)
            hostingEventArrayList.addAll(tempSet);

        tempSet = prefs.getStringSet("attend_set", null);
        if (tempSet != null)
            attendingEventArrayList.addAll(tempSet);

        hostingAdapter.notifyDataSetChanged();
        attendingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        WePay.updatePendingCheckouts(getApplicationContext());
    }

    @Override
    public void onStop(){
        super.onStop();
        final SharedPreferences prefs_stp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor_stp = prefs_stp.edit();
        Set<String> host_set = new HashSet<String>();
        Set<String> attend_set = new HashSet<String>();

        host_set.addAll(hostingEventArrayList);
        attend_set.addAll(attendingEventArrayList);
        editor_stp.putStringSet("host_set", host_set);
        editor_stp.putStringSet("attend_set", attend_set);
        editor_stp.commit();

        hostingEventArrayList = new ArrayList<String>();
        attendingEventArrayList = new ArrayList<String>();

    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putStringArrayList("host_list", hostingEventArrayList);
        outstate.putStringArrayList("attend_list", attendingEventArrayList);
    }

}

