package com.ThreeBranch;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Twitter.GraphRTFileProcessor;

import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Stream;

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
                    graph = new Graph();
                    fp = new GraphRTFileProcessor(graph);
                    fp.populateRetweetGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    System.out.println("Retweet Graph Built");
                    break;
                    
                case "build retweeted":
                    if(!graph.isEmpty())
                      System.out.println("Old Graphs Dropped");
                    System.out.println("Building Retweeted Graph");
                    graph = new Graph();
                    fp = new GraphRTFileProcessor(graph);
                    fp.populateRetweetedGraphFromFile(Configuration.getValueFor("graph.tweetsInput"));
                    System.out.println("Retweeted Graph Built");
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
}