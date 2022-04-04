# 3Branch

Twitter Analytics Project

Eric Whye, Amira Ayadi, Tessa Brewer

Eric.Whye@ucdconnect.ie<br>
tjobs959@gmail.com

Goal: Analysis of vaccine related tweets, primarily on identification of a pro or anti-vax stance.

Using twitter4j libraries and developed using Maven.

Currently able to gather tweets and process tweets into a graph structure(Sprint 3).

<strong><br>Details from this Sprint:<br></strong>
Tweet Data is processed into custom Graph data structure that is interfaced well.

the Graph class has a sister class called GraphRTFileProcessor(bit of a bad name) 
that uses the Graph data structure for the specific usage of processing tweet data.
Usage of the tweet processor is decided in with argument parameters.

<br>
Functionality (Subject to Change):

-Loads config data from a configuration file (twitter4j.properties)

-Gathers real-time tweets based on given search terms and language(s) in config file.

-Project is object-oriented. To what degree, I cannot confirm.

-Stores tweets in a file (VaxData/vaxtweets.txt) and also stores account data associated with the tweets in VaxData/AccountsFromTweets.txt <br>
(Can be configured in config file).

-Can be restarted and stopped very easily with the use of a SearchBuffer in config file

-Tweet and Account duplication is factored in. The validity of these processes is verified

-Currently robust against exceptions including streaming TwitterExceptions.

-There is unit testing on major classes
 
<br>
Self Contained Jar with sources, class files and configuration file included.

How to run:
java -jar 3Branch-1.0-SNAPSHOT-shaded.jar -G (for tweet gathering)
java -jar 3Branch-1.0-SNAPSHOT-shaded.jar -graph -w (for writing graph data to file from tweet data)
java -jar 3Branch-1.0-SNAPSHOT-shaded.jar -graph -r (for reading graph data into memory)

Currently, 5190 english tweets and 3923 accounts associated with those tweets have been gathered according to these search terms: <br>
"#vaccinate,#immunization,#immunisation,#immunize,#vaccineswork,#keepsusafe,#omicron,#maskup,#vaccinerollout,#ventilation,#antivaccine,#vaccineskill,#vaxxed,#forcedinjections,#antivaccine,#medicalfreedom,#knowtherisk,#antivaxx,#vaccinsanity,#pharma,#bewaretheneedle,#vaccineinjury,#byebyebigpharma,#pharmthesheep,#vaccineeducation"

From 5300 onwards, search terms are:
"#vaccine #vaccination #vaccines #vaccinate #vaccinated #vaxxed #vaccineswork #VaccinesSaveLives #getvaccinated #GetVaxxed #vaccinerollout #vaccinatetheworld #PhizerGang #TeamModerna #immunization #immunisation #immunizeunder5s #immunize #keepsusafe #omicron #maskup #ventilation #covidisnotover #CancelCovid #vaccinechoice #noflushot #omicron #covid19 #pfizer #mask #antivaccine #antivaxxers #antivax #learntherisk #vaccineskill #vaxxed #vaccinescauseAIDS #researchdontregret #idonotconsent #stoppoisoningyourkids #forcedinjections #antivaccine #medicalfreedom #knowtherisk #antivaxx #vaccinsanity #pharma #bewaretheneedle #vaccineinjury #byebyebigpharma #pharmthesheep #vaccineeducation"