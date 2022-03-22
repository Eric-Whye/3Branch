package com.ThreeBranch;

import twitter4j.Status;

import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/*
The Code that is commented out is my attempt to prevent tweet duplication, but I have no time to finish it at the moment
 */
public class OutputTweets{
    //Holds the Ids of gathered tweets for duplication checking purposes
    private final HashSet<Long> tweetIDs = new HashSet<>();

    /**
     * Writes list.size() line entries to output where each entry consists of tokens with a delimiter
     * @param list of lists where each inner list has tokens to form a line
     * @param delim between each token
     * @param newLineDelim between each line
     * @param outputFile
     */
    public void writeToFile(List<List<String>> list, char delim, char newLineDelim, File outputFile){
        Writer writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(outputFile, true));

            for (List<String> line : list){
                StringBuilder string = new StringBuilder();
                for (int i = 0; i < line.size(); i++)
                    string.append(line.get(i)).append(delim);

                string.deleteCharAt(string.length()-1);
                string.append(newLineDelim);
                writer.write(string.toString());
            }
        }catch(IOException e){e.printStackTrace();}
        finally{
            try{
                assert writer != null;
                writer.close();
            }catch(IOException e){e.printStackTrace();}
        }
    }

    /**
     * Twitter Status duplication checking
     * @param tweetID Twitter.Status id
     * @return true if found
     */
    public boolean checkDupID(Long tweetID){
        return tweetIDs.contains(tweetID);
    }
    public void addTweetID(Long tweetID){
        tweetIDs.add(tweetID);
    }
    /**
     * Writes tweets to designated config file in the form of
     * (tweetId + "\t" + username + "\t" + text + "\t" + numRetweets + "\t" + time + "\n")
     * and also writes the user data of tweets in the form of
     * (username + "\t" + "location" + "\t" + bio + "\t" + numFollowers + "\n")
     * @param tweets List of Tweets
     */
    public void writeTweetsToFile(List<Status> tweets) {
        Writer tweetWriter = null;
        Writer accountsWriter = null;
        try {
            tweetWriter = new BufferedWriter(new FileWriter(tweetsFile, true));
            accountsWriter = new BufferedWriter(new FileWriter(accountsFile, true));

            for (Status tweet : tweets){
                //Checking for tweet Duplication
                if (tweetIDs.contains(tweet.getId()))
                    continue;
                tweetIDs.add(tweet.getId());


                long id = tweet.getId();
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
                    tweetWriter.write(id + "\t" + username + "\t" + text + "\t" + numRetweets + "\t" + time + "\n");
                    accountsWriter.write(username + "\t" + location + "\t" + bio + "\t" + numFollowers + "\n");
                }catch(IOException e){e.printStackTrace();}
            }
        }catch(IOException e){e.printStackTrace();}
        finally{
            try {
                assert tweetWriter != null;
                assert accountsWriter != null;
                tweetWriter.close();
                accountsWriter.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Reads tweetIDs into HashSet field for easier duplication checking
     */
    private void readTweetIDs(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(Configuration.getOutputFile()));
            while (reader.ready()){
                reader.lines().map(line -> line.split("\\s+")[1]).forEach(t -> tweetIDs.add(Long.parseLong(t)));
            }
        }catch(IOException e){e.printStackTrace();}
        finally{
            try {
                assert reader != null;
                reader.close();
            }catch(IOException e){e.printStackTrace();}
        }

    }

    public OutputTweets() {
        readTweetIDs();
    }
}
