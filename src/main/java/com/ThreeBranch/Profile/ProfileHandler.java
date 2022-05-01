package com.ThreeBranch.Profile;

import java.util.Iterator;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Hashtags.HashtagHandler;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.StanceProcessing;

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
}