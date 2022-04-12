package com.ThreeBranch.Graph;

import java.util.Objects;

public class Vertex extends Point{
    private final String name;

    public String getName() {return name;}

    protected Vertex(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
