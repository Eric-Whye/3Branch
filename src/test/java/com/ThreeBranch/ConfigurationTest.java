package com.ThreeBranch;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConfigurationTest {

    @Test
    void testReadAndSetConfig() throws Exception {
        Configuration.initialise();

        assertEquals(Configuration.getConfigInfo(),
                "\nSearch Terms:\n" +
                "[Zesty, ?.,[]098jd, Testy, Zesty, 0987]" + "\n" +
                "\nLanguages:\n" +
                "[en]" + "\n" +
                "\nSearch and Write Buffer: " + "10" + "\n" +
                "\nMax Tweets: " + "100000" + "\n" +
                "\nOutput Directory: " + "TestFiles" +
                "\nTweet Output File: " + "TestFiles/testTweets.txt" +
                "\nAccount Output File: " + "TestFiles/testAccounts.txt" + "\n");
    }
}