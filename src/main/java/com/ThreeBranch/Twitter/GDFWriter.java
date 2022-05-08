package com.ThreeBranch.Twitter;

import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class GDFWriter {
    private GDFWriter(){}

    public static void writeAllDataToGDF(){
        Configuration config = Configuration.getInstance();
        String inFile = config.getValueFor("graph.tweetsInput");
        String outputFile = config.getValueFor("graph/GDFOutput");

        GraphRTFileProcessor fp;
        StanceProcessing sp;
        Graph retweet = new Graph();
        Graph userToH = new Graph();
        Graph combined = new Graph();


        List<String> lines = new ArrayList<>();
        lines.add("nodedef>name VARCHAR, stance VARCHAR");

        //--------------------Retweet Graph, var retweet
        fp = new GraphRTFileProcessor(retweet);
        sp = new StanceProcessing(retweet);
        fp.populateRetweetGraphFromFile(inFile);
        sp.initialiseStances(config.getValueFor("stance.influentials"));
        for (int i = 0; i < Integer.parseInt(config.getValueFor("stance.iterations")); i++){
            if (!sp.calcStances())
                break;
        }
        System.out.println("Retweet Graph Built");
        //--------------------User to Hashtag Graph, var userToH
        fp = new GraphRTFileProcessor(userToH);
        sp = new StanceProcessing(userToH);
        fp.populateUserToHashtagGraph(inFile);
        sp.initialiseStances(config.getValueFor("stance.hashtags"));
        for (int i = 0; i < Integer.parseInt(config.getValueFor("stance.iterations")); i++){
            if (!sp.calcStances())
                break;
        }
        System.out.println("User to Hashtag Graph Built");
        //--------------------Combined retweet and userToH, var combined
        List<Graph> graphsList = new ArrayList<>();
        graphsList.add(retweet);
        graphsList.add(userToH);
        GraphQueries.graphBootstrapping(graphsList, combined);
        System.out.println("Combined Graph Built");
        //--------------------
        int minStance = Integer.parseInt(config.getValueFor("stance.minStance"));
        int maxStance = Integer.parseInt(config.getValueFor("stance.maxStance"));

        for (Point p : combined){
            String stance = " ";
            if (((StancePoint) p).getStance().isPresent()){
                int stanceNum = ((StancePoint) p).getStance().get();

                if (stanceNum >= minStance && stanceNum <= -1)
                    stance = "anti";
                else if (stanceNum <= maxStance && stanceNum >= 1)
                    stance = "pro";
                else if (stanceNum == 0)
                    stance = "neutral";
            }
            lines.add(p.getName() + ", " + stance);
        }

        lines.add("edgedef>node1 VARCHAR,node2 VARCHAR, weight INTEGER");
        for (Point p : combined){
            for (Edge e : combined.getAdj(p)){
                lines.add(e.getSource() + "," + e.getDestination() + "," + e.getWeight());
            }
        }
        FileEntryIO.writeLineByLine(lines,
                config.getValueFor("format.newLineDelim").charAt(0),
                config.getValueFor("graph.GDFOutput")
        );
    }
}
