package com.ThreeBranch.Twitter;

import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class StanceProcessing {
    private final Graph graph;

    public StanceProcessing(Graph graph){
        this.graph = graph;
    }

    /**
     *
     */
    public void initialiseStances(){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader("Week3/Influential Users.txt"));
            while (reader.ready()){
                StringTokenizer tokens = new StringTokenizer(reader.readLine());
                if (tokens.countTokens() >= 2){
                    String userName = tokens.nextToken();
                    Optional<Point> userOption = graph.getPointIfExists(userName);

                    if(userOption.isPresent()) {
                      Point p = userOption.get();
                      if(p instanceof User) {
                        User user = (User) p;
                        switch(tokens.nextToken()){
                            case "mid":
                                user.setStance(Integer.parseInt(Configuration.getValueFor("stance.midStance")));
                                break;
                            case "pro":
                                user.setStance(Integer.parseInt(Configuration.getValueFor("stance.maxStance")));
                                break;
                            case "anti":
                                user.setStance(Integer.parseInt(Configuration.getValueFor("stance.minStance")));
                                break;
                        }
                      } else {
                        System.err.println(userName + " is not a user object");
                      }
                    } else {
                      System.err.println(userName + " is not present in graph");
                    }
                }
            }
        } catch(IOException e){ e.printStackTrace(); }
        finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    public boolean calcStances() throws ClassCastException{
      boolean change = false;
      
      for(Point p : graph) {
        if(!(p instanceof User)) 
          throw new ClassCastException("This function only works on User objects");
        
        User u = (User) p;
        
        int neighbors = 0;
        int stanceSum = 0;
        
        for(Edge e : graph.getAdj(u)) {
          Point p2 = e.getDestination();
          if(!(p2 instanceof User))
              throw new ClassCastException("This function only works on User objects");
          User u2 = (User) p2;
          
          Optional<Integer> stance = u2.getStance();
          if (stance.isPresent()) {
            neighbors++;
            stanceSum += stance.get();
          }
        }
        
        if(neighbors != 0) {
          int newStance = stanceSum / neighbors;
          
          Optional<Integer> stance = u.getStance();
          if((stance.isPresent() && stance.get() != newStance) || !stance.isPresent()) {
            change = true;
            u.setStance(newStance);
          }
        }

        //if(neighbors != 0)
        //System.out.println("Worked on " + p.getName() + " neighbours = " + neighbors + " weight = " + u.getStance());
      }
      
      return change;
    }

    public void writeStances(Graph graph){
        List<List<String>> list = new ArrayList<>();
        for (Point user : graph){
            try {
                List<String> entry = new ArrayList<>();
                entry.add(user.getName());
                entry.add(((User) user).getStance().toString());
                list.add(entry);
            }catch(ClassCastException e){e.printStackTrace();}
        }
        FileEntryIO.writeToFile(list,
                Configuration.getValueFor("format.delim").charAt(0),
                Configuration.getValueFor("format.newLineDelim").charAt(0),
                Configuration.getValueFor("stance.output"));
    }

    public String stanceCoverage(){
        StringBuilder str = new StringBuilder();

        int sumPos = 0;
        int sumNeg = 0;
        int sumStances = 0;
        for (Point p : graph){
            if (((User)p).getStance().isPresent()) {
                sumStances++;

                if (((User) p).getStance().get() > 0)
                    sumPos++;
                else if (((User) p).getStance().get() < 0)
                    sumNeg++;
            }
        }

        str.append("Coverage: " + ((double)sumStances/ graph.size())*100 + "%" + "\n");
        str.append("Positive Stances: " + ((double)sumPos / (double)sumStances)*100 + "%" + "\n");
        str.append("Negative Staneces: "+ ((double)sumNeg / (double)sumStances)*100 + "%" + "\n");

        return str.toString();
    }

}
