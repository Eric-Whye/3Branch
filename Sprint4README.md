1. Self Contained Jar and source files and README and Config file
2. retweeted Graph can be constructed from GraphShell using build retweeted.
3. These people can be identified from GraphShell using print mostretweeted.
4. 20 of the most influential evangelists were chosen.
5. stances can be percolated with "assign stances" after the stance graph has been built with "build graph".
6. iterations is specified in config file.
7. coverage with 20 evangelists is 66.66%, with 30% positive and 70% negative. This can be check with "print coverage"
8. I have no idea how accurate it is.
9. Through investigation with random users, there are some inconsistencies but the dataset as a whole does seem to tend towards the negative side because of vaccine mandates and vaccine passports.


We have a mini-shell implemented where you can make different graphs and other commands related to graphs.

How to Run:
Graph Shell: java -jar target/3Branch-1.0-SNAPSHOT.jar -graph
Tweet Gathering: java -jar target/3Branch-1.0-SNAPSHOT.jar -g

All the commands for the graph shell can be found in the source files