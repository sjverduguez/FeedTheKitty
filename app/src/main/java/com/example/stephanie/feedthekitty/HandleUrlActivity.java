package com.example.stephanie.feedthekitty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HandleUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_url);

        Uri data = getIntent().getData();
        String eventId = data.getPath();

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.stephanie.FeedTheKitty", MODE_MULTI_PROCESS);

        if (sharedPreferences.contains("AccessToken")){
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            intent.putExtra("EVENT_ID", eventId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }

}
