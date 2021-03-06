/*
Main controller code for Arduino. 
Documentaion near end is not perfect but I hope its comprehensible. This code was done for a hackathon so it is not completely efficient but at least it does the job as seen in the live demo recording
By Mina Rady
*/


#include < cactus_io_AM2302.h > #include < Servo.h >

  #include < SPI.h > // needed for Arduino versions later than 0018
  #include < Ethernet2.h > #include < EthernetUdp2.h > // UDP library from: bjoern@cs.stanford.edu 12/30/2008

  #define AM2302_PIN 2


//Initializing pin connections
AM2302 dht(AM2302_PIN);
int servoPin = 9;

int RED = 5;
int BLUE = 7;
int YELLOW = 6;

bool WebManualControl = 0;
bool FireAlarm = 0;
bool TempAlarm = 0;
bool LeakAlarm = 0;
bool JoyStickManualControl = 0;
int DelayValue, WebDelayValue;

Servo servo;

int servoAngle = 0; // servo position in degrees
int RotorSpeed, MoistureSensor, FlameSensor, JoyStickX;
int iWebRSpeed = 0;
//Communication Interface Ethernet
byte mac[] = {
  0x90,
  0xA2,
  0xDA,
  0x10,
  0xBF,
  0xB1
};
// Initializing Remote IP Address
IPAddress ip(169, 254, 91, 116);
unsigned int localPort = 8888;
char ReplyBuffer[] = "acknowledged";
//EthernetUDP Udp;
EthernetServer server(80);
void setup() {
  // start the Ethernet and UDP:
  Ethernet.begin(mac, ip);
  //Udp.begin(localPort);

  Serial.begin(9600);
  servo.attach(servoPin);
  pinMode(A0, INPUT); // Servo Motor
  pinMode(A1, INPUT); // Flame Sensor on A1
  pinMode(A2, INPUT); // Moisture Sensor on A2
  pinMode(A3, INPUT); // JoyStick Sensor on A3
  pinMode(2, OUTPUT); // Temperature & Humidity Sensor
  // pinMode(A4,INPUT);// Temp Sensor on A4
  dht.begin();

}

