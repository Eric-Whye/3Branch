package com.ThreeBranch;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        new File("VaxData").mkdirs();
        Configuration.readAndSetConfig();
        Twitterer twitterer = new Twitterer();
        OutputTweets ot = new OutputTweets();
        twitterer.searchByHashtags(ot);

        System.out.println("done");
    }
}
