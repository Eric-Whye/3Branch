package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;

import java.util.Optional;

public class ProfileMain {
  private Graph hashtagPositions = new Graph();
  private Graph userPositions = new Graph();
  
  public ProfileMain(Graph hashtagsToLabels, Graph usersToHashtags) {
    for(Point p : hashtagsToLabels) {
      HashPosition hp = new HashPosition(p.getName());
      for(Edge e : hashtagsToLabels.getAdj(p))
        hp.add(e.getDestination());
      hashtagPositions.addPoint(hp);
    }
    
    for(Point p : usersToHashtags) {
      UserPosition up = new UserPosition(p.getName());
      for(Edge e : usersToHashtags.getAdj(p)) {
        Optional<Point> hashtag = hashtagPositions.getPointIfExists(e.getDestination().getName());
        if(!hashtag.isPresent()) {
          System.err.println(e.getDestination().getName() + " has no associated labels");
        } else {
          up.add(hashtag.get());
        }
      }
      
      userPositions.addPoint(up);
    }
  }
  
  public Graph getUserPositions() {
    return userPositions;
  }
}