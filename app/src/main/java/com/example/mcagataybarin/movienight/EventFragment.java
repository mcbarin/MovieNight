package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcagataybarin.movienight.Models.Event;


import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventFragment extends Fragment {

    private static final String ARG_PARENT = "parent-name";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_EXTRA1 = "extra-1";
    private static final String ARG_EXTRA2 = "extra-2";

    /*
    * If parent is movie, extra-1 is week, extra-2 is movie index.
    * If parent is profile, extra-1 is user id. extra-2 will be empty and not used.
    * */

    private String mParent = "movie"; // movie or profile
    private int mColumnCount = 1;
    private String mExtra1 = "";
    private String mExtra2 = "";
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    public static EventFragment newInstance(int columnCount, String parent, String extra1, String extra2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENT, parent);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_EXTRA1, extra1);
        args.putString(ARG_EXTRA2, extra2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParent = getArguments().getString(ARG_PARENT);
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Change this later.
            List<Event> events;

            if(mParent.equals("profile"))
                events = FirebaseFunctions.getInstance().getUserEventsById(mExtra1);
            else
                events = FirebaseFunctions.getInstance().getMovieEvents(mExtra1, mExtra2);
            recyclerView.setAdapter(new EventRecyclerViewAdapter(getApplicationContext(), events, mParent, mListener));
        }
        return view;
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
        void onListFragmentInteraction(Event event);
    }
}