package com.ThreeBranch.Week3;

public class SubMain {
  public static void handle() {
    FileProcessor fp = new FileProcessor();
    Graph data = fp.getGraph();
    fp.writeGraphToFile(data);
  }
}