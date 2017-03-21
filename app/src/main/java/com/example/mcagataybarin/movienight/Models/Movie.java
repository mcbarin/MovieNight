package com.example.mcagataybarin.movienight.Models;

import java.util.HashMap;

/**
 * Created by mcagataybarin on 3/15/17.
 */

public class Movie {
    public String date, detail, director, duration, genre, image, title;

    public Movie(){

    }

    public Movie(HashMap<String, String> movie_info) {
        this.date = movie_info.get("date");
        this.detail = movie_info.get("detail");
        this.director = movie_info.get("director");
        this.duration = movie_info.get("duration");
        this.genre = movie_info.get("genre");
        this.image = movie_info.get("image");
        this.title = movie_info.get("title");
    }



}
