package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class EventDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Event event;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        // Get the arguments from intent.
        Intent intent = getIntent();
        String event_id = intent.getStringExtra("event_id");
        if (event_id!= null)
            Log.i("EVENT ID", event_id);

        // Retrieve event object from database
        FirebaseFunctions.getInstance().getEventById(new Runnable() {
            @Override
            public void run() {
                event = FirebaseFunctions.getInstance().temp_event;
                // Now we have the event info. retrieve movie info
                getMovieInfo(event.week, event.movie);
            }
        }, event_id);




    }

    // Retrieve movie object from firebase.
    public void getMovieInfo(String week, String index){
        FirebaseFunctions.getInstance().getMovieByWeekAndIndex(new Runnable() {
            @Override
            public void run() {
                movie = FirebaseFunctions.getInstance().temp_movie;
                setInfoOnScreen();
            }
        }, week, index);
    }

    public void setInfoOnScreen(){
        TextView eventDetailTitle = (TextView) findViewById(R.id.eventDetailTitle);
        TextView movieLabel = (TextView) findViewById(R.id.eventMovieLabel);
        ImageView movieImage = (ImageView) findViewById(R.id.eventMovieImage);
        TextView movieName = (TextView) findViewById(R.id.eventMovieName);
        TextView cityLabel = (TextView) findViewById(R.id.eventCityLabel);
        TextView cityName = (TextView) findViewById(R.id.eventCityName);
        TextView dateLabel = (TextView) findViewById(R.id.eventDateLabel);
        TextView dateName = (TextView) findViewById(R.id.eventDateName);
        TextView usersLabel = (TextView) findViewById(R.id.eventUsersLabel);

        eventDetailTitle.setText("EVENT DETAILS");
        movieLabel.setText("Movie? ");
        Picasso.with(getApplicationContext()).load(movie.image).into(movieImage);
        movieName.setText(movie.title);
        cityLabel.setText("City? ");
        cityName.setText(event.city);
        dateLabel.setText("When? ");
        dateName.setText(event.date);
        usersLabel.setText("WHO'S GOING? ");
    }
}
