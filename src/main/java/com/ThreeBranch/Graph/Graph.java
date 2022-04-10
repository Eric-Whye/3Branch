package com.ThreeBranch.Graph;

import java.util.*;

public class Graph implements Iterable<Point<String>>{
  private final Hashtable<Point<String>, List<Edge>> adjacencyList = new Hashtable<>();

  public void addVertex(String s) {
    addVertex(new Vertex<>(s));
  }
  
  public void addVertex(Point<String> p) {
    if(!hasVertex(p))
      adjacencyList.put(p, new ArrayList<Edge>());
  }
  
  public boolean hasVertex(String name) {
    return hasVertex(new Vertex<>(name));
  }
  
  public boolean hasVertex(Point<String> p) {
    return adjacencyList.containsKey(p);
  }
  
  public Point<String> getVertex(String s) {
    if(hasVertex(s))
      return new Vertex<>(s);
    addVertex(s);
    return new Vertex<>(s);
  }

  public List<Edge> removeVertex(String s){
    return adjacencyList.remove(new Vertex<>(s));
  }
  public List<Edge> removeVertex(Point<String> p){
    return adjacencyList.remove(p);
  }
  
  public List<Edge> getAdj(String s) throws IllegalArgumentException{
    return getAdj(new Vertex<>(s));
  }
  
  public List<Edge> getAdj(Point<String> p) throws IllegalArgumentException{
    List<Edge> adj = adjacencyList.get(p);
    if(adj == null)
      throw new IllegalArgumentException("Edge " + p.getName() + " does not exist");
    return adj;
  }

  public Boolean hasAdj(Point<String> p){
    return adjacencyList.get(p).size() != 0;
  }
  
  public boolean arcExists(String from, String to) {
    return arcExists(new Vertex<>(from), new Vertex<>(to));
  }
  
  public boolean arcExists(Point<String> from, Point<String> to) {
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
    return getArcIfExists(new Vertex<>(from), new Vertex<>(to));
  }
  
  public Edge getArcIfExists(Point<String> from, Point<String> to) {
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
    Point<String> fromV = getVertex(from);
    Point<String> toV = getVertex(to);
    
    addArc(fromV, toV, 1);
  }
  
  public void addArc(Point<String> from, Point<String> to) {
    addArc(from, to, 1);
  }
  
  public void addArc(String from, String to, int weight) {
    Point<String> fromP = getVertex(from);
    Point<String> toP = getVertex(to);
    
    addArc(fromP, toP, weight);
  }
  
  public void addArc(Point<String> from, Point<String> to, int weight) {
    Arc a = (Arc) getArcIfExists(from, to);
    if(a == null) {
      getAdj(from).add(new Arc(from, to, weight));
    } else {
      a.incrementValue();
    }
  }

  public void clear(){
    adjacencyList.clear();
  }
  
  private Iterator<Point<String>> arbitraryAccess() {
    return adjacencyList.keySet().iterator();
  }

  @Override
  public Iterator<Point<String>> iterator() {
    return arbitraryAccess();
  }

  public List<Point<String>> getPointsList(){
    List<Point<String>> list = new ArrayList<>();
    for (Point<String> p : this){
      list.add(p);
    }
    return list;
  }

  public int size(){return adjacencyList.size();}
  public boolean isEmpty(){return adjacencyList.isEmpty();}
}
