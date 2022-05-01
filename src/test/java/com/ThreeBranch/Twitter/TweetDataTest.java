package com.ThreeBranch.Twitter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TweetDataTest {

    @Test
    void testInitialise(){
        TweetData.initialise();

        assertTrue(TweetData.checkDupAccount("@" + "Testy"), "Testy is a duplicate Account");
        assertTrue(TweetData.checkDupAccount("@" + "Zesty"), "Zesty is a duplicate Account");
        assertFalse(TweetData.checkDupAccount("@" + "testy"), "testy is not a duplicate Account");
        assertFalse(TweetData.checkDupAccount("\n"), "Special Characters are not duplicates");
        assertTrue(TweetData.checkDupID(Long.parseLong("1508957873686122445")), "This ID is also a duplicate");
    }

}