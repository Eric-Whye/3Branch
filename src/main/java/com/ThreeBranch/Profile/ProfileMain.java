package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;

import java.util.Optional;

//WE NEED TO GET RID OF THIS ASAP
import java.lang.reflect.Constructor;
import com.ThreeBranch.Twitter.StancePoint;

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
      
      if(p instanceof StancePoint) {
        StancePoint sp = (StancePoint) p;
        up.setStance(sp.getStance());
      }
      
      for(Edge e : usersToHashtags.getAdj(p)) {
        //This weird constructor nonsense is because its would be too much of a pain to make Graph generic, this should be removed ASAP
        Constructor c = null;
        try {
          c = HashPosition.class.getConstructor(String.class);
        } catch (Exception exception) {
          System.err.println("Error getting constructor in ProfileMain");
          return;
        }
        
        Optional<Point> hashtag = hashtagPositions.getPointIfExists(e.getDestination().getName(), c);
        
        if(!hashtag.isPresent()) {
          //System.err.println(e.getDestination().getName() + " has no associated labels");
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