package com.ThreeBranch.Graph;

import java.util.Objects;

public class Vertex implements Point{
    private final String name;

    public String getName() {return name;}

    protected Vertex(String name){
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Point))
            return false;
        Point p = (Point) o;
        return getName().equals(p.getName());
    }

    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString(){
        return name;
    }
}
