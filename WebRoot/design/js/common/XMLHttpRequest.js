window.onload =  function(){/*
	
	//1.获取a节点，并为其添加onclick响应函数
	document.getElementsByTagName("a")[0].onclick = Function(){
		//3.创建一个XMLHttpRequest对象
		var request = new XMLHttpRequest();
		//4.准备发送请求的数据：url
		var url = this.href + "?time=" + new Date();
		var method = "GET";
		//5.调用XMLHttpRequest对象的open方法
		request.open(method,url);
		request.setRequestHeader("ContentType","application/x-www-form-urlencoded");
		//6.调用XMLHttpRequest对象的send方法
		request.send(null);
		//7.为XMLHttpRequest对象添加onreadystatechange函数
		request.onreadystatechange = function() {
			//alert(request.readyState);
			if(request.status == 200 || request.status == 304){
				//10. 打印响应结果
				alert(request.response);
			}
		}
		//2.取消a节点默认行为
		return false;
	}
*/}