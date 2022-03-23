package com.ThreeBranch;


import java.io.File;


public class Main {

    public static void main(String[] args){
        new File("VaxData").mkdirs();
        Configuration.initialise();//read from config file
        TweetData.readTweetIDs();
        System.out.println(/*Configuration.getConfigInfo()*/);

        Twitterer twitterer = new Twitterer();
        twitterer.streamStart();

        //Shell to receive commands
        StreamShell shell = new StreamShell();
        Thread threadShell = new Thread(shell);
        
        System.out.println("done");
    }
}
