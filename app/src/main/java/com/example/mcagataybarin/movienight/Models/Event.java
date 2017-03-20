package com.example.mcagataybarin.movienight.Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aea on 18/03/17.
 */

public class Event {
    public String week, movie, date, city, creator, hour, min, event_id;
    public ArrayList participants, requests;

    public Event(){

    }

    public Event(HashMap<String, Object> event_info) {
        this.week = (String) event_info.get("week");
        this.movie = (String) event_info.get("movie");
        this.date = (String) event_info.get("date");
        this.city = (String) event_info.get("city");
        this.creator = (String) event_info.get("creator");
        this.participants = (ArrayList) event_info.get("participants");
        this.requests = (ArrayList) event_info.get("requests");
        this.hour = (String) event_info.get("hour");
        this.min = (String) event_info.get("min");
        this.event_id = (String) event_info.get("event_id");
    }


}
