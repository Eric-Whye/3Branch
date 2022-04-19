package com.ThreeBranch.Twitter;

import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Graph.Vertex;

import java.util.Optional;

public class StancePoint extends Vertex {
    private final int MAX_STANCE = Integer.parseInt(Configuration.getValueFor("stance.maxStance"));
    private final int MIN_STANCE = Integer.parseInt(Configuration.getValueFor("stance.minStance"));
  
    private int stance = MAX_STANCE + 1; //Anything outside of the MIN to MAX range will be considered null

    public StancePoint(String name){
        super(name);
    }
    protected StancePoint(String name, int stance) {
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
