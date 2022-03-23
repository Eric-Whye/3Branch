package com.ThreeBranch;


import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Twitterer {
    private TwitterStream twitterStream;

    public Twitterer(){
    }

    public void streamStart(){

        twitterStream = new TwitterStreamFactory().getInstance().addListener(new StatusListener() {
            //Vector is thread-safe
            private Vector<Status> tweets = new Vector<>();
            private int counter = 1;

            @Override
            public void onStatus(Status status) {
                System.out.println(counter);
                tweets.add(status);

                //After vector buffer is filled, write tweet and account data to file according to correct config format
                if (counter % Configuration.getSearchBuffer() == 0){
                    TweetData.writeDataToFile(convertToListOfStringLists(tweets),
                            Configuration.getDelim(),
                            Configuration.getNewLineDelim(),
                            new File(Configuration.getOutputFile())
                    );
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
                streamShutdown();
                System.exit(-1);
            }
        });
        //StatusListener filter
        String str = Configuration.getSearchTermsList().toString();
        str = str.replace("[", "").replace("]", "").replace(" ", "");
        twitterStream.filter(new FilterQuery(str));
    }
    private List<List<String>> convertToListOfStringLists(Vector<Status> tweets){
        List<List<String>> tweetList = new ArrayList<>();

        for (Status tweet : tweets){
            List<String> line = new ArrayList<>();
            line.add(String.valueOf(tweet.getId()));
            line.add(tweet.getUser().getScreenName());
            line.add(tweet.getText().replaceAll("\n", ""));
            line.add(tweet.getCreatedAt().toString());

            tweetList.add(line);
        }

        return tweetList;
    }

    public void streamShutdown(){
        twitterStream.shutdown();
    }

}
