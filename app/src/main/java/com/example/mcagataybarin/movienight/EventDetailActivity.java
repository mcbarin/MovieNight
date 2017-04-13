package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity implements UserListFragment.OnListFragmentInteractionListener {

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
                // Show the User List.
                UserListFragment fragment = UserListFragment.newInstance();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.eventUsersList, fragment).commit();

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
        TextView descLabel = (TextView) findViewById(R.id.eventDescriptionLabel);
        TextView descName = (TextView) findViewById(R.id.eventDescriptionName);
        Button reqbutton = (Button) findViewById(R.id.reqButton);

        eventDetailTitle.setText("EVENT DETAILS");
        movieLabel.setText("Movie? ");
        Picasso.with(getApplicationContext()).load(movie.image).into(movieImage);
        movieName.setText(movie.title);
        cityLabel.setText("City? ");
        cityName.setText(event.city);
        dateLabel.setText("When? ");
        dateName.setText(event.date + " " + event.time);
        descLabel.setText("Info?");
        descName.setText(event.description);
        usersLabel.setText("WHO'S GOING? ");
        if(!event.creator.equalsIgnoreCase(FirebaseFunctions.getInstance().user_id)) {
            reqbutton.setVisibility(View.VISIBLE);
        }
    }

    public void requestJoin(View view){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        Event temp = event;
        if(temp.requests == null){
            temp.requests = new ArrayList<String>();
        }
        temp.requests.add(FirebaseFunctions.getInstance().user_id);
        FirebaseFunctions.getInstance().postEventDirect(temp);

        Toast.makeText(this, "Request Sent.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListFragmentInteraction(String item) {

    }
}
