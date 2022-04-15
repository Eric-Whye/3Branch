package com.ThreeBranch.Twitter;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Twitter.Configuration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

/*
The Code that is commented out is my attempt to prevent tweet duplication, but I have no time to finish it at the moment
 */
public abstract class TweetData {
    //Holds the Ids of gathered tweets for duplication checking purposes
    private static final HashSet<Long> tweetIDs = new HashSet<>();
    public static String count;
    //Holds the userhandles of all the accounts
    public static final HashSet<String> userhandles = new HashSet<>();

    /**
     * Reads TweetIDs and userhandles from output files for duplication checking procedures
     * @throws NumberFormatException upon unexpected type parsing
     */
    public static void initialise() throws NumberFormatException {
        FileEntryIO.streamFromFile(Configuration.getValueFor("tweet.vaxFile"), new readTweetIDs());
        FileEntryIO.streamFromFile(Configuration.getValueFor("tweet.accountsFile"), new readUserhandles());
    }

    /**
     * Twitter Status duplication checking
     * @param tweetID Twitter.Status id
     * @return true if found
     */
    public static boolean checkDupID(Long tweetID){return tweetIDs.contains(tweetID);}
    protected static void addTweetID(Long tweetID){tweetIDs.add(tweetID);}
    /**
     * Twitter user duplication checking
     * @param userhandle Twitter userhandle
     * @return true if found
     */
    public static boolean checkDupAccount(String userhandle) { return userhandles.contains(userhandle);}
    protected static void addUserhandle(String userhandle) { userhandles.add(userhandle);}

    //Class that can be called to add the first token of a line to tweetID
    private static class readTweetIDs implements Callable {
        @Override
        public void call(Object o){
            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.hasMoreTokens())
                addTweetID(Long.valueOf(tokens.nextToken()));
        }
    }
    //Class that can be called to add the first token of a line to userhandles
    private static class readUserhandles implements Callable{
        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.hasMoreTokens())
                addUserhandle(tokens.nextToken());
        }
    }
}
