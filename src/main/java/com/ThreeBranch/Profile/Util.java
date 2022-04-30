//-----------------------------------------------------------//
//--------All utility functions for the profile stuff--------//
//---NOTHING in here can be instantiated, it is all STATIC---//
//-----------------------------------------------------------//

package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;

public class Util {
  private Util() {
    throw new IllegalStateException("Cannot be instantiated");
  }
  
  public static <T extends Position> T genPosition(Graph adjGraph, Point source) {
    Position output = new T();
    
    for(Edge e : adjGraph.getAdj(source))
      output.add(e.getDestination());
    
    return output;
  }
}