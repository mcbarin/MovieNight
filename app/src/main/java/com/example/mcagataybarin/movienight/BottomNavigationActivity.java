package com.example.mcagataybarin.movienight;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;

public class BottomNavigationActivity extends AppCompatActivity
        implements ProfileFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener, MovieFragment.OnListFragmentInteractionListener, EventFragment.OnListFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    selectedFragment = MovieFragment.newInstance(1);
                    break;
                case R.id.navigation_profile:
                    selectedFragment = ProfileFragment.newInstance(FirebaseFunctions.getInstance().getCurrentUserId());
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = NotificationFragment.newInstance("param1", "param2");
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Movie Fragment will be automatically opened.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, MovieFragment.newInstance(1));
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void onListFragmentInteraction(Movie item) {

    }

    @Override
    public void onListFragmentInteraction(Event event){

    }
}
