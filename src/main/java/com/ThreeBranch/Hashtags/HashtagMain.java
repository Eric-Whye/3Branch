package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Twitter.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.String;

public class HashtagMain {
    private Configuration config = Configuration.getInstance(Configuration.ConfigFilename);
    private List<String> hashtags = new ArrayList<>();
    private Graph lexicon = new HashtagLexicon(config.getValueFor("hashtags.lexiconFile"));

    public Graph run(String tweetsFilename){
        FileEntryIO.streamLineByLine(tweetsFilename, new collectHashtags()); //Note: after this point "hashtags" is filled
        HashtagLexicon lexicon = new HashtagLexicon(config.getValueFor("hashtags.lexiconFile"));
        
        HashtagSplitter splitter = new HashtagSplitter(hashtags);
        Graph splitTags = splitter.getSplitTags();
        
        //Output needs to be a graph of hashtags & their labels
        Graph output = new Graph();
        for(Point hashtag : splitTags) {
          for(Edge termE : splitTags.getAdj(hashtag)) {
            String term = termE.getDestination().getName().toLowerCase();
            try {
            for(Edge label : lexicon.getAdj(term))
              output.addArc(hashtag, label.getDestination(), label.getWeight());
            } catch(Exception e) {}
          }
        }
        
        return output;
    }

    private class collectHashtags implements Callable {

        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.countTokens() >= 3) {
                tokens.nextToken();
                while (tokens.hasMoreTokens()) {
                    String token = tokens.nextToken();
                    token = token.replaceAll("[^a-zA-Z0-9#_]", "");
                    if (token.length() <= 1) continue;
                    if (token.charAt(0) == '#') {
                        hashtags.add(token);
                    }
                }
            }
        }
    }
}
