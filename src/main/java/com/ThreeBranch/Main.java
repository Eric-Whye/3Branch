package com.ThreeBranch;

public class Main {

    public static void main(String[] args) {
        switch(args[0]){

                case "-g":

                    break;

                case "-G":
                default:
                    Configuration.initialise();//read from config file
                    TweetData.initialise();
                    System.out.println(Configuration.getConfigInfo());

                    Twitterer twitterer = new Twitterer();
                    twitterer.streamStart();
        }
    }
}
