<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
  <head>
 	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/content/home_left.css" />
  </head>
	<dl>
		<dd>
			<a style="cursor: pointer;background: #78C2FF" onclick="MainTop(1)" title="一键测试" id="label_Left1">一键测试</a>
			<a style="cursor: pointer;background: #78C2FF" onclick="MainTop(2)" title="指标分析" id="label_Left2">指标分析</a> 
			<a style="cursor: pointer;background: #78C2FF" onclick="MainTop(3)" title="报告输出" id="label_Left3">报告输出</a> 
		</dd>
	</dl>
	<script src="<%=request.getContextPath()%>/design/js/content/home_left.js" type="text/javascript"></script>