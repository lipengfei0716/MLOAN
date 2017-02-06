<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>用户注册</title>
      <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/demo.css" />
      <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/style.css" />
	  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/animate-custom.css" /> 
	  <script src="<%=request.getContextPath()%>/design/js/off/jquery-1.6.2.min.js"></script>	   
	  <script src="<%=request.getContextPath()%>/design/js/admin/register.js"></script>	
<style type="text/css">
body
{
 margin:0px;
 padding:0px;
 font-family:Arial;
 font-size:12px;
 padding:10px;
}
#myemail, .newemail, .newemailtitle{ 
 cursor:default;
 line-height:18px;
}
input,img{vertical-align:middle;}
</style>
</head>
</head>
<body>
	<div class="container">
    	<div id="container_demo" >
	        <div id="wrapper">
            	<div id="login" class="animate form" style="padding-bottom: 0px;">
	            	<form  action="<%=request.getContextPath()%>/user/register.do"  method="post"  > 
	                    <h1 style="font-size: 30px;">注册信息</h1>
	                    <p id="P1"> 
	                        <label for="usernausermail" class="unausermail" data-icon="u"  >
	                        	<b style="color: red;">*</b>用户名   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font id="nausermailfont" color="red"  hidden="hidden">该用户已被注册</font><font id="nausermailfont1" color=" #1a1b1b" hidden="hidden">此账号可以注册</font>
	                        </label>
	                        <input id="usernausermail" name="username" placeholder="请设置用户名...支持  字母 开头、字母数字的组合，4-12个字符" maxlength="12" required  type="text" ONBLUR="checkConfirm()" value=""/>
	                        <span id="span" style="color:red;" hidden="hidden">必须是 不含中文的,  字母  数字  符号开头 或 字母数字 特殊符合 的组合，4-12个字符</span>
	                    </p>
	                    <p> 
	                        <label for="usermail" class="usermail" data-icon="u" >
	                        	<b style="color: red">*</b> 绑定邮箱 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font id="mailfont" color="red" hidden="hidden">该邮箱已被注册</font><font id="mailfont1" color="#1a1b1b" hidden="hidden">此邮箱可以注册</font>
	                        </label>
	                        
	                        <input id="usermail" name="usermail"  placeholder="请输入您要绑定的邮箱..."  required="required" type="text"  ONBLUR='mailverify()' value=""/>
	                        <span id="span3" style="color:red;" hidden="hidden">格式不对..</span>
	                    </p>
	                    <p> 
	                        <label for="password" class="youpasswd" data-icon="p">
	                        	<b style="color: red;">*</b> 密码
	                        </label>
	                        <input id="password" name="password" placeholder="请设置密码.使用字母、数字两种组合最多6个字符" maxlength="6" required="required" type="password" ONBLUR="pass()" value="" /> 
	                        <span id="span2" style="color:red;" hidden="hidden">必须是  数字,字母 ,字母数字的组合 6个字符</span>
	                    </p>
	                    <p> 
	                        <label for="password" class="youpasswd" data-icon="p">
	                        	<b style="color: red;">*</b> 确认密码 
	                        </label>
	                        <input id="password2" name="password2" placeholder="请再次输入密码..." required="required" maxlength="6" type="password" value="" oninput="YanZheng()" /> 
	                        <span id="span4" style="color:red;" hidden="hidden"> 两次输入密码不正确:</span>
	                    </p>
	                    <p class="login button" style="text-align: -webkit-center;"> 
                            <input type="submit" value="提交" />
						</p>
						<p style="text-align: -webkit-right;">
							已有账户？&nbsp;&nbsp;
	                       	<a href="<%=request.getContextPath()%>/jsp/admin/login.jsp" style="margin-top: 20px;font-size: 25px;text-decoration: initial;">直接登录</a>
						</p>
                  	</form>
                </div>
            </div>
        </div>  
      </div>
      
      
