# P2P Neural Distributed Associative Memory Storage System

This system was done as final project for Dr. Evgeny Osipov at Lulea University of Technology in Sweden. It contains a storage agent and a master controller. The storage agent, which is stored on a node in a stogatge cluster, manages local storage RW  requests and search queries. It simulates the neural memory and it is more specicifed according to the original [home exam assignment](Images/Exam.pdf) .

## System Features

+ Allows multiple storage size addresses 
+ Dynamic Capacity Allocation
+ Compressed addresses - uses hex format to store memory adds
+ Its stateless - every request is independent from other request, 
+ Peers don’t need to keep session info
+ Can respond to multiple requests of multiple storage systems at same time 

## Architecture Overview
![alt text](Images/Architecture.PNG)

## Storage Agent CLI commands
![alt text](Images/Functionalities.PNG)

## Master GUI 

The current Master GUI is not completely functional because of some problems with intializing concurrent TPC connections. Future releases may better depend on Jade agent interfacing. 

![alt text](Images/Master%20GUI.PNG)

# Demo Screenshots
## Node Local Disk Storage Mounting
![alt text](Images/Slide13.PNG)

## Initialize Storage Agents
![alt text](Images/Slide14.PNG)

## Processing Neural Memory Store Command
![alt text](Images/Slide15.PNG)

## Processing Neural Memory Search Command
![alt text](Images/Slide16.PNG)

## Storage File View
![alt text](Images/Slide17.PNG)

## Node Multiple Store Mounting
![alt text](Images/Slide18.PNG)


## Search Results
![alt text](Images/Slide20.PNG)
