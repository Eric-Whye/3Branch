package com.ThreeBranch.Graph;

import com.ThreeBranch.Twitter.Configuration;
import static org.junit.jupiter.api.Assertions.*;

import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import org.junit.jupiter.api.Test;

import java.io.*;

class GraphRTFileProcessorTest {
    @Test
    void testReadRetweets(){
        try {
            Configuration.initialise("testConfiguration.properties");
        } catch (FileNotFoundException e) {e.printStackTrace();}

        Graph graph = new Graph();
        GraphRTFileProcessor fp  = new GraphRTFileProcessor(graph);
        fp.populateGraphFromTweetFile(Configuration.getValueFor("graph.tweetsInput"));

        fp.writeGraphToFile(graph);
        BufferedReader reader = null;
        StringBuilder testOutput = new StringBuilder();
        try{
            reader = new BufferedReader(new FileReader(Configuration.getValueFor("graph.output")));
            while (reader.ready()){
                testOutput.append(reader.readLine());
            }
        } catch (IOException e) {e.printStackTrace();} finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        assertEquals("@Zinger" +
                        "@Testy(1)\t@Oof(1)" +
                        "@DGHisham" +
                        "@bernamadotcom(1)" +
                        "@Testy" +
                        "@Testy(4)\t@Zinger(1)",
                testOutput.toString());
    }

    @Test
    void testWriteRetweets(){
        try{
            Configuration.initialise("testConfiguration.properties");
        }catch(IOException e){e.printStackTrace();}

        Graph graph  = new Graph();
        GraphRTFileProcessor fp = new GraphRTFileProcessor(graph);
        fp.populateGraphFromGraphFile(Configuration.getValueFor("graph.output"));

        fp.writeGraphToFile(graph);



        BufferedReader reader = null;
        StringBuilder testOutput = new StringBuilder();
        try{
            reader = new BufferedReader(new FileReader(Configuration.getValueFor("graph.output")));
            while (reader.ready()){
                testOutput.append(reader.readLine());
            }
        } catch (IOException e) {e.printStackTrace();} finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        assertEquals("@Zinger" +
                        "@Testy(1)\t@Oof(1)" +
                        "@DGHisham" +
                        "@bernamadotcom(1)" +
                        "@Testy" +
                        "@Testy(4)\t@Zinger(1)",
                testOutput.toString());

    }
}