package com.ThreeBranch.Twitter;

import com.ThreeBranch.Graph.Vertex;

public class User extends Vertex {
    private int stance = 0;

    protected User(String name){
        super(name);
    }
    protected User(String name, int stance) {
        super(name);
        this.stance = stance;
    }

    public int getStance() {
        return stance;
    }

    public void setStance(int stance) {
        this.stance = stance;
    }
}
