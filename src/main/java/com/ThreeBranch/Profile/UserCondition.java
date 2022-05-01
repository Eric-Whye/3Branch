package com.ThreeBranch.Profile;

import com.ThreeBranch.Graph.*;
import com.ThreeBranch.Id;

import java.util.StringTokenizer;
import java.util.Scanner; //This is JUST used for debugging

public class UserCondition implements Condition {
  private UserPosition target = new UserPosition(Id.get());
  
  public UserCondition(String cond) {
    StringTokenizer hashLists = new StringTokenizer(cond, ";", false);
    while(hashLists.hasMoreTokens()) {
      String hashList = hashLists.nextToken().trim();
      
      HashPosition hp = new HashPosition(Id.get());
      
      //Handle the lean
      Tri positiveLean = Tri.NONE;
      char sign = hashList.charAt(0);
      switch(sign) {
        case '+':
          positiveLean = Tri.TRUE;
          hashList = hashList.substring(1, hashList.length());
          break;
        case '-':
          positiveLean = Tri.FALSE;
          hashList = hashList.substring(1, hashList.length());
          break;
        default:
      }
      hp.setLean(positiveLean);
      
      //Add all of the labels
      StringTokenizer st = new StringTokenizer(hashList, ",", false);
      while(st.hasMoreTokens())
        hp.add(st.nextToken());
      
      //And now that the HashPosition is set up we add it to the UserPosition, giving us our final target for matches
      target.add(hp);
    }
  }
  
  public Tri matches(Point p) {
    if(!(p instanceof UserPosition))
      return Tri.NONE;
    
    UserPosition up = (UserPosition) p;
    
    return up.contains(target) ? Tri.TRUE : Tri.FALSE;
  }
  
  //This is JUST for debugging
  public String toString() {
    String output = "UserCondition: \n";
    
    Scanner scn = new Scanner(target.toString());
    while(scn.hasNextLine()) {
      output += "\t";
      output += scn.nextLine();
      output += "\n";
    }
    scn.close();
    
    return output;
  }
}