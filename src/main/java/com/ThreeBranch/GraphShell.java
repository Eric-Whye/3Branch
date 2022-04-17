package com.ThreeBranch;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import com.ThreeBranch.Twitter.StancePoint;
import com.ThreeBranch.Twitter.StanceProcessing;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;
import java.util.Optional;

public class GraphShell {
    Graph graph = new Graph();

    public GraphShell() {
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        List<Graph> listOfGraphs = new ArrayList<>();
        String stanceFile = "";


        while (true) {
            GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
            StanceProcessing sp = new StanceProcessing(graph);

            try {
                Configuration.initialise(Configuration.ConfigFilename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.print("GraphShell> ");
            String input = in.nextLine();

            switch (input.toLowerCase().trim()) {
                case "build retweet":
                    if(!graph.isEmpty())
                      System.out.println("Old Graph Dropped");
                    System.out.println("Building Retweet Graph");
                    graph.clear();
                    fp.populateRetweetGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    stanceFile = Configuration.getValueFor("stance.influentials");
                    System.out.println("Retweet Graph Built");
                    break;
                    
                case "build retweeted":
                    if(!graph.isEmpty())
                      System.out.println("Old Graph Dropped");
                    System.out.println("Building Retweeted Graph");
                    graph.clear();
                    fp.populateRetweetedGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    stanceFile = Configuration.getValueFor("stance.influentials");
                    System.out.println("Retweeted Graph Built");
                    break;

                case "combine graphs":
                    if (!graph.isEmpty())
                        System.out.println("Old Graph Dropped");
                    System.out.println("Building Stance Graph");
                    graphBootstrapping(listOfGraphs);
                    break;

                case "build user to hashtag":
                    if (!graph.isEmpty())
                        System.out.println("Old Graph Dropped");
                    System.out.println("Building user to hashtag Graph");
                    graph.clear();
                    fp.populateUserToHashtagGraph(Configuration.getValueFor("graph.tweetsInput"));
                    stanceFile = Configuration.getValueFor("stance.hashtags");
                    System.out.println("User to hashtag graph built");
                    break;

                case "build hashtag to user":
                    if (!graph.isEmpty())
                        System.out.println("Old Graph Dropped");
                    System.out.println("Building hashtag to user Graph");
                    graph.clear();
                    fp.populateHashtagToUserGraph(Configuration.getValueFor("graph.tweetsInput"));
                    stanceFile = Configuration.getValueFor("stance.hashtags");
                    System.out.println(graph.size());
                    System.out.println("hashtag to user graph built");
                    break;

                case "add graph":
                    if (graph.isEmpty()) {
                        System.out.println("No graph built");
                        break;
                    }
                    listOfGraphs.add(graph);
                    break;

                case "clear list":
                    listOfGraphs.clear();
                    break;

                case "write":
                    if (graph.isEmpty()) {
                        System.out.println("No Graph built");
                    } else {
                        fp.writeGraphToFile(graph);
                    }
                    break;
                    
                case "print most retweeted":
                    findInfluentials(graph);
                    break;
                    
                case "quit":
                    System.out.println("Goodbye");
                    System.exit(0);
                    break;

                case "assign stances":
                    System.out.println("Assigning Stances");
                    if (graph.isEmpty()) {
                        System.out.println("No Graph built");
                    } else {
                        sp.initialiseStances(stanceFile);
                        int i = 0;
                        while (sp.calcStances() && i++ < Integer.parseInt(Configuration.getValueFor("stance.iterations"))) {}
                        sp.writeStances(graph);
                        System.out.println("Stances Assigned");
                    }
                    break;

                case "print coverage":
                    System.out.println(sp.stanceCoverage());
                    break;

                case "get random user":
                  randomUserHandler();
                  break;

                case "compare algorithms":
                  algCompHandler();
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
    
    private void randomUserHandler() {
      StancePoint u = new StancePoint("");
      Point p;
      int retweets = 0;
      try {
        do {
          p = graph.getRandomPoint();
          
          if(!(p instanceof StancePoint)) {
            System.out.println("Graph contains non-users");
            return;
          }
          u = (StancePoint) p;
          
          retweets = graph.getAdj(u).stream().mapToInt(o -> o.getWeight()).sum();
        } while(retweets < 10);
      } catch (IllegalStateException e) {
        System.err.println("Graph is empty");
      }
      Optional<Integer> stance = u.getStance();
      if(stance.isPresent()) {
        System.out.println(u.getName() + " Stance: " + stance.get() + " Retweets: " + retweets);
      } else {
        System.out.println(u.getName() + " No Stance Assigned " + " Retweets: " + retweets);
      }
    }

    private void graphBootstrapping(List<Graph> list){
        HashSet<Point> nonStances = new HashSet<>();
        Graph output = new Graph();

        for (Point p : list.get(0)) {
            //If the point has a stance assigned, add it without change to the output
            //
            if (((StancePoint) p).getStance().isPresent()) {
                output.addArc(p, list.get(0).getAdj(p));
            } else {
                nonStances.add(p);
            }
        }

        for (Point p : nonStances){
            if (list.get(1).hasVertex(p.getName()) && list.get(1).getAdj(p).size() != 0){
                output.addArc(p, list.get(1).getAdj(p));
            }
        }

        graph = output;
    }
    
    private void algCompHandler() {
      final int comparisonCount = Integer.parseInt(Configuration.getValueFor("comparison.count"));
      String stanceFile = "";
      List<Graph> listOfGraphs = new ArrayList<>();
      
      System.out.println("This is for comparing how much the hashtags contribute to stance assignment, it will take a while");
      
      //---------------------------------------
      System.out.println("Building first graph");
      Graph unenhancedGraph = new Graph();
      GraphRTFileProcessor ufp = new GraphRTFileProcessor(unenhancedGraph);
      StanceProcessing usp = new StanceProcessing(unenhancedGraph);
      ufp.populateRetweetGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
      stanceFile = Configuration.getValueFor("stance.influentials");
      usp.initialiseStances(stanceFile);
      int i = 0;
      while (usp.calcStances() && i++ < Integer.parseInt(Configuration.getValueFor("stance.iterations"))) {}
      
      //---------------------------------------
      System.out.println("Building second graph");
      
      listOfGraphs.clear();
      
      Graph uthGraph = new Graph();
      GraphRTFileProcessor uthfp = new GraphRTFileProcessor(uthGraph);
      StanceProcessing uthsp = new StanceProcessing(uthGraph);
      uthfp.populateUserToHashtagGraph(Configuration.getValueFor("graph.tweetsInput"));
      stanceFile = Configuration.getValueFor("stance.hashtags");
      uthsp.initialiseStances(stanceFile);
      i = 0;
      while (uthsp.calcStances() && i++ < Integer.parseInt(Configuration.getValueFor("stance.iterations"))) {}
      
      listOfGraphs.add(uthGraph);
      
      Graph htuGraph = new Graph();
      GraphRTFileProcessor htufp = new GraphRTFileProcessor(htuGraph);
      StanceProcessing htusp = new StanceProcessing(htuGraph);
      htufp.populateHashtagToUserGraph(Configuration.getValueFor("graph.tweetsInput"));
      stanceFile = Configuration.getValueFor("stance.hashtags");
      htusp.initialiseStances(stanceFile);
      i = 0;
      while (htusp.calcStances() && i++ < Integer.parseInt(Configuration.getValueFor("stance.iterations"))) {}
      
      listOfGraphs.add(htuGraph);
      
      graphBootstrapping(listOfGraphs);
      Graph enhancedGraph = graph;
      
      System.out.println("All Graphs Built");
      
      //---------------------------------------
      System.out.println("Comparing Graphs");
      
      double avergageDifference = 0;
      int sum = 0;
      int count = 0;
      Point p1;
      Point p2;
      StancePoint u1;
      StancePoint u2;
      
      for(i = 0; i < comparisonCount; i++) {
        try{
          p1 = unenhancedGraph.getRandomPoint();
          if(!(p1 instanceof StancePoint)) {
            System.err.println("Illegal Graph Structure, Please Use StancePoint");
            return;
          }
          u1 = (StancePoint)p1;
          
          Optional<Point> op = enhancedGraph.getPointIfExists(u1.getName());
          if(op.isPresent()) {
            p2 = op.get();
            if(!(p2 instanceof StancePoint)) {
              System.err.println("Illegal Graph Structure, Please, Use StancePoint");
              return;
            }
            u2 = (StancePoint)p2;
            
            System.out.println(u1.toString() + " --- " + u2.toString());
            
          } else {
            System.err.println("Second graph does not contain " + u1.getName());
          }
          
        } catch (IllegalStateException e) {
          System.err.println("Error: At least one graph is empty");
          e.printStackTrace();
        }
      }
      
    }
}