<?php

header('Content-Type: application/json');
 
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "health";

parse_str($_SERVER['QUERY_STRING']);

if ($method=="DFAPI"){
	$reading = file_get_contents('php://input');
	$obj = json_decode($reading);
	$intent = $obj->result->metadata->intentName;
	$userid = $_SERVER["HTTP_USER"];
	
	
	
	//file_put_contents("/var/www/html/log", print_r($_SERVER,true));
	//file_put_contents("/var/www/html/log", print_r($reading,true)."\n",FILE_APPEND);
	//file_put_contents("/var/www/html/log", "Intent:".$intent."\n",FILE_APPEND);
	InsertReading ($userid,$reading);
	if ($intent =="OpeningHours")
	{
		$ServiceMessage ="Opening hours are currently unavailable";
		//file_put_contents("/var/www/html/log", "intent opening hours for $userid \n",FILE_APPEND);
		//file_put_contents("/var/www/html/log", "1\n",FILE_APPEND);
		if ($userid == "1000")
		{
		//file_put_contents("/var/www/html/log", "2\n",FILE_APPEND);
		$ServiceMessage = GetServiceMessage("OpeningHours");
		//file_put_contents("/var/www/html/log", "2.1\n",FILE_APPEND);
		}
		else
		{
		//file_put_contents("/var/www/html/log", "3\n",FILE_APPEND);
		$ServiceMessage = GetServiceMessage("OpeningHoursElders");
		//file_put_contents("/var/www/html/log", "3.1\n",FILE_APPEND);
		}
		//file_put_contents("/var/www/html/log", "Message: $ServiceMessage",FILE_APPEND);
	
		$data = [ 
		'speech' => $ServiceMessage, 
		'displayText' => '',
		'data' =>  array('date'=>'dummy'),
		'contextOut' => array() ,
		'source' => 'Skelleftea Municipality' 
		];
		echo json_encode($data);
		
	}
	if ($intent =="DriverLicense")
	{
		$ServiceMessage ="Driving license instructions are currently unavailable";
		file_put_contents("/var/www/html/log", "intent driver license",FILE_APPEND);
		$ServiceMessage = GetServiceMessage("DriverLicense");
		file_put_contents("/var/www/html/log", "Message: ".$ServiceMessage."\n",FILE_APPEND);
		$data = [ 
		'speech' => $ServiceMessage, 
		'displayText' => '',
		'data' =>  array('date'=>'dummy'),
		'contextOut' => array() ,
		'source' => 'Skelleftea Municipality' 
		];
		echo json_encode($data);
		
	}	
	if ($intent =="ServiceInquire")
	{
		$ServiceMessage ="Driving license instructions are currently unavailable";
		file_put_contents("/var/www/html/log", "intent driver license",FILE_APPEND);
		$ServiceName = $BodyPart=$obj->result->parameters->Services;
		$ServiceMessage = GetServiceMessage($ServiceName);
		file_put_contents("/var/www/html/log", "Message: ".$ServiceMessage."\n",FILE_APPEND);
		$data = [ 
		'speech' => $ServiceMessage, 
		'displayText' => '',
		'data' =>  array('date'=>'dummy'),
		'contextOut' => array() ,
		'source' => 'Skelleftea Municipality' 
		];
		echo json_encode($data);
		
	}	
	if ($intent =="ServiceInquireEmail")
	{
		$ServiceMessage ="Email service is currently unavailable";
		file_put_contents("/var/www/html/log", "intent driver license",FILE_APPEND);
		$ServiceName = $BodyPart=$obj->result->parameters->Services;
		$ServiceEmail = GetServiceEmail($ServiceName);
		$Subject = $ServiceEmail["EmailSubject"];
		$Email = $ServiceEmail["EmailMessage"];
		$Attachment = $ServiceEmail["EmailAttachment"];
		file_put_contents("/var/www/html/log", "email: ".$Email."\n",FILE_APPEND);
		$res = exec ("perl /home/ubuntu/mail.pl '$Subject'  '$Email' '$Attachment'");
		file_put_contents("/var/www/html/log", "command: "."perl /home/ubuntu/mail.pl $'$Subject'  $$Email $'$Attachment'"."\n",FILE_APPEND);
		$data = [ 
		'speech' => "$ServiceName relevant documents have been sent to your e-mail, Is there anything else we can help you with today?", 
		'displayText' => '',
		'data' =>  array('date'=>'dummy'),
		'contextOut' => array() ,
		'source' => 'Skelleftea Municipality' 
		];
		echo json_encode($data);
		
	}
	
	if ($intent =="PensionDate")
	{
		$userid = $_SERVER["HTTP_USER"];
		$ServiceMessage ="Pension date is currently unavailable";
		file_put_contents("/var/www/html/log", "intent pension date",FILE_APPEND);
		$ServiceMessage = GetPensionDate($userid);
		$data = [ 
		'speech' => $ServiceMessage, 
		'displayText' => '',
		'data' =>  array('date'=>'dummy'),
		'contextOut' => array() ,
		'source' => 'Skelleftea Municipality' 
		];
		echo json_encode($data);
	}
	if ($intent =="BodyActivity")
	{
		$userid = $_SERVER["HTTP_USER"];
		//$ServiceMessage ="Pension date is currently unavailable";
		$BodyActivity = $obj->result->parameters->BodyActivity;
		$BodyPart=$obj->result->parameters->BodyPart;
		InsertPhysicalIssue($userid,$BodyActivity,$BodyPart);
		file_put_contents("/var/www/html/log", "intent pension date",FILE_APPEND);
		//$ServiceMessage = GetPensionDate($userid);
		
		echo json_encode($data);
	}	

}

