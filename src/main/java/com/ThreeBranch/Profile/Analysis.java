package com.ThreeBranch.Profile;

import java.util.StringTokenizer;

import com.ThreeBranch.Graph.*;

public class Analysis {
  public static double analyze(String func, Graph data) {
    //Remove everything but the info we care about
    func = func.substring(2, func.length() - 1);
        
    StringTokenizer st = new StringTokenizer(func, "|", false);
    Condition conditionA = ConditionFactory.get(st.nextToken().trim());
    
    if(st.hasMoreTokens()) {
      Condition conditionB = ConditionFactory.get(st.nextToken().trim());
      return calc(conditionA, conditionB, data);
    } else {
      return calc(conditionA, data);
    }
  }
  
  private static double calc(Condition a, Graph data) {
    long total = 0;
    long matches = 0;
    
    for(Point p : data) {
      switch(a.matches(p)) {
        case TRUE:
          total++;
          matches++;
          break;
        case FALSE:
          total++;
          break;
        default:
      }
    }
    
    return ((double) matches) / total;
  }
  
  private static double calc(Condition a, Condition b, Graph data) {
    long total = 0;
    long matchesBoth = 0;
    long matchesB = 0;
    
    for(Point p : data)
      switch(b.matches(p)) {
        case TRUE:
          total++;
          matchesB++;
          if(a.matches(p) == Tri.TRUE)
            matchesBoth++;
          break;
          
        case FALSE:
          total++;
          break;
          
        default:
      }
    
    double pOfB = ((double) matchesB) / total;
    double pOfBoth = ((double) matchesBoth) / total;
    return pOfBoth / pOfB;
  }
}