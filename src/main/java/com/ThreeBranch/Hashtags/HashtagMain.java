package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Twitter.Configuration;
import com.ThreeBranch.LockedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.String;

import java.util.concurrent.locks.*;

public class HashtagMain{
    private Configuration config = Configuration.getInstance();
    private LockedObject<List<String>> hashtags = new LockedObject(new ArrayList<>(), new ReentrantLock());
    private Graph lexicon = new HashtagLexicon(config.getValueFor("hashtags.lexiconFile"));

    public Graph run(String tweetsFilename){
        List<String> hts = hashtags.get();
        hashtags.getLock().lock();

        FileEntryIO.streamLineByLine(tweetsFilename, new collectHashtags()); //Note: after this point "hashtags" is filled
        HashtagLexicon lexicon = new HashtagLexicon(config.getValueFor("hashtags.lexiconFile"));
        
        HashtagSplitter splitter = new HashtagSplitter(hts);
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
        
        hashtags.getLock().lock();

        return output;
    }

    public LockedObject<Graph> runConcurrent(String tweetsFilename) {
      Lock lock = new ReentrantLock();
      Graph output = new Graph();
      LockedObject<Graph> lo = new LockedObject(output, lock);

      Thread htw = new hashtagWorker(tweetsFilename, lo);
      htw.run();
      return lo;
    }

    private class hashtagWorker extends Thread {
      private Lock lock;
      private Graph output;
      private String filename;

      public hashtagWorker(String filename, LockedObject<Graph> lo) {
        this.filename = filename;
        this.lock = lo.getLock();
        this.output = lo.get();
      }

      public void run() {
        lock.lock();
        List<String> hts = hashtags.get();
        hashtags.getLock().lock();

        FileEntryIO.streamLineByLine(filename, new collectHashtags()); //Note: after this point "hashtags" is filled
        HashtagLexicon lexicon = new HashtagLexicon(config.getValueFor("hashtags.lexiconFile"));

        HashtagSplitter splitter = new HashtagSplitter(hts);
        Graph splitTags = splitter.getSplitTags();

        //Output needs to be a graph of hashtags & their labels
        for(Point hashtag : splitTags) {
          for(Edge termE : splitTags.getAdj(hashtag)) {
            String term = termE.getDestination().getName().toLowerCase();
            try {
            for(Edge label : lexicon.getAdj(term))
              output.addArc(hashtag, label.getDestination(), label.getWeight());
            } catch(Exception e) {}
          }
        }

        hashtags.getLock().unlock();
        lock.unlock();
      }
    }

    private class collectHashtags implements Callable {

        @Override
        public void call(Object o) {
            List<String> hts = hashtags.get();
            hashtags.getLock().lock();

            StringTokenizer tokens = new StringTokenizer((String)o);
            if (tokens.countTokens() >= 3) {
                tokens.nextToken();
                while (tokens.hasMoreTokens()) {
                    String token = tokens.nextToken();
                    token = token.replaceAll("[^a-zA-Z0-9#_]", "");
                    if (token.length() <= 1) continue;
                    if (token.charAt(0) == '#') {
                        hts.add(token);
                    }
                }
            }
          hashtags.getLock().unlock();
        }
    }
}