function GetPensionDate($userid){
	file_put_contents("/var/www/html/log", "Inside Pension for $userid\n",FILE_APPEND);
	$servername = "localhost";
	$username = "root";
	$password = "root";
	$dbname = "health";
	$sql = "SELECT NextPayment,PaymentAmount FROM `user_info`where userid = '$userid'";
	file_put_contents("/var/www/html/log", $sql."\n",FILE_APPEND);
	$conn = new mysqli($servername, $username, $password, $dbname);
	$result = $conn->query($sql);
	$result_arr=array();
	if ($result->num_rows > 0) 
	{
		// output data of each row
		while($row = $result->fetch_assoc())
		{
			$ServiceMessage= "Your next pension of the amount ".$row["PaymentAmount"]. " kronas is due on ".$row["NextPayment"];
			file_put_contents("/var/www/html/log", "Message:".$ServiceMessage."\n",FILE_APPEND);
		}
	}
	return $ServiceMessage;
	//$conn->close();
}
function InsertReading($userid,$reading){
	file_put_contents("/var/www/html/log", "Inside Insert Readings for $userid\n",FILE_APPEND);
	$obj = json_decode($reading);
	$RequestName = $obj->result->metadata->intentName;
	$ServiceName = (isset ($obj->result->parameters->Services))? $obj->result->parameters->Services:"";
	$servername = "localhost";
	$username = "root";
	$password = "root";
	$dbname = "health";
	$sql = "INSERT INTO `readings`(`userid`, `reading`, `RequestName`,`ServiceName`) VALUES ('$userid','$reading','$RequestName','$ServiceName')";
	file_put_contents("/var/www/html/log", $sql."\n",FILE_APPEND);
	$conn = new mysqli($servername, $username, $password, $dbname);
	$result = $conn->query($sql);
	
	//$conn->close();
}
function InsertPhysicalIssue($userid,$BodyActivity,$BodyPart){
	file_put_contents("/var/www/html/log", "Inside PhysicalIssue for $userid\n",FILE_APPEND);
	$servername = "localhost";
	$username = "root";
	$password = "root";
	$dbname = "health";
	$sql = "INSERT INTO `physical_issues`(`userid`, `BodyActivity`, `BodyPart`) VALUES ('$userid','$BodyActivity','$BodyPart')";
	file_put_contents("/var/www/html/log", $sql."\n",FILE_APPEND);
	$conn = new mysqli($servername, $username, $password, $dbname);
	$result = $conn->query($sql);
	
	//$conn->close();
}
function GetServiceEmail($Service){
	file_put_contents("/var/www/html/log", "Inside GetServiceMessage for $Service\n",FILE_APPEND);
	$servername = "localhost";
	$username = "root";
	$password = "root";
	$dbname = "health";
	$sql = "SELECT EmailSubject,EmailMessage,EmailAttachment FROM `service_messages` WHERE Service = '$Service'";
	file_put_contents("/var/www/html/log", $sql."\n",FILE_APPEND);
	$conn = new mysqli($servername, $username, $password, $dbname);
	$result = $conn->query($sql);
	$result_arr=array();
	if ($result->num_rows > 0) 
	{
		// output data of each row
		while($row = $result->fetch_assoc())
		{
			$Email= $row;
			//file_put_contents("/var/www/html/log", "Message:".$row["ServiceMessage"]."\n",FILE_APPEND);
		}
	}
	return $Email;
	//$conn->close();
}
function GetServiceMessage($Service){
	file_put_contents("/var/www/html/log", "Inside GetServiceMessage for $Service\n",FILE_APPEND);
	$servername = "localhost";
	$username = "root";
	$password = "root";
	$dbname = "health";
	$sql = "SELECT ServiceMessage FROM `service_messages` WHERE Service = '$Service'";
	file_put_contents("/var/www/html/log", $sql."\n",FILE_APPEND);
	$conn = new mysqli($servername, $username, $password, $dbname);
	$result = $conn->query($sql);
	$result_arr=array();
	if ($result->num_rows > 0) 
	{
		// output data of each row
		while($row = $result->fetch_assoc())
		{
			$ServiceMessage= ($row["ServiceMessage"]);
			//file_put_contents("/var/www/html/log", "Message:".$row["ServiceMessage"]."\n",FILE_APPEND);
		}
	}
	return $ServiceMessage;
	//$conn->close();
}
exit;





