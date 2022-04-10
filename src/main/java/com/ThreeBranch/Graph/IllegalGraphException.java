package com.ThreeBranch.Graph;

public class IllegalGraphException extends RuntimeException{
    public IllegalGraphException(String message){
        super(message);
    }
    public IllegalGraphException(){
        super();
    }
}
