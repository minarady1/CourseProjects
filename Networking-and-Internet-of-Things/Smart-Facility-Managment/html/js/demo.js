

/*{
  "Arg":"lamp2 state",
  "Results": [
  {
    "Name":"lamp2",
    "Readings": {      "state": { "Value":"off", "Time":"2017-04-26 11:07:30" }    }
  }  ],
  "totalResultsReturned":1
}
*/
$( document ).ready(function() {
    console.log( "ready!" );
	
	$.ajax({url: "http://172.16.16.129/service.php?method=GetDeviceStatus&device=lamp2", success: function(result2){
				//var obj = jQuery.parseJSON( result2 );
				//console.log( obj );
				//console.log( obj.Results [0].Readings.state.Value );
				//var state = obj.Results [0].Readings.state.Value ;
				var state = result2;
				console.log ("lamp2");
				console.log (state);
				$("#switch6").prop("checked", (state =="on") ? true:false).change();
				$("#switch6")
					.switchbutton({
						checkedLabel: 'ON',
						uncheckedLabel: 'OFF'
					})
					.change(function(){
						//alert("Switch 6 changed to " + ($(this).prop("checked") ? "checked" : "unchecked"));
						$.ajax({url: "http://172.16.16.129/service.php?method=SetDeviceStatus&device=lamp2&state="+($(this).prop("checked") ? "on" : "off"), success: function(result){
							
						}});
					});				
			}});
			
		$.ajax({url: "http://172.16.16.129/service.php?method=GetDeviceStatus&device=fan2", success: function(result2){
				//var obj = jQuery.parseJSON( result2 );
				//console.log( obj );
				//console.log( obj.Results [0].Readings.state.Value );
				//var state = obj.Results [0].Readings.state.Value ;
				var state = result2;
				console.log ("fan2");
				console.log (state);				
				$("#switch7").prop("checked", (state =="on") ? true:false).change();
					
				$("#switch7").switchbutton({
						checkedLabel: 'ON',
						uncheckedLabel: 'OFF'
					})
					.change(function(){
						//alert("Switch 6 changed to " + ($(this).prop("checked") ? "checked" : "unchecked"));
						$.ajax({url: "http://172.16.16.129/service.php?method=SetDeviceStatus&device=fan2&state="+($(this).prop("checked") ? "on" : "off"), success: function(result){
							
						}});
					});
			}});
		$.ajax({url: "http://172.16.16.129/service.php?method=GetDeviceStatus&device=lock", success: function(result2){
				//var obj = jQuery.parseJSON( result2 );
				//console.log( obj );
				//console.log( obj.Results [0].Readings.state.Value );
				//var state = obj.Results [0].Readings.state.Value ;
				var state = result2;
				console.log ("fan2");
				console.log (state);				
				$("#switch8").prop("checked", (state =="on") ? true:false).change();
					
				$("#switch8").switchbutton({
						checkedLabel: 'OPEN',
						uncheckedLabel: 'CLOSED'
					})
					.change(function(){
						//alert("Switch 6 changed to " + ($(this).prop("checked") ? "checked" : "unchecked"));
						//$.ajax({url: "http://172.16.16.129/service.php?method=SetDeviceStatus&device=fan2&state="+($(this).prop("checked") ? "on" : "off"), success: function(result){
							
						//}});
					});
			}});
		
		/* $.ajax({url: "http://172.16.16.129/service.php", success: function(result2){
				var obj = jQuery.parseJSON( result2 );
				console.log( obj );
				console.log( obj.Results [0].Readings.state.Value );
				var state = obj.Results [0].Readings.state.Value ;
				
				
			}}); */	
$(function(){ 



	
});			
});