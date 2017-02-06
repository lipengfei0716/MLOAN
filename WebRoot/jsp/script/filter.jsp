<%@page import="org.apache.log4j.Logger"%>
<%@ page pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
		Logger log = Logger.getLogger(this.getClass());
		//1.获取用户登录信息
		String username = request.getParameter("username");
		//2.若登录信息正确，则把信息放到session中
		if(username!=null && !username.trim().equals("")){
			log.info(username);
			session.setAttribute("username", username);
			//3重定向到list.jsp
			out.print("已登录");
		}else
			out.print("<script language>alert('请登录！…………');</script>");
	%>