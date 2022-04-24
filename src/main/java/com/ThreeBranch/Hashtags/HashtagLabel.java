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
    
    //We need this stuff so we can use this as a point in a graph
    public String getName() {
      return hashtag;
    }
    
    public boolean equals(Object o) {
      if (this == o)
        return true;
      
      if (o == null)
        return false;
      
      if(!(o instanceof HashtagLabel))
        return false;
      
      HashtagLabel hl = (HashtagLabel) o;
      
      return hl.getName().equals(this.getName());
    }
    
    public int hashCode() {
      return Objects.hash(this.getName());
    }
}
