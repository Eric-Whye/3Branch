package com.ThreeBranch;

import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;

public class GraphMain {
  public static void readGraph() {
    try {
      Configuration.initialise(Configuration.ConfigFilename);
    } catch(Exception e) {e.printStackTrace();}
      Graph graph = new Graph();
      GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
      fp.readGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
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