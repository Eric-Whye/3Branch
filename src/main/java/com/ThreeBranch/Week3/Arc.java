package com.ThreeBranch.Week3;

import java.util.Objects;

public class Arc{
    private final Vertex startVertex;
    private final Vertex endVertex;
    private int value = 1;

    public Vertex getStartVertex() {return startVertex;}
    public Vertex getEndVertex() {return endVertex;}
    
    protected Arc(Vertex startVertex, Vertex endVertex){
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public void incrementValue(){
        this.value++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc arc = (Arc) o;
        return value == arc.value && startVertex.equals(arc.startVertex) && endVertex.equals(arc.endVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startVertex, endVertex);
    }
}
