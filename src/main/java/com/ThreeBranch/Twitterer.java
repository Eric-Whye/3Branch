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

                //Checking for Duplication
                if (TweetData.checkDupID(status.getId()))
                    return;
                if (TweetData.checkDupAccount("@" + status.getUser().getScreenName()))
                    return;

                //Adding new Tweets and Account data
                TweetData.addTweetID(status.getId());
                TweetData.addUserhandle("@" + status.getUser().getScreenName());

                //Adding tweets to buffer
                tweets.add(status);

                //After vector buffer is filled, write tweet and account data to file according to correct config format
                if (counter % Configuration.getSearchBuffer() == 0){
                    writeDataToFile(tweets);
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
                writeDataToFile(tweets);
                e.printStackTrace();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {ex.printStackTrace();}

            }
        });
        //StatusListener filter

        FilterQuery query = new FilterQuery();

        query.track(removeSpecialCharacters(Configuration.getSearchTermsList().toString()));
        query.language(removeSpecialCharacters(String.valueOf(Configuration.getLanguages())));
        twitterStream.filter(query);
    }

    private List<List<String>> convertTweetsToListOfStringLists(List<Status> tweets){
        List<List<String>> tweetList = new ArrayList<>();

        for (Status tweet : tweets){
            List<String> line = new ArrayList<>();

            line.add(String.valueOf(tweet.getId()));//id
            line.add("@" + tweet.getUser().getScreenName());//userhandle
            line.add(tweet.getText().replaceAll("\\p{C}", " "));//Text

            if (tweet.getRetweetedStatus() != null)
                line.add(String.valueOf(tweet.getRetweetedStatus().getRetweetCount()));//RetweetCount of original tweet
            else
                line.add(String.valueOf(tweet.getRetweetCount()));

            line.add(tweet.getCreatedAt().toString());//Date

            tweetList.add(line);
        }

        return tweetList;
    }

    private List<List<String>> convertAccountsToListOfStringList(List<Status> tweets){
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

    /**
     * Write Data to File using FileEntryIO
     * @param tweets List of twiter4j.Status
     */
    private void writeDataToFile(List<Status> tweets){
        //Writing Tweet Data to File
        FileEntryIO.appendToFile(convertTweetsToListOfStringLists(tweets),
                Configuration.getDelim(),
                Configuration.getNewLineDelim(),
                Configuration.getOutputFile()
        );

        //Writing Account Data to file
        FileEntryIO.appendToFile(convertAccountsToListOfStringList(tweets),
                Configuration.getDelim(),
                Configuration.getNewLineDelim(),
                Configuration.getAccountsOutputFile()
        );
    }

    public void streamShutdown(){
        twitterStream.shutdown();
    }

}
