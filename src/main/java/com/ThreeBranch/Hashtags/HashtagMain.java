package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Twitter.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class HashtagMain {
    private static List<String> hashtags = new ArrayList<>();
    private static Graph lexicon = new HashtagLexicon(Configuration.getValueFor("hashtags.lexiconFile"));

    public static void run(String tweetsFilename){
        FileEntryIO.streamLineByLine(tweetsFilename, new collectHashtags());
        HashtagLexicon lexicon = new HashtagLexicon(Configuration.getValueFor("hashtags.lexiconFile"));

        HashtagSplitter splitter = new HashtagSplitter(hashtags);
        Graph splitTags = splitter.getSplitTags();
    }

    private static class collectHashtags implements Callable {

        @Override
        public void call(Object o) {
            StringTokenizer tokens = new StringTokenizer((String)o);
            while (tokens.hasMoreTokens()){
                String currToken = tokens.nextToken();
                if (currToken.charAt(0) == '#'){
                    hashtags.add(currToken);
                }
            }
        }
    }
}
