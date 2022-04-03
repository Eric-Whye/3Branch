package com.ThreeBranch.Week3;

import com.ThreeBranch.*;

public class SubMain {
  public static void handle() {
    try {
      Configuration.initialise(Configuration.ConfigFilename);
      
      FileProcessor fp = new FileProcessor();
      Graph data = fp.getGraph();
      fp.writeGraphToFile(data);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}