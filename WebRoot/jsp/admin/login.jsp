<%@page import="org.apache.log4j.Logger"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>登录</title>
      <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/demo.css" />
      <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/style.css" />
	  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/animate-custom.css" />
	  <!--非IE用最新版本-->
	 <!--[if !IE]><!-->
	 <script src="<%=request.getContextPath()%>/design/js/off/jquery-3.0.0.js"></script>
	 <!-- <![endif]-->
	  <!--IE8以下支持jQuery1.9-->
	 <!--[if lte IE 8]>
	  <script src="<%=request.getContextPath()%>/design/js/off/jquery-1.11.1.min.js"></script>
	 <![endif]-->
	  <!--IE8以上-->
	 <!--[if gt IE 8]>
	  <script src="<%=request.getContextPath()%>/design/js/off/jquery-3.0.0.js"></script>
	 <![endif]-->
	  <script src="<%=request.getContextPath()%>/design/js/admin/login.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">
		<div id="container_demo" >
		    <div id="wrapper">
		        <div id="login" class="animate form">
		            <form id="myForm" action="<%=request.getContextPath()%>/user/login.do" method="post" autocomplete="off"> 
                        <!-- <h1>您好 ! 请登录</h1> -->
                        <h1 id="loginH1"></h1>
                        <p> 
                            <label for="username" class="uname" data-icon="u" > 用户名:  
                            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            	<font id="username1" color="red"  hidden="hidden">该用户已在其它地点登录</font>
                            	<font id="username9" color="red"  hidden="hidden">该用户已失效，无法登录</font>
                            	<font id="username2" color="red"  hidden="hidden">该用户已在该浏览器登录</font>
                            </label>
                            <input id="username" name="username" class='inpuText' required oninvalid="setCustomValidity('请输入用户名');" oninput="setCustomValidity('');" 
                            	   type="text" onblur="checkConfirm()"/>
                        </p>
                        <p> 
                            <label for="password" class="youpasswd" data-icon="p"> 密码: </label>
                            <input id="password" name="password" class='inpuText' required oninvalid="setCustomValidity('请输入密码');"  oninput="setCustomValidity('');"
                            	   type="text" onFocus="pwd()"/> 
                        </p>
                        <p class="login button"> 
                            <input type="submit" value="登录"/> 
						</p>
                        <p class="change_link"> 免费注册:
							<a href="<%=request.getContextPath()%>/jsp/admin/register.jsp" class="to_register">注册</a>
						</p>
                    </form>
                </div>
            </div>
        </div>  
    </div>
</body>
</html>



