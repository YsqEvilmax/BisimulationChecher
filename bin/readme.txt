1.File Organization
There are totally 3 file in this project.

BisimulationChecker.java  This file defines the main object named "BisimulationChecker", which can read data of state-machine in certain format from txt files,  checking whether they are bisimilar or not and outputing results.
Graph.java This file defines the fundamental data structures which are used to build a graph demonstrating state-machines.
Laucher.java This file defines nothing but the "main()" which starts this application.

There are sufficient comments on each function as well as key parts. Any uncertain parts, please check comments in each *.java.

2.Compile and Run
Before you start, make sure you have the appropriate JRE on your local machine. This project is designed for JavaSE 1.8.
Prepare machines with desired format named "P.txt" and "Q.txt" respectively. Put them in the folder of your running environment.
If you are using IDE, just import *.java and then compile and run.
If you are using command line,  cd to directory of *.java, call "javac BisimulationChecker.java Graph.java Laucher.java".
After that, you should see several *.class files. Call "java Laucher" 
All the outputs will be written in a file named "Result.txt" at the same location.
