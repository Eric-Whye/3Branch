package com.ThreeBranch.Profile;

import java.util.HashSet;
import java.util.Objects;

import com.ThreeBranch.Graph.*;

public class HashPosition implements Position, Point {
  //We could store this position as a single string, but for the comparisons I think a hashset will be more efficient
  private String hashtag;
  private HashSet<String> labels = new HashSet();
  private boolean inverted = false;
  private Tri positiveLean = Tri.NONE;
  
  public HashPosition(String hashtag) {
    this.hashtag = hashtag;
  }
  
  public Point create(String s) { return new HashPosition(s); }
  
  public String getName() {
    return hashtag;
  }
  
  public void add(Point p) {
    add(p.getName());
  }
  
  public void add(String s) {
    switch(s) {
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
        labels.add(s);
    }
  }
  
  public boolean contains(Position p) {
    if(!(p instanceof HashPosition))
      return false;
    
    HashPosition hp = (HashPosition) p;
    
    Tri hpLean = hp.rectify();
    Tri thisLean = this.rectify();
    
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
        return Tri.FALSE;
      case FALSE:
        return Tri.TRUE;
    }
    
    return Tri.NONE;
  }
 
  public void invert() {
    inverted = !inverted;
  }
  
  public void setLean(Tri lean) {
    positiveLean = lean;
  }
 
  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch(this.rectify()) {
      case TRUE:
        sb.append("+");
        break;
      case FALSE:
        sb.append("-");
        break;
    }
    for(String s : labels) {
      sb.append(s);
      sb.append(", ");
    }
    return sb.toString();
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
  
  //This is JUST for debugging purposes
  public String debugString() {
    String output = hashtag + ": \n";
    
    if(inverted) {
      output += "\tinverted == true\n";
    } else {
      output += "\tinverted == false\n";
    }
    
    switch(positiveLean) {
      case TRUE:
        output += "\tpositiveLean == TRUE\n";
        break;
      case FALSE:
        output += "\tpositiveLean == FALSE\n";
        break;
      case NONE:
        output += "\tpositiveLean == NONE\n";
        break;
    }
    
    output += "\tLabels: \n";
    for(String s : labels)
      output += "\t\t" + s + "\n";
    
    return output;
  }
}