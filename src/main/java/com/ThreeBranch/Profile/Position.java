package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;

import java.lang.Comparable;

public interface Position{
  public void add(Point p);
  
  //Should return true iff p is <= this
  public boolean contains(Position p);
}