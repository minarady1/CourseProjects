# Sensing As A Service for Smart Facility Management

 Based on FHEM Home Automation Platform and FS20 sensors, we created a service layer above sensors such as temperature, door, and motion and electric switch actuators. Base on calendar reservations, the system enables electrical devices. System would send email alarms if motion detected or door opened during unscheduled room time (unauthorized access) and system would ensure lights are off during unreserved room times.
## Context
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/slides/Slide4.PNG)
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/slides/Slide5.PNG)
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/slides/Slide9.PNG)
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/slides/Slide10.PNG)
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/slides/Slide12.PNG)
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/slides/Slide13.PNG)
![alt text](https://github.com/minarady1/CourseProjects/blob/master/Smart-Facility-Managment/Physical%20Architecture.PNG)



## Getting Started

+ HTML folder has the server files which will be installed on the Apache server on RPI
  - index.php contains the main interface which shows room status and displays interactive switches for controlling the appliances.
  - js/demo.js contains the client adapter which translates client-side requests to backgroudn server-side calls to API at service.php. 
  - service.php contains the service layer which translates client side requests to back-end FHEM server API calls
  
+ fhem file: contains an export of the FHEM server configuration for different FS20 sensors and actuators used in the project
+ 99_myUtils.pm: contains PERL customizations of FHEM server including e-mail function.
+ door_status, room_status, warning_status: ctonains the different status registry files that are updated by FHEM events depending on motion sensor, door sensor and google clendar reading
+ room_display: contains the view which will be displayed at room entrance monitor to show a summary of the room status such as: next meeting scheduled on xyz titled ABC, or no upcoming room reservations today., or room currently busy bu meeting ABC


### Installing on RPI

Ensure: 
+ FHEM is installed and configured properly for sensors/actuators radio sync. 
  - Then import fhem file and 99_myUtils.pm for the configutation
+ Apache server is installed
  - copy html contents into www directory and update all IPs in the code file to the new IP of RPI



## Acknowledgments
To PERCCOM home automation lab at Lappeenranta University of Technology, Finland in Spring 2017.
