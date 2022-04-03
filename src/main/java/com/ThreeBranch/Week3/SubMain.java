package com.ThreeBranch.Week3;

import com.ThreeBranch.*;

public class SubMain {
  public static void handle() {
    try {
      Configuration.initialise(Configuration.ConfigFilename);
      
      FileProcessor fp = new FileProcessor();
      Graph graph = new Graph();
      graph.createGraph(fp.getUserRTRelations());
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}