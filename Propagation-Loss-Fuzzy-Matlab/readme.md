# Fuzzy Rules to Simulate Wireless Propagation in Complex Terrain

In this simple code, I attempt to create a very basic simulation to assess the wireless network coverage. I assume a wireless network made of 4 transmitters (Txi) and several receivers (Rxi). This was an exercise for me to learn about fuzzy inference systems. It can be, however, useful to use it simulate complex terrain where the propagation loss is not linear in log scale!

I assume the network distribution looks approximately like:

![alt text](https://4.bp.blogspot.com/-NCbFjTTZ16Q/WRMEpAijcxI/AAAAAAAAA5Q/57mhei6-R4EdgG8c6LxmGB7Wp6SuKhfjACEw/s400/1.PNG)

I created random positions for Rxi. I assumed Txi to be distributed at the corners of a square area as the plot above. Then, I calculated the distance of each Rxi to each of the Txj (represented as dRxiTxj) using Euclidian distance formula. All the calculations are the attached Excel file.

The random Rx nodes are plotted below in terms of X and Y coordinates: 

![alt text](https://3.bp.blogspot.com/-8naX6TJDsmA/WRMEpBEqMEI/AAAAAAAAA5I/dzCwc76Q3ZM6r-2xMGpgBZesMjrHsCqRACEw/s1600/2.PNG)

I created Fuzzy Inference System in the attached FIS file. I created 4 input variables representing signal strength for each Txi. Signal strength is assumed to be 1- dRxiTxj.
## Parameter Assignment

I defined Tx2, Tx3, and Tx4 to have the same signal strength. Tx1 has a larger
I classified the output evaluation to 3 main classes: Reliable, Acceptable, Unacceptable. The final result of the evaluation is translated in the graph below. The scatter point size represents the final evaluation which should indicate the signal strength. In this case, we can see that in the final result, the signal strength at a given Rxi node is higher as it is closer to any of the Txj nodes (in red) below.   

![alt text](https://1.bp.blogspot.com/-I6l3ID_TVeA/WRMEpPrkjKI/AAAAAAAAA5M/VHF_crHkyPQMy2RwxouFl64k81sm13o1ACEw/s1600/3.PNG)
## Output Evaluation
I assume output evaluation range of 1-100, to the values: Unacceptable, Acceptable, and Reliable as per the fuzzy representation below:

![alt text](https://1.bp.blogspot.com/-I6l3ID_TVeA/WRMEpPrkjKI/AAAAAAAAA5M/VHF_crHkyPQMy2RwxouFl64k81sm13o1ACEw/s1600/4.PNG)

## Assumed Fuzzy Inference Rules:
 In the rule system, I assume the following (basic) rules (rules may differ according to routing protocol and network purpose)
 
+ Receiver Rxi has a reliable connection if it has high signal from at least one transmitter.
+ Receiver Rxi has an acceptable connection if it has medium strength signal from at least one transmitter.
+ Receiver Rxi has an unacceptable connection if it has low strength with every other transmitter.

![alt text](https://1.bp.blogspot.com/-I6l3ID_TVeA/WRMEpPrkjKI/AAAAAAAAA5M/VHF_crHkyPQMy2RwxouFl64k81sm13o1ACEw/s1600/5.PNG)

## Simulation Result:

In the simulation, I plot all Tx and Rx on the graph by their GPS coordinates. I use a scatter plot and I programmed the scatter point size to reflect the point connection evaluation. We can notice that evaluation does correspond successfully to Rx node distance to Tx. Also, Tx11  signal strength seems to be really higher and influencing more nodes relatively far, while other nodes at similar distances from other transmitters have less signal evaluation.

![alt text](https://1.bp.blogspot.com/-I6l3ID_TVeA/WRMEpPrkjKI/AAAAAAAAA5M/VHF_crHkyPQMy2RwxouFl64k81sm13o1ACEw/s1600/6.PNG)

