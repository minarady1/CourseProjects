/*
    ------------ [GP_02] - CO2 ------------

    Explanation: This is the basic code to manage and read the carbon dioxide
    (CO2) gass sensor. The concentration and the enviromental variables will be
    stored in a frame. Cycle time: 5 minutes

    Copyright (C) 2015 Libelium Comunicaciones Distribuidas S.L.
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

    Version:           0.2
    Design:            David Gascón
    Implementation:    Alejandro Gállego
*/

#include <WaspSensorGas_Pro.h>
#include <WaspFrame.h>
#include <WaspUART.h>
#include <WaspLoRaWAN.h>

/*
   Define object for sensor: CO2
   Input to choose board socket.
   Waspmote OEM. Possibilities for this sensor:
  	- SOCKET_1
   P&S! Possibilities for this sensor:
  	- SOCKET_A
  	- SOCKET_B
  	- SOCKET_C
  	- SOCKET_F
*/

// define radio settings
//////////////////////////////////////////////

uint8_t PORT = 3;
uint8_t power = 15;
uint32_t frequency = 863750000;
char spreading_factor[] = "sf12";
char coding_rate[] = "4/8";
uint16_t bandwidth = 125;
char crc_mode[] = "on";
// will fix packet size 
// will use 3 freq
//total mins for 10 mins = 100 320 
//total
//19 pwr levels 
uint8_t pwr_min = -3;
uint8_t pwr_max = 15;
//default value is closest to plain transmission
//11 channels this frequencey rqnge will be used for 11 channels of 500 khz bw each
uint32_t freq_min= 863750000;
uint32_t freq_max= 869250000;
//6 SFs
char SF_a[6] [5] = {"sf7","sf8","sf9","sf10","sf11","sf12"};
//payload max 255 bytes
//4 CRs
char cr_a[4] [4] = {"4/5","4/6","4/7","4/8"};
uint16_t bw_a[3] = {125,250,500};
//2 CRC modes
char crc_a [2][4]= {"on","off"};
char logfile [] = "LOGFREQ1.TXT";

uint8_t socket = SOCKET0;


uint8_t error;
// Encryption Parameters

void setup()
{
  SD.ON();
  SD.ls();  
  //SD.del(logfile);
  SD.create(logfile);
  SD.ls();  
  //SD.ls(LS_R); 
  Log("Setup Led Off");
}


