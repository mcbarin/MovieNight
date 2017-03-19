package com.example.mcagataybarin.movienight;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.example.mcagataybarin.movienight.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mcagataybarin on 3/15/17.
 */

class FirebaseFunctions {
    private DatabaseReference mDatabase;
    private ArrayList<Movie> upcoming_movies = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final FirebaseFunctions ourInstance = new FirebaseFunctions();

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
    public Movie getMovieByWeekAndIndex(String week, String index){
        Movie movie;

        movie = upcoming_movies.get(0);
        return movie;
    }

    // TODO: Implement the query.
    // Returns the user object by its id.
    public User getUserById(String id){
        User user = new User();
        user.pp_url = "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAATTAAAAJGYyMTJiOTk0LTE3MTktNDc1OC1hMDIyLTEzYWQ4NjAyOWMwZA.jpg";
        user.username = "Mehmet Cagatay Barin";

        return user;
    }

    // TODO: Implement the query.
    // Returns the created events for a movie.
    public ArrayList<Event> getMovieEvents(String week, String index){
        ArrayList<Event> events = new ArrayList<>();
        Event event = new Event();
        event.city = "Mugla";
        event.movie = "GORA";
        event.date = "20.04.2017";
        event.creator = "asdasd";
        events.add(event);

        return events;
    }

    // TODO: Implement the query.
    // Returns the events of a user by its id.
    public ArrayList<Event> getUserEventsById(String id){
        ArrayList<Event> events = new ArrayList<>();
        Event event = new Event();
        event.city = "Mugla";
        event.movie = "GORA";
        event.date = "20.04.2017";
        event.creator = "asdasd";
        events.add(event);
        return events;
    }

    public String getCurrentUserId(){
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
    }

    // TODO: For now its 0, implement later.
    public String getCurrentWeek(){
        return "0";
    }
}