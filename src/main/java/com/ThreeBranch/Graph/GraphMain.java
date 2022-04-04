package com.ThreeBranch.Graph;

import com.ThreeBranch.*;

public class GraphMain {
  public static void readGraph() {
    try {
      Configuration.initialise(Configuration.ConfigFilename);
    } catch(Exception e) {e.printStackTrace();}
      Graph graph = new Graph();
      GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
      fp.readGraphFromFile(Configuration.getGraphOutput());
  }

  public static void writeGraph(){
    try {
      Configuration.initialise(Configuration.ConfigFilename);
    } catch(Exception e) {e.printStackTrace();}

    Graph graph = new Graph();
    GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
    fp.writeGraphToFile(graph);
  }
}