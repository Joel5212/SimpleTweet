package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;


public class  ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 140;

    Button btnTweet;
    EditText etCompose;
    TwitterClient client;
    TextView tvCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCount = findViewById(R.id.tvCount);


        // Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                //Make an API call to Twitter to publish the tweet

                if(tweetContent.isEmpty())
                {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() > 280)
                {
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();
                //Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                           Log.i(TAG, "Published tweet says: " + tweet.body);
                           Intent intent = new Intent();
                           intent.putExtra("tweet", Parcels.wrap(tweet));//handeled by intent.putExtra()   //Android doesn't know by default how to take an arbitrary object and pass it between activities
                           //set result code and bundle data for response
                           setResult(RESULT_OK);
                           //closes the activity, passes data to the parent
                           finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)






            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed

                int count = 0;
                String characterCount;

                    count += s.length();
                    characterCount = count + "/" + "280";
                    tvCount.setText(characterCount);
                if(count >= 260 && count <= 279)
                {
                    ForegroundColorSpan fcsYellow = new ForegroundColorSpan(Color.YELLOW);
                    SpannableString string = new SpannableString(characterCount);
                    count++;
                    characterCount = count + "/" + "280";
                    string.setSpan(fcsYellow,0, characterCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvCount.setText(string);
                }
                else if(count >= 280)
                {
                    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
                    SpannableString string2 = new SpannableString(characterCount);
                    count ++;
                    characterCount = count + "/" + "280";
                    string2.setSpan(fcsRed,0, characterCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvCount.setText(string2);
                }





                //ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
                //SpannableString string = new SpannableString(characterCount);
                //string.setSpan(fcsRed,0, s.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        });





        // Make an API call to TWitter to
    }
}