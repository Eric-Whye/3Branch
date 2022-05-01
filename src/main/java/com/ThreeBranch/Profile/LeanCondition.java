package com.ThreeBranch.Profile;

import java.util.Optional;

import com.ThreeBranch.Graph.*;

public class LeanCondition implements Condition {
  private Tri positiveLean = Tri.NONE;
  
  public LeanCondition(Tri positiveLean) {
    this.positiveLean = positiveLean;
  }
  
  public Tri matches(Point p) {
    if(!(p instanceof UserPosition))
      return Tri.NONE;
    
    UserPosition up = (UserPosition)p;
    
    Optional<Integer> stanceOption = up.getStance();
    if(!stanceOption.isPresent())
      return Tri.NONE;
    
    int stance = stanceOption.get();
    
    switch(positiveLean) {
      case TRUE:
        return (stance > 0) ? Tri.TRUE : Tri.FALSE;
      case FALSE:
        return (stance < 0) ? Tri.TRUE : Tri.FALSE;
      case NONE:
        return (stance == 0) ? Tri.TRUE : Tri.FALSE;
    }
    
    return Tri.NONE;
  }
  
  //This is JUST for debugging
  public String toString() {
    switch(positiveLean) {
      case TRUE:
        return "TRUE";
      case FALSE:
        return "FALSE";
      default:
        return "NONE";
    }
  }
}