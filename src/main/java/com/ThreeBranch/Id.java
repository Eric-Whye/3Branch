package com.ThreeBranch;

public class Id {
  private static int currentId = 0;
  
  public static String get() {
    currentId++;
    return "ID" + Integer.toString(currentId);
  }
}