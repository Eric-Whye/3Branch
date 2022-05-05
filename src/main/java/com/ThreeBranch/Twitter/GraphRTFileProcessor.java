package com.ThreeBranch.Twitter;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;

import java.util.*;

public class GraphRTFileProcessor {
    private final Graph graph;

    public GraphRTFileProcessor(Graph graph) {
        this.graph = graph;
    }


    private class readRetweets implements Callable {
        boolean reverse;
        /**
         * populates graph with retweets from file. The graph can be a retweet or retweeted graph depending on reverse
         * @param reverse true for retweeted graph, false for retweet graph.
         */
        private readRetweets(boolean reverse){this.reverse = reverse;}

        @Override
        public void call(Object o) throws IncorrectGraphFileException{
            StringTokenizer tokens = new StringTokenizer((String)o);
            if(tokens.countTokens() >= 3) {//If the Tweet has at least a text field
                tokens.nextToken();
                String user1 = tokens.nextToken();//Save userhandle
                if (!tokens.nextToken().equals("RT")) return; //If not a retweet then discard
                String user2 = tokens.nextToken();//Save userhandle

                if (!reverse){
                    graph.addArc(new StancePoint(user1), new StancePoint(removeSpecialCharacters(user2)));
                } else {
                    graph.addArc(new StancePoint(removeSpecialCharacters(user2)), new StancePoint(user1));
                }
            }
        }
    }

    //Removes ':'
    private String removeSpecialCharacters(String str){
        return str.replace(":", "").replace(",", "");
    }

    public synchronized void writeGraphToFile(Graph graph) {
        Configuration config = Configuration.getInstance();
        String outputFile = config.getValueFor("graph.output");
        String delim = config.getValueFor("format.delim");
        String newline = config.getValueFor("format.newLineDelim");

        List<List<String>> entries = new ArrayList<>();
        for (Point p : graph) {
            if (!graph.hasAdj(p))//Skip adding users who haven't done a retweet / been retweeted
                continue;
            List<String> entry = new ArrayList<>();
            entry.add(p.getName());

            List<Edge> retweets = graph.getAdj(p);
            retweets.sort(Collections.reverseOrder());

            for (Edge e : retweets)
                entry.add(e.toString());

            entries.add(entry);
        }
        FileEntryIO.writeDataBlocks(entries,
                delim.charAt(0),
                newline.charAt(0),
                outputFile
        );
    }

    public void writeGraphToGDFFile(Graph graph){
        Configuration config = Configuration.getInstance();
        String outputFile = config.getValueFor("graph/GDFOutput");

        List<String> lines = new ArrayList<>();
        lines.add("nodedef>name VARCHAR");
        for (Point p : graph){
            lines.add(p.getName());
        }
        lines.add("edgedef>node1 VARCHAR,node2 VARCHAR, att1 INTEGER");
        for (Point p : graph){
            for (Edge e : graph.getAdj(p)){
                String stanceNum;
                StancePoint source = (StancePoint) e.getSource();
                if (source.getStance().isPresent())
                    stanceNum = String.valueOf(source.getStance().get());
                else stanceNum = " ";
                lines.add(e.getSource() + "," + e.getDestination() + "," + stanceNum);
            }
        }
        FileEntryIO.writeLineByLine(lines,
                config.getValueFor("format.newLineDelim").charAt(0),
                config.getValueFor("graph.GDFOutput")
        );
    }

    public synchronized void populateRetweetGraphFromFile(String filename){
        graph.clear();
        try{
            FileEntryIO.streamLineByLine(filename, new readRetweets(false));
        }catch(IncorrectGraphFileException e){
            System.err.println("Error: Retweet data file not found");
            //populateFromGraphFile();
        }
    }

    public synchronized void populateRetweetedGraphFromFile(String filename){
        graph.clear();
        try{
            FileEntryIO.streamLineByLine(filename, new readRetweets(true));
        }catch(IncorrectGraphFileException e){
            populateFromGraphFile();
        }
    }


    public synchronized void populateUserToHashtagGraph(String filename){
        graph.clear();
        try{
            FileEntryIO.streamLineByLine(filename, new readHashtags(false));
        }catch(IncorrectGraphFileException e){e.printStackTrace();}
    }

    public synchronized void populateHashtagToUserGraph(String filename){
        graph.clear();
        try{
            FileEntryIO.streamLineByLine(filename, new readHashtags(true));
        }catch(IncorrectGraphFileException e){e.printStackTrace();}
    }


    public readHashtags getReadHashtags(boolean reverse){ return new readHashtags(reverse); }

    private class readHashtags implements Callable{
        boolean reverse = false;
        private readHashtags(boolean reverse){this.reverse = reverse;}

        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.countTokens() >= 3) {
                tokens.nextToken();
                String user1 = tokens.nextToken();
                while (tokens.hasMoreTokens()) {
                    String token = tokens.nextToken();
                    token = token.replaceAll("[^a-zA-Z0-9#_]", "");
                    if (token.length() <= 1) continue;
                    if (token.charAt(0) == '#') {
                        if (!reverse)
                            graph.addArc(new StancePoint(user1), new StancePoint(token));
                        else
                            graph.addArc(new StancePoint(token), new StancePoint(user1));
                    }
                }
            }
        }
    }



    public synchronized void populateFromGraphFile(){
        graph.clear();
        try {
            Configuration config = Configuration.getInstance();
            assert config != null;
            FileEntryIO.streamLineByLine(config.getValueFor("graph.output"), new readRetweetsFromGraphFile());
        }catch(IncorrectGraphFileException e){
            e.printStackTrace();
        }
    }

    private class readRetweetsFromGraphFile implements Callable {
        private String currentTweeter = null;

        @Override
        public void call(Object o) throws IncorrectGraphFileException{

            //First line is the retweeter
            if(currentTweeter == null) {
              currentTweeter = (String)o;
              return;
            }

            //Second line are the retweetees
            StringTokenizer tokens = new StringTokenizer((String)o);
            while(tokens.hasMoreTokens()) {
              String s = tokens.nextToken();
              s = s.substring(0, s.length() - 1);
              String[] parsedString = s.split("\\(");
              String to = parsedString[0];
              int weight = Integer.parseInt(parsedString[1]);

              graph.addArc(currentTweeter, to, weight);
            }

            //Third line is a newline, so currentTweeter can be nullified
            if(((String)o).length() == 0) {
                currentTweeter = null;
            }
        }
    }
}
