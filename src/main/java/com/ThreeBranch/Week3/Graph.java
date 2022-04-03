package com.ThreeBranch.Week3;

import java.util.*;

public class Graph implements Iterable<Vertex>{
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
        if(a.getEndVertex().equals(to))
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
        if(a.getEndVertex().equals(to))
          return a;
      return null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  
  public void addArc(String from, String to) {
    Vertex fromV = getVertex(from);
    Vertex toV = getVertex(to);
    
    addArc(fromV, toV, 1);
  }
  
  public void addArc(Vertex from, Vertex to) {
    addArc(from, to, 1);
  }
  
  public void addArc(String from, String to, int weight) {
    Vertex fromV = getVertex(from);
    Vertex toV = getVertex(to);
    
    addArc(fromV, toV, weight);
  }
  
  public void addArc(Vertex from, Vertex to, int weight) {
    Arc a = getEdgeIfExists(from, to);
    if(a == null) {
      getAdj(from).add(new Arc(from, to, weight));
    } else {
      a.incrementValue();
    }
  }
  
  public Iterator<Vertex> arbitraryAccess() {
    return adjacencyList.keySet().iterator();
  }
  
  public Iterator<Vertex> iterator() {
    return arbitraryAccess();
  }
}
