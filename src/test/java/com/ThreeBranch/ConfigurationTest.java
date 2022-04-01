package com.ThreeBranch;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConfigurationTest {

    @Test
    void testReadAndSetConfig() throws Exception {
        Configuration.initialise();

        assertEquals(Configuration.getConfigInfo(),
                "\nSearch Terms:\n" +
                "[#vaccine, #vaccination, #vaccines, #vaccinate, #vaccinated, #vaxxed, #vaccineswork, #VaccinesSaveLives, #getvaccinated, #GetVaxxed, #vaccinerollout, #vaccinatetheworld, #PhizerGang, #TeamModerna, #immunization, #immunisation, #immunizeunder5s, #immunize, #keepsusafe, #maskup, #ventilation, #covidisnotover, " +
                        "#CancelCovid, #vaccinechoice, #noflushot, #pfizer, #mask, " +
                        "#antivaccine, #antivaxxers, #antivax, #learntherisk, #vaccineskill, #vaxxed, #vaccinescauseAIDS, #researchdontregret, #idonotconsent, #stoppoisoningyourkids, #forcedinjections, #antivaccine, #medicalfreedom, #knowtherisk, #antivaxx, #vaccinsanity, #pharma, #bewaretheneedle, #vaccineinjury, #byebyebigpharma, #pharmthesheep, #vaccineeducation]" + "\n" +
                "\nLanguages:\n" +
                "[en]" + "\n" +
                "\nSearch and Write Buffer: " + "10" + "\n" +
                "\nMax Tweets: " + "100000" + "\n" +
                "\nOutput Directory: " + "VaxData" +
                "\nTweet Output File: " + "VaxData/vaxtweets.txt" +
                "\nAccount Output File: " + "VaxData/AccountsFromTweets.txt" + "\n");
    }
}