package com.ThreeBranch;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
          defaultHandle();
        } else {
          switch(args[0]){
                  case "-graph":
                    com.ThreeBranch.Week3.SubMain.handle();
                    break;

                  case "-g":
                  case "-G":
                      try {
                          Configuration.initialise(Configuration.ConfigFilename);//read from config file
                          TweetData.initialise();//Read TweetIDs and userhandles for duplication checking
                      }catch(Exception e){e.printStackTrace();}
                      System.out.println(Configuration.getConfigInfo());

                      Twitterer twitterer = new Twitterer();
                      twitterer.streamStart();
                      break;
                  default:
                    defaultHandle();
          }
        }
    }

    public static void defaultHandle() {
      System.out.println("Please enter an option");
      System.out.println("-g: Gather tweet data");
      System.out.println("-graph: process a text file as a retweet graph");
    }
}
