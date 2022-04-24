package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Graph.Vertex;
import com.ThreeBranch.Twitter.Configuration;

import java.util.*;

public class HashtagSplitter {
    private List<String> hashtags;
    private Graph splitTags = new Graph();
    private Graph lowerTags = new Graph();
    private List<Point> lowercaseHashtags = new ArrayList<>();
    private List<Point> camelHashtags = new ArrayList<>();
    private HashSet<Point> lowerSet = new HashSet<>();

    protected HashtagSplitter(List<String> hashtags){
        this.hashtags = hashtags;
        splitHashtags();
    }

    private void splitHashtags(){
        for (String hashtag : hashtags) {
            boolean isCamel = false;

            for (int i = 0; i < hashtag.length(); i++) {
                if (Character.isUpperCase(hashtag.charAt(i))) {
                    isCamel = true;
                    break;
                }
            }

            //If the hashtag is camelCased then split it up according to the camel casing, then add it to camel hashtags
            //Otherwise add it to lowercaseHashtags
            if (isCamel){
                StringBuilder word = new StringBuilder();
                List<Point> splitWords = new ArrayList<>();

                word.append(hashtag.charAt(0));
                for (int i = 1; i < hashtag.length(); i++){
                    char currentLetter = hashtag.charAt(i);

                    //If letter is uppercase, add the camelled word to splitWords, then add the uppercase letter to start the new word
                    if (Character.isUpperCase(hashtag.charAt(i))) {
                        splitWords.add(new HashtagLabel(word.toString()));
                        word.delete(0, word.length()-1);
                        word.append(currentLetter);
                    }
                    else
                        word.append(currentLetter);
                }
                //Add the current label with an arc for each camelled word.
                for (Point p : splitWords)
                    splitTags.addArc(new HashtagLabel(hashtag), p);
            }
            else {
                lowerSet.add(new HashtagLabel(hashtag));
            }

        }
    }

    private void matchHashtags(){
        for (Point p : camelHashtags){
            if (lowerSet.contains(new HashtagLabel(p.getName().toLowerCase()))){

            }
        }
    }

}
