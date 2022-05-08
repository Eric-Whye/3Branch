# 3Branch

Twitter Analytics Project

Eric Whye, Tessa Brewer

Eric.Whye@ucdconnect.ie<br>
tjobs959@gmail.com

Goal: Analysis of vaccine related tweets.

Using twitter4j libraries and developed using Maven.



<br><br>
<strong>Commands for how to run the specification for each sprint.</strong><br>

Sprint 1: 

Sprint 2: java -jar target/3Branch-1.0-SNAPSHOT.jar -g

Sprint 3: java -jar target/3Branch-1.0-SNAPSHOT.jar -graph<br>
To Write to file specified under "graph.output" in config file do "build retweet" then "write".<br>
To read retweet and retweeted Graph from File, do "read from file".

Sprint 4: java -jar target/3Branch-1.0-SNAPSHOT.jar -graph<br>
To find the most the retweeted people, do "build retweeted" then "print highest weight".<br>
To assign stances to the graph, do "build retweet" then "assign stances".<br>
To look at stance assignment result/coverage, do "print coverage".<br>
Stances of each user are automatically written to file specified under "stance.output" in config file.<br>
To look at a random user. do "get random user".

Sprint 5: java -jar target/3Branch-1.0-SNAPSHOT.jar -graph<br>
To build to combined retweet and hashtag graph do 
"build retweet" then "assign stances" then "add graph". 
"build user to hashtag" then "assign stances" then "add graph", 
then finally do "combine graphs" and "print coverage" to see the result.

Sprint 6: 
1) "java -jar target/3Branch-1.0-SNAPSHOT.jar -graph"<br>
2) Run the command "build hashtag graph", this will build a graph of hashtags and their labels, and then store this in memory<br>
3.1) From here you can either run "print hashtag graph" to print the raw graph as a list of nodes and their adjacent nodes (ie hashtags and their labels)<br>
3.2) Or you can run the command "count labels" to print out the number of every argument, where an argument is derived from the labels associated with a hashtag<br>

Sprint 7: 
1) "java -jar target/3Branch-1.0-SNAPSHOT.jar -graph"<br>
2) Run the command "build hashtag graph", this will build a graph of hashtags and their labels, and then store this in memory
3.1) From here you can either run "print hashtag graph" to print the raw graph as a list of nodes and their adjacent nodes (ie hashtags and their labels)
3.2) Or you can run the command "count labels" to print out the number of every argument, where an argument is derived from the labels associated with a hashtag
Sprint 8: java -jar target/3Branch-1.0-SNAPSHOT.jar -graph<br>
To write the all the correct graph to GDF, do "write all to gdf".<br>
This writes a curated graph in GDF format to a GDF directory.


<br><br>
<strong>Source files associated with each Sprint</strong><br>
Sprint 1 and 2: Callable, FileEntryIO and Twitter Package. Namely TweetData, Twitterer, Configuration.<br>
Sprint 3: GraphShell and Graph package.<br>
Sprint 4: GraphShell and Twitter Package. Namely StancePoint, StanceProcessing, GraphRtFileProcessor and GraphQueries.
Sprint 5: GraphShell and Twitter Package. Namely StanceProcessing and GraphQueries.<br>
Sprint 6: GraphShell and Hashtags Package.<br>
Sprint 7: GraphShell, Profile and Analysis Packages<br>
Sprint 8: GDFWriter in Twitter Package.<br>

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