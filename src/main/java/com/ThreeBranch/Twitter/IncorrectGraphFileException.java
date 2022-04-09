package com.ThreeBranch.Twitter;

public class IncorrectGraphFileException extends RuntimeException{
    public IncorrectGraphFileException(String message){
        super(message);
    }
    public IncorrectGraphFileException(){
        super();
    }
}
