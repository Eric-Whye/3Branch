package com.ThreeBranch;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileEntryIOTest {
    private BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(new FileWriter("TestFiles/testFileEntryIO.txt"));
    }
    private BufferedReader getReader() throws IOException {
        return new BufferedReader(new FileReader("TestFiles/testFileEntryIO.txt"));
    }

    @Test
    void testAppendToFile() throws Exception {
        Writer writer = getWriter();
        writer.write("");
        writer.close();


        Configuration.initialise();
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            List<String> line = new ArrayList<>();
            line.add(i + "\t" + (i + 1));
            list.add(line);
        }
        FileEntryIO.appendToFile(list, '\t', '\n', "TestFiles/testFileEntryIO.txt");

        int count = 0;
        BufferedReader reader = getReader();
        while (reader.ready()){
            String line = reader.readLine();
            assertEquals(line, count++ + "\t" + count, "File should be in the form of i + \t + i+1");
        }
    }

}