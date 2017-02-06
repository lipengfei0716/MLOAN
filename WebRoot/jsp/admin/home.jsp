
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<title>操作中心</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/admin/home.css"/>
	<script src="<%=request.getContextPath()%>/design/js/utilBefore.js" type="text/javascript"></script>
</head>
<body>
	<div class="home" >
			<div class="top">
				<%@ include file="/jsp/content/top.jsp" %>
			</div>
			<div class="content">
				<div class="home_left">
					<%@ include file="/jsp/content/home_left.jsp" %>
				</div>
			<div class="right" id="right">
				<div class="home_maintop">
					<%@ include file="/jsp/content/home_maintop.jsp" %>
				</div>
	 		    <div class="home_mainform" > 
						<div id="home_form1" class="home_form1">
							<%@ include file="/jsp/content/home_form1.jsp" %>
						</div>
						<div id="home_form2" class="home_form2">
							<%@ include file="/jsp/content/home_form2.jsp" %>
						</div>
						<div id="home_form3" class="home_form3"  >
							<%@ include file="/jsp/content/home_form3.jsp" %>
						</div>
				</div> 
			</div>
			</div>
	       <!--底部 -->
			<div class="copyright" style="text-align: center;font-size: 12px;width: 100%;float: left;height:auto;bottom: 0;position:fixed;background: #f8f8f8;">&copy;2016 中国移动通信研究院 版权所有</div>
	</div>
	<script src="<%=request.getContextPath()%>/design/js/admin/home.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/design/js/utilAfter.js" type="text/javascript"></script>
 </body>
</html>