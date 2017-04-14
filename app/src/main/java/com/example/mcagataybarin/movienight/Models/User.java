package com.example.mcagataybarin.movienight.Models;

/**
 * Created by aea on 14/03/17.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String pp_url;
    public String bio;

    public User(){

    }

    public User(String username, String email, String pp_url) {
        this.name = username;
        this.email = email;
        this.pp_url = pp_url;
    }

    public User(String username, String email, String pp_url, String bio) {
        this.name = username;
        this.email = email;
        this.pp_url = pp_url;
        this.bio = bio;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("pp_url", pp_url);
        result.put("bio", bio);

        return result;
    }

    public String toString(){
        return this.name + " " + this.email + " " + this.pp_url + " " + this.bio;
    }

}
