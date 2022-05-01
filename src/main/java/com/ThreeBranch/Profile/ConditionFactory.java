package com.ThreeBranch.Profile;

public class ConditionFactory {
  public static Condition get(String cond) {
    //Normalize the condition we're given (remove whitespace and set everything to the same case)
    cond = cond.replaceAll("\\s", "");
    cond = cond.toLowerCase();
    
    switch(cond) {
      case "pro":
        return new LeanCondition(Tri.TRUE);
      case "anti":
        return new LeanCondition(Tri.FALSE);
      case "neut":
        return new LeanCondition(Tri.NONE);
      default:
        return new UserCondition(cond);
    }
  }
}