package com.ThreeBranch.Twitter;

import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Graph.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StanceProcessing {
    private final Graph graph;

    public StanceProcessing(Graph graph){
        this.graph = graph;
    }

    /**
     *
     */
    public void initialiseStances(){
        BufferedReader reader = null;
        List<Point> list = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader("Week3/Influential Users.txt"));
            while (reader.ready()){
                StringTokenizer tokens = new StringTokenizer(reader.readLine());
                if (tokens.countTokens() >= 2){
                    User user = new User(tokens.nextToken());
                    switch(tokens.nextToken()){
                        case "mid":
                            user.setStance(-500);
                            break;
                        case "pro":
                            user.setStance(1000);
                            break;
                        case "anti":
                            user.setStance(-1000);
                            break;
                    }
                    list.add(user);
                }
            }
        } catch(IOException e){ e.printStackTrace(); }
        finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        for (Point p : list){
            List<Edge> arcs = graph.removeVertex(p.getName());
            for (Edge arc : arcs){
                graph.addArc(p, arc.getDestination());
            }
        }
        for (Point p : graph){

        }
    }


    private class GraphStanceFileProcessor extends GraphRTFileProcessor{

        public GraphStanceFileProcessor(Graph graph) {
            super(graph);
        }
    }
}
