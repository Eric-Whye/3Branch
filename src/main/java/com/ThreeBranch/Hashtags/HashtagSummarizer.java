package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Graph.*;

public class HashtagSummarizer {
  public static void rawPrintGraph(Graph g) {
    for(Point p : g) {
      System.out.println(p.getName());
      for(Edge e : g.getAdj(p))
        System.out.println("\t" + e.getDestination().getName() + " * " + e.getWeight());
      System.out.println("");
    }
  }
}
