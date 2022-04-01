package com.ThreeBranch.Week3;

public class Arc implements Edge {
    //Normally we'd hold Vertex objects, but because Vertex equality is determined by their String name, and we don't want weird null pointers we just store the Vertex names
    private String source;
    private String destination;
    private int weight;
  
    public Arc(String source, String destination, int weight) {
      this.source = source;
      this.destination = destination;
      this.weight = weight;
    }
    
    public Arc(String source, String destination) {
      this(source, destination, 0);
    }
  
    public String toString() {
      return "@" + destination + "(" + weight + ")";
    }
    
    public String getSource() {
      return source;
    }
    
    public String getDestination() {
      return destination;
    }
    
    public int getWeight() {
      return weight;
    }
    
    public void inc() {
      weight++;
    }
}
