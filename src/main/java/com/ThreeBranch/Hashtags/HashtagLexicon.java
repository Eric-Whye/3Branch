package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Twitter.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class HashtagLexicon extends Graph{

    protected HashtagLexicon(String lexiconFilename){
        FileEntryIO.streamLineByLine(lexiconFilename, new readLexicon());
    }

    private class readLexicon implements Callable {
        @Override
        public void call(Object o) {
            String word;
            StringTokenizer tokens = new StringTokenizer(((String)o).replace(", ", ","));
            if (tokens.hasMoreTokens()) {
                tokens.nextToken();//Skipping numbered column
                word = tokens.nextToken();

                String label = tokens.nextToken().replace("[", "").replace("]", "");
                
                StringTokenizer labels = new StringTokenizer(label, ",");
                while(labels.hasMoreTokens())
                  addArc(new HashtagLabel(word), new HashtagLabel(labels.nextToken()));
            }
        }
    }
}
