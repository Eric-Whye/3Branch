package com.ThreeBranch;

public class Main {

    public static void main(String[] args) {
        switch(args[0]){

                case "-g":

                    break;

                case "-G":
                default:
                    try {
                        Configuration.initialise();//read from config file
                        TweetData.initialise();//Read TweetIDs and userhandles for duplication checking
                    }catch(Exception e){e.printStackTrace();}
                    System.out.println(Configuration.getConfigInfo());

                    Twitterer twitterer = new Twitterer();
                    twitterer.streamStart();
        }
    }
}
