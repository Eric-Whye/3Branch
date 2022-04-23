package com.ThreeBranch.Hashtags;

import java.util.List;

public class HashtagLabel implements SplitString{
    private String hashtag;
    private List<String> splitWords;
    private String argument;

    HashtagLabel(List<String> splitWords){
        StringBuilder hashtag = new StringBuilder();
        for (String word : splitWords)
            hashtag.append(word);
        this.hashtag = hashtag.toString();
        this.splitWords = splitWords;

        calculateArgument();
    }

    private void calculateArgument(){

    }

    public List<String> getSplitWords() {
        return splitWords;
    }
}
