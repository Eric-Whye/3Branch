package com.ThreeBranch.Twitter;

import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Profile.*;
import com.ThreeBranch.LockedObject;
import com.ThreeBranch.Hashtags.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;
import java.util.HashSet;
import java.util.Iterator;

public abstract class GDFWriter {
    private GDFWriter(){}

    public static void writeAllDataToGDF(){
        Configuration config = Configuration.getInstance();
        String inFile = config.getValueFor("graph.tweetsInput");
        String outputFile = config.getValueFor("graph/GDFOutput");
        
        Graph retweet = new Graph();
        
        Graph userToH = new Graph();
        
        Graph combined = new Graph();

        Graph hashtagsToLabels;
        
        Graph profileGraph = new Graph();

        List<String> lines = new ArrayList<>();
        lines.add("nodedef>name VARCHAR, stance VARCHAR, class VARCHAR");

        //--------------------Retweet Graph, var retweet
        GraphRTFileProcessor fp1 = new GraphRTFileProcessor(retweet);
        fp1.populateRetweetGraphFromFile(inFile);
        System.out.println("Retweet Graph Built");
        
        //--------------------User to Hashtag Graph, var userToH
        GraphRTFileProcessor fp2 = new GraphRTFileProcessor(userToH);
        fp2.populateUserToHashtagGraph(inFile);
        System.out.println("Users to Hashtags Graph Built");
        
        //Hashtag to labels graph
        hashtagsToLabels = (new HashtagMain()).run(config.getValueFor("graph.tweetsInput"));
        System.out.println("Hashtags to labels built" + hashtagsToLabels.size());
        
        //Do all of the stance processing, ideally this would be separate threads, but that's a lot of code to write
        StanceProcessing sp = new StanceProcessing(retweet);
        sp.initialiseStances(config.getValueFor("stance.influentials"));
        for (int i = 0; i < Integer.parseInt(config.getValueFor("stance.iterations")); i++){
            if (!sp.calcStances())
                break;
        }
        System.out.println("Retweet Graph Stances Calculated");
        sp = new StanceProcessing(userToH);
        sp.initialiseStances(config.getValueFor("stance.hashtags"));
        for (int i = 0; i < Integer.parseInt(config.getValueFor("stance.iterations")); i++){
            if (!sp.calcStances())
                break;
        }
        System.out.println("User to Hashtag Graph Stances Calculated");
        
        //Build the profile graph
        ProfileMain pm = new ProfileMain(hashtagsToLabels, userToH);
        profileGraph = pm.getUserPositions();
        System.out.println("Profile Graph Built");
        
        //--------------------Combined retweet and userToH, var combined
        List<Graph> graphsList = new ArrayList<>();
        graphsList.add(retweet);
        graphsList.add(userToH);
        GraphQueries.graphBootstrapping(graphsList, combined);
        System.out.println("Combined Graph Built");
        //--------------------
        int minStance = Integer.parseInt(config.getValueFor("stance.minStance"));
        int maxStance = Integer.parseInt(config.getValueFor("stance.maxStance"));

        //Output all of the users
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
            lines.add(p.getName() + ", " + stance + ", " + "USER");
        }
        System.out.println("Users Written");

        //Output all of the hashtags and labels
        HashSet<String> hashtags = new HashSet();
        HashSet<String> labels = new HashSet();
        for(Point p : profileGraph) {
          if(p instanceof UserPosition) {
            UserPosition up = (UserPosition) p;
            Iterator<HashPosition> hpIter = up.getHashtags();
            while(hpIter.hasNext()) {
              HashPosition hp = hpIter.next();
              if(!hashtags.contains(hp.getName())) {
                hashtags.add(hp.getName());
                lines.add(hp.getName() + ", " + "NA" + ", " + "HASHTAG"); 
                Iterator<String> labelIter = hp.getLabels();
                while(labelIter.hasNext()) {
                  String s = labelIter.next();
                  if(!labels.contains(s)) {
                    labels.add(s);
                    lines.add(s + ", " + "NA" + ", " + "LABEL");
                  }
                }
              }
            }
          }
        }
        System.out.println("Hashtags and Labels Written");

        lines.add("edgedef>node1 VARCHAR,node2 VARCHAR, weight INTEGER");
        for (Point p : combined){
            for (Edge e : combined.getAdj(p)){
                lines.add(e.getSource() + "," + e.getDestination() + "," + e.getWeight());
            }
        }
        System.out.println("Retweet Edges Written");
        
        //All of the hashtag and label stuff
        hashtags.clear();
        for(Point p : profileGraph) {
          if(p instanceof UserPosition) {
            UserPosition up = (UserPosition) p;
            Iterator<HashPosition> hpIter = up.getHashtags();
            while(hpIter.hasNext()) {
              HashPosition hp = hpIter.next();
              lines.add(up.getName() + ", " + hp.getName() + ", " + "1");
              if(!hashtags.contains(hp.getName())) {
                hashtags.add(hp.getName());
                Iterator<String> labelIter = hp.getLabels();
                while(labelIter.hasNext()) {
                  String s = labelIter.next();
                  lines.add(hp.getName() + ", " + s + ", " + "1");
                }
              }
            }
          }
        }
        System.out.println("Hashtag Edges Written");
        
        FileEntryIO.writeLineByLine(lines,
                config.getValueFor("format.newLineDelim").charAt(0),
                config.getValueFor("graph.GDFOutput")
        );
    }
}
