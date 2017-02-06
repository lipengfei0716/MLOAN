var jsonObject = {
		"name":"renbaokun",
		"age":25,
		"address":{"city":"BeiJing","school":"handan"},
		"teaching":function(){
			alert("JavaEE,Android...");
		}
};
alert(jsonObject.name);
alert(jsonObject.address.city);

jsonObject.teaching();

var jsonStr = "{'name':'renbaokun'}";

//把一个字符串转为JSON对象
alert(jsonStr.name);
//使用eval()方法
var testStr = "alert(''hello eval)";
alert(testStr);

//eval 可以把一个字符串转为本地的JS代码来执行
eval(testStr);
var testObject = eval("("+jsonStr+")");
alert(testObject.name);


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
						var result = request.responseText;
						var object = eval("("+result+")");
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