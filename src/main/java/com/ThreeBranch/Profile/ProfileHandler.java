package com.ThreeBranch.Profile;

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
}