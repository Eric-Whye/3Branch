package com.ThreeBranch;

import twitter4j.TwitterStreamFactory;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        Configuration.initialise();
        new File("VaxData").mkdirs();
        
        System.out.println("done");
    }
}
