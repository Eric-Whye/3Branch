package com.ThreeBranch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class Configuration {
    private static int searchBuffer;

    private static int numTweets;

    private static String tweetsOutput;
    private static String accountsOutput;
    private static String tweetIDsResourceFile;

    private static final List<String> hashtagsList = new ArrayList<>();
    private static int numTweetsPerHashtag;


    public static int getSearchBuffer() {return searchBuffer;}
    public static int getNumTweets() {return numTweets;}
    public static String getOutputFile() {return tweetsOutput;}
    public static String getAccountsOutputFile() {return accountsOutput;}
    public static String getTweetIDsResourceFile() {return tweetIDsResourceFile;}
    public static List<String> getHashtagsList() {return hashtagsList;}
    public static int getNumTweetsPerHashtag() {return numTweetsPerHashtag;}

    //Holds the configuration file in memory
    private static final Properties properties = new Properties();


    /**
     * Reads from Configuration file and changes fields based on its values
     */
    public static void readAndSetConfig() {
        readConfig();
        setConfig();
    }

    //Read from Configuration file
    private static void readConfig(){
        try {
            properties.load(Configuration.class.getClassLoader().getResourceAsStream("twitter4j.properties"));
        } catch (Exception e) {e.printStackTrace();}
    }

    //Set up the class with the properties for global use
    private static void setConfig(){
        searchBuffer = Integer.parseInt(properties.getProperty("SearchBuffer"));

        numTweets = Integer.parseInt(properties.getProperty("NumTweets"));

        tweetsOutput = properties.getProperty("TweetsOutput");

        accountsOutput = properties.getProperty("AccountsOutput");

        tweetIDsResourceFile = properties.getProperty("TweetIDsResourceFile");

        StringTokenizer tokens = new StringTokenizer(properties.getProperty("SearchByHashtagsList"));
        while (tokens.hasMoreTokens()){
            hashtagsList.add(tokens.nextToken());
        }

        if (properties.getProperty("NumTweetsPerHashtag").equals("")){
            numTweetsPerHashtag = numTweets / hashtagsList.size();
        } else
            numTweetsPerHashtag = Integer.parseInt(properties.getProperty("NumTweetsPerHashtag"));


    }

    private Configuration(){
    }

}
