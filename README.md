# LogParser
This project aims to provide a simple framework generate different parsers for different files. It uses a configuration file in order to parse the input into its elements and return the desired information.
# Installation
To run the code [java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) is required and MySQL 5.5+ is used to persist the parsed result, if configured. To build it, [ant](http://ant.apache.org/) is used.

With Ant installed, clone the repository, and execute the ant build:
```bash
 git clone https://github.com/albertodepaola/logparser.git logparser;
 cd logparser;
 ant build-jar
 ```
 
 The resulting jar file, located in dist/parser.jar, is ready for use with a simple command line utility or to be embedded in another java application. 

# License
The content of this repository is licensed under a [Creative Commons Attribution License](https://creativecommons.org/licenses/by/3.0/us/)



   