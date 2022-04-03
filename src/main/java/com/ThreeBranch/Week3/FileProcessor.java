package com.ThreeBranch.Week3;

import com.ThreeBranch.Callable;
import com.ThreeBranch.Configuration;
import com.ThreeBranch.FileEntryIO;

import java.util.*;
import java.io.*;

public class FileProcessor {
    Graph graph = new Graph();
  
    //There is ABSOLUTELY a better way to do this, but idk what it is, so here we are
    public Graph getGraph() {
      return graph;
    }
  
    public FileProcessor() {
        FileEntryIO.streamFromFile(Configuration.getGraphInputFile(), new readRetweets());
    }

    private class readRetweets implements Callable {
        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            if(tokens.countTokens() >= 4) {
              tokens.nextToken();//Skip status Id
              String user1 = tokens.nextToken();//Save userhandle
              if (!tokens.nextToken().equals("RT")) return; //If not a retweet then discard

              graph.addArc(user1, tokens.nextToken());
            }
        }
    }

    public void writeGraphToFile(Graph graph){
      String outputFile = Configuration.getGraphOutput();
      String delim = Character.toString(Configuration.getDelim());
      String newline = Character.toString(Configuration.getNewLineDelim());
      
      Writer writer;
      try {
        writer = new BufferedWriter(new FileWriter(outputFile, true));
      
        for(Vertex v : graph) {
          writer.write(v.getName());
          writer.write(newline);
          
          List<Arc> retweets = graph.getAdj(v);
          Collections.sort(retweets, Collections.reverseOrder());
          
          StringBuilder sb = new StringBuilder();
          for(Arc a : retweets) {
            sb.append(a.endName());
            sb.append("(");
            sb.append(a.getValue());
            sb.append(")");
            sb.append(delim);
          }
          
          //Fence post :(
          if (sb.length() > 0)
            sb.setLength(sb.length() - delim.length());
          
          
          sb.append(newline);
          sb.append(newline);
          writer.write(sb.toString());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private class readGraphFromFile implements Callable {
        @Override
        public void call(Object o){
            StringTokenizer tokens = new StringTokenizer((String)o);
        }
    }
}
