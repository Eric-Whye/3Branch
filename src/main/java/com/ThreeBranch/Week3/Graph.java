package com.ThreeBranch.Week3;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

public class Graph {
  private Hashtable<Vertex, List<Arc>> adjacencyList = new Hashtable<>();
  
  public void addVertex(String s) {
    addVertex(new Vertex(s));
  }
  
  public void addVertex(Vertex v) {
    if(!hasVertex(v))
      adjacencyList.put(v, new ArrayList<Arc>());
  }
  
  public boolean hasVertex(String name) {
    return hasVertex(new Vertex(name));
  }
  
  public boolean hasVertex(Vertex v) {
    return adjacencyList.containsKey(v);
  }
  
  public Vertex getVertex(String s) {
    if(hasVertex(s))
      return new Vertex(s);
    addVertex(s);
    return new Vertex(s);
  }
  
  public List<Arc> getAdj(String s) throws IllegalArgumentException{
    return getAdj(new Vertex(s));
  }
  
  public List<Arc> getAdj(Vertex v) throws IllegalArgumentException{
    List<Arc> adj = adjacencyList.get(v);
    if(adj == null)
      throw new IllegalArgumentException("Vertex " + v.getName() + " does not exist");
    return adj;
  }
  
  public boolean edgeExists(String from, String to) {
    return edgeExists(new Vertex(from), new Vertex(to));
  }
  
  public boolean edgeExists(Vertex from, Vertex to) {
    try {
      for (Arc a : getAdj(from))
        if(a.getEndVertex() == to)
          return true;
      return false;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
  
  //Returns the arc if it exists, returns null if it doesn't
  public Arc getEdgeIfExists(String from, String to) {
    return getEdgeIfExists(new Vertex(from), new Vertex(to));
  }
  
  public Arc getEdgeIfExists(Vertex from, Vertex to) {
    try {
      for (Arc a : getAdj(from))
        if(a.getEndVertex() == to)
          return a;
      return null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  
  public void addArc(String from, String to) {
    Vertex fromV = getVertex(from);
    Vertex toV = getVertex(to);
    
    Arc a = getEdgeIfExists(fromV, toV);
    if(a == null) {
      getAdj(fromV).add(new Arc(fromV, toV));
    } else {
      a.incrementValue();
    }
  }
}
