package com.ThreeBranch.Graph;

import java.util.*;

public class Graph implements Iterable<Point>{
  private final Hashtable<Point, List<Edge>> adjacencyList = new Hashtable<>();


  public void addVertex(String s) {
    addVertex(new Vertex(s));
  }
  
  public void addVertex(Point v) {
    if(!hasVertex(v))
      adjacencyList.put(v, new ArrayList<Edge>());
  }
  
  public boolean hasVertex(String name) {
    return hasVertex(new Vertex(name));
  }
  
  public boolean hasVertex(Point p) {
    return adjacencyList.containsKey(p);
  }
  
  public Point getVertex(String s) {
    if(hasVertex(s))
      return new Vertex(s);
    addVertex(s);
    return new Vertex(s);
  }
  
  public List<Edge> getAdj(String s) throws IllegalArgumentException{
    return getAdj(new Vertex(s));
  }
  
  public List<Edge> getAdj(Point p) throws IllegalArgumentException{
    List<Edge> adj = adjacencyList.get(p);
    if(adj == null)
      throw new IllegalArgumentException("Vertex " + p.getName() + " does not exist");
    return adj;
  }
  
  public boolean arcExists(String from, String to) {
    return arcExists(new Vertex(from), new Vertex(to));
  }
  
  public boolean arcExists(Point from, Point to) {
    try {
      for (Edge a : getAdj(from))
        if(a.getDestination().equals(to))
          return true;
      return false;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
  
  //Returns the arc if it exists, returns null if it doesn't
  public Edge getArcIfExists(String from, String to) {
    return getArcIfExists(new Vertex(from), new Vertex(to));
  }
  
  public Edge getArcIfExists(Point from, Point to) {
    try {
      for (Edge a : getAdj(from))
        if(a.getDestination().equals(to))
          return a;
      return null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  
  public void addArc(String from, String to) {
    Point fromV = getVertex(from);
    Point toV = getVertex(to);
    
    addArc(fromV, toV, 1);
  }
  
  public void addArc(Point from, Point to) {
    addArc(from, to, 1);
  }
  
  public void addArc(String from, String to, int weight) {
    Point fromV = getVertex(from);
    Point toV = getVertex(to);
    
    addArc(fromV, toV, weight);
  }
  
  public void addArc(Point from, Point to, int weight) {
    Arc a = (Arc) getArcIfExists(from, to);
    if(a == null) {
      getAdj(from).add(new Arc(from, to, weight));
    } else {
      a.incrementValue();
    }
  }
  
  private Iterator<Point> arbitraryAccess() {
    return adjacencyList.keySet().iterator();
  }

  @Override
  public Iterator<Point> iterator() {
    return arbitraryAccess();
  }
}
