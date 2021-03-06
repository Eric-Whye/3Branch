package com.ThreeBranch.Twitter;


import com.ThreeBranch.FileEntryIO;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class Twitterer {
    private TwitterStream twitterStream;
    private final Configuration config = Configuration.getInstance();

    public Twitterer(){
    }

    public void searchByHashtags(){
        String hashtags = config.getValueFor("tweet.searchTerms");
        StringTokenizer tokens = new StringTokenizer(hashtags);

        List<Status> tweets = new ArrayList<>();
        List<String> hashtagsList = new ArrayList<>();
        while (tokens.hasMoreTokens())
            hashtagsList.add(tokens.nextToken());


        Twitter twitter = new TwitterFactory().getInstance();
        for (String hashtag : hashtagsList) {
            try {
                Query query = new Query(hashtag);
                QueryResult result = null;
                result = twitter.search(query);
                tweets.addAll(result.getTweets());

                tweets.clear();
            } catch (TwitterException e) {e.printStackTrace();}
        }


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

                //Adding new Tweets and Account data
                TweetData.addTweetID(status.getId());

                //Adding tweets to buffer
                tweets.add(status);

                //After vector buffer is filled, write tweet and account data to file according to correct config format
                if (counter % Integer.parseInt(config.getValueFor("tweet.searchBuffer")) == 0){
                    writeDataToFile(tweets);
                    //Clearing Buffer
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

        //To convert a string seperated by white space to be readable by api
        String track = config.getValueFor("tweet.searchTerms").replace(" ", ",");

        query.track(track);
        query.language(config.getValueFor("tweet.languages"));
        twitterStream.filter(query);
    }

    //Breaking up tweets buffer into relevant tweet data
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

    //Breaking up tweets buffer into relevant user data
    private List<List<String>> convertAccountsToListOfStringList(List<Status> tweets){
        List<List<String>> tweetList = new ArrayList<>();

        for (Status tweet : tweets) {
            //Checking for Account duplication in file
            if (TweetData.checkDupAccount("@" + tweet.getUser().getScreenName()))
                continue;

            TweetData.addUserhandle("@" + tweet.getUser().getScreenName());

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

    /**
     * Write Data to File using FileEntryIO
     * @param tweets List of twiter4j.Status
     */
    private synchronized void writeDataToFile(List<Status> tweets){
        char delim = config.getValueFor("format.delim").charAt(0);
        char newLineDelim = config.getValueFor("format.newLineDelim").charAt(0);
        FileEntryIO.appendLineByLine(convertTweetsToListOfStringLists(tweets),
                delim, newLineDelim,
                config.getValueFor("tweet.vaxFile")
        );
        FileEntryIO.appendLineByLine(convertAccountsToListOfStringList(tweets),
                delim, newLineDelim,
                config.getValueFor("tweet.accountsFile")
        );
    }

    public void streamShutdown(){
        twitterStream.shutdown();
    }

}
