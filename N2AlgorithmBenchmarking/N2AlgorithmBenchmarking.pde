/*
Running N complexity algorithm to compute average.
Each 10 secs, I log+

- begin and end timestamps
- battery
- data array size n
- The number of operations executed

So this is only to calculate the energy cost only. actually you can calculate the time cost as well

*/

// Put your libraries here (#include ...)

char logfile [] = "Average2.TXT";
char buf1 [32];
char buf2 [32];
char buf3 [32];
char msg [100];
long int counter = 0;
void setup()
{
  // put your setup code here, to run once:
  SD.ON();
  SD.create(logfile);
  Log("Experiment N2 Begin");
  for (int i =0;i<1080;i++) //1080 time, 10 secs each. so total of 3 hours each run for 7 runs
  {
    LaunchNExperiment(200,10);
  }
  for (int i =0;i<1080;i++) 
  {
    LaunchNExperiment(400,10);
  }
    for (int i =0;i<1080;i++) 
  {
    LaunchNExperiment(600,10);
  }
   for (int i =0;i<1080;i++) 
  {
    LaunchNExperiment(800,10);
  }
     for (int i =0;i<1080;i++) 
  {
    LaunchNExperiment(1000,10);
  }
      for (int i =0;i<1080;i++) 
  {
    LaunchNExperiment(1200,10);
  } 
        for (int i =0;i<1080;i++) 
  {
    LaunchNExperiment(1400,10);
  } 
   
}


void loop()
{
  // put your main code here, to run repeatedly:
 



}

float LaunchNExperiment(int n, int duration)
{
    float data [n];
    for (int i=0;i<n;i++)
    {
      data [i] = 9999999.999  /rand();
    }
    counter =0;
    unsigned long t = RTC.getEpochTime();
    unsigned long e = t+duration;//each d secs
    unsigned long t1 = RTC.getEpochTime();
     
    while (t<e)
    {
      //USB.println("counting");
      counter++;
     for (int i =0;i<n;i++) // do n operations n times
     {
      float res = Mean(data,n);
     }
      //USB.println(res);
     

      /*
       * This is to report the time consumption of a single operation, will disable it now 
       * and will run it only with continuous recording for short amount of time
      */
      /*
      if (counter%1000==0)//every 1000th operation
      {
        
      }*/
      t = RTC.getEpochTime();
    }
    unsigned long t2 = RTC.getEpochTime();
    sprintf(buf1,"%lu",t1);
    sprintf(buf2,"%lu",t2);
    float bat = PWR.getBatteryVolts();
    dtostrf(bat,1,10,buf3);
    sprintf(msg,"%s,%s,%s,%i,%i",buf3,buf1,buf2,n,counter); 
    Log(msg);
  //  USB.println(msg);
  //  USB.println("done");
   // USB.println(counter);
 
   // delay(5000);
        

}
float Multiply (float data1, float data2)
{
  
 }
float Mean(float data [], int size)
{

  float sum = 0;
  
  for (int i =0;i<size;i++)
  {
    sum+= data[i];  
  }
  return sum/size;
}
void Log (char s [])
{
  unsigned long t = RTC.getEpochTime();
  char tc [10] ;
  sprintf(tc,"%lu",t);
  SD.append(logfile, tc);
  SD.append(logfile, ",");
  SD.appendln(logfile, s);
  //SD.append(logfile, "\n");
  
}
