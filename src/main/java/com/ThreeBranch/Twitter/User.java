package com.ThreeBranch.Twitter;

import com.ThreeBranch.Graph.Vertex;

import java.util.Objects;
import java.util.Optional;

public class User extends Vertex {
    private final int MAX_STANCE = Integer.parseInt(Configuration.getValueFor("stance.maxStance"));
    private final int MIN_STANCE = Integer.parseInt(Configuration.getValueFor("stance.minStance"));
  
    private int stance = -9999; //Anything outside of the -1000 to 1000 range will be considered null

    public User(String name){
        super(name);
    }
    protected User(String name, int stance) {
        super(name);
        this.stance = stance;
    }

    public Optional<Integer> getStance() {
        if(stance > MAX_STANCE || stance < MIN_STANCE)
          return Optional.empty();
        return Optional.of(stance);
    }

    public void setStance(int stance) {
        this.stance = stance;
    }
}
