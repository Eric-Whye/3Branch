package com.ThreeBranch;

import java.lang.StringBuilder;
import java.util.Iterator;
import java.util.List;

public class TweetFormatter implements Formatter {
  //List<List<String>> list, char delim, char newLineDelim, File outputFile
  
  private char delim;
  private char newLineDelim;
  
  public TweetFormatter(char delim, char newLineDelim) {
    this.delim = delim;
    this.newLineDelim = newLineDelim;
  }
  
  public String format(List<String> data) {
    StringBuilder output = new StringBuilder();
    Iterator<String> iter = data.iterator();
    
    if(iter.hasNext())
      output.append(iter.next());
    
    while(iter.hasNext()) {
      output.append(delim);
      output.append(iter.next());
    }
    
    output.append(newLineDelim);
    return output.toString();
  }
}