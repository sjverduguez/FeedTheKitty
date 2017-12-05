package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    static ArrayList<String> hostingEventArrayListIds = new ArrayList<String>();
    static ArrayList<String> attendingEventArrayListIds = new ArrayList<String>();

    static ArrayAdapter<String> hostingAdapter;
    static ArrayAdapter<String> attendingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);


        setContentView(R.layout.activity_view_events);

        attendingListView = (ListView) findViewById(R.id.attendingList);
        hostingListView = (ListView) findViewById(R.id.hostingList);

        hostingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, hostingEventArrayList);
        attendingAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, attendingEventArrayList);

        Set<String> tempSet = prefs.getStringSet("host_set", null);
        if (tempSet != null) {
            for (String eventId:  tempSet) {
                addInitialHostingEvent(eventId);
            }
        }

        tempSet = prefs.getStringSet("attend_set", null);
        if (tempSet != null) {
            for (String eventId:  tempSet) {
                addInitialAttendingEvent(eventId);

            }
        }

        createEventButton = (Button) findViewById(R.id.createButton);


        hostingListView.setAdapter(hostingAdapter);
        attendingListView.setAdapter(attendingAdapter);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventViewActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });


        hostingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", hostingEventArrayListIds.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
        attendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", attendingEventArrayListIds.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }


    public static void addInitialHostingEvent(final String eventId) {
        Log.i("EventView", "Adding to hosting events");
        DatabaseReference events = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");
        DatabaseReference eventDetails = events.child(eventId);


        eventDetails.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String name = dataSnapshot.child("Name").getValue().toString();
                   hostingEventArrayList.add(name);
                   hostingEventArrayListIds.add(eventId);
                   hostingAdapter.notifyDataSetChanged();
               }

               @Override
                public void onCancelled(DatabaseError error) {

               }
           });
    }

    public static void addInitialAttendingEvent(final String eventId) {
        DatabaseReference events = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");
        DatabaseReference eventDetails = events.child(eventId);


        eventDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                attendingEventArrayList.add(name);
                attendingEventArrayListIds.add(eventId);
                attendingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        Set<String> tempSet = prefs.getStringSet("host_set", null);
        if (tempSet != null) {
            for (String eventId:  tempSet) {
                addInitialHostingEvent(eventId);
            }
        }

        tempSet = prefs.getStringSet("attend_set", null);
        if (tempSet != null) {
            for (String eventId:  tempSet) {
                addInitialAttendingEvent(eventId);
            }
        }

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

        host_set.addAll(hostingEventArrayListIds);
        attend_set.addAll(attendingEventArrayListIds);
        editor_stp.putStringSet("host_set", host_set);
        editor_stp.putStringSet("attend_set", attend_set);
        editor_stp.commit();

        hostingEventArrayList = new ArrayList<String>();
        attendingEventArrayList = new ArrayList<String>();
        hostingEventArrayListIds = new ArrayList<String>();
        attendingEventArrayListIds = new ArrayList<String>();

    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putStringArrayList("host_list", hostingEventArrayListIds);
        outstate.putStringArrayList("attend_list", attendingEventArrayListIds);
    }

}

