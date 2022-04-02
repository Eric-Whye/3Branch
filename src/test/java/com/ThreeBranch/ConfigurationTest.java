package com.ThreeBranch;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConfigurationTest {

    @Test
    void testReadAndSetConfig() throws Exception {
        Configuration.initialise("testConfiguration.properties");

        assertEquals(Configuration.getConfigInfo(),
                "\nSearch Terms:\n" +
                        "[Swirly, Wirly, 123.4356, ;567d[]]" + "\n" +
                        "\nLanguages:\n" +
                        "[en]" + "\n" +
                        "\nSearch and Write Buffer: " + "10" + "\n" +
                        "\nMax Tweets: " + "100000" + "\n" +
                        "\nOutput Directory: " + "TestFiles" +
                        "\nTweet Output File: " + "TestFiles/testTweets.txt" +
                        "\nAccount Output File: " + "TestFiles/testAccounts.txt" + "\n");
    }
}