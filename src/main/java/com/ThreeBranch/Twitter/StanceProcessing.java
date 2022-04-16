package com.ThreeBranch.Twitter;

import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileNotFoundException;


public class StanceProcessing {
    private final Graph graph;

    private int LARGEST_WEIGHT_CALCULATED;
    private int SMALLEST_WEIGHT_CALCULATED;
    private final int MAX_STANCE;
    private final int MIN_STANCE;
    
    public StanceProcessing(Graph graph){
        try {
            Configuration.initialise(Configuration.ConfigFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
      
        MAX_STANCE = Integer.parseInt(Configuration.getValueFor("stance.maxStance"));
        MIN_STANCE = Integer.parseInt(Configuration.getValueFor("stance.minStance"));
         
        LARGEST_WEIGHT_CALCULATED = MAX_STANCE;
        SMALLEST_WEIGHT_CALCULATED = MIN_STANCE;
      
        this.graph = graph;
    }

    /**
     *Assign Stances to the recorded influential users
     */
    public void initialiseStances(String filename){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(filename));
            while (reader.ready()){
                StringTokenizer tokens = new StringTokenizer(reader.readLine());
                if (tokens.countTokens() >= 2){
                    String name = tokens.nextToken();
                    Optional<Point> userOption = graph.getPointIfExists(name);

                    if(userOption.isPresent()) {
                      Point p = userOption.get();
                      if(p instanceof StancePoint) {
                        StancePoint stancePoint = (StancePoint) p;
                        switch(tokens.nextToken()){
                            case "mid":
                                stancePoint.setStance(Integer.parseInt(Configuration.getValueFor("stance.midStance")));
                                break;
                            case "pro":
                                stancePoint.setStance(Integer.parseInt(Configuration.getValueFor("stance.maxStance")));
                                break;
                            case "anti":
                                stancePoint.setStance(Integer.parseInt(Configuration.getValueFor("stance.minStance")));
                                break;
                        }
                      } else {
                        System.err.println(name + " is not a user object");
                      }
                    } else {
                      System.err.println(name + " is not present in graph");
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
        if(!(p instanceof StancePoint))
          throw new ClassCastException("This function only works on User objects");
        
        StancePoint u = (StancePoint) p;
        
        int neighbors = 0;
        int stanceSum = 0;
        int weights = 0;
        
        for(Edge e : graph.getAdj(u)) {
          Point p2 = e.getDestination();
          if(!(p2 instanceof StancePoint))
              throw new ClassCastException("This function only works on User objects");
          StancePoint u2 = (StancePoint) p2;
          
          Optional<Integer> stance = u2.getStance();
          if (stance.isPresent()) {
            neighbors++;
            stanceSum += stance.get() * e.getWeight();
            weights += e.getWeight();
          }
        }
        
        if(neighbors != 0) {
          int newStance = stanceSum / weights;
          newStance = newStance / neighbors;
          
          //Update the range of calculated values
          if(newStance > LARGEST_WEIGHT_CALCULATED)
            LARGEST_WEIGHT_CALCULATED = newStance;
          
          if(newStance < SMALLEST_WEIGHT_CALCULATED)
            SMALLEST_WEIGHT_CALCULATED = newStance;
          
          //Scale the calculated weight to the new range
          int newRange = (MAX_STANCE - MIN_STANCE);
          int oldRange = (LARGEST_WEIGHT_CALCULATED - SMALLEST_WEIGHT_CALCULATED);
          
          newStance = (((newStance - SMALLEST_WEIGHT_CALCULATED) * newRange) / oldRange) + MIN_STANCE;
          
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
                entry.add(((StancePoint) user).getStance().toString());
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
        Double[] list = {
                0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
                0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
                0.0};

        int sumPos = 0;
        int sumNeg = 0;
        int sumStances = 0;
        for (Point p : graph){
            if (((StancePoint)p).getStance().isPresent()) {
                sumStances++;

                int number = ((StancePoint) p).getStance().get();
                int firstDigit = Integer.parseInt(Integer.toString(Math.abs(number)).substring(0, 1));

                if (number < 0) {
                    sumNeg++;
                    //For the number -1000
                    if (number == Integer.parseInt(Configuration.getValueFor("stance.minStance"))){
                        list[9]++;
                        continue;
                    }
                    //For the numbers that at single or double digits
                    if (number >= -99){
                        list[0]++;
                        continue;
                    }
                    list[firstDigit]++;
                }
                else{//Numbers Greater than zero
                    sumPos++;
                    //For the number 1000
                    if (number == Integer.parseInt(Configuration.getValueFor("stance.maxStance"))){
                        list[19]++;
                        continue;
                    }
                    //For the numbers that at single or double digits
                    if (number <= 99){
                        list[10]++;
                        continue;
                    }
                    list[firstDigit+10]++;
                }
            }
        }

        str.append("Coverage: " + ((double)sumStances/ graph.size())*100 + "%" + "\n");
        str.append("Positive Stances: " + ((double)sumPos / (double)sumStances)*100 + "%" + "\n");
        str.append("Negative Stances: "+ ((double)sumNeg / (double)sumStances)*100 + "%" + "\n");

        for(int i = 0; i < list.length;i++){
            list[i] = (list[i] / (double)sumStances)*100;
        }

        str.append("-1000 - -900\t" + list[9] + "%\n");
        str.append("-899 - -800\t" + list[8] + "%\n");
        str.append("-799 - -700\t" + list[7] + "%\n");
        str.append("-699 - -600\t" + list[6] + "%\n");
        str.append("-599 - -500\t" + list[5] + "%\n");
        str.append("-499 - -400\t" + list[4] + "%\n");
        str.append("-399 - -300\t" + list[3] + "%\n");
        str.append("-299 - -200\t" + list[2] + "%\n");
        str.append("-199 - -100\t" + list[1] + "%\n");
        str.append("-099 - -001\t" + list[0] + "%\n");
        str.append(" 000 -  099\t" + list[10] + "%\n");
        str.append(" 100 -  199\t" + list[11] + "%\n");
        str.append(" 200 -  299\t" + list[12] + "%\n");
        str.append(" 300 -  399\t" + list[13] + "%\n");
        str.append(" 400 -  499\t" + list[14] + "%\n");
        str.append(" 500 -  599\t" + list[15] + "%\n");
        str.append(" 600 -  699\t" + list[16] + "%\n");
        str.append(" 700 -  799\t" + list[17] + "%\n");
        str.append(" 800 -  899\t" + list[18] + "%\n");
        str.append(" 900 - 1000\t" + list[19] + "%\n");

        return str.toString();
    }

}
