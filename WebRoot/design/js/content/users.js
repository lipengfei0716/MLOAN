$(document).ready(function(){
	$.ajax({
		type : "GET",
		dataType : "json",			//  /ajax/map     index/chart
		url : "/MLOAN/data/usrsinfo.do",
		error: function() {
			alert("error!---服务器返回失败");		//服务器返回失败调用的方法
		},
		success : function(data) {
			var args = "<table><tr class='title'><td>用户名/密码</td><td>指标文件</td><td>指标项</td></tr>";
			var users = data.users;
			var userdata = data.userdata;
			for(var k=0;k<users.length;k++){
				var username = users[k].username;
				var password = users[k].password;
				var j = 0;
				var info1 = "";
				var info2 = "";
				for(var i=0;i<userdata.length;i++){
					var name = userdata[i].username;
					if(name!=username)
						continue;
					var list = userdata[i].list;
					var listname = userdata[i].listname;
					if(j==0){
						info1 += "<td style='padding-right:15'>"+listname+"</td><td style='padding-right:15'>"+list+"</td>";
					}else{
						info2 += "<tr><td style='padding-right:15'>"+listname+"</td><td style='padding-right:15'>"+list+"</td></tr>";
					}
					j++;}
				args += "<tr><td rowspan='"+j+"' style='padding-right:15'>用户名："+username+"  <br> 密&nbsp;&nbsp;&nbsp;码："+password+" </td>";   
				if(info1=="")
					info1="<td style='padding-right:15'>无指标文件</td><td style='padding-right:15'>无指标项</td>";
				args += info1;
				args += info2;
				args += "</tr>";
			}
			args += "</table>";
			$('#right_info2').append(args);
			
			args = "<table><tr class='title'><td>用户名/密码</td><td>指标文件</td><td>指标项</td></tr>";
			for(var k=0;k<users.length;k++){
				var username = users[k].username;
				if(username!=$("#username").text())
					continue;
				var password = users[k].password;
				var j = 0;
				var info1 = "";
				var info2 = "";
				for(var i=0;i<userdata.length;i++){
					var name = userdata[i].username;
					if(name!=username)
						continue;
					var list = userdata[i].list;
					var listname = userdata[i].listname;
					if(j==0){
						info1 += "<td style='padding-right:15'>"+listname+"</td><td style='padding-right:15'>"+list+"</td>";
					}else{
						info2 += "<tr><td style='padding-right:15'>"+listname+"</td><td style='padding-right:15'>"+list+"</td></tr>";
					}j++;}
				args += "<tr><td rowspan='"+j+"' style='padding-right:15'>用户名："+username+"  <br> 密&nbsp;&nbsp;&nbsp;码："+password+" </td>";   
				if(info1=="")
					info1="<td style='padding-right:15'>无指标文件</td><td style='padding-right:15'>无指标项</td>";
				args += info1;
				args += info2;
				args += "</tr>";}
			$('#right_info3').append(args);
}})});
$('#label_Left1').click(function(){
	var index = $(this).index();
	$('.users_left dl dd >a').css("background","#f8f8f8");
	$('.users_left dl dd >a:eq('+index+')').css("background","#c0c0c0");
	$('.users_right >div').css("display","none")
	$('.users_right >div:eq('+index+')').css("display","");})
$('#label_Left2').click(function(){
	var index = $(this).index();
	$('.users_left dl dd >a').css("background","#f8f8f8");
	$('.users_left dl dd >a:eq('+index+')').css("background","#c0c0c0");
	$('.users_right >div').css("display","none")
	$('.users_right >div:eq('+index+')').css("display","");})
$('#label_Left3').click(function(){
	var index = $(this).index();
	$('.users_left dl dd >a').css("background","#f8f8f8");
	$('.users_left dl dd >a:eq('+index+')').css("background","#c0c0c0");
	$('.users_right >div').css("display","none")
	$('.users_right >div:eq('+index+')').css("display","");})