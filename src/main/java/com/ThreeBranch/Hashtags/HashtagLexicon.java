package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.Twitter.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public abstract class HashtagLexicon {
    private final String lexiconFile = Configuration.getValueFor("hashtags.lexiconFile");
    private HashMap<String, List<String>> lexicon = new HashMap<>();


/*
    private class readLexicon implements Callable {

        @Override
        public void call(Object o) {
            String word;
            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.hasMoreTokens())
                word = tokens.nextToken();
            else return;

            String label = ((String)o).substring(word.length()+1);
            lexicon.put(word, hLabel);
        }
    }*/
}
