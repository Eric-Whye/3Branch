package com.ThreeBranch;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.*;
import com.ThreeBranch.Hashtags.*;

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
            } catch (FileNotFoundException e) {e.printStackTrace();}

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
                    
                case "print most retweeted":
                    GraphQueries.findInfluentials(graph);
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
                        for (int i = 0; i < Integer.parseInt(Configuration.getValueFor("stance.iterations")); i++){
                            if (!sp.calcStances())
                                break;
                        }
                        sp.writeStances(graph);
                        System.out.println("Stances Assigned");
                    }
                    break;

                case "print coverage":
                    System.out.println(sp.stanceCoverage());
                    break;

                case "get random user":
                  GraphQueries.randomUserHandler(graph);
                  break;

                case "compare algorithms":
                  GraphQueries.algCompHandler(graph);
                  break;

                case "build hashtag graph":
                  graph = HashtagHandler.build(graph);
                  break;

                case "sum hashtag graph":
                  HashtagHandler.summerize(graph);
                  break;

                default:
                    System.out.println("Unrecognised");
            }
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