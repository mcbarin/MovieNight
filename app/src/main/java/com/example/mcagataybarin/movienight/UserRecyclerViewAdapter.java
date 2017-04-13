package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.User;
import com.example.mcagataybarin.movienight.UserListFragment.OnListFragmentInteractionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    Context context;
    private final ArrayList<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public UserRecyclerViewAdapter(Context context, ArrayList<String> items, OnListFragmentInteractionListener listener) {
        this.context =  context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        String UID = mValues.get(position);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user_read = dataSnapshot.getValue(User.class);
                if(!(user_read == null)) {
                    if(!user_read.pp_url.isEmpty()){
                        Picasso.with(context).load(user_read.pp_url).into(holder.userImage);
                    } else{
                        Picasso.with(context).load(R.drawable.com_facebook_button_icon_white).into(holder.userImage);
                    }
                    holder.userName.setText(user_read.name);

                    if (position == 0)
                        holder.creatorLabel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("UID", mValues.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircularImageView userImage;
        public final TextView userName;
        public final TextView creatorLabel;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            userImage = (CircularImageView) view.findViewById(R.id.userImage);
            userName = (TextView) view.findViewById(R.id.userName);
            creatorLabel = (TextView) view.findViewById(R.id.eventCreatorLabel);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
}
