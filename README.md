# 3Branch

Twitter Analytics Project

Eric Whye, Amira Ayadi, Tessa Brewer

Eric.Whye@ucdconnect.ie<br>
Amira.Ayadi@ucdconnect.ie<br>
tjobs959@gmail.com

Goal: Analysis of vaccine related tweets, primarily on identification of a pro or anti-vax stance.

Using twitter4j libraries and developed using Maven.

Currently in very early stages (Sprint 2).

<br>
Functionality (Subject to Change):

-Loads config data from a configuration file (twitter4j.properties)

-Gathers real-time tweets based on given search terms and language(s) in config file.

-Stores tweets in a file (VaxData/vaxtweets.txt) and also stores account data associated with the tweets in VaxData/AccountsFromTweets.txt <br>
(Can be configured in config file).

-can be restarted and stopped very easily with the use of a SearchBuffer in config file

-Tweet and Account duplication is factored in. The validity of these processes is not verified

-Currently semi-robust against exceptions.

-Major lack of unit testing
 
<br>
Self Contained Jar with sources, class files and configuration file included.

How to run:
java -jar 3Branch-1.0-SNAPSHOT-shaded.jar

Currently, 5190 english tweets and 3923 accounts associated with those tweets have been gathered according to these search terms: <br>
"#vaccinate,#immunization,#immunisation,#immunize,#vaccineswork,#keepsusafe,#omicron,#maskup,#vaccinerollout,#ventilation,#antivaccine,#vaccineskill,#vaxxed,#forcedinjections,#antivaccine,#medicalfreedom,#knowtherisk,#antivaxx,#vaccinsanity,#pharma,#bewaretheneedle,#vaccineinjury,#byebyebigpharma,#pharmthesheep,#vaccineeducation"