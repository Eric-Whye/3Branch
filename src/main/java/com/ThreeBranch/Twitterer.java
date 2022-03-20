package com.ThreeBranch;

import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class Twitterer {
    private Configuration config = new Configuration();
    private Twitter twitter;
    private List<Status> tweets = new ArrayList<>();
    private int buffer;

    public Twitterer(){
        twitter = new TwitterFactory().getInstance();
    }

    public List<Status> searchByHashtags(){
        Query query = new Query(/*List from Config File*/);
        query.setCount(config.getNumTweets());
        try{
            QueryResult result = twitter.search(query);
            tweets.addAll(result.getTweets());
        }catch (TwitterException te){ te.printStackTrace();}

        for (Status tweet : tweets){
            String user = tweet.getUser().getName();
            String msg = tweet.getText();

            System.out.println(user + " wrote " + msg);
        }

        return tweets;
    }
}
