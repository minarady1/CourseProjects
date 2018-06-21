# IoT Node Benchmarking Experiments
This is code used in the M.Sc. thesis experiments in benchmarking the behavior of different IoT node components in Energy space and Time Space.
The code is pretty much self explanatory. Although I hope to be able to fully document it later. However here is a brief of the purpose of each code source:
+ IOBenchmarking: to measure energy consumption of activating SD IO and the IO throughput by varying stream size and logging the timestamps before and after IO operations.
+LoRaWANDynamicTx.pde : provides means to automate LoRa transmission by allowing variable PHY configuration at each transmission. This is important to measure energy and time behavior of different LoRa configurations without having to set up different running codes. 
+ LoRaWANTxWithSensorsRand: Simulates random backoff before transmission while sampling some sensors and adding them to the packet payload.
+ LoRaWANTxWithoutSensorsRand.pde : Same as pervious code but without adding any sensor information to the packet payload
+ LoRaWan_ReceiverDecrypt: Manual decryption of received LoRa packet in AES128. 
+ LoRaWan_Receiver_TimeStamped: A receiver code that logs all information of received packet including hex payload with local timestamp of the receiver. 
+ LogEnergyMeasurement: A code that logs battery discharge of the device at a given configuration for a long duration while activating IO component only during IO operations to ensure accurate measurements. 
+ N2AlgorithmBenchmarking: Records the time and the energy consumed by running and averaing algorithm of complexiy N^2. It allows dynamic intialization of array size and the duration to run the algorithm depending on experimental interests. 
