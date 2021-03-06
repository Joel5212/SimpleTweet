package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> //extend the recyclerview adapter but parametrize that with this new viewholder that we just defined
{
    Context context;
    List<Tweet> tweets;
    //Pass in the context and list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets) {
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

    public int getItemCount() {
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
        RelativeLayout container;
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimestamp;
        TextView tvHandle;
        ImageView ivtweetImage;

        public ViewHolder(@NonNull View itemView)  //item view is one row in the recycler view(tweet)
        {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvHandle = itemView.findViewById(R.id.tvName);
            container = itemView.findViewById(R.id.container);
           // ivtweetImage = itemView.findViewById(R.id.ivtweetImage);


        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvTimestamp.setText(tweet.getFormattedTimeStamp(tweet.createdAt));
            tvHandle.setText(tweet.user.name);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
           // Glide.with(context).load(tweet.url.displayUrl).into(ivtweetImage);


       /*     container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, DetailActivity.class);

                   i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);

                }
            });

        }
   */
        }


    }
}
