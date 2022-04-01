package com.ThreeBranch.Week3;

import com.ThreeBranch.Callable;
import com.ThreeBranch.Configuration;
import com.ThreeBranch.FileEntryIO;

import java.util.Hashtable;


public class FileProcessor {

    private Hashtable<String, Integer> table = new Hashtable<>();



    public FileProcessor() {
        FileEntryIO.streamFromFile(Configuration.getOutputFile(), new readUserHandles());
    }






    private static class readUserHandles implements Callable {
        @Override
        public void call(String line) {

        }
    }
}
