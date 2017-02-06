<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
  <head>
 	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/content/setup_left.css" />
  </head>
	<dl>
		<dd>
			<a style="cursor: pointer;" href="<%=request.getContextPath()%>/data/test.do?username=${username}" title="个人信息" id="label_Left1" >个人信息</a>
			<a style="cursor: pointer;" href="<%=request.getContextPath()%>/jsp/admin/users.jsp" title="修改密码" id="label_Left2" >修改密码</a>
			<a style="cursor: pointer;" href="<%=request.getContextPath()%>/jsp/admin/indexW.jsp" title="指标维护" id="label_Left2"  >指标维护</a>
		</dd>
	</dl>