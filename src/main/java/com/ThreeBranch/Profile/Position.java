package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;

import java.lang.Comparable;

public abstract class Position{
  public Position(String s) {};

  public void add(Point p);
  
  //Should return true iff p is <= this
  public boolean contains(Position p);
}