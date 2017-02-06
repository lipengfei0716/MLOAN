window.onload =  function(){

	var aNodes = document.getElementsByTagName("a");
	for(var i = 0; i<aNodes.length; i++){
		
		aNodes[i].onclick = function() {
			
			var request = new XMLHttpRequest();
			var method  = "GET";
			var url 	= this.href;
			
			request.open(method,url);
			request.send(null);
			
			request.onreadystatechange = function() {
				
				if(request.readyState == 4){
					
					if(request.status == 200 || request.status == 304){
						
						document.getElementById("detail").innerHTML = request.responseText;
					}
				}
			return false;
			}
		}
	}
}