package com.ThreeBranch;


import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Twitterer {
    private TwitterStream twitterStream;

    protected Twitterer(){
    }

    public void streamStart(){

        twitterStream = new TwitterStreamFactory().getInstance().addListener(new StatusListener() {
            //Vector is thread-safe
            private final Vector<Status> tweets = new Vector<>();
            private int counter = 1;

            @Override
            public void onStatus(Status status) {
                System.out.print(counter + " tweets gathered" + "\r");
                System.out.flush();

                if (TweetData.checkDupID(status.getId()))
                    return;
                if (TweetData.checkDupAccount("@" + status.getUser().getScreenName()))
                    return;

                TweetData.addTweetID(status.getId());
                TweetData.addUserhandle("@" + status.getUser().getScreenName());

                tweets.add(status);

                //After vector buffer is filled, write tweet and account data to file according to correct config format
                if (counter % Configuration.getSearchBuffer() == 0){

                    //Writing Tweet Data to File
                    TweetData.writeDataToFile(convertTweetsToListOfStringLists(tweets),
                            Configuration.getDelim(),
                            Configuration.getNewLineDelim(),
                            new File(Configuration.getOutputFile())
                    );

                    //Writing Account Data to file
                    TweetData.writeDataToFile(convertAccountsToListOfStringList(tweets),
                            Configuration.getDelim(),
                            Configuration.getNewLineDelim(),
                            new File(Configuration.getAccountsOutputFile())
                    );

                    //Resetting Buffer
                    tweets.clear();
                }
                counter++;
            }
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //Delete the corresponding tweet in file
            }
            @Override
            public void onTrackLimitationNotice(int i) {
            }
            @Override
            public void onScrubGeo(long l, long l1) {
            }
            @Override
            public void onStallWarning(StallWarning stallWarning) {
            }
            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        });
        //StatusListener filter

        FilterQuery query = new FilterQuery();

        query.track(removeSpecialCharacters(Configuration.getSearchTermsList().toString()));
        query.language(removeSpecialCharacters(String.valueOf(Configuration.getLanguages())));
        twitterStream.filter(query);
    }

    private List<List<String>> convertTweetsToListOfStringLists(Vector<Status> tweets){
        List<List<String>> tweetList = new ArrayList<>();

        for (Status tweet : tweets){
            List<String> line = new ArrayList<>();

            line.add(String.valueOf(tweet.getId()));
            line.add("@" + tweet.getUser().getScreenName());

            //To get the full text of the tweet
            if (tweet.getRetweetedStatus() == null) {
                line.add(tweet.getText().replaceAll("\\p{C}", " "));
                line.add(String.valueOf(tweet.getRetweetCount()));
            }
            else {
                line.add(tweet.getRetweetedStatus().getText().replaceAll("\\p{C}", " "));
                line.add(String.valueOf(tweet.getRetweetedStatus().getRetweetCount()));
            }

            line.add(tweet.getCreatedAt().toString());

            tweetList.add(line);
        }

        return tweetList;
    }

    private List<List<String>> convertAccountsToListOfStringList(Vector<Status> tweets){
        List<List<String>> tweetList = new ArrayList<>();

        for (Status tweet : tweets) {
            List<String> line = new ArrayList<>();

            line.add("@" + tweet.getUser().getScreenName());
            line.add(tweet.getUser().getLocation());

            if (tweet.getUser().getDescription() != null)
                line.add(tweet.getUser().getDescription().replaceAll("\\p{C}", " "));
            else
                line.add((tweet.getUser().getDescription()));
            line.add(String.valueOf(tweet.getUser().getFollowersCount()));

            tweetList.add(line);
        }

        return tweetList;
    }
    //Removes '[' and ']' and ' '
    private String removeSpecialCharacters(String str){
        return str.replace("[", "").replace("]", "").replace(" ", "");
    }

    public void streamShutdown(){
        twitterStream.shutdown();
    }

}
