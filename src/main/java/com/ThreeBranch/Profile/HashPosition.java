package com.ThreeBranch.Profile;

import java.util.HashSet;
import java.util.Objects;

public HashPosition implements Position, Point {
  //We could store this position as a single string, but for the comparisons I think a hashset will be more efficient
  private String hashtag;
  private HashSet<String> labels = new HashSet();
  private boolean inverted = false;
  private Tri positiveLean = Tri.NONE;
  
  public HashPosition(String hashtag) {
    this.hashtag = hashtag;
  }
  
  public String getName() {
    return hashtag;
  }
  
  public void add(Point p) {
    switch(p.getName()) {
      case "rejecting":
        positiveLean = Tri.FALSE;
        break;
        
      case "accepting":
        positiveLean = Tri.TRUE;
        break;
        
      case "negation":
        inverted = true;
        break;
        
      default:
        labels.add(p.getName());
    }
  }
  
  public boolean contains(Position p) {
    if(!(p instanceof HashPosition))
      return false;
    
    HashPosition hp = (HashPosition) p;
    
    hpLean = hp.rectify();
    thisLean = this.rectify();
    
    if(hpLean != thisLean)
      return false;
    
    for(String s : hp.labels)
      if(!this.labels.contains(s))
        return false;
      
    return true;
  }
  
  private Tri rectify() {
    if(!inverted)
      return positiveLean;
    
    switch(positiveLean) {
      case TRUE:
        return FALSE;
      case FALSE:
        return TRUE;
    }
    
    return Tri.NONE;
  }
 
  public boolean equals(Object o) {
    if(!(o instanceof HashPosition))
      return false;
    
    HashPosition hp = (HashPosition) o;
    
    return hp.hashtag.equals(this.hashtag);
  }
  
  public int hashCode() {
    return Objects.hash(hashtag);
  }
}