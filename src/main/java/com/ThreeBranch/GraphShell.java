package com.ThreeBranch;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import com.ThreeBranch.Twitter.StanceProcessing;
import com.ThreeBranch.Twitter.User;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Stream;
import java.util.Optional;

public class GraphShell {
    Graph graph = new Graph();

    public GraphShell() {
        try {
            Configuration.initialise(Configuration.ConfigFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        GraphRTFileProcessor fp;

        while (true) {
            System.out.print("GraphShell> ");
            String input = in.nextLine();

            switch (input.toLowerCase().trim()) {
                case "build retweet":
                    if(!graph.isEmpty())
                      System.out.println("Old Graphs Dropped");
                    System.out.println("Building Retweet Graph");
                    graph.clear();
                    fp = new GraphRTFileProcessor(graph);
                    fp.populateRetweetGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    fp.writeGraphToFile(graph);
                    System.out.println("Retweet Graph Built");
                    break;
                    
                case "build retweeted":
                    if(!graph.isEmpty())
                      System.out.println("Old Graphs Dropped");
                    System.out.println("Building Retweeted Graph");
                    graph.clear();
                    fp = new GraphRTFileProcessor(graph);
                    fp.populateRetweetedGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    fp.writeGraphToFile(graph);
                    System.out.println("Retweeted Graph Built");
                    break;

                case "build stance":
                    if (!graph.isEmpty())
                        System.out.println("Old Graphs Dropped");
                    System.out.println("Building Stance Graph");
                    graph.clear();
                    fp = new GraphRTFileProcessor(graph);
                    fp.populateStanceFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    fp.writeGraphToFile(graph);
                    System.out.println("Stance graph built");
                    break;

                case "write":
                    if (graph.isEmpty()) {
                        System.out.println("Graph is empty");
                    } else {
                        fp = new GraphRTFileProcessor(graph);
                        fp.writeGraphToFile(graph);
                    }
                    break;
                    
                case "sort":
                    findInfluentials(graph);
                    break;
                    
                case "quit":
                    System.out.println("Goodbye");
                    System.exit(0);
                    break;

                case "assign stances":
                  stanceHandler();
                  break;

                case "print stances":
                  printStanceHandler();
                  break;

                default:
                    System.out.println("Unrecognised");
            }
        }
    }

    public static void findInfluentials(Graph graph) {

        Hashtable<Point, Integer> table = new Hashtable<>();
        for (Point p : graph) {
            List<Edge> list = graph.getAdj(p);
            for (Edge e : list) {
                if (table.containsKey(e.getDestination())) {
                    int rt = table.get(e.getDestination());
                    table.put(e.getDestination(), e.getWeight() + rt);
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
    
    private void stanceHandler() {
      System.out.println("Assigning Stances");
      
      StanceProcessing sp = new StanceProcessing(graph);
      sp.initialiseStances();
      int i = 0;
      while(sp.calcStances() && i < 16) {
      System.out.println(i++);}
      
      System.out.println("Stances Assigned");
    }      
    
    private void printStanceHandler() {
      for (Point p : graph) {
        if(p instanceof User) {
          User u = (User) p;
          Optional<Integer> stance = u.getStance();
          if(stance.isPresent()) {
            System.out.println(u.getName() + ": " + u.getStance().get());
          } else {
            //System.out.println(u.getName() + ": No Stance Assigned");
          }
        } else {
          System.out.println(p.getName() + ": Not a user");
        }
      }
    }
}