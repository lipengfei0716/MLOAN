$(document).ready(function(){
	localStorage.clear();
	
	var hour = new Date().getHours();
	var h1 = document.getElementById('loginH1');
	 if(hour < 6){document.write("凌晨好! &nbsp; 请登录")} 
	else if (hour < 9){ h1.innerHTML = "早上好! 请登录"; }
	 else if (hour < 12){ h1.innerHTML = "上午好! &nbsp; 请登录"; }
	 else if (hour < 14){ h1.innerHTML = "中午好! &nbsp; 请登录"; }
	 else if (hour < 17){ h1.innerHTML = "下午好! &nbsp; 请登录"; }
	 else if (hour < 19){ h1.innerHTML = "傍晚好! &nbsp; 请登录"; } 
	else if (hour < 22){ h1.innerHTML = "晚上好! &nbsp; 请登录"; } 
	else { h1.innerHTML = "夜里好! &nbsp; 请登录"; } 
});
window.load = function(){   
	document.getElementById('username').value='';   
	document.getElementById('password').value='';   
	};  
	
function pwd(){
	document.getElementById('password').setAttribute('type','password');
}	
function checkConfirm() {
	var username = $("#username").val();
	if(username !=""){
		
	$.ajax({
		data : {'username':username},
		type : "GET",
		dataType : "json",
		url : "/MLOAN/user/checkLogged.do",
		error: function() {},
		success : function(data) {
			switch (data.msg) {
			case "1":	$("#username1").show();	$("#username9").hide();	$("#username2").hide();	break;
			case "9":	$("#username1").hide();	$("#username9").show();	$("#username2").hide();	break;
			case "2":	$("#username1").hide();	$("#username9").hide();	$("#username2").show();	break;
			default: 	$("#username1").hide();	$("#username9").hide();	$("#username2").hide();	break;
			}
		}})
	}
}
/*$(document).ready(function (){
	localStorage.clear();
})*/

/*//写cookies
function setCookie(name, value) {
	var Days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();
}
// 读取cookies
function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}
// 删除cookies
function delCookie(name) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval = getCookie(name);
	if (cval != null)
		document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}
// 使用示例
setCookie("name", "hayden");
alert(getCookie("name"));
// 如果需要设定自定义过期时间
// 那么把上面的setCookie 函数换成下面两个函数就ok;
// 程序代码
function setCookie(name, value, time) {
	var strsec = getsec(time);
	var exp = new Date();
	exp.setTime(exp.getTime() + strsec * 1);
	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();
}
function getsec(str) {
	alert(str);
	var str1 = str.substring(1, str.length) * 1;
	var str2 = str.substring(0, 1);
	if (str2 == "s") {
		return str1 * 1000;
	} else if (str2 == "h") {
		return str1 * 60 * 60 * 1000;
	} else if (str2 == "d") {
		return str1 * 24 * 60 * 60 * 1000;
	}
}
// 这是有设定过期时间的使用示例：
// s20是代表20秒
// h是指小时，如12小时则是：h12
// d是天数，30天则：d30
setCookie("name", "hayden", "s20");*/