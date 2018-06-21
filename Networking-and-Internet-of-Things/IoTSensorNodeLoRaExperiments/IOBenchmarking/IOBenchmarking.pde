/*
 * Running IO operations for string with N size
 * (but will use 4 times the n because compute experiments rely on floats 
 * which cost 4 bytes but IO relay on chars which cost 1 byte)
 * also will subtract 1 from n because compiler uses 1 char for eol.
 * Moreover, it was tested before writing that available memory was almost 
 * 4400 bytes, so max array size was set to 4200 and 7 experiments were run in increment of 
 * 600 bytes.
  Each 10 secs, I log

  - begin and end timestamps
  - battery
  - data array size n
  - The number of IO operations executed

  So this is only to calculate the energy cost only. actually you can calculate the time cost as well

*/

// Put your libraries here (#include ...)

char logfile [] = "IOSD.TXT";
char wrfile [] = "WRTest.TXT";
char buf1 [32];
char buf2 [32];
char buf3 [32];
char msg [100];
long int counter = 0;
void setup()
{
  // put your setup code here, to run once:
  SD.ON();
  
  
  //SD.create(logfile);
  //SD.create(wrfile);
  Log("Experiment Begin");
  for (int i = 0; i < 1080; i++) //1080 time, 10 secs each. so total of 3 hours each run for 7 runs
  {
    LaunchNExperiment(600 , 10);
  }
  for (int i = 0; i < 1080; i++)
  {
    LaunchNExperiment(600 * 2, 10);
  }
  for (int i = 0; i < 1080; i++)
  {
    LaunchNExperiment(600 * 3, 10);
  }
  for (int i = 0; i < 1080; i++)
  {
    LaunchNExperiment(600 * 4, 10);
  }
  for (int i = 0; i < 1080; i++)
  {
    LaunchNExperiment(600 * 5, 10);
  }
  for (int i = 0; i < 1080; i++)
  {
    LaunchNExperiment(600 * 6, 10);
  }
  for (int i = 0; i < 1080; i++)
  {
    LaunchNExperiment(600 * 7, 10);
  }

}


void loop()
{
  // put your main code here, to run repeatedly:




}

float LaunchNExperiment( int n, int duration)
{
  char data [n];
  for (int i = 0 ; i < n-1; i++)
  {
    char c = 65 + (rand() % 25);
    data[i] = c;

  }


  counter = 0;
  unsigned long t = RTC.getEpochTime();
  unsigned long e = t + duration; //each d secs
  unsigned long t1 = RTC.getEpochTime();
 
  while (t < e)
  {
    //USB.println("counting");
    counter++;
    
    SD.writeSD(wrfile,data,0);
    //float res = Mean(data, n);
    //USB.println(res);


    /*
       This is to report the time consumption of a single operation, will disable it now
       and will run it only with continuous recording for short amount of time
    */
    /*
      if (counter%1000==0)//every 1000th operation
      {

      }*/
    t = RTC.getEpochTime();
  }
  
  unsigned long t2 = RTC.getEpochTime();
  sprintf(buf1, "%lu", t1);
  sprintf(buf2, "%lu", t2);
  float bat = PWR.getBatteryVolts();
  dtostrf(bat, 1, 10, buf3);
  sprintf(msg, "%s,%s,%s,%i,%i", buf3, buf1, buf2, n, counter);
  Log(msg);
  //  USB.println(msg);
  //  USB.println("done");
  // USB.println(counter);

  // delay(5000);


}
float Mean(float data [], int size)
{

  float sum = 0;

  for (int i = 0; i < size; i++)
  {
    sum += data[i];
  }
  return sum / size;
}
void Log (char s [])
{
  unsigned long t = RTC.getEpochTime();
  char tc [10] ;
  sprintf(tc, "%lu", t);
  SD.append(logfile, tc);
  SD.append(logfile, ",");
  SD.appendln(logfile, s);
  //SD.append(logfile, "\n");

}
