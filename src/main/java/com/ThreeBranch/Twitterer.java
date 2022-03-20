package com.ThreeBranch;


import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class Twitterer {

    private final Twitter twitter;
    private final List<Status> tweets = new ArrayList<>();

    public Twitterer(){
        twitter = new TwitterFactory().getInstance();
    }

    public void searchByHashtags(OutputTweets outputTweets){
        List<String> hashtagsList = Configuration.getHashtagsList();

        int numWriteCalls = Configuration.getNumTweetsPerHashtag() / Configuration.getSearchBuffer();


        for (String hashtag : hashtagsList) {
            Query query = new Query(hashtag);

            //The number of tweets gathered from each query/write cycle is at most the WriteBuffer
            query.setCount(Math.min(Configuration.getSearchBuffer(), Configuration.getNumTweetsPerHashtag()));

            for (int j = 0; j <= numWriteCalls; j++) {

                try {
                    QueryResult result = twitter.search(query);
                    tweets.addAll(result.getTweets());
                    outputTweets.writeTweetsToFile(tweets);
                    tweets.clear();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
