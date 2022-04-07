package com.ThreeBranch;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class GraphMain {
  public static void readGraph() {
    try {
      Configuration.initialise(Configuration.ConfigFilename);
    } catch(Exception e) {e.printStackTrace();}
      Graph graph = new Graph();
      GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
      fp.populateGraphFromGraphFile(Configuration.getValueFor("graph.output"));
  }

  public static void writeGraph(){
    try {
      Configuration.initialise(Configuration.ConfigFilename);
    } catch(Exception e) {e.printStackTrace();}

    Graph graph = new Graph();
    GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
    fp.populateGraphFromTweetFile(Configuration.getValueFor("graph.tweetsInput"));
    fp.writeGraphToFile(graph);
  }

  public static void findInfluentials(){
    try{
      Configuration.initialise(Configuration.ConfigFilename);
    } catch (FileNotFoundException e) {e.printStackTrace();}

    Graph graph = new Graph();
    GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
    //fp.populateGraphFromTweetFile(Configuration.getValueFor("graph.tweetsInput"));
    fp.populateGraphFromGraphFile(Configuration.getValueFor("graph.output"));

    Hashtable<Point, Integer> table = new Hashtable<>();
    for (Point p : graph){
      List<Edge> list = graph.getAdj(p);
      for (Edge e : list){
        if (table.containsKey(e.getDestination())){
          int rt = table.get(e.getDestination());
          table.put(e.getDestination(), e.getWeight()+rt);
        } else
          table.put(e.getDestination(), e.getWeight());
      }
    }

    Set<Map.Entry<Point, Integer>> numRetweetsByUser = table.entrySet();
    Stream<Map.Entry<Point, Integer>> thing = numRetweetsByUser.stream().sorted((o1, o2) -> {
      if (o1.getValue().equals(o2.getValue()))
        return 0;
      else if (o1.getValue() > o2.getValue())
        return 1;
      else return -1;
      });

    System.out.println(Arrays.toString(thing.toArray()));
  }
}