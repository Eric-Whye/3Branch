package com.ThreeBranch.Profile;

import java.util.Iterator;
import java.util.Scanner;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Hashtags.HashtagHandler;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.StanceProcessing;
import com.ThreeBranch.Analysis.Analysis;

public class ProfileHandler{
  public static Graph handleBuild() {
    Graph usersToHashtags = new Graph();
    Graph hastagsToLabels = new Graph();
    
    String stanceFile = Configuration.getValueFor("stance.hashtags");
    StanceProcessing sp = new StanceProcessing(usersToHashtags);
    
    System.out.println("Old Graphs Dropped");
    
    //Build the usersToHashtags graph and assign it stances
    GraphRTFileProcessor fp = new GraphRTFileProcessor(usersToHashtags);
    fp.populateUserToHashtagGraph(Configuration.getValueFor("graph.tweetsInput"));
    sp.initialiseStances(stanceFile);
    for(int i = 0; i < Integer.parseInt(Configuration.getValueFor("stance.iterations")); i++)
      if(!sp.calcStances())
        break;
    System.out.println("User to hashtags graph built");
    
    //Build the hashtag to label graph
    hastagsToLabels = HashtagHandler.build(hastagsToLabels);
    System.out.println("Hashtag to labels graph built");
    
    //Combine them and convert everything to positions
    ProfileMain pm = new ProfileMain(hastagsToLabels, usersToHashtags);
    System.out.println("Final graph built");
    return pm.getUserPositions();
  }
  
  public static void handleRawPrint(Graph g) {
    for(Point p : g) {
      if(!(p instanceof UserPosition)) {
        System.err.println("---------- GRAPH CONTAINS NONE UserPosition ENTRIES");
      } else {
        UserPosition up = (UserPosition) p;
        if(up.getStance().isPresent()) {
          System.out.println(up.getName() + ": " + up.getStance().get());
        } else {
          System.out.println(up.getName() + ": No Stance");
        }
        
        Iterator<HashPosition> hpIter = up.getHashtags();
        while(hpIter.hasNext()){
          HashPosition hp = hpIter.next();
          System.out.println("\t" + hp.getName() + ": " + hp.toString());
        }
      }
    }
  }
  
  //This is basically a subshell, sorry
  public static void handleAnalysis(Scanner in, Graph data) {
    boolean endFlag = false;
    
    while(!endFlag) {
      System.out.print("\nEquation> ");
      
      String input = in.nextLine();
      
      input = input.trim();
      input = input.toLowerCase();
      
      if(!verifyEquation(input)) {
        System.out.println("Please enter a valid equation");
        System.out.println("Format: \"P( condition | condition)\"");
        System.out.println("For help please type \"help\"");
      } else {
        switch(input) {
          case "quit":
            endFlag = true;
            break;
          case "help":
            handleHelp();
            break;
          case "clear":
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            break;
          default:
            System.out.println(Analysis.analyze(input, data));
            //endFlag = true;
            break;
        }
      }
    }
  }
  
  private static boolean verifyEquation(String input) {
    //These have special meaning
    if(input.equals("help"))
      return true;
    if(input.equals("quit"))
      return true;
    
    /*//Check the pretty stuff is there
    if(input.charAt(0) != 'p')
      return false;
    if(input.charAt(1) != '(')
      return false;
    if(input.charAt(input.length() - 1) != ')')
      return false;
    
    //Check that there is something for the first condition
    if(input.charAt(2) == "|" || input.charAt(2) == ")")
      return false;
    
    //If this is a 2 condition equation make sure there's something for the 2nd condition
    if(input.contains("|"))
      if(input.charAt(input.length() - 2) == '|')
        return false;*/
      
    //Originally I wrote all of that code to do this job, but then I figured out that a regex works better for this, but I want to leave that there in case this regex goes sour, and as a sort of explanation for what I want this to do
    //We require an equation to
      //Start with a P
      //Be enclosed in brackets
      //Contain zero or one "|" separators to possibly split up the 2 conditions
      //And to not have an empty condition
    return input.matches("^p\\([^\\|]+\\|?[^\\|]+\\)$");
  }
  
  private static void handleHelp() {
    System.out.println("First please construct a profile graph using the command \"build profile graph\", once you've done that you can use this subshell to analyze it");
    
    System.out.println("---USAGE---");
    System.out.println("Enter a probability equation starting with a 'P', and with correct brackets. You can either enter a direct probability equation by omitting the '|', or you can enter a conditional probability equation by including the '|'. Please ensure that neither condition is empty.");
    
    System.out.println("You can enter multiple labels in each condition by separating them with commas.");
    System.out.println("So the following command would mean \"The probability of a user tweeting a hashtag labelled with both (but not only) 'references covid' and 'solution'\"");
    System.out.println("\tP(-ref:covid, solution)\n");
    
    System.out.println("You can specify the lean (pro or anti) of a hashtag by including a '+' or '-' at the start of the list");
    System.out.println("So the following command would mean \"The probability of a user tweeting a hashtag labelled as 'pro', 'vaccine', and 'solution'\"");
    System.out.println("\tP(+ref:vaccine, solution)\n");
    
    System.out.println("Multiple sets of labels for each condition can be specified by separating each list with a ';'");
    System.out.println("So the following command would mean \"The probability of a user tweeting at least one hashtag labelled '1st' and 'social', and at least one hashtag labelled 'ref:australia' and 'place'");
    System.out.println("\tP(1st, social; ref:australia, place)");
    System.out.println("Note: These lists can both be satisfied by a single tweet, a user who tweeted \"#MyBodyMyChoice\" (labelled as \"1st, social, and rights\") and nothing else would get caught by the following command");
    System.out.println("\tP(1st, social; rights)");
    
    System.out.println("A user's stance can be queried by using the special labels 'pro', 'anti', and 'neut'.");
    System.out.println("'pro' will match any user with a stance greater than zero, 'anti' will match any user with a stance less than zero, and 'neut' will match any user with a stance equal to zero. Users with invalid stances, or who haven't been given a stance will never be matched");
    System.out.println("This CAN NOT be combined in the same list as the labels. It can however be used in the same command as a set of label lists if and only if the two are in different conditions, and the stance filter is by itself.");
    System.out.println("So the following command would return the probability of a user being pro vaccine");
    System.out.println("\tP(pro)\n");
    
    System.out.println("Conditional probabilities can be calculated by making use of '|'");
    System.out.println("This works the exact same way it would in ordinary statistics. So the following command would mean \"The probability of a user being anti vaccine given they tweet about Fauci in a negative light\"");
    System.out.println("\tP(anti | -ref:fauci)\n");
    
    System.out.println("Please not that in the previous example the stance condition and the hashtag condition were separate, if they had been included on the same side of the '|' the \"anti\" would've been interpreted literally as a label, and because we have no \"anti\" label (we use it as a protected term to refer to stances, it will never be a label), this will likely give incorrect results. Future versions may flag this as illegal and fail to execute the command at all. So the following command is INCORRECT and should NOT be used.");
    System.out.println("(Incorrect example): P(anti, -ref:fauci)");
  }
}