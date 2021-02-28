package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    private static com.codepath.apps.restclienttemplate.TimeFormatter TimeFormatter;
    public String body;
    public String createdAt;
    public User user;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text"); //changing the json object into a java tweet object
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user")); //should be a java User model, fromJson will take in a json object and it will return a use model
        return tweet;
    }

    public String getFormattedTimeStamp(String jsonObject)
    {
        return TimeFormatter.getTimeDifference(createdAt);
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException { //passing in a JsonArray and return a list of tweet objects
    List<Tweet> tweets = new ArrayList<>();
   for(int i = 0; i < jsonArray.length(); i++)
    {
        tweets.add(fromJson(jsonArray.getJSONObject(i)));
    }
   return tweets;
    }
}

