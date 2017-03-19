package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.EventFragment.OnListFragmentInteractionListener;
import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.example.mcagataybarin.movienight.Models.User;
import com.squareup.picasso.Picasso;

import java.util.List;


public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private final List<Event> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private String mParent; // movie or profile

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String user_movie_text = "";
        String imageURL = "";
        if (mParent.equals("profile")){// If parent is MovieFragment
            Movie m = FirebaseFunctions.getInstance().getMovieByWeekAndIndex(mValues.get(position).week, mValues.get(position).movie);
            user_movie_text = m.title;
            imageURL = m.image;
        }else if(mParent.equals("movie")){
            User user = FirebaseFunctions.getInstance().getUserById(mValues.get(position).creator);
            user_movie_text = user.username;
            imageURL = user.pp_url;
        }
        holder.user_movie.setText(user_movie_text);
        holder.city.setText(mValues.get(position).city);
        holder.date.setText(mValues.get(position).date);
        Picasso.with(mContext).load(imageURL).into(holder.image);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
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
        public final ImageView image;
        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            user_movie = (TextView) view.findViewById(R.id.user_movie);
            city = (TextView) view.findViewById(R.id.city);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.eventImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + user_movie.getText() + "'";
        }
    }
}
