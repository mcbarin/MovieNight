package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mcagataybarin.movienight.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;


import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment {
    private static final String ARG_USERID = "userid";
    private static String mUserId;

    private DatabaseReference mDatabase;
    private TextView name;
    private View view;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String userid) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_USERID);
        }

        EventFragment eventFragment = EventFragment.newInstance(1, "profile", mUserId, "");
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.user_events, eventFragment);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = (TextView) view.findViewById(R.id.user_profile_name);

        mDatabase.child("users").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user_read = dataSnapshot.getValue(User.class);
                if(!(user_read == null)) {
                    name.setText(user_read.name);

                    String photoUrl = user_read.pp_url;
                    FirebaseFunctions.getInstance().user_pp_url = user_read.pp_url;
                    if(!photoUrl.isEmpty()) {
                        Uri photo_url = Uri.parse(photoUrl);

                        CircularImageView imageView = (CircularImageView) view.findViewById(R.id.user_profile_photo);
                        Picasso.with(getApplicationContext()).load(photo_url).into(imageView);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
