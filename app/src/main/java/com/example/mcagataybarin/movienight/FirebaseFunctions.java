package com.example.mcagataybarin.movienight;

import android.util.Log;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.example.mcagataybarin.movienight.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by mcagataybarin on 3/15/17.
 */

class FirebaseFunctions {
    private DatabaseReference mDatabase;
    protected ArrayList<Movie> upcoming_movies = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected String currentWeek = "";
    protected String user_id;
    protected String user_pp_url = "";
    private static final FirebaseFunctions ourInstance = new FirebaseFunctions();
    private Movie movie;
    private User user;

    private TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
    private Task dbTask = dbSource.getTask();

    static FirebaseFunctions getInstance() {
        return ourInstance;
    }

    private FirebaseFunctions() {
    }


    // TODO: Implement the query.
    // Returns the events of a user by its id.
    public ArrayList<Event> getUserEventsById(String id) {
        ArrayList<Event> events = new ArrayList<>();
        Event event = new Event();
        event.city = "Mugla";
        event.movie = "GORA";
        event.date = "20.04.2017";
        event.creator = "asdasd";
        events.add(event);
        return events;
    }

    public String getCurrentUserId() {
        return user_id;
    }

    public String getCurrentUserPp() {
        return user_pp_url;
    }

    public String get_random_id() {
        int length = 15;
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return sb.toString();

    }

    public void postEvent(Event new_event) {
        //Event new_event = new Event();
        //new_event.event_id = ff.get_random_id();
        //new_event.creator = ff.getCurrentUserId();

//        new_event.movie = "2";
//        new_event.city = "Angara";
//        new_event.date = "20/03/2017";
//        new_event.hour = "8";
//        new_event.min = "30";

        new_event.creator = getCurrentUserId();
        new_event.week = getCurrentWeek();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String event_id = mDatabase.child("events").push().getKey();
        new_event.event_id = event_id;
        mDatabase.child("events").child(event_id).setValue(new_event);
    }

    public String getCurrentWeek() {

        return currentWeek;
    }


}