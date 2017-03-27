package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.EventFragment.OnListFragmentInteractionListener;
import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.example.mcagataybarin.movienight.Models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.util.List;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private final List<Event> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private String mParent; // movie or profile
    private DatabaseReference mDatabase;
    private Movie m;
    private User user;
    private String user_movie_text = "";
    private String imageURL = "";


    public EventRecyclerViewAdapter(Context context, List<Event> items, String parent, OnListFragmentInteractionListener listener) {
        mContext = context;
        mParent = parent;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        if (mParent.equals("profile")) {// If parent is MovieFragment

            FirebaseFunctions.getInstance().getMovieByWeekAndIndex(new Runnable() {
                public void run() {
                    m = FirebaseFunctions.getInstance().temp_movie;
                    profil();
                    holder.user_movie.setText(user_movie_text);
                    holder.city.setText(mValues.get(position).city);
                    holder.date.setText(mValues.get(position).date);
                    if (!imageURL.isEmpty())
                        Picasso.with(mContext).load(imageURL).into(holder.image);
                    holder.cityLabel.setText("Şehir: ");
                    holder.dateLabel.setText("Tarih: ");
                }
            }, mValues.get(position).week, mValues.get(position).movie);

            imageURL = FirebaseFunctions.getInstance().user_pp_url;

        } else if (mParent.equals("movie")) {

            FirebaseFunctions.getInstance().getUserById(new Runnable() {
                public void run() {
                    user = FirebaseFunctions.getInstance().temp_user;
                    movieProfil();
                    holder.user_movie.setText(user_movie_text);
                    holder.city.setText(mValues.get(position).city);
                    holder.date.setText(mValues.get(position).date);
                    if (!imageURL.isEmpty())
                        Picasso.with(mContext).load(imageURL).into(holder.image);
                }
            }, mValues.get(position).creator);

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    Log.i("EVENTID111", holder.mItem.event_id);
                    // Event Clicked
                    Intent intent = new Intent(mContext, EventDetailActivity.class);
                    intent.putExtra("event_id", holder.mItem.event_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void profil() {
        user_movie_text = m.title;
        imageURL = m.image;
    }

    public void movieProfil(){
        user_movie_text = user.name;
        imageURL = user.pp_url;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView user_movie;
        public final TextView city;
        public final TextView date;
        public final TextView dateLabel;
        public final TextView cityLabel;
        public final ImageView image;
        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            user_movie = (TextView) view.findViewById(R.id.user_movie);
            city = (TextView) view.findViewById(R.id.city);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.eventImage);
            dateLabel = (TextView) view.findViewById(R.id.eventDateLabel);
            cityLabel = (TextView) view.findViewById(R.id.cityLabel);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + user_movie.getText() + "'";
        }
    }
}
