package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HashtagMain {
    private List<String> hashtags = new ArrayList<>();
    private Graph lexicon = new HashtagLexicon(Configuration.getValueFor("hashtags.lexiconFile"));

    public void run(String tweetsFilename){
        FileEntryIO.streamLineByLine(tweetsFilename, new collectHashtags());
        HashtagLexicon lexicon = new HashtagLexicon(Configuration.getValueFor("hashtags.lexiconFile"));

        HashtagSplitter splitter = new HashtagSplitter(hashtags);
        Graph splitTags = splitter.getSplitTags();

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
