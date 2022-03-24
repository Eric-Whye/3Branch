package com.ThreeBranch;


import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class Main {

    public static void main(String[] args){

        Configuration.initialise();//read from config file
        TweetData.readTweetIDs();
        System.out.println(/*Configuration.getConfigInfo()*/);

        Twitterer twitterer = new Twitterer();
        twitterer.streamStart();
/*
        //Shell to receive commands
        StreamShell shell = new StreamShell();
        Thread threadShell = new Thread(shell);*/
        System.out.println("done");
    }
}
