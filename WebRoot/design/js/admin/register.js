

function YanZheng(){
	var psw=$("#password").val();
	var psw2=$("#password2").val();
	if(psw2 != psw)
	{
	$("#span4").show();
	}else{
		$("#span4").hide();
	}

}
function pass(){
	var psw=$("#password").val();
	console.log(psw.length)
	if(psw==""){
		$("#span2").hide();
	}
	else if(psw.length <6 || false ==password(psw))
	{
		$("#span2").show();
	}else if(psw.length ==6 & true== password(psw))
	{
		$("#span2").hide();
		
	}	
	
}



function checkConfirm() {
	var username = $("#usernausermail").val();
	console.log(username)
	if(username == "" ){
		$("#nausermailfont1").hide();
		$("#nausermailfont").hide();
		$("#span").hide();
	}
	else if(username !="" &  true == check(username) )
	{
		var tl=check(username);
		console.log("第二章查询了数;")
		console.log(tl);
		$("#span").hide()
	$.ajax({
		data : {'username':username},
		type : "GET",
		dataType : "json",
		url : "/MLOAN/user/checking.do",
		error: function() {
			alert("服务器异常，请联系管理员！！！");
		},
		success : function(data1) {
			console.info(data1)
			if(data1.msg == "1"){
				$("#nausermailfont1").hide();
				$("#nausermailfont").show();
				$("#span").hide();
			}else{
				$("#nausermailfont1").show();
				$("#nausermailfont").hide();
				$("#span").hide();
			}
				
		}})
	}else if(username !="" & false ==check(username)){
		
		$("#span").show();
		$("#nausermailfont1").hide();
		$("#nausermailfont").hide();
		console.log("8888")
	}
	
	
	
}

//正则;用户的
function check(str){
     var t=/(?!^\d+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]{3,15}/.test(str);
     return t;
    }

function password(str){
	return /^[0-9a-zA-Z]{6}$/.test(str);
}


function mailverify() {
	var usermail = $("#usermail").val();
	if(usermail==""){
		$("#mailfont1").hide();
		$("#mailfont").hide();
		$("#span3").hide();
	}else if(usermail !="" &  true ==isEmail(usermail)) {
		console.log("正确请球")
		console.log(usermail);
		console.log(isEmail(usermail))
	$.ajax({
		data : {'usermail':usermail},
		type : "GET",
		dataType : "json",
		url : "/MLOAN/user/checking.do",
		error: function() {
			alert("服务器异常，请联系管理员！！！");
		},
		success : function(data) {
			console.log(data.msg)
			if(data.msg=="2"){
				$("#mailfont").hide();
				$("#mailfont1").show();
				$("#span3").hide();
			}else{
				$("#mailfont").show();
				$("#mailfont1").hide();
				$("#span3").hide();
			}
		}})
	}
}


//检查email邮箱
function isEmail(str){
 if(str.indexOf("@") > 0) 
 { 
 return true;
 } else {
 return false;
 }
}