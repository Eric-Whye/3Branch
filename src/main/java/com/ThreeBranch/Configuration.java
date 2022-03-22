package com.ThreeBranch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class Configuration {
    private static int searchBuffer;

    private static int numTweets;

    private static String tweetsOutput;
    private static String accountsOutput;

    private static final List<String> searchTermsList = new ArrayList<>();
    private static int numTweetsPerHashtag;


    public static int getSearchBuffer() {return searchBuffer;}
    public static int getNumTweets() {return numTweets;}
    public static String getOutputFile() {return tweetsOutput;}
    public static String getAccountsOutputFile() {return accountsOutput;}
    public static List<String> getSearchTermsList() {return searchTermsList;}
    public static int getNumTweetsPerHashtag() {return numTweetsPerHashtag;}

    //Holds the configuration file in memory
    private static final Properties properties = new Properties();


    /**
     * Returns String information on Config values for human reading
     */
    public static String getConfigInfo(){
        StringBuilder string = new StringBuilder();

        return string.toString();
    }

    /**
     * Reads from Configuration file and changes fields based on its values
     */
    public static void initialise() {
        readConfig();
        setConfig();
    }

    //Read from Configuration file
    private static void readConfig(){
        try {
            properties.load(Configuration.class.getClassLoader().getResourceAsStream("twitter4j.properties"));
        } catch (IOException e) {e.printStackTrace();}
    }

    //Set up the class with the properties for global use
    private static void setConfig(){
        searchBuffer = Integer.parseInt(properties.getProperty("SearchBuffer"));

        numTweets = Integer.parseInt(properties.getProperty("NumTweets"));

        tweetsOutput = properties.getProperty("TweetsOutput");

        accountsOutput = properties.getProperty("AccountsOutput");

        StringTokenizer tokens = new StringTokenizer(properties.getProperty("SearchTerms"));
        while (tokens.hasMoreTokens()){
            searchTermsList.add(tokens.nextToken());
        }

        if (properties.getProperty("NumTweetsPerHashtag").equals("")){
            numTweetsPerHashtag = numTweets / searchTermsList.size();
        } else
            numTweetsPerHashtag = Integer.parseInt(properties.getProperty("NumTweetsPerHashtag"));


    }

    private Configuration(){
    }

}
