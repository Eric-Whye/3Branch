package com.ThreeBranch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class Configuration {
    //Holds the configuration file in memory
    private static final Properties properties = new Properties();

    //SearchTerms
    private static final List<String> searchTermsList = new ArrayList<>();


    //How many tweets to hold in memory before writing
    public static int getSearchBuffer() {return Integer.parseInt(properties.getProperty("SearchBuffer"));}

    //Total Number of tweets to collect
    public static int getNumTweets() {return Integer.parseInt(properties.getProperty("NumTweets"));}

    //Output Files
    public static String getOutputFile() {return properties.getProperty("TweetsOutput");}
    public static String getAccountsOutputFile() {return properties.getProperty("AccountsOutput");}

    //List of Search Terms
    public static List<String> getSearchTermsList() {return searchTermsList;}

    //Formatting Config
    public static char getDelim() {return properties.getProperty("Delim").charAt(0);}
    public static char getNewLineDelim() {return properties.getProperty("NewLineDelim").charAt(0);}


    /**
     * Returns String information on Config values for human reading
     */
    public static String[] getConfigInfo() {
        StringBuilder str = new StringBuilder();

        return null;
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
        StringTokenizer tokens = new StringTokenizer(properties.getProperty("SearchTerms"));
        while (tokens.hasMoreTokens()){
            searchTermsList.add(tokens.nextToken());
        }
    }

    private Configuration(){
    }

}
