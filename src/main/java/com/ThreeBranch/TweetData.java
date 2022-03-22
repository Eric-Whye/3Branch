package com.ThreeBranch;

import java.io.*;
import java.util.HashSet;
import java.util.List;

/*
The Code that is commented out is my attempt to prevent tweet duplication, but I have no time to finish it at the moment
 */
public class TweetData {
    //Holds the Ids of gathered tweets for duplication checking purposes
    private static final HashSet<Long> tweetIDs = new HashSet<>();

    /**
     * Writes list.size() line entries to output where each entry consists of tokens with a delimiter
     * @param list of lists where each inner list has tokens to form a line
     * @param delim between each token
     * @param newLineDelim between each line
     * @param outputFile
     */
    public static void writeDataToFile(List<List<String>> list, char delim, char newLineDelim, File outputFile){
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
    public static boolean checkDupID(Long tweetID){
        return tweetIDs.contains(tweetID);
    }
    public static void addTweetID(Long tweetID){
        tweetIDs.add(tweetID);
    }

    /**
     * Reads tweetIDs into HashSet field for easier duplication checking
     */
    public static void readTweetIDs(){
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

    private TweetData() {
    }
}
