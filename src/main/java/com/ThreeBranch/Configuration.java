package com.ThreeBranch;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;

public class Configuration {
    private int numTweets;
    private String tweetsOutput;

    private List<String> hashtagsList;

    public int getNumTweets() {return numTweets;}
    public void setNumTweets(int numTweets) {this.numTweets = numTweets;}
    public String getOutputFile() {return tweetsOutput;}
    public void setOutputFile(String outputFile) {this.tweetsOutput = outputFile;}
    public List<String> getHashtagsList() {return hashtagsList;}
    public void setHashtagsList(List<String> hashtagsList) {this.hashtagsList = hashtagsList;}

    
    public Configuration(){
        InputStream inputStream = getClass().getResourceAsStream("twitter4j.properties");
        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while (reader.ready()) {
                String line = reader.readLine();
                StringTokenizer tokens = new StringTokenizer(line, "\t");
                if (!tokens.hasMoreTokens())
                    continue;

                switch(tokens.nextToken()){
                    case "numTweets":
                        numTweets = Integer.parseInt(tokens.nextToken());
                        break;

                    case "outputFile":
                        tweetsOutput = tokens.nextToken();
                        break;
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
}
