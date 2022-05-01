package com.ThreeBranch.Profile;

import java.util.Iterator;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Hashtags.HashtagHandler;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import com.ThreeBranch.Twitter.Configuration;

public class ProfileHandler{
  public static Graph handleBuild() {
    Graph usersToHashtags = new Graph();
    Graph hastagsToLabels = new Graph();
    
    System.out.println("Old Graphs Dropped");
    
    GraphRTFileProcessor fp = new GraphRTFileProcessor(usersToHashtags);
    fp.populateUserToHashtagGraph(Configuration.getValueFor("graph.tweetsInput"));
    System.out.println("User to hashtags graph built");
    
    hastagsToLabels = HashtagHandler.build(hastagsToLabels);
    System.out.println("Hashtag to labels graph built");
    
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
        System.out.println(up.getName());
        
        Iterator<HashPosition> hpIter = up.getHashtags();
        while(hpIter.hasNext()){
          HashPosition hp = hpIter.next();
          System.out.println("\t" + hp.getName() + ": " + hp.toString());
        }
      }
    }
  }
}