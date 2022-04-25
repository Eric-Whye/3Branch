package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Graph.*;

import java.util.Hashtable;

public class HashtagSummarizer {
  public static void rawPrintGraph(Graph g) {
    for(Point p : g) {
      System.out.println(p.getName());
      for(Edge e : g.getAdj(p))
        System.out.println("\t" + e.getDestination().getName() + " * " + e.getWeight());
      System.out.println("");
    }
  }
  
  public static void labelCount(Graph g) {
    Hashtable labels = new Hashtable<String, Integer>();
    
    for(Point p : g) {
      Tri accepting = Tri.NONE;
      String desc = "";
      
      for(Edge e : g.getAdj(p)) {
        String label = e.getDestination().getName().toLowerCase();
        
        //Accepting / rejecting stuff
        switch(label) {
          case "rejecting":
            accepting = Tri.FALSE;
            break;
            
          case "accepting":
            accepting = Tri.TRUE;
            break;
            
          case "negation":
            if(accepting == Tri.TRUE) {
              accepting = Tri.FALSE;
            } else if (accepting == Tri.FALSE) {
              accepting = Tri.TRUE;
            } else {
              //We should prob do something here? But idk what
            }
            break;
            
          default:
            desc += label;
            desc += ", ";
        }
      }
      
      //More accepting / rejecting stuff
      switch(accepting) {
        case TRUE:
          desc = "Pro: " + desc;
          break;
          
        case FALSE:
          desc = "Anti: " + desc;
          break;
          
        default:
      }
      
      if(labels.containsKey(desc)) {
        int count = (int)labels.get(desc);
        count++;
        labels.replace(desc, count);
      } else {
        labels.put(desc, 1);
      }
    }
    
    for(Object desc : labels.keySet())
      if(((String)desc).length() != 0) {
      System.out.println(desc + "* " + labels.get(desc));
      } else {
        System.out.println("No Case * " + labels.get(desc));
      }
  }
  
  private enum Tri {
    TRUE, FALSE, NONE
  };
}
