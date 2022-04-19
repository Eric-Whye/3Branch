package com.ThreeBranch.Twitter;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;

import java.util.*;
import java.util.stream.Stream;

public abstract class GraphQueries {
    public static void algCompHandler(Graph graph) {
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
        for (int i = 0; i < Integer.parseInt(Configuration.getValueFor("stance.iterations")); i++){
            if (!usp.calcStances())
                break;
        }
        //---------------------------------------
        System.out.println("Building second graph");

        Graph uthGraph = new Graph();
        GraphRTFileProcessor uthfp = new GraphRTFileProcessor(uthGraph);
        StanceProcessing uthsp = new StanceProcessing(uthGraph);
        uthfp.populateUserToHashtagGraph(Configuration.getValueFor("graph.tweetsInput"));
        stanceFile = Configuration.getValueFor("stance.hashtags");
        uthsp.initialiseStances(stanceFile);
        for (int i = 0; i < Integer.parseInt(Configuration.getValueFor("stance.iterations")); i++){
            if (!uthsp.calcStances())
                break;
        }
        listOfGraphs.add(uthGraph);

        Graph htuGraph = new Graph();
        GraphRTFileProcessor htufp = new GraphRTFileProcessor(htuGraph);
        StanceProcessing htusp = new StanceProcessing(htuGraph);
        htufp.populateHashtagToUserGraph(Configuration.getValueFor("graph.tweetsInput"));
        stanceFile = Configuration.getValueFor("stance.hashtags");
        htusp.initialiseStances(stanceFile);
        for (int i = 0; i < Integer.parseInt(Configuration.getValueFor("stance.iterations")); i++){
            if (!htusp.calcStances())
                break;
        }
        listOfGraphs.add(htuGraph);

        HashSet<Point> nonStances = new HashSet<>();
        Graph output = new Graph();

        for (Point p : listOfGraphs.get(0)) {
            //If the point has a stance assigned, add it without change to the output
            //
            if (((StancePoint) p).getStance().isPresent() && listOfGraphs.get(0).getAdj(p).size() != 0)
                output.addArc(p, listOfGraphs.get(0).getAdj(p));
            else
                nonStances.add(p);
        }

        for (Point p : nonStances)
            if (listOfGraphs.get(1).hasVertex(p.getName()) && listOfGraphs.get(1).getAdj(p).size() != 0)
                output.addArc(p, listOfGraphs.get(1).getAdj(p));
        Graph enhancedGraph = graph;

        System.out.println("All Graphs Built");

        //---------------------------------------
        System.out.println("Comparing Graphs");

        int agreements = 0;
        int disagreements = 0;
        int count = 0;
        Point p1;
        Point p2;
        StancePoint u1;
        StancePoint u2;

        for(int i = 0; i < comparisonCount; i++) {
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


                    Optional<Integer> os1 = u1.getStance();
                    Optional<Integer> os2 = u2.getStance();
                    if(os1.isPresent() && os2.isPresent()) {
                        int s1 = os1.get();
                        int s2 = os2.get();

                        if((s1 < 0 && s2 < 0) ||
                                (s1 == 0 && s2 == 0) ||
                                (s1 > 0 && s2 > 0)
                        ) {
                            agreements++;
                            count++;
                        } else {
                            disagreements++;
                            count++;
                        }
                    }

                }

            } catch (IllegalStateException e) {
                System.err.println("Error: At least one graph is empty");
                e.printStackTrace();
            }
        }

        System.out.println("----------------------------------");
        System.out.println("Agrees: " + agreements);
        System.out.println(((double)agreements / count) * 100 + "%");
        System.out.println("Disagreements: " + disagreements);
        System.out.println(((double)disagreements / count) * 100 + "%");
        System.out.println("Total: " + count);
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

    public static void randomUserHandler(Graph graph) {
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
}