// the main execution cycle
void loop() {
	// GET JoyStick position
    JoyStickX = analogRead(A3);
	
    // if Joystick is in manual mode
    if (JoyStickX != 517) {
      JoyStickManualControl = 1;
     
    } else { //else proceed with automatic mode
 
      JoyStickManualControl = 0;
      WebDelayValue = ProcessWebService();
      //This is an incomplete part, it is meant to control the servo from the web interface through the WebDelayValue coming from the client HTTP request
      if (WebManualControl) {
        DelayValue = WebDelayValue; //assign servo speed from Web value
      } else {
        DelayValue = ComputeServoDelay(); //define servo speed according to this procedure (which checks Joystick status and different alarms)
      }
    }
	
	// Rotate Servo for 180 degrees

    for (servoAngle = 0; servoAngle <= 180; servoAngle++) //move the micro servo from 0 degrees to 180 degrees
    {
	    // Get Sensor Readings from this procedure
      UpdateSensors();
	    //Follow same process as above (redundant but can be optimized in further releases) 
      WebDelayValue = ProcessWebService();
      if (JoyStickX != 517) {
        JoyStickManualControl = 1;
        WebManualControl = 0;

        DelayValue = MapJoyStickToServo(JoyStickX);
      } else {ol
        JoyStickManualControl = 0;
        DelayValue = ComputeServoDelay();
      }
      if (DelayValue != 0) {

        delay(DelayValue);
        servo.write(servoAngle);

      } else {
        //        Serial.println("Milestone 8 ");
        // servoAngle--;
      }
      JoyStickX = analogRead(A3);
    }
`	// Rotate Servo another 180 degrees with the same procedures as above

    for (servoAngle = 180; servoAngle > 0; servoAngle--) //now move back the micro servo from 0 degrees to 180 degrees
    {
      UpdateSensors();
      WebDelayValue = ProcessWebService();
      if (JoyStickX != 517) {
        JoyStickManualControl = 1;
        WebManualControl = 0;

        DelayValue = MapJoyStickToServo(JoyStickX);
      } else {
        JoyStickManualControl = 0;
        DelayValue = ComputeServoDelay();
      }
      if (DelayValue != 0) {
        delay(DelayValue);
        servo.write(servoAngle);

      } else {

      }
      JoyStickX = analogRead(A3);
    }

  }
  // Update sensor sample
void UpdateSensors() {
    RotorSpeed = analogRead(A0);
    FlameSensor = analogRead(A1);
    MoistureSensor = analogRead(A2);
    JoyStickX = analogRead(A3);
  }
  // Compute Final Servo speed based on all input conditions
int ComputeServoDelay() {
  UpdateSensors();
  dht.computeHeatIndex_C();
  int ComputedDelayValue;
  dht.readHumidity();
  dht.readTemperature();


  JoyStickManualControl = 0;
	// If any of the alarming sensors are in positive reading, set its flag and turn on red led
  if (FlameSensor > 550 || dht.computeHeatIndex_C() > 28.5 || MoistureSensor > 300) {
    if (FlameSensor > 550) {
      digitalWrite(RED, HIGH);

      FireAlarm = 1;
      Serial.println("FireAlarm ON");
    } else {
      digitalWrite(RED, LOW);
      FireAlarm = 0;
      Serial.println("FireAlarm Off");
    }

    if (dht.computeHeatIndex_C() > 28.5) {

      digitalWrite(YELLOW, HIGH);
      TempAlarm = 1;
      //delay(1000);
    } else {
      digitalWrite(YELLOW, LOW);
      TempAlarm = 0;
    }
    if (MoistureSensor > 300) {
      //        digitalWrite(RED, LOW);
      digitalWrite(BLUE, HIGH);
      //        digitalWrite(YELLOW, LOW);
      //          Serial.println("LeakAlarm");
      // delay(1000);
      LeakAlarm = 1;
    } else {
      LeakAlarm = 0;
      digitalWrite(BLUE, LOW);
    }
	// Stop the servo motor
    return 0;
  } else {
    digitalWrite(RED, LOW);
    digitalWrite(BLUE, LOW);
    digitalWrite(YELLOW, LOW);
    FireAlarm = 0;
    TempAlarm = 0;
    LeakAlarm = 0;
    //     otherwise move with normal speed
    return 60;

  }

  //  else
  //  {
  //    JoyStickManualControl=1;
  //    ComputedDelayValue = MapJoyStickToServo(JoyStickX);
  //    Serial.print("Returning from Compute with:");Serial.print(ComputedDelayValue);
  //    return ComputedDelayValue;
  //  }

}

// Map JoyStick value to Servo Value
int MapJoyStickToServo(int XValue) {
    //Serial.println(XValue , DEC);
    if (XValue == 517) {
      return 60;
    }
    if (XValue < 100) {
      return 10;
    }
    return (XValue * 0.1);
  }
  //Respond to Client
int ProcessWebService() {

  // if there's data available, read a packet
  iWebRSpeed = 0;
  char cWebRSpeed[4];
  //bool ReceivedControl;
  //  Serial.println("Milestone W-1");
  EthernetClient client = server.available();
  if (client) {
    //    Serial.println("new client");
    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    //    Serial.write("BeginHTTP");

    while (client.connected()) {
      if (client.available()) {
        char c = client.read();



        if (c == '?') {
 
          cWebRSpeed[0] = client.read();
          cWebRSpeed[1] = client.read();
          cWebRSpeed[2] = client.read();
          cWebRSpeed[3] = '\0';
          iWebRSpeed = atoi(cWebRSpeed);
          if (iWebRSpeed == -1) {
            WebManualControl = 0;
          } else {
            WebManualControl = 1;
          }
        }
        // if you've gotten to the end of the line (received a newline
        // character) and the line is blank, the http request has ended,
        // so you can send a reply
        if (c == '\n' && currentLineIsBlank) {
          // send a standard http response header
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/xml");
          client.println("Connection: close"); // the connection will be closed after completion of the response
          client.println();
          RotorSpeed = analogRead(A0);
          FlameSensor = analogRead(A1);
          MoistureSensor = analogRead(A2);
          JoyStickX = analogRead(A3);
          client.print("<output>");
          client.print("<sensor>");
          client.print("<name>RotorSpeed");
          client.print("</name>");
          client.print("<value>");
          client.print(RotorSpeed);
          client.print("</value>");
          client.print("    </sensor>");
          client.print("<sensor>");
          client.print("<name>FlameSensor");
          client.print("</name>");
          client.print("<value>");
          client.print(FlameSensor);
          client.print("</value>");
          client.print("    </sensor>");
          client.print("    <sensor>");
          client.print("        <name>LeakSensor");
          client.print("</name>");
          client.print("        <value>");
          client.print(MoistureSensor);
          client.print("</value>");
          client.print("    </sensor>");

          client.print("    <sensor>");
          client.print("        <name>HeatIndex");
          client.print("</name>");
          client.print("        <value>");
          client.print(dht.computeHeatIndex_C());
          client.print("</value>");
          client.print("    </sensor>");

          client.print("    <sensor>");
          client.print("        <name>TempSensor");
          client.print("</name>");
          client.print("        <value>");
          client.print(dht.temperature_C);
          client.print("</value>");
          client.print("    </sensor>");

          client.print("    <sensor>");
          client.print("        <name>HumiditySensor");
          client.print("</name>");
          client.print("        <value>");
          client.print(dht.humidity);
          client.print("</value>");
          client.print("    </sensor>");

          client.print("    <sensor>");
          client.print("        <name>JoystickSensor");
          client.print("</name>");
          client.print("        <value>");
          client.print(JoyStickX);
          client.print("</value>");
          client.print("    <sensor>");
          client.print("    <Alarms>");
          client.print("    <Alarm>");
          client.print("        <name>FireAlarm");
          client.print("</name>");
          client.print("        <value>");
          client.print(FireAlarm);
          client.print("</value>");
          client.print("    <Alarm>");

          client.print("    <Alarm>");
          client.print("        <name>TempAlarm");
          client.print("</name>");
          client.print("        <value>");
          client.print(TempAlarm);
          client.print("</value>");
          client.print("    <Alarm>");

          client.print("    <Alarm>");
          client.print("        <name>LeakAlarm");
          client.print("</name>");
          client.print("        <value>");
          client.print(LeakAlarm);
          client.print("</value>");
          client.print("    <Alarm>");

          client.print("    </Alarms>");
          client.print("</output>");
          Serial.println("Milestone W-6");
          break;
        }
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
        } else if (c != '\r') {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    //     Serial.write("EndHTTP");
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    //    Serial.println("client disconnected");
  }
  //Serial.println("Milestone W-7");
  return iWebRSpeed;
}
