package com.ThreeBranch.Analysis;

import com.ThreeBranch.Tri;
import com.ThreeBranch.Graph.Point;

public interface Condition { 
  public Tri matches(Point p);
  
  public String toString(); //This should ONLY EVER be used for debugging
}