package com.ThreeBranch.Twitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Configuration {
    private static Configuration configuration;
    private static String filename;

    public static final String ConfigFilename = "twitter4j.properties";//config file

    //Holds the configuration file in memory
    private static final Properties properties = new Properties();

    public synchronized String getValueFor(String configName) throws NullPointerException{
        return properties.getProperty(configName);
    }

    /**
     * Returns String information on Config values for human reading
     */
    public String getConfigInfo() {
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

    public synchronized static Configuration getInstance(String filename){
        if (configuration == null || !Configuration.filename.equals(filename)){
            Configuration.filename = filename;
            configuration = new Configuration(filename);
        }

        return configuration;
    }

    public synchronized static Configuration getInstance(){
        if (configuration != null) return configuration;
        else return null;
    }


    private Configuration(String filename){readConfig(filename);}
    /**
     * Reads from Configuration file and changes fields based on its values
     * @param filename Name of configuration File
     */
    private static void readConfig(String filename){
        try {
            properties.load(new FileReader(filename));
        } catch (IOException e) {e.printStackTrace();}
    }
}