void loop()
{
  unsigned long now = RTC.getEpochTime();
  unsigned long en = now +18000; //5 hours
  Log("Min Frequency Tx");
  uint8_t TxCounter = 0;//counters not used now
  uint8_t TxLimit = 300; 
  char buf [100];
  float bat ;
  /*  
  frequency = freq_min; 
  ConfigTx();
  
  while (now<en)
  {
    Transmit();
    now = RTC.getEpochTime();
    bat = PWR.getBatteryVolts();
    dtostrf(bat,1,10,buf);
    Log(buf);
    delay(1000);//leave 3 secs for transmission
  }*/
  Log("Max Frequency Tx");
  now = RTC.getEpochTime();
  en = now +18000; //5 hours
  
  frequency = freq_max;
  ConfigTx();
  while (now<en)
  {
    Transmit();
    now = RTC.getEpochTime();
    bat = PWR.getBatteryVolts();
    dtostrf(bat,1,10,buf);
    Log(buf);
    delay(1000);//leave 3 msecs for transmission
  }
}
uint8_t ConfigTx()
{
  error = radioModuleSetup();
  return error;
}
void Transmit()
{
  //USB.println(" :Preprocessing Ceomplete, Send Data Prepare");
  char data []="AAAAA";
  USB.print(RTC.getEpochTime());
  error = LoRaWAN.sendRadio(data);
  
  if (error == 0)
  {
   // USB.print(RTC.getEpochTime());
    //USB.println(" :Send Complete, Will Blink");
    //USB.println(F("--> Packet sent OK"));

    //USB.print(RTC.getEpochTime());
    //USB.println(" :Blink Complete");
  } else
  {
    
    //USB.printf("Error %d\n", error );
  }
}
void bp(char val) {
  for (int i = 7; 0 <= i; i--) {
    USB.printf("%c", (val & (1 << i)) ? '1' : '0');
  }
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
uint8_t radioModuleSetup()
{

  uint8_t status = 0;
  uint8_t e = 0;

  //////////////////////////////////////////////
  // 1. switch on
  //////////////////////////////////////////////

  e = LoRaWAN.ON(socket);

  // Check status
  if (e == 0)
  {
    USB.println(F("1. Switch ON OK"));
  }
  else
  {
    USB.print(F("1. Switch ON error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));


  //////////////////////////////////////////////
  // 2. Enable P2P mode
  //////////////////////////////////////////////

  e = LoRaWAN.macPause();

  // Check status
  if (e == 0)
  {
    USB.println(F("2. P2P mode enabled OK"));
  }
  else
  {
    USB.print(F("2. Enable P2P mode error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));



  //////////////////////////////////////////////
  // 3. Set/Get Radio Power
  //////////////////////////////////////////////

  // Set power
  e = LoRaWAN.setRadioPower(power);

  // Check status
  if (e == 0)
  {
    USB.println(F("3.1. Set Radio Power OK"));
  }
  else
  {
    USB.print(F("3.1. Set Radio Power error = "));
    USB.println(e, DEC);
    status = 1;
  }

  // Get power
  e = LoRaWAN.getRadioPower();

  // Check status
  if (e == 0)
  {
    USB.print(F("3.2. Get Radio Power OK. "));
    USB.print(F("Power: "));
    USB.println(LoRaWAN._radioPower);
  }
  else
  {
    USB.print(F("3.2. Get Radio Power error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));



  //////////////////////////////////////////////
  // 4. Set/Get Radio Frequency
  //////////////////////////////////////////////

  // Set frequency
  e = LoRaWAN.setRadioFreq(frequency);

  // Check status
  if (e == 0)
  {
    USB.println(F("4.1. Set Radio Frequency OK"));
  }
  else
  {
    USB.print(F("4.1. Set Radio Frequency error = "));
    USB.println(e, DEC);
    status = 1;
  }

  // Get frequency
  e = LoRaWAN.getRadioFreq();

  // Check status
  if (e == 0)
  {
    USB.print(F("4.2. Get Radio Frequency OK. "));
    USB.print(F("Frequency: "));
    USB.println(LoRaWAN._radioFreq);
  }
  else
  {
    USB.print(F("4.2. Get Radio Frequency error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));



  //////////////////////////////////////////////
  // 5. Set/Get Radio Spreading Factor (SF)
  //////////////////////////////////////////////

  // Set SF
  e = LoRaWAN.setRadioSF(spreading_factor);

  // Check status
  if (e == 0)
  {
    USB.println(F("5.1. Set Radio SF OK"));
  }
  else
  {
    USB.print(F("5.1. Set Radio SF error = "));
    USB.println(e, DEC);
    status = 1;
  }

  // Get SF
  e = LoRaWAN.getRadioSF();

  // Check status
  if (e == 0)
  {
    USB.print(F("5.2. Get Radio SF OK. "));
    USB.print(F("Spreading Factor: "));
    USB.println(LoRaWAN._radioSF);
  }
  else
  {
    USB.print(F("5.2. Get Radio SF error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));



  //////////////////////////////////////////////
  // 6. Set/Get Radio Coding Rate (CR)
  //////////////////////////////////////////////

  // Set CR
  e = LoRaWAN.setRadioCR(coding_rate);

  // Check status
  if (e == 0)
  {
    USB.println(F("6.1. Set Radio CR OK"));
  }
  else
  {
    USB.print(F("6.1. Set Radio CR error = "));
    USB.println(e, DEC);
    status = 1;
  }

  // Get CR
  e = LoRaWAN.getRadioCR();

  // Check status
  if (e == 0)
  {
    USB.print(F("6.2. Get Radio CR OK. "));
    USB.print(F("Coding Rate: "));
    USB.println(LoRaWAN._radioCR);
  }
  else
  {
    USB.print(F("6.2. Get Radio CR error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));



  //////////////////////////////////////////////
  // 7. Set/Get Radio Bandwidth (BW)
  //////////////////////////////////////////////

  // Set BW
  e = LoRaWAN.setRadioBW(bandwidth);

  // Check status
  if (e == 0)
  {
    USB.println(F("7.1. Set Radio BW OK"));
  }
  else
  {
    USB.print(F("7.1. Set Radio BW error = "));
    USB.println(e, DEC);
  }

  // Get BW
  e = LoRaWAN.getRadioBW();

  // Check status
  if (e == 0)
  {
    USB.print(F("7.2. Get Radio BW OK. "));
    USB.print(F("Bandwidth: "));
    USB.println(LoRaWAN._radioBW);
  }
  else
  {
    USB.print(F("7.2. Get Radio BW error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));



  //////////////////////////////////////////////
  // 8. Set/Get Radio CRC mode
  //////////////////////////////////////////////

  // Set CRC
  e = LoRaWAN.setRadioCRC(crc_mode);

  // Check status
  if (e == 0)
  {
    USB.println(F("8.1. Set Radio CRC mode OK"));
  }
  else
  {
    USB.print(F("8.1. Set Radio CRC mode error = "));
    USB.println(e, DEC);
    status = 1;
  }

  // Get CRC
  e = LoRaWAN.getRadioCRC();

  // Check status
  if (e == 0)
  {
    USB.print(F("8.2. Get Radio CRC mode OK. "));
    USB.print(F("CRC status: "));
    USB.println(LoRaWAN._crcStatus);
  }
  else
  {
    USB.print(F("8.2. Get Radio CRC mode error = "));
    USB.println(e, DEC);
    status = 1;
  }
  USB.println(F("-------------------------------------------------------"));


  return status;
}
