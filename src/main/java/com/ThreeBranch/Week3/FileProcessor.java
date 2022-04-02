package com.ThreeBranch.Week3;

import com.ThreeBranch.Callable;
import com.ThreeBranch.Configuration;
import com.ThreeBranch.FileEntryIO;

import java.util.*;


public class FileProcessor {


    public FileProcessor() {
        FileEntryIO.streamFromFile(Configuration.getGraphInputFile(), new readRetweets());
    }



    private void addArc(String user1, String user2){
        Arc arc = new Arc(new Vertex(user1), new Vertex(user2));
        /*if (retweets.containsKey(new Vertex(user1))){
            retweets.get(new Vertex(user1)).incrementValue();
        }*/
    }

    private class readRetweets implements Callable {
        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            while (tokens.hasMoreTokens()){
                tokens.nextToken();//Skip status Id
                String user1 = tokens.nextToken();//Save userhandle
                if (tokens.nextToken().equals("RT")) return; //If not a retweet then discard

                addArc(user1, tokens.nextToken());
            }
        }
    }

    public void writeGraphToFile(Graph graph){

    }

    private class readGraphFromFile implements Callable {
        @Override
        public void call(Object o){
            StringTokenizer tokens = new StringTokenizer((String)o);
        }
    }
}
