package com.ThreeBranch.Analysis;

import java.util.StringTokenizer;
import java.util.Optional;

import com.ThreeBranch.Tri;
import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Profile.UserPosition;

public class Analysis {
  private static double mu = 0;
  private static double sigma = 0;
  
  public static void init(String func, Graph data) {
    mu = analyze(func, data);
    sigma = calcSigma(data);
  }
  
  private static double calcSigma(Graph data) {
    long total = 0;
    long sum = 0;
    
    for(Point p : data) {
      if(p instanceof UserPosition) {
        UserPosition up = (UserPosition) p;
        Optional<Integer> s = up.getStance();
        if(s.isPresent()) {
          sum += Math.pow(mu - s.get(), 2);
          total++;
        }
      }
    }
    
    return Math.sqrt(sum / ((double)total));
  }
  
  public static double zScore(double d) {
    double distance = Math.abs(d - mu);
    return distance / sigma;
  }
  
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