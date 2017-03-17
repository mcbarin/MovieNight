package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get index of which movie to show.
        Intent intent = getIntent();
        int movie_index = intent.getIntExtra("movie_index", 0);
        Movie movie = FirebaseFunctions.getInstance().retrieveMovies().get(movie_index);

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

    }
}
