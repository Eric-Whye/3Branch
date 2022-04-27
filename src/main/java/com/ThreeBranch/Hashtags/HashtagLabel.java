package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Graph.Vertex;

import java.util.List;

public class HashtagLabel extends Vertex {
    //Hashtag arguments will be seen as the sum of their assoc labels, which are not accessible from here.
    //private String argument;

    protected HashtagLabel(String name){
        super(name);
    }

    /*HashtagLabel(List<String> splitWords){
        StringBuilder hashtag = new StringBuilder();
        for (String word : splitWords)
            hashtag.append(word);
        this.hashtag = hashtag.toString();
        this.splitWords = splitWords;

        calculateArgument();
    }*/

    /*private void calculateArgument() {

    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }*/
}
