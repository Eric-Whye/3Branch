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
        String outputFile = config.getValueFor("graph/GDFOutput");
        Graph retweet = new Graph();
        Graph userToH = new Graph();


        GraphRTFileProcessor fp = new GraphRTFileProcessor(retweet);
        StanceProcessing sp = new StanceProcessing(userToH);
        List<String> lines = new ArrayList<>();
        lines.add("nodedef>name VARCHAR");
        /*
        for (Point p : graph){
            lines.add(p.getName());
        }
        lines.add("edgedef>node1 VARCHAR,node2 VARCHAR, weight INTEGER, att1 VARCHAR, att2 VARCHAR");
        for (Point p : graph){
            for (Edge e : graph.getAdj(p)){
                String stanceNum;
                StancePoint source = (StancePoint) e.getSource();
                if (source.getStance().isPresent()) {
                    stanceNum = String.valueOf(source.getStance().get());
                }
                else stanceNum = " ";
                lines.add(e.getSource() + "," + e.getDestination() + "," + e.getWeight() + "," + stanceNum);
            }
        }*/
        FileEntryIO.writeLineByLine(lines,
                config.getValueFor("format.newLineDelim").charAt(0),
                config.getValueFor("graph.GDFOutput")
        );
    }
}
