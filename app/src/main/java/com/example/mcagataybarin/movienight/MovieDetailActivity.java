package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements EventFragment.OnListFragmentInteractionListener {
    private Movie movie;
    private int movie_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get index of which movie to show.
        Intent intent = getIntent();
        movie_index = intent.getIntExtra("movie_index", 0);
        movie = FirebaseFunctions.getInstance().upcoming_movies.get(movie_index);

        // Get the views from layout by their ids
        ImageView movieImage = (ImageView) findViewById(R.id.imageView);
        TextView movieName = (TextView) findViewById(R.id.movieName);
        TextView genreInfo = (TextView) findViewById(R.id.genreInfo);
        TextView dateInfo = (TextView) findViewById(R.id.dateInfo);
        TextView directorInfo = (TextView) findViewById(R.id.directorInfo);

        // Update the movie's information
        Picasso.with(getApplicationContext()).load(movie.image).into(movieImage);
        movieName.setText(movie.title);
        genreInfo.setText(movie.genre);
        dateInfo.setText(movie.date);
        directorInfo.setText(movie.director);


        // Create the event fragment inside activity.
        EventFragment eventFragment = EventFragment.newInstance(1, "movie", FirebaseFunctions.getInstance().getCurrentWeek(), ""+movie_index);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.eventFragment, eventFragment);
        transaction.commit();


    }

    public void addEvent(View view){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        Intent intent = new Intent(this, EventCreate.class);
        intent.putExtra("movie_id", movie_index);
        startActivity(intent);

    }

    @Override
    public void onListFragmentInteraction(Event event) {

    }
}
