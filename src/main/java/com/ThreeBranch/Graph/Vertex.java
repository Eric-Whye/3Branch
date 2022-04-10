package com.ThreeBranch.Graph;

import java.util.Objects;

public class Vertex<T> implements Point<T>{
    private final T name;

    public T getName() {return name;}

    protected Vertex(T name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex<T> vertex = (Vertex<T>) o;
        return name.equals(vertex.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString(){
        return (String)name;
    }
}
