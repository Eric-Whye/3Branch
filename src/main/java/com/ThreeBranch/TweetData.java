package com.ThreeBranch;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

/*
The Code that is commented out is my attempt to prevent tweet duplication, but I have no time to finish it at the moment
 */
public class TweetData {
    //Holds the Ids of gathered tweets for duplication checking purposes
    private static final HashSet<Long> tweetIDs = new HashSet<>();

    //Holds the userhandles of all the accounts
    private static final HashSet<String> userhandles = new HashSet<>();

    /**
     * Writes list.size() line entries to output where each entry consists of tokens with a delimiter<br>
     * Synchronized
     * @param list of lists where each inner list has tokens to form a line
     * @param delim between each token
     * @param newLineDelim between each line
     * @param outputFile a File object
     */
    public static synchronized void writeDataToFile(List<List<String>> list, char delim, char newLineDelim, File outputFile){
        Writer writer = null;
        Formatter fm = new TweetFormatter(delim, newLineDelim);
        
        try{
            writer = new BufferedWriter(new FileWriter(outputFile, true));

            for (List<String> line : list) {
                String temp = fm.format(line);
                writer.write(temp);
            }
              
        }catch(IOException e){e.printStackTrace();}
        finally{
            try{
                assert writer != null;
                writer.close();
            }catch(IOException e){e.printStackTrace();}
        }
    }

    public static void initialise(){
        readTweetIDs();
        readUserhandles();
    }

    /**
     * Twitter Status duplication checking
     * @param tweetID Twitter.Status id
     * @return true if found
     */
    public static boolean checkDupID(Long tweetID){
        return tweetIDs.contains(tweetID);
    }
    public static void addTweetID(Long tweetID){
        tweetIDs.add(tweetID);
    }
    public static int getNumTweetsInData(){return tweetIDs.size();}

    public static boolean checkDupAccount(String userhandle) { return userhandles.contains(userhandle);}
    public static void addUserhandle(String userhandle) { userhandles.add(userhandle);}

    /**
     * Reads tweetIDs into HashSet field for easier duplication checking
     */
    private static void readTweetIDs(){
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new FileReader(Configuration.getOutputFile()));
            }catch(FileNotFoundException ex){ return; }

            while (reader.ready()){
                StringTokenizer tokens = new StringTokenizer(reader.readLine());
                if (tokens.hasMoreTokens())
                    tweetIDs.add(Long.valueOf(tokens.nextToken()));//HardCoded to read IDs
            }
        }catch(IOException e){e.printStackTrace();}
        finally{
            try {
                assert reader != null;
                reader.close();
            }catch(NullPointerException e){System.out.println("Output File does not exist");}
            catch (IOException e) {e.printStackTrace();}
        }
    }

    private static void readUserhandles(){
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new FileReader(Configuration.getAccountsOutputFile()));
            }catch(FileNotFoundException ex){ return; }

            while (reader.ready()){
                StringTokenizer tokens = new StringTokenizer(reader.readLine());
                if (tokens.hasMoreTokens()) {
                    userhandles.add(tokens.nextToken());//HardCoded to read Userhandles
                }
            }
        }catch(IOException e){e.printStackTrace();}
        finally{
            try {
                assert reader != null;
                reader.close();
            }catch(NullPointerException e){System.out.println("Output File does not exist");}
            catch (IOException e) {e.printStackTrace();}
        }
    }

    private TweetData() {
    }
}
