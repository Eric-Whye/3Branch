package com.ThreeBranch.Twitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public abstract class Configuration {
    public static final String ConfigFilename = "twitter4j.properties";//config file

    //Holds the configuration file in memory
    private static final Properties properties = new Properties();

    public static String getValueFor(String configName) throws NullPointerException{
        return properties.getProperty(configName);
    }
    //How many tweets to hold in memory before writing

    //Formatting Config


    /**
     * Returns String information on Config values for human reading
     */
    public static String getConfigInfo() {
        return "\nSearch Terms:\n" +
                getValueFor("tweet.searchTerms") + "\n" +
                "\nLanguages:\n" +
                getValueFor("tweet.languages") + "\n" +
                "\nSearch and Write Buffer: " + getValueFor("tweet.searchBuffer") + "\n" +
                "\nMax Tweets: " + getValueFor("tweet.numTweets") + "\n" +
                "\nOutput Directory: " + getValueFor("tweet.vaxDir") +
                "\nTweet Output File: " + getValueFor("tweet.vaxFile") +
                "\nAccount Output File: " + getValueFor("tweet.accountsFile") + "\n";
    }

    /**
     * Reads from Configuration file and changes fields based on its values
     * @param filename Name of configuration File
     */
    public static void initialise(String filename) throws FileNotFoundException {
        readConfig(filename);

        new File(getValueFor("tweet.vaxDir")).mkdirs();
        new File(getValueFor("graph.inputDir")).mkdirs();
    }

    //Read from Configuration file
    private static void readConfig(String filename){
        try {
            properties.load(new FileReader(filename));
        } catch (IOException e) {e.printStackTrace();}
    }
}