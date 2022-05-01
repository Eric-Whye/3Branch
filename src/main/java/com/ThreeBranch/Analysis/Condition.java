package com.ThreeBranch.Analysis;

import com.ThreeBranch.Tri;
import com.ThreeBranch.Graph.Point;

public interface Condition { 
  Tri matches(Point p);
  
  String toString(); //This should ONLY EVER be used for debugging
}