package com.ThreeBranch.Twitter;

import java.util.concurrent.locks.*;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.FileEntryIO;

public class RTProcessorConcurrent extends GraphRTFileProcessor {
  public RTProcessorConcurrent(Graph g) {
    super(g);
  }
  
  public synchronized Lock populateUserToHashtagGraphConcerent(String filename){
    graph.clear();
    Lock lock = null;
    try{
      lock = (new FileEntryIO()).streamLineByLineConcurrent(filename, new   readHashtags(false));
    }catch(IncorrectGraphFileException e){
      e.printStackTrace();
    }

    return lock;
  } 
}