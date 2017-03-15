package com.example.mcagataybarin.movienight;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mcagataybarin on 3/15/17.
 */

class FirebaseFunctions {
    private DatabaseReference mDatabase;
    private ArrayList<Movie> upcoming_movies = new ArrayList<>();

    private static final FirebaseFunctions ourInstance = new FirebaseFunctions();

    static FirebaseFunctions getInstance() {
        return ourInstance;
    }

    private FirebaseFunctions() {
    }

    public ArrayList<Movie> retrieveMovies() {
        if (upcoming_movies == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference movies_reference = mDatabase.child("movies");
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
}