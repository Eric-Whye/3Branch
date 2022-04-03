package com.ThreeBranch.Week3;

import java.util.Objects;

public class Arc implements Edge, Comparable<Edge>{
    private final Point source;
    private final Point destination;
    private int weight;

    public Point getSource() {return source;}
    public Point getDestination() {return destination;}
    
    protected Arc(Point source, Point destination){
      this(source, destination, 1);
    }

    protected Arc(Point source, Point destination, int weight) {
      this.source = source;
      this.destination = destination;
      this.weight = weight;
    }

    public void incrementValue(){
        this.weight++;
    }

    public int getWeight() {
      return weight;
    }
    
    public String endName() {
      return destination.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc arc = (Arc) o;
        return weight == arc.weight && source.equals(arc.source) && destination.equals(arc.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }
    
    @Override
    public int compareTo(Edge a) {
      return this.weight - a.getWeight();
    }

    @Override
    public String toString(){
        return destination + "(" + weight + ")";
    }
}
