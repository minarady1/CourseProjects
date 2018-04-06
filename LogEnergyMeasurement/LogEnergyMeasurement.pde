/*
    ------ Waspmote Pro Code Example --------

    Explanation: This is the basic Code for Waspmote Pro

    Copyright (C) 2016 Libelium Comunicaciones Distribuidas S.L.
    http://www.libelium.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

// Put your libraries here (#include ...)
char logfile []="NEUT.TXT";
unsigned long now;
unsigned long en;
void setup()
{
  // put your setup code here, to run once:
  SD.ON();
    //SD.del(logfile);

  SD.create(logfile);
  now = RTC.getEpochTime();
  en = now + 1800;//every half an hour
  Log("Setup");
}


void loop()
{

  // put your main code here, to run repeatedly:
  if (now>en){
  
  float bat = PWR.getBatteryVolts();
  char buf [100];
  dtostrf(bat,1,10,buf);
  Log(buf);
  en = now +1800;
  }
  now= RTC.getEpochTime();
 
}

void Log (char s [])
{
  unsigned long t = RTC.getEpochTime();
  char tc [10] ;
  sprintf(tc,"%lu",t);
  SD.append(logfile, tc);
  SD.append(logfile, ",");
  SD.appendln(logfile, s);
  
  
}
