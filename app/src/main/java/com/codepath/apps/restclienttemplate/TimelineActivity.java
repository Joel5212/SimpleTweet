package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.codepath.apps.restclienttemplate.models.Tweet.fromJsonArray;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity ";

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    TwitterClient client;//we will use this instance in multiple methods
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);




        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                public void onRefresh() {
                                                    Log.i(TAG, "fetching new data!");
                                                    populateHomeTimeline();
                                                }
                                            });
        // Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Init the list of tweets and adapters
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        //Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore " + page);
                loadMoreData();
            }
        };
        //Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);
        populateHomeTimeline();
        //calling a twitter API by using the twitter client

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);  //inflating the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.compose)
        {
            //Compose icon has been selected
            //Navigate to the compose activity

            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, REQUEST_CODE);   //launches the child activity and the child activity gives us back a tweet if the user inputted it

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            //Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet")); //result of whatever the user published
            // update the RecyclerView with this new tweet
            // Modify data source of tweets
            tweets.add(0, tweet);
            // Update the adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data); //Request code is the same as what we launches the activity with, the result code is something defined by android to make sure the child activity has finished successfully, data is the data that child activity has communicated back to us

    }

    private void loadMoreData() {
        // 1. Send an API request to retrieve appropriate paginated data
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
                                       @Override
                                       public void onSuccess(int statusCode, Headers headers, JSON json) {
                                        Log.i(TAG, "onSuccess for loadMoreData!" + json.toString());
                                           // 2. Deserialize and construct new model objects from the API response
                                           JSONArray jsonArray = json.jsonArray;
                                           try{
                                               // 3. Append the new data objects to the existing set of items inside the array of items
                                               // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
                                               List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                                               adapter.addAll(tweets);
                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                           }






                                       }

                                       @Override
                                       public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                        Log.e(TAG, "onFailure for loadMoreData!" + throwable);
                                       }
            }, tweets.get(tweets.size() - 1).id);  //passing in the last tweet if available in our timeline

        // 2. Deserialize and construct new model objects from the API response
        // 3. Append the new data objects to the existing set of items inside the array of items
        // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) { //Checking to see if we are getting json data from the API
                Log.i(TAG, "onSuccess!" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try
                {
                    adapter.clear(); //clear out before apending the new ones
                    tweets.addAll(fromJsonArray(jsonArray)); //the data has come back, add new items to your chapter...

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);

                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure!" + response, throwable);
            }
        });
    }
}