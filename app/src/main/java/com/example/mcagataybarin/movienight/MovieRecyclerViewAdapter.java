package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.Movie;
import com.example.mcagataybarin.movienight.MovieFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private final List<Movie> upcoming_movies;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MovieRecyclerViewAdapter(Context context, List<Movie> upcoming_movies, OnListFragmentInteractionListener listener) {
        this.context = context;
        this.upcoming_movies = upcoming_movies;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = upcoming_movies.get(position);
        holder.mIdView.setText(upcoming_movies.get(position).title);
        holder.mContentView.setText(upcoming_movies.get(position).detail);
        Picasso.with(context).load(upcoming_movies.get(position).image).into(holder.mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the active callbacks interface (the activity, if the
// fragment is attached to one) that an item has been selected.
                if (null != mListener) mListener.onListFragmentInteraction(holder.mItem);

                // Movie Selected. Open MovieDetailActivity
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movie_index", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return upcoming_movies.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