if ($method=="search"){
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$city_query = "";
$name_query= "";
$gender_query="";
$sp_query="";
$names = explode(" ",$n);
$name_query= "(".format_like_filter($names,"name").")";


if ($c!=""){
$cities = explode(" ",$c);
$city_query=" AND (". format_like_filter($cities,"city").")";

}


if ($sp!=""){
$specialty = explode(",",$sp);
$sp_query=" AND (".format_like_filter($specialty,"specialty").")";
}

if ($g!=""){
$gender = explode(",",$g);
if (count($gender)==1){
	$gender_query=" AND (gender like '".$gender[0]."')";
}
}

// $sql = "SELECT * FROM doctors where ".$name_query. $city_query. $gender_query.$sp_query." ORDER BY RAND()";
$sql = "SELECT id,name,city,gender,image,openings FROM doctors where ".$name_query. $city_query. $gender_query.$sp_query." ORDER BY RAND()";
//echo $sql."\n";
//exit;
$result = $conn->query($sql);
$result_arr=array();
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		//echo $row["name"]."\n";
		if ($at=="on")
		{
			array_push($result_arr,$row);
		}else{
			//echo "will check open";
			//var_dump( $row);
			if ($row["openings"]!=""){
				
			$open = check_openning($d,$t,$row["openings"]);
			
			if ($open)
				array_push($result_arr,$row);
			}
			
		}
		
    }
	echo json_encode($result_arr);
} else {
    echo "0 results";
}
$conn->close();
}
if ($method=="searchall"){
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
/*
split name 
split gender
split city
split specialty 
*/
$city_query = "";
$name_query= "";
$gender_query="";
$sp_query="";
$names = explode(" ",$n);
$name_query= "(".format_like_filter($names,"name").")";


if ($c!=""){
$cities = explode(" ",$c);
$city_query=" AND (". format_like_filter($cities,"city").")";

}


if ($sp!=""){
$specialty = explode(",",$sp);
$sp_query=" AND (".format_like_filter($specialty,"specialty").")";
}

if ($g!=""){
$gender = explode(",",$g);
if (count($gender)==1){
	$gender_query=" AND (gender like '".$gender[0]."')";
}
}

// $sql = "SELECT * FROM doctors where ".$name_query. $city_query. $gender_query.$sp_query." ORDER BY RAND()";
$sql = "SELECT * FROM doctors where ".$name_query. $city_query. $gender_query.$sp_query." ORDER BY RAND()";
//echo $sql."\n";
//exit;
$result = $conn->query($sql);
$result_arr=array();
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		//echo $row["name"]."\n";
		if ($at=="on")
		{
			array_push($result_arr,$row);
		}else{
			if ($row["openings"]!=""){
			$open = check_openning($d,$t,$row["openings"]);
			
			if ($open)
				array_push($result_arr,$row);
			}
			
		}
		
    }
	echo json_encode($result_arr);
} else {
    echo "0 results";
}
$conn->close();
}




if ($method=="person"){
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
/*
split name 
split gender
split city
split specialty 
*/
$id_query = "";
$sql = "SELECT * FROM doctors where id=".$id;
//echo $sql."\n";
$result = $conn->query($sql);
$result_arr=array();
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		echo json_encode($row);
    }

}

$conn->close();
}
function check_openning($day,$time,$schedule){
	$js = json_decode($schedule,true);
	//var_dump(json_decode($schedule));
	//echo $day."\n";
	//echo $time."\n";
	//var_dump(json_decode($schedule),true);
	$open = explode(":",$js[0][$day]["open"])[0];
	$close = explode(":",$js[0][$day]["close"])[0];
	$time =  explode(":",$time)[0];
	//echo $open ."-".$close."\n";
	if ($time>= $open &&$time<=$close)
	{	//echo "open\n";
		return true;
	}
	else
	{
		//echo "close\n";
		return false;
	}
}

function format_like_filter ($array,$colname)
{
	$res=array();
	foreach ($array as $x)
	{
		//echo $x."\n";
		array_push($res , $colname." LIKE '%".$x."%' ");
	}
	$res_query = implode(" OR ",$res);
	return $res_query;
}
?>