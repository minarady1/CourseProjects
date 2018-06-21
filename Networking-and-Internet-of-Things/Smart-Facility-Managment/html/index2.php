<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css"/>
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

<meta http-equiv="refresh" content="5" />
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
</div>


</body>
</html>