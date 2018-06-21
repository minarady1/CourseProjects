<?php
 //step1
parse_str($_SERVER['QUERY_STRING']);
$method;
$device;
$state;
$cmd;

switch ($method)
{
	case "GetDeviceStatus": 
		$cmd= "jsonlist $device state";
		
		$result=fhem ($cmd);
		$data = json_decode($result,true);
		echo $data ["Results"][0]["Readings"]["state"]["Value"];
		break;
	case "SetDeviceStatus": 
		$cmd= "set $device $state";
		echo fhem ($cmd);
		break;
	case "GetDevices": 
		$cmd= "jsonlist lamp2||fan2||lock";
		echo fhem ($cmd);
		break;
	
	case "GetRoomStatus": 
		$file2 = fopen("/home/room_display.txt","r");
		while(! feof($file2))
		  {
		  echo json_encode(fgets($file2));
		  }
		fclose($file2);
		break;
	default:
		echo "Invalid Selection";
		break;
	
}

function fhem ($cmd)
{

	$cSession = curl_init(); 
	curl_setopt($cSession, CURLOPT_CONNECTTIMEOUT ,0); 
	curl_setopt($cSession, CURLOPT_TIMEOUT, 400); //timeout in seconds
	//step2
	curl_setopt($cSession,CURLOPT_URL,"http://172.16.16.129:8083/fhem");
	curl_setopt($cSession,CURLOPT_RETURNTRANSFER,true);
	curl_setopt($cSession,CURLOPT_HEADER, false); 
	curl_setopt($cSession, CURLOPT_POSTFIELDS, "cmd=".$cmd."&XHR=1");

	//step3
	$result=curl_exec($cSession);
	//step4

	curl_close($cSession);
	//step5
	return $result;
}

?>
