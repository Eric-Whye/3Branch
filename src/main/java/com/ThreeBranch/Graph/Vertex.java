package com.ThreeBranch.Graph;

import java.util.Objects;

public class Vertex implements Point{
    private final String name;

    public String getName() {return name;}

    protected Vertex(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return name.equals(vertex.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString(){
        return name;
    }
}
