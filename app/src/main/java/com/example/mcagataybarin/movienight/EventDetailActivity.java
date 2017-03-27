package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EventDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        // Get the arguments from intent.
        Intent intent = getIntent();
        String event_id = intent.getStringExtra("event_id");

//        // Retrieve event object from database
//        FirebaseFunctions.getInstance().getEventById(new Runnable() {
//            @Override
//            public void run() {
//                event = FirebaseFunctions.getInstance().temp_event;
//
//            }
//        }, event_id);

    }
}
