package com.ThreeBranch;

import twitter4j.GeoLocation;
import twitter4j.Status;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/*
The Code that is commented out is my attempt to prevent tweet duplication, but I have no time to finish it at the moment
 */
public class OutputTweets{
    private final File tweetsFile = new File(Configuration.getOutputFile());
    private final File accountsFile = new File(Configuration.getAccountsOutputFile());
    //private File tweetIDsFile;
    private final HashSet<Long> tweetIDs = new HashSet<>();

    /**
     * Writes tweets to designated config file in the form of
     * (username + "\t" + text + "\t" + numRetweets + "\t" + time + "\n")
     * and also writes the user data of tweets in the form of
     * (username + "\t" + "location" + "\t" + bio + "\t" + numFollowers + "\n")
     * @param tweets List of Tweets
     */
    public void writeTweetsToFile(List<Status> tweets) {
        Writer tweetWriter = null;
        Writer accountsWriter = null;
        //Writer IDWriter = null;
        try {
            tweetWriter = new BufferedWriter(new FileWriter(tweetsFile, true));
            accountsWriter = new BufferedWriter(new FileWriter(accountsFile, true));
            //IDWriter = new BufferedWriter(new FileWriter(tweetIDsFile, true));
        }catch (IOException e){ e.printStackTrace();}
        assert tweetWriter != null;
        assert accountsWriter != null;
        //assert IDWriter != null;

        for (Status tweet : tweets){
            if (tweetIDs.contains(tweet.getId())) {
                System.out.println("yay");
                continue;
            }

            tweetIDs.add(tweet.getId());

            String username = tweet.getUser().getScreenName();
            String text = tweet.getText();
            text = text.replaceAll("\n", " ");
            int numRetweets = tweet.getRetweetCount();
            Date time = tweet.getCreatedAt();

            String location = tweet.getUser().getLocation();
            String bio = tweet.getUser().getDescription();
            bio = bio.replaceAll("\n", " ");
            int numFollowers = tweet.getUser().getFollowersCount();

            try {
                tweetWriter.write(username + "\t" + text + "\t" + numRetweets + "\t" + time + "\n");
                accountsWriter.write(username + "\t" + location + "\t" + bio + "\t" + numFollowers + "\n");
                //IDWriter.write(tweet.getId() + "\n");
            }catch(IOException e){e.printStackTrace();}
        }


        try {
            tweetWriter.close();
            accountsWriter.close();
            //IDWriter.close();
        }catch(IOException e){e.printStackTrace();}
    }

    public OutputTweets() {
        /*
        //Reading in tweetIDs for duplication checking
        InputStream is = getClass().getClassLoader().getResourceAsStream(Configuration.getTweetIDsResourceFile());
        try {
            assert is != null;
            Files.copy(is, Paths.get(Configuration.getTweetIDsResourceFile()));
        } catch (IOException e) {e.printStackTrace();}

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while (reader.ready()) {
                tweetIDs.add(Long.parseLong(reader.readLine()));
            }

            reader.close();
            is.close();
        } catch (Exception e) {e.printStackTrace();}*/
    }
}
