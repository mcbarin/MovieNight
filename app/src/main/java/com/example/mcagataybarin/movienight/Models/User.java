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

    public String username;
    public String email;
    public String pp_url;

    public User(){

    }

    public User(String username, String email, String pp_url) {
        this.username = username;
        this.email = email;
        this.pp_url = pp_url;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("email", email);
        result.put("pp_url", pp_url);

        return result;
    }

}
