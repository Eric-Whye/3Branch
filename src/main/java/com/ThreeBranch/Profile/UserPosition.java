package com.ThreeBranch.Profile;

import java.util.HashSet;
import java.util.Objects;

import com.ThreeBranch.Graph.*;

public class UserPosition implements Position, Point{
  private HashSet<HashPosition> hashtags = new HashSet();
  private String username;
  
  public UserPosition(String username) {
    this.username = username;  
  }
  
  public String getName() {
    return username;
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
  
  public boolean equals(Object o) {
    if(!(o instanceof UserPosition))
      return false;
    
    UserPosition up = (UserPosition) o;
    
    return up.username.equals(this.username);
  }
  
  public int hashCode() {
    return Objects.hash(username);
  }
}