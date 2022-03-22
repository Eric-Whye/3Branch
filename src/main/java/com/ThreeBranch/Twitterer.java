package com.ThreeBranch;


import twitter4j.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Twitterer {

    private final Twitter twitter;
    private final List<Status> tweets = new ArrayList<>();

    public Twitterer(){
        twitter = new TwitterFactory().getInstance();
    }

    public void searchByHashtags(){
        List<String> searchTermsList = Configuration.getSearchTermsList();

        //Divides up the tweetsPerHashtag according to the searchBuffer so the data can be written and api can rest a little
        int numWriteCalls = Configuration.getNumTweetsPerHashtag() / Configuration.getSearchBuffer();


        for (String hashtag : searchTermsList) {
            Query query = new Query(hashtag);
            //The number of tweets gathered from each query/write cycle is at most the WriteBuffer
            query.setCount(Math.min(Configuration.getSearchBuffer(), Configuration.getNumTweetsPerHashtag()));

            for (int j = 0; j <= numWriteCalls; j++) {
                /*

                try {
                    QueryResult result = twitter.search(query);
                    tweets.addAll(result.getTweets());

                    List<List<String>> tweetsList = new ArrayList<>();
                    List<List<String>> accountsList = new ArrayList<>();
                    for (Status tweet: tweets){

                        long id = tweet.getId();
                        if (outputTweets.checkDupID(id))
                            continue;
                        String username = tweet.getUser().getScreenName();
                        String text = tweet.getText().replaceAll("\n", " ");
                        String numRetweets = Integer.parseInt(tweet.getRetweetCount();
                        String time = tweet.getCreatedAt().toString();

                        String location = tweet.getUser().getLocation();
                        String bio = tweet.getUser().getDescription();
                        bio = bio.replaceAll("\n", " ");
                        int numFollowers = tweet.getUser().getFollowersCount();

                        List<String> tweetLine = new ArrayList<>();
                        line.add(username);
                        line.add(text);
                        line.add(numRetweets);
                        line.add(time);

                        tweetsList.add(line);
                        accountsList.add)
                    }
                    outputTweets.writeTweetsToFile(tweets);
                    tweets.clear();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }*/
            }


        }
    }
}
