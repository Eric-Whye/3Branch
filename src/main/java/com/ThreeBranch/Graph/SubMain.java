package com.ThreeBranch.Graph;

import com.ThreeBranch.*;

public class SubMain {
  public static void handle() {
    try {
      Configuration.initialise(Configuration.ConfigFilename);

      Graph graph = new Graph();
      GraphFileProcessor fp = new GraphFileProcessor(graph);
      fp.writeGraphToFile(graph);
    } catch(Exception e) {e.printStackTrace();}
  }
}