package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.Point;

public interface Condition { 
  public Tri matches(Point p);
  
  public String toString(); //This should ONLY EVER be used for debugging
}