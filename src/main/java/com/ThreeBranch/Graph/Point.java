package com.ThreeBranch.Graph;

import java.util.Objects;

public abstract class Point {
    abstract public String getName();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Point))
          return false;
        Point p = (Point) o;
        return getName().equals(p.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
