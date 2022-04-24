package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Graph.Vertex;

import java.util.List;

public class HashtagLabel extends Vertex {
    private String argument;

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

    private void calculateArgument() {

    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
