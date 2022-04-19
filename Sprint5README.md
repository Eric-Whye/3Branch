Please see "3Branch-1.0-SNAPSHOT.jar", this is our executable jar.

Instructions:
-For the regular stance algorithm first launch the program from the command line with "-graph" as the only argument (this launches the shell)
-Then run the command "build retweet" to build the retweet graph
-Then run the command "assign stances" to preform the stance assignment algorithm

-For the enhanced algorithm first launch the program from the command line with "-graph" as the only argument (this launches the shell)
-Then run the command "build retweet" to build the retweet graph
-Then "assign stances" to preform the normal stance assignment algorithm on this graph
-Then "add graph" to store the graph for future use
-Then "build user to hashtag" to build the hashtag graph
-Then "assign stances"
-Then "add graph"
-Then "combine graphs" to combine these graphs into one combined graph

Algorithm implementation completed by Eric, algorithm annalysis implemented by Deven.

Answers to questions:
3c ) Coverage is roughly 74.6%, 30.16% positive, 69.84% negative. This is up from roughly 67% coverage for the previous week.
4a ) The stances assigned by the enhanced algorithm seem to be a lot more nuenced. Scores of 0 change to 500 or -500, scores of -500 become -250, or -375. The two algorithms rarely disagree on which side of the argument a person lies.
4b ) They agree in almost exactly 33% of cases, while in the other 66% they disagree. This is mostly because the first algorithm tended to assign users a score of "0", which the second algorithm corrected.
4c ) After (temporarily) modifying the analysis code to only look at users who retweeted more than 10 times I found that the number of disagreements went up to 85.96%. Again, it appears as though this is mostly due to users going from a 0 score to a positive or negative score. This doesn't male much sense though, more connected users should be less likely to have a 0 score.