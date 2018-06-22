# Parallel Task Scheduling
Consider the problem of having n processors and m tasks each task consumes t amount of time. How can tasks be assigned to processors (which will run in parallel) such that the total timespan of the whole process is minimal? [full description](ex3_17.pdf)

Here you can find two approaches for parallel task scheduling:

## ILP formaulation
In the code file `Parallel-Task-Scheduling-ILP.m` you can find solution to the problem in ILP approach which finds the global optimum assignment, if available. 

## Longest Process Time Heuristic
This simple heuristic in the file `Parallel-Task-Scheduling-LPT-Heuristic.m` iterates on the processes and automatically assigns the next longest time task to the least occupied processor (round-robin)
