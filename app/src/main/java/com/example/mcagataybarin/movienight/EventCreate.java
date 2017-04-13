package com.example.mcagataybarin.movienight;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcagataybarin.movienight.Models.Event;
import com.example.mcagataybarin.movienight.Models.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.Calendar;

@SuppressWarnings("deprecation")
public class EventCreate extends AppCompatActivity {

    Event event;
    Movie movie;
    private String movie_index = "";
    private int year, month, day;
    private TextView dateView, description;
    private DatePicker datePicker;
    private Spinner city, hour, minute;
    private Calendar calendar;
    private int movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Intent intent = getIntent();

        movie_id = intent.getIntExtra("movie_id", 0);
        movie_index = String.valueOf(movie_id);
        city = (Spinner) findViewById(R.id.city);

        dateView = (TextView) findViewById(R.id.dateView);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        description = (TextView) findViewById(R.id.eventDescription);
        hour = (Spinner) findViewById(R.id.timeHour);
        minute = (Spinner) findViewById(R.id.timeMin);

        showDate();

    }

    public void createEvent(View view) {
        // create a new event object and save to firebase
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);

        String city_name = String.valueOf(city.getSelectedItem());

        Event new_ev = new Event();
        new_ev.movie = movie_index;
        new_ev.city = city_name;

        String month_name = getMonthForInt(month);

        new_ev.date = day + " " + month_name + " " + year;

        String hourText = String.valueOf(hour.getSelectedItem());
        String minText = String.valueOf(minute.getSelectedItem());
        new_ev.time = hourText + ":" + minText;

        new_ev.description = description.getText().toString();

        FirebaseFunctions.getInstance().postEvent(new_ev);


        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie_index", movie_id);
        startActivity(intent);

    }

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Select a Date",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    year = arg1;
                    month = arg2 + 1;
                    day = arg3;
                    showDate();
                }
            };

    String getMonthForInt(int num) {
        String month = "wrong";
        num--;
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    private void showDate() {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}
