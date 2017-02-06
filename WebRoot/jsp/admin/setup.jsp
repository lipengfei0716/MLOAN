<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>个人信息</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/demo.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/style.css" />
		<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/design/css/login/animate-custom.css" />
		<!--非IE用最新版本-->
		<!--[if !IE]><!--> 
			<script src="<%=request.getContextPath()%>/design/js/off/jquery-3.0.0.js"></script>
		<![endif]-->
		<!--IE8一下支持jQuery1.9-->
		<!--[if lte IE 8]>
			<script src="<%=request.getContextPath()%>/design/js/off/jquery-1.11.1.min.js"></script>
		<![endif]-->
		<!--IE8以上-->
		<!--[if gt IE 8]>
			<script src="<%=request.getContextPath()%>/design/js/off/jquery-3.0.0.js"></script>
		<![endif]-->
	</head>
	<body>
			<div id="container_demo">
				<div class="top">
					<%@ include file="/jsp/content/top.jsp"%>
				</div>
				<div style="float: left;">
					<div class="setup_left">
						<%@ include file="/jsp/content/setup_left.jsp"%>
					</div>
				</div>
				<div style="width:300px;height:200px;  position:absolute;  left:50%;  top:50%;  margin:-100px 0 0 -150px">
						用户：<font color="red">${map.username }</font><br/>
						邮箱：<font color="red">${map.usermail }</font>
			</div>
			</div>
			
	</body>
</html>