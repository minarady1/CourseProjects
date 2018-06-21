<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css"/>
<link rel="stylesheet" type="text/css" href="css/ui.switchbutton.min.css"/>
<link rel="stylesheet" type="text/css" href="css/demo.css"/>
		<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="js/jquery.tmpl.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
		<script type="text/javascript" src="js/jquery.switchbutton.min.js"></script>
		<script type="text/javascript" src="js/demo.js"></script>
<style>
#display {
  color: #FFFFFF;
  padding: 13px;
  font-family: helvetica;
  font-style: normal;
  font-size: 25px;
  line-height: 30px;
  word-spacing: 3px;
  text-shadow: 1px 1px 6px #0F0F0F;
}
</style>

<meta http-equiv="refresh" content="20" />
</head>

<body  bgcolor="#428EFF">

<div id="display">

<?php
$file2 = fopen("/home/room_display.txt","r");
while(! feof($file2))
  {
  echo fgets($file2). "<br />";
  }
fclose($file2);
?>
<br/>
<br/>
<br/>
<input type="checkbox" id="switch6" checked="checked" /> <span> <label for="switch6">Meeting Room Light</label></span>
<br/>
<input type="checkbox" id="switch7" checked="checked" /> <span> <label for="switch6">Meeting Room Air Conditioning	</label></span>
<br/>
<input type="checkbox" id="switch8"  /> <span> <label for="switch6">Meeting Room Door Lock	</label></span>

</div>
</body>
</html>


