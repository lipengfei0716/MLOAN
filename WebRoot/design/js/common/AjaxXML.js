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
						
						//1.结果为XML格式，所以需要使用responseXML来获取
						var result = request.responseXML;
						//2.结果不能直接使用，必须先创建对应的节点，再把节点加入到#details中
						//目标格式为：
							/*	
							 	<h2>
							 		<a href="mailto:andy@clearleft.com">Andy Budd</a>
							 	</h2>
							 	<a href="http://andybudd.com/">http://andybudd.com/</a>
							 * */
						var name = result.getElementsByTagName("name")[0].firstChild.nodeValue;
						var website = result.getElementsByTagName("website")[0].firstChild.nodeValue;
						var email = result.getElementsByTagName("email")[0].firstChild.nodeValue;
						
						alert(name);
						alert(website);
						alert(email);
						
						var aNode = document.createElement("a");
						aNode.appendChild(document.createTextNode(name));
						aNode.href = "mailto" + email;
						
						var h2Node = document.createElement("h2");
						h2Node.appendChild(aNode);
						
						var aNode2 = document.createElement("a");
						aNode2.appendChild(document.createTextNode(website));
						aNode2.href = website;
						
						var detailsNode = document.getElementById("deatils");
						detailsNode.innerHTML = "";
						detailsNode.appendChild(h2Node);
						detailsNode.appendChild(aNode2);
					}
				}
			return false;
			}
		}
	}
}











