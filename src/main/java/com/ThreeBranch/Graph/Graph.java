package com.ThreeBranch.Graph;

import com.ThreeBranch.Twitter.StancePoint;

import java.nio.charset.StandardCharsets;
import java.util.*;

//WE NEED TO GET RID OF THIS ASAP
import java.lang.reflect.Constructor;

public class Graph implements Iterable<Point>{
  private final Hashtable<Point, List<Edge>> adjacencyList = new Hashtable<>();
  private final Hashtable<Point, Point> keysTable = new Hashtable<>();


  public Optional<Point> getPointIfExists(String s) {
    return getPointIfExists(new Vertex(s));
  }

  public Optional<Point> getPointIfExists(Point p){
    /*for (Point q: adjacencyList.keySet()){
      if (p.getName().trim().equals(q.getName().trim())){
        System.out.println(q.getName());
      }
    }*/
    if (adjacencyList.containsKey(p)){
      return Optional.of(keysTable.get(p));
    }
    else return Optional.empty();
    /*for (Map.Entry<Point, List<Edge>> entry: adjacencyList.entrySet()){
      if (p.equals(entry.getKey()))
        return Optional.of(entry.getKey());
    }
    return Optional.empty();*/
  }

  //This is BAD, we need to make graph generic ASAP
  public Optional<Point> getPointIfExists(String s, Constructor c) {
    Object o = null;
    try {
      o = c.newInstance(s);
    } catch (Exception e) {
      throw new IllegalArgumentException("Improper Constructor");
    }
    if(!(o instanceof Point))
      throw new IllegalArgumentException("Improper Constructor");

    Point target = (Point) o;
    List<Edge> adj = adjacencyList.get(target);
    if(adj != null)
      return Optional.of(adj.get(0).getSource());
    return Optional.empty();
  }

  public void addPoint(Point p) {
    if(!hasVertex(p)) {
      ArrayList<Edge> adjacencies = new ArrayList<Edge>();
      adjacencies.add(new Arc(p, p));
      keysTable.put(p, p);
      adjacencyList.put(p, adjacencies);
    }
  }

  public boolean hasVertex(String name) {
    return hasVertex(new Vertex(name));
  }
  
  public boolean hasVertex(Point p) {
    return adjacencyList.containsKey(p);
  }

  public List<Edge> getAdj(String s){
    return null;
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
        getAdj(from).add(new Arc(getPointIfExists(from).get(), to));

      else {//Totally new entry added
        List<Edge> edges = new ArrayList<>();
        edges.add(new Arc(from, to));
        adjacencyList.put(from, edges);
        keysTable.put(from, from);
      }
    }
  }
  
  public void addArc(String from, String to, int weight) {
    addArc(new Vertex(from), new Vertex(to), weight);
  }
  
  public void addArc(Point from, Point to, int weight) {
    Arc arc = (Arc) getArcIfExists(from, to);
    if (arc != null) {//increase weight because arc exists
      arc.setWeight(weight);
    }
    else {//Arc doesn't exist
      //Check if source Point exists
      if (adjacencyList.containsKey(from))
        getAdj(from).add(new Arc(getPointIfExists(from).get(), to, weight));

      else {//Totally new entry added
        List<Edge> edges = new ArrayList<>();
        edges.add(new Arc(from, to));
        adjacencyList.put(from, edges);
        keysTable.put(from, from);
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

  public Graph clone(){
    Graph clone = new Graph();
    for (Point p : this){
      for (Edge e : getAdj(p))
        clone.addArc(p , e.getDestination());
    }
    return clone;
  }
  public int size(){return adjacencyList.size();}
  public boolean isEmpty(){return adjacencyList.isEmpty();}

  public String toString() {
    System.err.println("WARNING: THE toString METHOD IN Graph IS FOR DEBUG PURPOSES ONLY");

    StringBuilder sb = new StringBuilder();
    for(Point p : adjacencyList.keySet()) {
      sb.append(p.getName());
      sb.append("\n");
      for(Edge e : adjacencyList.get(p)) {
        sb.append("\t");
        sb.append(e.getSource());
        sb.append(" -> ");
        sb.append(e.getWeight());
        sb.append(" -> ");
        sb.append(e.getDestination());
        sb.append("\n");
      }
    }

    return sb.toString();
  }
}
