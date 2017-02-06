<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>修改密码</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/demo.css" />
		<link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/design/css/login/animate-custom.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/admin/users.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/login/login.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/content/setup_left.css" />
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
					<div class="users_right" id="users_right">
						<div id="right_info1" class="right_info1">
							<div id="login" class="animate form" style="padding-bottom: 0px;">
								<form action="<%=request.getContextPath()%>/user/updataUser.do" autocomplete="on" method="post" name="iform" onSubmit="return validate()">
									<p>
										<label for="password1" class="youpasswd1" data-icon="p">
											<b style="color: red;">*</b> 当前密码 
										</label> 
										<input id="password1" name="password1"   placeholder="请输入您要更改的新密码..."required="required" type="password" maxlength="6" />
									</p>
									<p>
										<label for="password" class="youpasswd" data-icon="p">
											<b style="color: red;">*</b> 新密码 
										</label> 
										<input id="password" name="password"   placeholder="请输入您要更改的新密码..." required="required" type="password" maxlength="6"  />
									</p>
									<p>
										<label for="password" class="youpasswd" data-icon="p">
											<b style="color: red;">*</b> 确认密码 
										</label> 
										<input id="password2" name="password2" placeholder="请再次输入要更改的新密码..." required="required"type="password" />
									</p>
									<p class="login button" style="text-align: -webkit-center;">
										<input type="submit" value="确认修改" />
									</p>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
	</body>
	<script src="<%=request.getContextPath()%>/design/js/content/users.js" type="text/javascript"></script>
</html>