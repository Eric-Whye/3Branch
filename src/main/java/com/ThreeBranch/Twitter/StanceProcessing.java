package com.ThreeBranch.Twitter;

import com.ThreeBranch.Callable;
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
    private final Configuration config = Configuration.getInstance();
    private final List<String> initialData = new ArrayList<>();

    private int largestWeightCalculated;
    private int smallestWeightCalculated;
    private final int MAX_STANCE;
    private final int MIN_STANCE;
    
    public StanceProcessing(Graph graph){
        MAX_STANCE = Integer.parseInt(config.getValueFor("stance.maxStance"));
        MIN_STANCE = Integer.parseInt(config.getValueFor("stance.minStance"));
         
        largestWeightCalculated = MAX_STANCE;
        smallestWeightCalculated = MIN_STANCE;
      
        this.graph = graph;
    }


    private class readInitialData implements Callable {
        @Override
        public void call(Object o) {
            initialData.add((String)o);
        }
    }
    /**
     *Assign Stances to the recorded influential users
     */
    public void initialiseStances(String filename){
        FileEntryIO.streamLineByLine(filename, new readInitialData());

        int count = 0;
        for (String str : initialData){
            StringTokenizer tokens = new StringTokenizer(str);
            if (tokens.countTokens() < 2) continue;
            String name = tokens.nextToken();
            int stanceNumber = 0;
            switch(tokens.nextToken()){
                case "mid":
                    stanceNumber = Integer.parseInt(config.getValueFor("stance.midStance"));
                    break;
                case "pro":
                    stanceNumber = Integer.parseInt(config.getValueFor("stance.maxStance"));
                    break;
                case "anti":
                   stanceNumber = Integer.parseInt(config.getValueFor("stance.minStance"));
                    break;
            }
            for (Point p : graph){
                List<Edge> edges = graph.getAdj(p);
                for (Edge e : edges){
                    if (e.getDestination().getName().equals(name)){
                        ((StancePoint)e.getDestination()).setStance(stanceNumber);
                    }
                }
            }
        }
        /*
            StringTokenizer tokens = new StringTokenizer(str);
            if (tokens.countTokens() >= 2){
                String name = tokens.nextToken();
                Optional<Point> userOption = graph.getPointIfExists(new StancePoint(name));

                if(userOption.isPresent()) {
                    Point p = userOption.get();
                    if(p instanceof StancePoint) {
                        StancePoint stancePoint = (StancePoint) p;
                        switch(tokens.nextToken()){
                            case "mid":
                                stancePoint.setStance(Integer.parseInt(config.getValueFor("stance.midStance")));
                                break;
                            case "pro":
                                stancePoint.setStance(Integer.parseInt(config.getValueFor("stance.maxStance")));
                                break;
                            case "anti":
                                stancePoint.setStance(Integer.parseInt(config.getValueFor("stance.minStance")));
                                break;
                        }
                    } else
                        System.err.println(name + " is not a user object");
                } else
                    System.err.println(name + " is not present in graph");

            }
        }*/
    }

    public boolean calcStances() throws ClassCastException{
      boolean change = false;
      for(Point p : graph) {
        StancePoint u = (StancePoint) p;

        int neighbors = 0;
        int stanceSum = 0;
        int weights = 0;

        for(Edge e : graph.getAdj(u)) {

            //Since Graph is a Point -> Edge structure, p1 are the points in destination edges that have stances
          Point p1 = e.getDestination();

          Optional<Integer> stanceNum = ((StancePoint) p1).getStance();
          if (stanceNum.isPresent()){
              neighbors++;
              stanceSum += stanceNum.get() * e.getWeight();
              weights += e.getWeight();
          }else{
              //p2 is p1 except it is a Point in the graph rather than a destination edge.
              Optional<Point> p2 = graph.getPointIfExists(p1);
              if (p2.isPresent()){
                  Optional<Integer> stanceNum2 = ((StancePoint) p2.get()).getStance();
                  if (stanceNum2.isPresent()){
                      neighbors++;
                      stanceSum += stanceNum2.get() * e.getWeight();
                      weights += e.getWeight();
                  }
              }
          }/*
          Point p2 = e.getDestination();
          if(!(p2 instanceof StancePoint)) throw new ClassCastException("This function only works on StancePoint objects");
          StancePoint u2 = (StancePoint) p2;

          Optional<Integer> stance = u2.getStance();
          if (stance.isPresent()) {
            neighbors++;
            stanceSum += stance.get() * e.getWeight();
            weights += e.getWeight();
          }*/
        }


        if(neighbors != 0) {
          int newStance = stanceSum / weights;
          newStance = newStance / neighbors;

          //Update the range of calculated values
          if(newStance > largestWeightCalculated)
            largestWeightCalculated = newStance;

          if(newStance < smallestWeightCalculated)
            smallestWeightCalculated = newStance;

          //Scale the calculated weight to the new range
          int newRange = (MAX_STANCE - MIN_STANCE);
          int oldRange = (largestWeightCalculated - smallestWeightCalculated);

          newStance = (((newStance - smallestWeightCalculated) * newRange) / oldRange) + MIN_STANCE;

          Optional<Integer> stance = u.getStance();
          if(!stance.isPresent() || stance.get() != newStance) {
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
        FileEntryIO.writeLineByLine(list,
                config.getValueFor("format.delim").charAt(0),
                config.getValueFor("format.newLineDelim").charAt(0),
                config.getValueFor("stance.output"));
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

                int stanceNum = ((StancePoint) p).getStance().get();
                int firstDigit = Integer.parseInt(Integer.toString(Math.abs(stanceNum)).substring(0, 1));

                if (stanceNum < 0) {
                    sumNeg++;
                    //For the number -1000
                    if (stanceNum == Integer.parseInt(config.getValueFor("stance.minStance"))){
                        list[9]++;
                        continue;
                    }
                    //For the numbers that at single or double digits
                    if (stanceNum >= -99){
                        list[0]++;
                        continue;
                    }
                    list[firstDigit]++;
                }
                else if (stanceNum > 0){//Numbers Greater than zero
                    sumPos++;
                    //For the number 1000
                    if (stanceNum == Integer.parseInt(config.getValueFor("stance.maxStance"))){
                        list[20]++;
                        continue;
                    }
                    //For the numbers that at single or double digits
                    if (stanceNum <= 99){
                        list[11]++;
                        continue;
                    }
                    list[firstDigit+11]++;
                }
                else//For the number 0
                    list[10]++;
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
        str.append(" 000\t  \t" + list[10] + "%\n");
        str.append(" 001 -  099\t" + list[11] + "%\n");
        str.append(" 100 -  199\t" + list[12] + "%\n");
        str.append(" 200 -  299\t" + list[13] + "%\n");
        str.append(" 300 -  399\t" + list[14] + "%\n");
        str.append(" 400 -  499\t" + list[15] + "%\n");
        str.append(" 500 -  599\t" + list[16] + "%\n");
        str.append(" 600 -  699\t" + list[17] + "%\n");
        str.append(" 700 -  799\t" + list[18] + "%\n");
        str.append(" 800 -  899\t" + list[19] + "%\n");
        str.append(" 900 - 1000\t" + list[20] + "%\n");

        return str.toString();
    }

}
