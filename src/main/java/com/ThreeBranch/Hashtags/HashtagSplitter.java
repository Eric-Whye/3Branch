package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Callable;
import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Twitter.Configuration;

import java.util.*;

public abstract class HashtagSplitter {
    private List<String> lowercaseHashtags = new ArrayList<>();
    private List<HashtagLabel> labelList = new ArrayList<>();

    public void initialise(){
        //FileEntryIO.streamLineByLine(lexiconFile, new readLexicon());
    }

    public void splitHashtags(List<String> hashtags){
        boolean isCamel = false;

        for (String hashtag : hashtags) {
            //Check to see if the hashtag contains *any* uppercase characters
            for (int i = 0; i < hashtag.length(); i++) {
                if (Character.isUpperCase(hashtag.charAt(i))) {
                    isCamel = true;
                    break;
                }
            }

            if (isCamel){
                StringBuilder word = new StringBuilder();
                word.append(hashtag.charAt(0));
                List<String> splitWords = new ArrayList<>();

                for (int i = 1; i < hashtag.length(); i++){
                    char currentLetter = hashtag.charAt(i);

                    if (Character.isUpperCase(hashtag.charAt(i))) {
                        splitWords.add(word.toString());
                        word.delete(0, word.length()-1);
                        word.append(currentLetter);
                    }
                    else
                        word.append(currentLetter);
                }
                HashtagLabel label = new HashtagLabel(splitWords);
                labelList.add(label);
            } else {
                lowercaseHashtags.add(hashtag);
            }

            isCamel = false;
        }

    }

}
