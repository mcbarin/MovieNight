package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcagataybarin.movienight.Models.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.*;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MovieFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private DatabaseReference mDatabase;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Movie> upcoming_movies = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MovieFragment newInstance(int columnCount) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            retrieveMovies(new Runnable() {
                public void run() {
                    recyclerView.setAdapter(new MovieRecyclerViewAdapter(getApplicationContext(), upcoming_movies, mListener));
                }
            });

        }
        return view;
    }

    public void retrieveMovies(final Runnable onLoaded) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference movies_reference = mDatabase.child("movies").child(FirebaseFunctions.getInstance().currentWeek);
        movies_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Object> movies = ((ArrayList<Object>) dataSnapshot.getValue());
                for (int i = 0; i < movies.size(); i++) {
                    try {
                        Movie movie = new Movie(((HashMap<String, String>) movies.get(i)));
                        upcoming_movies.add(movie);
                    } catch (ClassCastException ex){
                        Log.i("MOVIE RETRIEVE ERROR", "ERROR");
                    }
                }
                FirebaseFunctions.getInstance().upcoming_movies = upcoming_movies;
                onLoaded.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addToDatabase(Movie movie){
        final SQLiteDatabase myDB = getActivity().openOrCreateDatabase("Movie", MODE_PRIVATE, null);
        //Add to SQLite, to the movie table.
        String detail=movie.detail.replaceAll("'", " ");
        String title = movie.title.replaceAll("'", " ");
        myDB.execSQL("INSERT INTO movie (date, detail, director, duration, genre, image, title) VALUES " +
                "('" + movie.date + "', '" + detail + "', '" + movie.director + "', '" +
                movie.duration + "', '" + movie.genre + "', '" + movie.image + "', '" + title + "' )");
        //End
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Movie item);
    }


}