<script type="text/javascript">
var nowid;
var totalid;
var can1press = false;
var emailafter;
var emailbefor;
$(document).ready(function(){ 
 $("#usermail").focus(function(){ //文本框获得焦点，插入Email提示层
 $("#myemail").remove();
 $(this).after("<div id='myemail' style='width:170px; height:auto; background:#fff; color:#6B6B6B; position:absolute; left:"+($(this).get(0).offsetLeft+255)+"px; top:"+($(this).get(0).offsetTop+$(this).height()+24)+"px; border:1px solid #ccc;z-index:5px; '></div>");
 if($("#myemail").html()){
  $("#myemail").css("display","block");
 $(".newemail").css("width",$("#myemail").width());
  can1press = true;
 } else {
  $("#myemail").css("display","none");
  can1press = false;
 }  
 }).keyup(function(){ //文本框输入文字时，显示Email提示层和常用Email
  var press = $("#usermail").val();
  if (press!="" || press!=null){
  var emailtxt = "";   
  var emailvar = new Array("@163.com","@126.com","@yahoo.com","@qq.com","@sina.com","@gmail.com","@hotmail.com","@foxmail.com");
  totalid = emailvar.length;
   var emailmy = "<div class='newemail' style='width:170px; color:#6B6B6B; overflow:hidden;'><font color='#D33022'>" + press + "</font></div>";
   if(!(isEmail(press))){
    for(var i=0; i<emailvar.length; i++) {
     emailtxt = emailtxt + "<div class='newemail' style='width:170px; color:#6B6B6B; overflow:hidden;'><font color='#D33022'>" + press + "</font>" + emailvar[i] + "</div>"
    }
   } else {
    emailbefor = press.split("@")[0];
    emailafter = "@" + press.split("@")[1];
    for(var i=0; i<emailvar.length; i++) {
     var theemail = emailvar[i];
     if(theemail.indexOf(emailafter) == 0)
     {
      emailtxt = emailtxt + "<div class='newemail' style='width:170px; color:#6B6B6B; overflow:hidden;'><font color='#D33022'>" + emailbefor + "</font>" + emailvar[i] + "</div>"
     }
    }
   }
   $("#myemail").html(emailmy+emailtxt);
   if($("#myemail").html()){
     $("#myemail").css("display","block");
     $(".newemail").css("width",$("#myemail").width());
     can1press = true;
   } else {
     $("#myemail").css("display","none");
     can1press = false;
   }
   beforepress = press;
  }
  if (press=="" || press==null){
   $("#myemail").html("");  
   $("#myemail").css("display","none"); 
  }
 })
 $(document).click(function(){ //文本框失焦时删除层
 if(can1press){
   $("#myemail").remove();
   can1press = false;
   if($("#usermail").focus()){
    can1press = false;
   }
  }
 })
 $(".newemail").live(" onmouseover",function(){ //鼠标经过提示Email时，高亮该条Email
 $(".newemail").css("background","#797d7d");
 $(this).css("background","#797d7d");
  $(this).focus();
  nowid = $(this).index();
 }).live("click",function(){ //鼠标点击Email时，文本框内容替换成该条Email，并删除提示层
 var newhtml = $(this).html();
 newhtml = newhtml.replace(/<.*?>/g,"");
 $("#usermail").val(newhtml);
 $("#myemail").remove();
 })
 $(document).bind("keydown",function(e)
 {
  if(can1press){
   switch(e.which) 
   {
    case 38:
    if (nowid > 0){
     $(".newemail").css("background","#FFF");
     $(".newemail").eq(nowid).prev().css("background","#CACACA").focus();
     nowid = nowid-1;
    }
    if(!nowid){
     nowid = 0;
     $(".newemail").css("background","#FFF");
     $(".newemail").eq(nowid).css("background","#CACACA");  
     $(".newemail").eq(nowid).focus();    
    }
    break; 
    case 40:
    if (nowid < totalid){
     $(".newemail").css("background","#FFF");
     $(".newemail").eq(nowid).next().css("background","#CACACA").focus(); 
     nowid = nowid+1;
    }
    if(!nowid){
     nowid = 0;
     $(".newemail").css("background","#FFF");
     $(".newemail").eq(nowid).css("background","#CACACA");  
     $(".newemail").eq(nowid).focus();    
    }
    break; 
    case 13:
    var newhtml = $(".newemail").eq(nowid).html();
    newhtml = newhtml.replace(/<.*?>/g,"");
    $("#usermail").val(newhtml); 
    $("#myemail").remove();
   }
  } 
 })
}) 

</script>
      
</body>


</html>