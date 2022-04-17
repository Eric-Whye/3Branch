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
        boolean run = true;
        Scanner in = new Scanner(System.in);
        List<Graph> listOfGraphs = new ArrayList<>();
        String stanceFile = "";


        while (run) {
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

                case "combine graphs":
                    if (!graph.isEmpty())
                        System.out.println("Old Graph Dropped");
                    if (listOfGraphs.size() <= 1) {
                        System.out.println("Graphs list doesn't not have enough graphs");
                        break;
                    }
                    System.out.println("Building Stance Graph");
                    graphBootstrapping(listOfGraphs);
                    System.out.println("Stance Graph built");
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
                    
                case "print mostretweeted":
                    findInfluentials(graph);
                    break;
                    
                case "quit":
                    run = false;
                    System.out.println("Goodbye");
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
            if (((StancePoint) p).getStance().isPresent() && list.get(0).getAdj(p).size() != 0) {
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
}