$(document).ready(function(){
	$("#thisMap").click(function(){
		$.ajax( {
			type : "POST",
			url : "ajax/map",
			error: function() {
				//服务器返回失败调用的方法
				alert("error!---说明服务器返回失败");
			},
			dataType : "json",
			success : function(data) {
				var thisMapValueStr = "";
				for(var i=0;i<data.length;i=i+1) {
					var caseMap = data[i]; //获取Map
				    for(var i=0;i<10;i=i+1){
				    	//注意这里用了 eval方法,js会将里面的字符串当做变量来处理
				    	thisMapValueStr ="<tr><td>" + eval("caseMap.test" + i) + "</tr></td>";
						$("#resultTable").append(thisMapValueStr);
						thisListValueStr = "";
				    } 
				}
				
			}
		});
	});
	
	$("#thisList").click(function(){
		$.ajax( {
			type : "POST",
			url : "ajax/list",
			error: function() {
				//服务器返回失败调用的方法
				alert("error!---说明服务器返回失败");
			},
			dataType : "json",
			success : function(data) {
				var thisListValueStr = "";
				for(var i=0;i<data.length;i=i+1) {
					//解释一下data[i][i],第一个[i]是获得JSONArray中的第i个值,
					//第二个[i]是JSONArray中的第i个值中的第i个值。
					//如本方法caseList[0],是data中第一个list中的第一个值
					var caseList = data[i]; //获取Map
				    for(var i=0;i<caseList.length;i=i+1){ 
				    	thisListValueStr ="<tr><td>" + caseList[i] + "</tr></td>";
						$("#resultTable").append(thisListValueStr);
						thisListValueStr = "";
				    } 
				}
			}
		});
	});
	
});