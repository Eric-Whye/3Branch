# 3Branch

Twitter Analytics Project

Eric Whye, Amira Ayadi, Tessa Brewer

Eric.Whye@ucdconnect.ie<br>
Amira.Ayadi@ucdconnect.ie<br>
tjobs959@gmail.com

Goal: Analysis of vaccine related tweets, primarily on identification of a pro or anti-vax stance.

Using twitter4j libraries and developed using Maven.

Currently in very early stages.

<br>
Functionality (Subject to Change):

-Loads config data from a configuration file (twitter4j.properties)

-Gathers tweets based on a given hashtag(s) in config file with other parameters in the file as well such as a number of tweets and rate limit etc.

-Stores tweets in a file (VaxData/vaxtweets.txt) and also stores account data associated with the tweets in VaxData/AccountsFromTweets.txt).

-Currently does not factor in tweet duplication or account data duplication in the output

-Currently not very robust against things like api exceptions.
 
<br>
Self Contained Jar with sources, class files and configuration file included.

How to run:
java -jar 3Branch-1.0-SNAPSHOT.jar

Currently Main only runs the implemented functionalities without any persistence 
so it will search for and write 10 tweets  and their associated accounts into the output files. 


<br><br><br>
TweetData.readTweetIDs needs to be less hardcoded.
Twitterer needs to be implemented with formatting.