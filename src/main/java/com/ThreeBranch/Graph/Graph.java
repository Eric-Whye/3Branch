package com.ThreeBranch.Graph;

import com.ThreeBranch.Twitter.StancePoint;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Graph implements Iterable<Point>{
  private final Hashtable<Point, List<Edge>> adjacencyList = new Hashtable<>();


  public Optional<Point> getPointIfExists(String s) {
    getPointIfExists(new Vertex(s));
    return Optional.empty();
  }

  public Optional<Point> getPointIfExists(Point p){
    for (Map.Entry<Point, List<Edge>> entry: adjacencyList.entrySet()){
      if (p.equals(entry))
        return Optional.of(p);
    }
    return Optional.empty();
  }
  
  public boolean hasVertex(String name) {
    return hasVertex(new Vertex(name));
  }
  
  public boolean hasVertex(Point p) {
    return adjacencyList.containsKey(p);
  }
  
  public List<Edge> getAdj(Point p) throws IllegalArgumentException{
    List<Edge> adj = adjacencyList.get(p);
    if(adj == null)
      throw new IllegalArgumentException("Point " + p.getName() + " does not exist");
    return adj;
  }

  public Boolean hasAdj(Point p){
    return adjacencyList.get(p).size() != 0;
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
      for (Edge e : getAdj(from))
        if(e.getDestination().equals(to))
          return e;
      return null;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  public void addArc(Point from, List<Edge> edges){
    if (adjacencyList.containsKey(from)) {
      Edge uniqEdge = null;
      //Finds the unique edges in edges and adds them if they are unique
      for (Edge e : getAdj(from)) {
        for (Edge e2 : edges) {
          uniqEdge = e2;
          if (e.equals(e2)) {
            uniqEdge = null;
            break;
          }
        }

        if (uniqEdge != null)
          getAdj(from).add(uniqEdge);
      }
    }
  }


  public void addArc(String from, String to) {
    addArc(new Vertex(from), new Vertex(to));
  }

  public void addArc(Point from, Point to) {
    Arc arc = (Arc) getArcIfExists(from, to);
    if (arc != null) {
      arc.incrementValue();
    }
    else {//Arc doesn't already exist
      //Check if source Point exists
      if (adjacencyList.containsKey(from))
        getAdj(from).add(new Arc(from, to));
      else {//Totally new entry added
        List<Edge> edges = new ArrayList<>();
        edges.add(new Arc(from, to));
        adjacencyList.put(from, edges);
      }
    }
  }
  
  public void addArc(String from, String to, int weight) {
    addArc(new Vertex(from), new Vertex(to), weight);
  }
  
  public void addArc(Point from, Point to, int weight) {
    Arc arc = (Arc) getArcIfExists(from, to);
    if (arc != null) {//increase weight as arc exists
      arc.setWeight(weight);
    }
    else {//Arc doesn't exist
      //Check if source Point exists
      if (adjacencyList.containsKey(from))
        getAdj(from).add(new Arc(from, to, weight));

      else {//Totally new entry added
        List<Edge> edges = new ArrayList<>();
        edges.add(new Arc(from, to));
        adjacencyList.put(from, edges);
      }
    }
  }

  public void clear(){
    adjacencyList.clear();
  }
  
  private Iterator<Point> arbitraryAccess() {
    return adjacencyList.keySet().iterator();
  }

  @Override
  public Iterator<Point> iterator() {
    return arbitraryAccess();
  }

  public List<Point> getPointsList(){
    List<Point> list = new ArrayList<>();
    for (Point p : this){
      list.add(p);
    }
    return list;
  }

  public Point getRandomPoint() throws IllegalStateException{
    if(size() == 0)
      throw new IllegalStateException("Graph is empty");
    
    List<Point> keys = new ArrayList<Point>(adjacencyList.keySet());
    return keys.get(new Random().nextInt(keys.size()));
  }

  public int size(){return adjacencyList.size();}
  public boolean isEmpty(){return adjacencyList.isEmpty();}
}
