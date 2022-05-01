package com.ThreeBranch.Graph;

import java.util.Objects;

public interface Point {
    String getName();
    boolean equals(Object o);
    int hashCode();
    Point create(String s);
}
