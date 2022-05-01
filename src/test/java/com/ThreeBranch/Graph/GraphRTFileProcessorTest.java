package com.ThreeBranch.Graph;

import com.ThreeBranch.Twitter.Configuration;
import static org.junit.jupiter.api.Assertions.*;

import com.ThreeBranch.Twitter.GraphRTFileProcessor;
import org.junit.jupiter.api.Test;

import java.io.*;

class GraphRTFileProcessorTest {
    @Test
    void testRetweetGraph(){
        Configuration config = Configuration.getInstance("testConfiguration.properties");
        Graph graph = new Graph();
        GraphRTFileProcessor fp  = new GraphRTFileProcessor(graph);

        fp.populateRetweetGraphFromFile(config.getValueFor("graph.tweetsInput"));
        fp.writeGraphToFile(graph);

        BufferedReader reader = null;
        StringBuilder testOutput = new StringBuilder();
        try{
            reader = new BufferedReader(new FileReader(config.getValueFor("graph.output")));
            while (reader.ready()){
                testOutput.append(reader.readLine()).append("\n");
            }
        } catch (IOException e) {e.printStackTrace();} finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        assertEquals("@Zinger\n"+
                        "@Testy(1)\t@Oof(1)\n\n" +
                        "@DGHisham\n" +
                        "@bernamadotcom(1)\n\n" +
                        "@Testy\n" +
                        "@Testy(4)\t@Zinger(1)\n\n",
                testOutput.toString());
    }

    @Test
    void testRetweetedGraph(){
        Configuration config = Configuration.getInstance("testConfiguration.properties");
        Graph graph = new Graph();
        GraphRTFileProcessor fp  = new GraphRTFileProcessor(graph);

        fp.populateRetweetedGraphFromFile(config.getValueFor("graph.tweetsInput"));
        fp.writeGraphToFile(graph);

        BufferedReader reader = null;
        StringBuilder testOutput = new StringBuilder();
        try{
            reader = new BufferedReader(new FileReader(config.getValueFor("graph.output")));
            while (reader.ready()){
                testOutput.append(reader.readLine()).append("\n");
            }
        } catch (IOException e) {e.printStackTrace();} finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {e.printStackTrace();}
        }

        assertEquals("@bernamadotcom\n" +
                        "@DGHisham(1)\n\n" +
                        "@Zinger\n" +
                        "@Testy(1)\n\n" +
                        "@Oof\n" +
                        "@Zinger(1)\n\n" +
                        "@Testy\n" +
                        "@Testy(4)\t@Zinger(1)\n\n",
                testOutput.toString());

    }
}