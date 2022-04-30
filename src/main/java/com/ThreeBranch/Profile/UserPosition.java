package com.ThreeBranch.Profile;

import java.util.HashSet;

public UserPosition implements Position {
  private HashSet<HashPosition> hashtags = new HashSet();
  private String username;
  
  public UserPosition(String username) {
    this.username = username;
  }
  
  public void add(Point p) {
    if(!(p instanceof HashPosition))
      throw new IllegalArgumentException("This only accepts HashPositions");
    
    hashtags.add((HashPosition)p);
  }
  
  public boolean contains(Position p) {
    if(!(p instanceof UserPosition))
      return false;
    
    UserPosition up = (UserPosition) p;
    
    for(HashPosition hp : up.hashtags)
      if(!this.hashtags.contains(hp))
        return false;
      
      return true;
  }
}