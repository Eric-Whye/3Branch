package com.ThreeBranch.Graph;

import com.ThreeBranch.Callable;
import com.ThreeBranch.Configuration;
import com.ThreeBranch.FileEntryIO;

import java.util.*;
import java.io.*;

public class GraphFileProcessor {
    private final Graph graph;

    public GraphFileProcessor(Graph graph) {
        this.graph = graph;
        FileEntryIO.streamFromFile(Configuration.getGraphInputFile(), new readRetweets());
    }

    private class readRetweets implements Callable {
        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            if(tokens.countTokens() >= 3) {//If the Tweet has at least a text field
                tokens.nextToken();//Skip status Id
                String user1 = tokens.nextToken();//Save userhandle
                if (!tokens.nextToken().equals("RT")) return; //If not a retweet then discard
                String user2 = tokens.nextToken();//Save userhandle

                graph.addArc(user1, user2);
            }
        }
    }

    public void writeGraphToFile(Graph graph){
      String outputFile = Configuration.getGraphOutput();
      String delim = Character.toString(Configuration.getDelim());
      String newline = Character.toString(Configuration.getNewLineDelim());
      
      Writer writer = null;
      try {
        writer = new BufferedWriter(new FileWriter(outputFile, true));
      
        for(Point p : graph) {
          writer.write(p.getName());
          writer.write(newline);
          
          List<Edge> edges = graph.getAdj(p);
          edges.sort(Collections.reverseOrder());
          
          StringBuilder sb = new StringBuilder();
          for(Edge a : edges) {
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

    private class readGraphFromFile implements Callable {
        private String currentVertex = null;
        
        @Override
        public void call(Object o){
            if(currentVertex == null) {
              currentVertex = (String)o;
              return;
            }

            if(((String)o).length() == 0) {
              currentVertex = null;
            }
            
            StringTokenizer tokens = new StringTokenizer((String)o);
            
            while(tokens.hasMoreTokens()) {
              String s = tokens.nextToken();
              s = s.substring(0, s.length() - 1);
              String[] parsedString = s.split("\\(");
              String to = parsedString[0];
              int weight = Integer.parseInt(parsedString[1]);
            }
            
        }
    }
}
