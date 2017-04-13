package com.example.mcagataybarin.movienight;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Event> movie_events = new ArrayList<>();
    private DatabaseReference mDatabase;
    private boolean hasNotif;
    private ArrayList<Request> requests = new ArrayList<>();
    private MyListAdaper my_list;
    private View view;
    private TextView notif;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyListAdaper extends ArrayAdapter<Request> {
        private int layout;
        private ArrayList<Request> mObjects;
        private MyListAdaper(Context context, int resource, ArrayList<Request> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                viewHolder.button2 = (Button) convertView.findViewById(R.id.list_item_btn2);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(getItem(position).request).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user_read = dataSnapshot.getValue(User.class);
                        if(!(user_read == null)) {
                            if(!user_read.pp_url.isEmpty()){
                                Picasso.with(getContext()).load(user_read.pp_url).into(viewHolder.thumbnail);
                            } else{
                                Picasso.with(getContext()).load(R.drawable.com_facebook_button_icon_white).into(viewHolder.thumbnail);
                            }
                            viewHolder.title.setText(user_read.name + "\nCity: " + getItem(position).event.city + " \nTime: " +getItem(position).event.time);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();

            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    v.startAnimation(shake);
                    Event temp = getItem(position).event;
                    String user_id = getItem(position).request;
                    temp.requests.remove(user_id);
                    if(temp.participants == null){
                        temp.participants = new ArrayList<String>();
                    }
                    temp.participants.add(user_id);
                    FirebaseFunctions.getInstance().postEventDirect(temp);

                    requests.remove(position);
                    my_list.clear();
                    my_list.notifyDataSetChanged();

                    hasNotif = checkNotifications(movie_events);
                    if(!hasNotif) notif.setVisibility(View.VISIBLE);

                }
            });

            mainViewholder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    v.startAnimation(shake);
                    Event temp = getItem(position).event;
                    String user_id = getItem(position).request;
                    temp.requests.remove(user_id);
                    FirebaseFunctions.getInstance().postEventDirect(temp);

                    requests.remove(position);
                    my_list.clear();
                    my_list.notifyDataSetChanged();


                    hasNotif = checkNotifications(movie_events);
                    if(!hasNotif) notif.setVisibility(View.VISIBLE);

                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("UID", getItem(position).request);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });

            return convertView;
        }
    }
    public class ViewHolder {

        ImageView thumbnail;
        TextView title;
        Button button;
        Button button2;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        notif = (TextView) view.findViewById(R.id.notif);

        if(movie_events.isEmpty()){
            retrieveUserMovies(new Runnable() {
                public void run() {
                    hasNotif = checkNotifications(movie_events);
                    System.out.println("Has notiften dönen: " + hasNotif );
                    if(!hasNotif) notif.setVisibility(View.VISIBLE);
                    ListView lv = (ListView) view.findViewById(R.id.listview);
                    my_list = new MyListAdaper(getActivity(), R.layout.notification_user, requests);
                    lv.setAdapter(my_list);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println("List item was clicked at " + position);
                        }
                    });
                }
            });
        } else{
            hasNotif = checkNotifications(movie_events);
            System.out.println("Has notiften dönen: else içi bu " + hasNotif );
            if(!hasNotif) notif.setVisibility(View.VISIBLE);
            ListView lv = (ListView) view.findViewById(R.id.listview);
            my_list = new MyListAdaper(getActivity(), R.layout.notification_user, requests);
            lv.setAdapter(my_list);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("List item was clicked at " + position);
                }
            });

        }

        return view;
    }

    private boolean checkNotifications(ArrayList<Event> movie_events) {
        boolean result = false;

        for(int i= 0; i<movie_events.size(); i++){
            Event temp = movie_events.get(i);

            if(temp.requests != null) {
                temp.requests = new ArrayList<String>(new LinkedHashSet<String>(temp.requests));
                for(String a : temp.requests){
                    Request temp_req = new Request(a, temp);
                    requests.add(temp_req);
                }
            }
        }

        if(!requests.isEmpty()) {
            result = true;
        }

        return result;
    }

    private class Request{

        private String request;
        private Event event;

        public Request(String request, Event event){
            this.request = request;
            this.event = event;

        }


    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public void retrieveUserMovies(final Runnable onLoaded) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("events").orderByChild("creator").equalTo(FirebaseFunctions.getInstance().getCurrentUserId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Event event = issue.getValue(Event.class);
//                        Log.d("Event ", event.city + " " + event.movie + " " + event.event_id);
                        movie_events.add(event);
                    }
                    onLoaded.run();
                }else{
                    notif.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
