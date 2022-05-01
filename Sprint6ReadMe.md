--Usage--

(Note: The compiled project is named "3Branch-1.0-SNAPSHOT-shaded.jar")
1) Launch the program from the command line with the "-graph" argument, this will start the shell
2) Run the command "build hashtag graph", this will build a graph of hashtags and their labels, and then store this in memory
3.1) From here you can either run "print hashtag graph" to print the raw graph as a list of nodes and their adjacent nodes (ie hashtags and their labels)
3.2) Or you can run the command "count labels" to print out the number of every argument, where an argument is derived from the labels associated with a hashtag

--Code Structure--
HashtagLexicon extends Graph.java and the added functionality is when a new object is created, it reads from the labelled tag elements file and fills itself.
The tag splits are a subclass of Vertex called HashtagLabel that can store the meaning of each split word.
The split tags are discovered with camel casing only and words that do not follow camel casing convention are not stored correctly.