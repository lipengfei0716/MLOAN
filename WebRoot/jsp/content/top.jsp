<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
  <head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/content/top.css">
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
	<script	src="<%=request.getContextPath()%>/design/js/content/home_form2.js"></script>
	<script	src="<%=request.getContextPath()%>/design/js/content/top.js"></script>
	<script src="<%=request.getContextPath()%>/design/three/layer/layer.js" type="text/javascript"></script>
  </head>
	<div class='logo' onclick="window.location.href='<%=request.getContextPath()%>/jsp/admin/home.jsp'">
		<img alt="CMLAB" src="<%=request.getContextPath()%>/design/img/index/logo.png">
	</div>
	<span class='title' id='title'>
			&nbsp;&nbsp;&nbsp;移动互联网业务品质智能测评系统 (v_0.8.4_alpha)
	</span>
	<div class="user" id="user">
		<%-- <a style="margin-top: 15px">
			<img onclick='help()' src="<%=request.getContextPath()%>/design/img/index/help.png" width="12" height="12" alt="系统帮助"></a> --%>
		<a href="<%=request.getContextPath()%>/jsp/admin/users.jsp"  class="define" id="define">
			用户管理</a>
		<a href="<%=request.getContextPath()%>/user/logout.do" class="logout" onclick="exit()"><font color="red">[退出]</font></a>
		<a href="<%=request.getContextPath()%>/data/test.do?username=${username}" id="username" class="username">[${username}]</a>
		<!-- <a href="<%=request.getContextPath()%>/data/test.do?username=${username}" id="username" class="username">用户名:</a> -->
		<a style="cursor: pointer;" onclick="home(1)" >[主页]</a> 
	</div>
	<div style="display:none" id="helpdiv">
		<ul class="helpinfo">
		   <li> 捕获到帮助信息</li>
		</ul>
	
	</div>
	<script type="text/javascript">
		function home(index) {
			var checkHome = document.getElementById('label_Left'+(index));
			if (checkHome != null && "" != checkHome) {
				window.location.href='<%=request.getContextPath()%>/jsp/admin/home.jsp';
			} else {
				MainTop(index+ 1);
			}
		}
	</script>
