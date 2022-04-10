package com.ThreeBranch.Twitter;

import com.ThreeBranch.Graph.Vertex;
import java.util.Optional;

public class User extends Vertex {
    public final int MAX_STANCE = 1000;
    public final int MIN_STANCE = -1000;
  
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
