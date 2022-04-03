package com.ThreeBranch.Graph;

import com.ThreeBranch.Configuration;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class GraphFileProcessorTest {
    @Test
    void testReadRetweets(){
        try {
            Configuration.initialise("testConfiguration.properties");
        } catch (FileNotFoundException e) {e.printStackTrace();}


    }
}