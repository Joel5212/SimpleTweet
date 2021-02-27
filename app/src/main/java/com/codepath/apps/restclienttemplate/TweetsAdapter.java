package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> //extend the recyclerview adapter but parametrize that with this new viewholder that we just defined
    {
        Context context;
        List<Tweet> tweets;
        //Pass in the context and list of tweets

        public TweetsAdapter(Context context, List<Tweet> tweets)
        {
            this.context = context;
            this.tweets = tweets;
        }
        //For each row inflate the layout
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_tweets, parent, false); //inflating the item_tweets xml
            return new ViewHolder(view); //wrapping view inside of viewholder
        }


       //Bind values based on the position of the element
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //Get the data at position
            Tweet tweet = tweets.get(position);
            //Bind the tweet with the view holder
            holder.bind(tweet);
        }

        public int getItemCount()
        {
            return tweets.size();
        }
        public void clear()    //clean all elements of the recycler
        {
            tweets.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Tweet> tweetList) //Add a list of items --changed to type used
        {
            tweets.addAll(tweetList);
            notifyDataSetChanged();
        }
    //Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;

        public ViewHolder(@NonNull View itemView)  //item view is one row in the recycler view(tweet)
        {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);

        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
        }
    }



    }
