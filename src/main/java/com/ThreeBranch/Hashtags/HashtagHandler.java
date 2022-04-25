//--------------------------------------------------------------//
//---Nothing in here should ever be necessary for an external---//
//-------user to make use of the hashtag processing stuff-------//
//instead this is just for the functions that the shell is going//
//---------------to call to handle user requests----------------//
//--------------------------------------------------------------//
package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.Graph.Graph;

public class HashtagHandler {
  public static Graph build(Graph graph) {
    graph.clear();
    System.out.println("Old graphs have been deleted");
    
    graph = (new HashtagMain()).run(Configuration.getValueFor("graph.tweetsInput"));
    
    System.out.println("Graph built");
    
    return graph;
  }
  
  //Input is assumed to be whatever buildHashtagGraph outputs
  public static void rawPrint(Graph graph) {
    HashtagSummarizer.rawPrintGraph(graph);
  }
  
  public static void labelCount(Graph g) {
    HashtagSummarizer.labelCount(g);
  }
}