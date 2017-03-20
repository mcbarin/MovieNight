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
    private ArrayList<Movie> upcoming_movies = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentWeek = "";
    private static final FirebaseFunctions ourInstance = new FirebaseFunctions();

    private TaskCompletionSource<DataSnapshot> dbSource = new TaskCompletionSource<>();
    private Task dbTask = dbSource.getTask();

    static FirebaseFunctions getInstance() {
        return ourInstance;
    }

    private FirebaseFunctions() {
    }

    public ArrayList<Movie> retrieveMovies() {
        if (upcoming_movies.size() == 0) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference movies_reference = mDatabase.child("movies").child("0");

            movies_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Object> movies = ((ArrayList<Object>) dataSnapshot.getValue());
                    for (int i = 0; i < movies.size(); i++) {
                        Movie movie = new Movie(((HashMap<String, String>) movies.get(i)));
                        upcoming_movies.add(movie);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        return this.upcoming_movies;
    }

    // TODO: Implement the query.
    // Returns the movie object by week and index of the movie.
    public Movie getMovieByWeekAndIndex(String week, String index) {
        Movie movie;

        movie = upcoming_movies.get(0);
        return movie;
    }

    // TODO: Implement the query.
    // Returns the user object by its id.
    public User getUserById(String id) {
        User user = new User();
        user.pp_url = "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAATTAAAAJGYyMTJiOTk0LTE3MTktNDc1OC1hMDIyLTEzYWQ4NjAyOWMwZA.jpg";
        user.name = "Mehmet Cagatay Barin";

        return user;
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
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
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

        if (currentWeek.isEmpty()) {

            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference movies_reference = mDatabase.child("movies");

            movies_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentWeek = String.valueOf(dataSnapshot.getChildrenCount() - 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        return currentWeek;
    }
}