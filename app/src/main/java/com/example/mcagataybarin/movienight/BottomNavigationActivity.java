package com.example.mcagataybarin.movienight;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BottomNavigationActivity extends AppCompatActivity
        implements ProfileFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener, MovieFragment.OnListFragmentInteractionListener, EventFragment.OnListFragmentInteractionListener {

    private SQLiteDatabase myDB;

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

        myDB = this.openOrCreateDatabase("Movie", MODE_PRIVATE, null);
        //date, detail, director, duration, genre, image, title;
        //myDB.execSQL("DROP TABLE movie");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS movie (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT not null, detail TEXT not null, director TEXT not null, duration TEXT not null," +
                "genre TEXT not null, image TEXT not null, title TEXT not null )");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // When no internet connection :
        System.out.println("INTERNET CONNECTION? ::   " + isNetworkAvailable());

        ArrayList<Movie> all_movies = getAllMovies();
        for(int i = 0;i<all_movies.size();i++){
            Movie tmp = all_movies.get(i);
            System.out.println("SQLitedan movie: "+ tmp);
        }
        // set the movie arraylist to all_movies instead of getting the movies from firebase



        // Movie Fragment will be automatically opened.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, MovieFragment.newInstance(1));
        transaction.commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public ArrayList<Movie> getAllMovies(){

        ArrayList<Movie> arrayList = new ArrayList<Movie>();

        Cursor c = myDB.rawQuery("SELECT * from movie",null);

        //date, detail, director, duration, genre, image, title;

        int date_index = c.getColumnIndex("date");
        int detail_index = c.getColumnIndex("detail");
        int director_index = c.getColumnIndex("director");
        int duration_index = c.getColumnIndex("duration");
        int genre_index = c.getColumnIndex("genre");
        int title_index = c.getColumnIndex("title");
        int image_index = c.getColumnIndex("image");

        c.moveToFirst();
        int a = 1;
        int count = c.getCount();

        if(count == 0){
            return arrayList;
        }

        if(count == 1){
            Movie temp = new Movie();
            temp.date = c.getString(date_index);
            temp.detail = c.getString(detail_index);
            temp.director = c.getString(director_index);
            temp.duration = c.getString(duration_index);
            temp.genre = c.getString(genre_index);
            temp.title = c.getString(title_index);
            temp.image = c.getString(image_index);
            arrayList.add(temp);

        } else {
            while (c != null) {
                a++;
                Movie temp = new Movie();
                temp.date = c.getString(date_index);
                temp.detail = c.getString(detail_index);
                temp.director = c.getString(director_index);
                temp.duration = c.getString(duration_index);
                temp.genre = c.getString(genre_index);
                temp.title = c.getString(title_index);
                temp.image = c.getString(image_index);
                arrayList.add(temp);
                if (count != a) {
                    c.moveToNext();
                } else {
                    c.moveToNext();
                    Movie temp2 = new Movie();
                    temp2.date = c.getString(date_index);
                    temp2.detail = c.getString(detail_index);
                    temp2.director = c.getString(director_index);
                    temp2.duration = c.getString(duration_index);
                    temp2.genre = c.getString(genre_index);
                    temp2.title = c.getString(title_index);
                    temp2.image = c.getString(image_index);
                    arrayList.add(temp2);
                    break;
                }

            }
        }

        return arrayList;
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
