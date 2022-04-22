package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Twitter.Configuration;

import java.util.*;

public abstract class HashtagSplitter {
    String lexiconFile = Configuration.getValueFor("hashtags.lexiconFile");
    List<String> hashtagsList;
    HashMap<String, HashtagLabel> lexicon = new HashMap<>();

    public void initialise(){
        FileEntryIO.streamLineByLine(lexiconFile, new readLexicon());
    }

    public List<String> split(String hashtag){
        
        for (int i = 0 ; i < hashtag.length(); i++){
            switch(hashtag.charAt(i)){

            }
        }
    }


    private class readLexicon implements Callable {

        @Override
        public void call(Object o) {
            String word;
            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.hasMoreTokens())
                word = tokens.nextToken();
            else return;

            String label = ((String)o).substring(word.length()+1);
            HashtagLabel hLabel = new HashtagLabel(label);
            lexicon.put(word, hLabel);
        }
    }
}
