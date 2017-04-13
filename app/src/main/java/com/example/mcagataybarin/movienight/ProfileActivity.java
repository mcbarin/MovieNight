package com.example.mcagataybarin.movienight;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mcagataybarin.movienight.Models.Event;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, EventFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String UID = intent.getStringExtra("UID");

        ProfileFragment fragment = ProfileFragment.newInstance(UID);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Event event) {

    }
}
