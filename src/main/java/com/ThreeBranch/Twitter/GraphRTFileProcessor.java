package com.ThreeBranch.Twitter;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;

import java.util.*;
import java.io.*;

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
        public void call(Object o){
            StringTokenizer tokens = new StringTokenizer((String)o);
            if(tokens.countTokens() >= 3) {//If the Tweet has at least a text field
                tokens.nextToken();
                /*try {//If the status id is not a number, then the file being read is the wrong file.
                    Long.parseLong(tokens.nextToken());
                }catch(NumberFormatException ignored){
                    System.out.println("something");
                    throw new IncorrectGraphFileException();
                }*/
                String user1 = tokens.nextToken();//Save userhandle
                if (!tokens.nextToken().equals("RT")) return; //If not a retweet then discard
                String user2 = tokens.nextToken();//Save userhandle
                if (!reverse)
                    graph.addArc(user1, removeSpecialCharacters(user2));
                else graph.addArc(removeSpecialCharacters(user2), user1);
            }
        }
    }

    //Removes ':'
    private String removeSpecialCharacters(String str){
        return str.replace(":", "");
    }

    public synchronized void writeGraphToFile(Graph graph){
      String outputFile = Configuration.getValueFor("graph.output");
      String delim = Configuration.getValueFor("format.delim");
      String newline = Configuration.getValueFor("format.newLineDelim");
      
      Writer writer = null;
      try {
        writer = new BufferedWriter(new FileWriter(outputFile));
      
        for(Point p : graph) {
          if (!graph.hasAdj(p))//Skip adding users who haven't done a retweet / been retweeted
              continue;
          writer.write(p.getName());
          writer.write(newline);
          
          List<Edge> retweets = graph.getAdj(p);
          retweets.sort(Collections.reverseOrder());
          
          StringBuilder sb = new StringBuilder();
          for(Edge a : retweets) {
            sb.append(a.toString());
            sb.append(delim);
          }
          
          //Fence post :(
          if (sb.length() > 0)
            sb.setLength(sb.length() - delim.length());
          
          
          sb.append(newline);
          sb.append(newline);
          writer.write(sb.toString());
        }
      } catch (IOException e) {e.printStackTrace();
      }finally{
          try {
              assert writer != null;
              writer.close();
          }catch(IOException e){e.printStackTrace();}
      }
    }

    public synchronized void populateRetweetedGraphFromFile(String filename){
        graph.clear();
        try{
            FileEntryIO.streamFromFile(filename, new readRetweets(true));
        }catch(IncorrectGraphFileException e){
            populateFromGraphFile();
        }
    }

    public synchronized void populateRetweetGraphFromFile(String filename){
        graph.clear();
        try{
            FileEntryIO.streamFromFile(filename, new readRetweets(false));
        }catch(IncorrectGraphFileException e){
            //populateFromGraphFile();
        }
    }

    public synchronized void populateFromGraphFile(){
        graph.clear();
        try {
            FileEntryIO.streamFromFile(Configuration.getValueFor("graph.output"), new readRetweetsFromGraphFile());
        }catch(IncorrectGraphFileException e){
            e.printStackTrace();
        }
    }

    private class readRetweetsFromGraphFile implements Callable {
        private String currentTweeter = null;

        @Override
        public void call(Object o){

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