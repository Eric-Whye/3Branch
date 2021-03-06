package com.ThreeBranch.Twitter;

import java.util.concurrent.locks.*;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.FileEntryIO;

public class RTProcessorConcurrent extends GraphRTFileProcessor {
  private final Graph graph;

  public RTProcessorConcurrent(Graph g) {
    super(g);
    this.graph = g;
  }
  
  public synchronized Lock populateUserToHashtagGraphConcurrent(String filename){
    graph.clear();
    Lock lock = null;
    try{
      lock = (new FileEntryIO()).streamLineByLineConcurrent(filename, getReadHashtags(false));
    }catch(Exception e){
      e.printStackTrace();
    }

    return lock;
  } 
  
  public synchronized Lock populateRetweetGraphFromFileConcurrent(String filename) {
    graph.clear();
    Lock lock = null;
    try {
      lock = (new FileEntryIO()).streamLineByLineConcurrent(filename, new readRetweets(false));
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return lock;
  }
}