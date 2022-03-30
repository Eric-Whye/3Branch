package com.ThreeBranch;


import twitter4j.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class Main {

    public static void main(String[] args) throws TwitterException {

        Configuration.initialise();//read from config file
        TweetData.initialise();
        System.out.println(Configuration.getConfigInfo());

        Twitterer twitterer = new Twitterer();
        twitterer.streamStart();

    }
}
