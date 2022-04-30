package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;

import java.util.HashMap;

public class ProfileMain {
  Graph hashtagPositions = new Graph();
  
  public void init(Graph hashtagsToLabels) {
    for(Point p : hashtagsToLabels.arbitraryAccess())
      hashtagPositions.addArc(p, Util.genPosition(hashtagsToLabels, p));
  }
}