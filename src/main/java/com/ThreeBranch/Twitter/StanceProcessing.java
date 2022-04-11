package com.ThreeBranch.Twitter;

import com.ThreeBranch.FileEntryIO;
import com.ThreeBranch.Graph.Edge;
import com.ThreeBranch.Graph.Graph;
import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Graph.IllegalGraphException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Optional;

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
        List<User> list = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader("Week3/Influential Users.txt"));
            while (reader.ready()){
                StringTokenizer tokens = new StringTokenizer(reader.readLine());
                if (tokens.countTokens() >= 2){
                    User user = new User(tokens.nextToken());
                    switch(tokens.nextToken()){
                        case "mid":
                            user.setStance(-500);
                            break;
                        case "pro":
                            user.setStance(1000);
                            break;
                        case "anti":
                            user.setStance(-1000);
                            break;
                    }
                    list.add(user);
                }
            }
        } catch(IOException e){ e.printStackTrace(); }
        finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        for (User p : list){
            graph.addUser(p.getName());
        }
        System.out.println(graph.size());
        writeStances(graph);
    }

    public boolean calcStances() throws IllegalGraphException{
      boolean change = false;
      
      for(Point p : graph) {
        if(!(p instanceof User)) 
          throw new IllegalGraphException("This function only works on users");
        
        User u = (User) p;
        
        int neighbors = 0;
        int stanceSum = 0;
        
        for(Edge e : graph.getAdj(u)) {
          Point p2 = e.getDestination();
          if(!(p2 instanceof User))
            throw new IllegalGraphException("This function only works on users");
          User u2 = (User) p2;
          
          Optional<Integer> stance = u2.getStance();
          if (stance.isPresent()) {
            neighbors++;
            stanceSum += stance.get();
            
          }
        }
        
        int newStance = stanceSum / neighbors;
        
        Optional<Integer> stance = u.getStance();
        if((stance.isPresent() && stance.get() != newStance) || !stance.isPresent()) {
          change = true;
          u.setStance(newStance);
        }
      }
      
      return change;
    }

    private void writeStances(Graph graph){
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
                Configuration.getValueFor("graph.stanceOutput"));
    }
}
