package com.ThreeBranch.Hashtags;

import com.ThreeBranch.Graph.Point;
import com.ThreeBranch.Twitter.Configuration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HashtagLexiconTest {

    @Test
    void testLexicon(){
        Configuration config = Configuration.getInstance("testConfiguration.properties");

        HashtagLexicon lexicon = new HashtagLexicon(config.getValueFor("hashtags.lexiconFile"));

        List<Point> list = lexicon.getPointsList();
        System.out.println(list);
    }
}