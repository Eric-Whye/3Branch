package com.ThreeBranch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public abstract class Configuration {
    public static final String ConfigFilename = "twitter4j.properties";//config file

    //Holds the configuration file in memory
    private static final Properties properties = new Properties();

    //SearchTerms
    private static final List<String> searchTermsList = new ArrayList<>();
    //Languages
    private static final List<String> languagesList = new ArrayList<>();


    //How many tweets to hold in memory before writing
    public static int getSearchBuffer() {return Integer.parseInt(properties.getProperty("SearchBuffer"));}

    //Total Number of tweets to collect
    public static int getNumTweets() {return Integer.parseInt(properties.getProperty("NumTweets"));}

    //Output Files
    public static String getOutputDir() {return properties.getProperty("OutputDir");}
    public static String getOutputFile() {return properties.getProperty("TweetsOutput");}
    public static String getAccountsOutputFile() {return properties.getProperty("AccountsOutput");}
    public static String getGraphOutput() { return properties.getProperty("GraphOutput");}

    //Graph Input Files
    public static String getGraphDir(){ return properties.getProperty("GraphInputDir"); }
    public static String getGraphInputFile(){ return properties.getProperty("GraphTweetsInput");}
    public static String getGraphAccountsFile(){ return properties.getProperty("GraphAccountsInput"); }

    //List of Search Terms
    public static List<String> getSearchTermsList() {return searchTermsList;}
    //List of Languages
    public static List<String> getLanguages(){return languagesList;}

    //Formatting Config
    public static char getDelim() {return properties.getProperty("Delim").charAt(0);}
    public static char getNewLineDelim() {return properties.getProperty("NewLineDelim").charAt(0);}


    /**
     * Returns String information on Config values for human reading
     */
    public static String getConfigInfo() {
        return "\nSearch Terms:\n" +
                getSearchTermsList().toString() + "\n" +
                "\nLanguages:\n" +
                getLanguages().toString() + "\n" +
                "\nSearch and Write Buffer: " + getSearchBuffer() + "\n" +
                "\nMax Tweets: " + getNumTweets() + "\n" +
                "\nOutput Directory: " + getOutputDir() +
                "\nTweet Output File: " + getOutputFile() +
                "\nAccount Output File: " + getAccountsOutputFile() + "\n";
    }

    /**
     * Reads from Configuration file and changes fields based on its values
     * @param filename Name of configuration File
     */
    public static void initialise(String filename) throws Exception{
        readConfig(filename);
        setConfig();

        new File(getOutputDir()).mkdirs();
    }

    //Read from Configuration file
    private static void readConfig(String filename){
        try {
            properties.load(new FileReader(filename));
        } catch (IOException e) {e.printStackTrace();}
    }

    //Set up the class with the properties for global use
    private static void setConfig(){
        StringTokenizer tokens = new StringTokenizer(properties.getProperty("SearchTerms"));
        while (tokens.hasMoreTokens()){
            searchTermsList.add(tokens.nextToken());
        }

        tokens = new StringTokenizer(properties.getProperty("Languages"));
        while (tokens.hasMoreTokens()){
            languagesList.add(tokens.nextToken());
        }
    }
}
