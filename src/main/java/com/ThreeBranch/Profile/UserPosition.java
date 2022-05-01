package com.ThreeBranch.Profile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Iterator;
import java.util.Optional;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Twitter.StancePoint;

public class UserPosition implements Position, Point{
  private HashSet<HashPosition> hashtags = new HashSet();
  private String username;
  private StancePoint stance; //We make use of a StancePoint to do the stance management stuff, the string it holds is a waste of space, ideally we'd refactor this so that a stance is a separate object which we can both hold an instance of
  
  public UserPosition(String username) {
    this(username, new StancePoint(username));
  }
  
  public UserPosition(String username, StancePoint stance) {
    this.username = username;
    this.stance = stance;
  }
  
  public Point create(String s) { return new UserPosition(s); }
  
  public String getName() {
    return username;
  }
  
  public Optional<Integer> getStance() {
    return stance.getStance();
  }
  
  public void setStance(int stance) {
    this.stance.setStance(stance);
  }
  
  public void setStance(Optional<Integer> stance) {
    this.stance.setStance(stance);
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
  
  public Iterator<HashPosition> getHashtags() {
    return hashtags.iterator();
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