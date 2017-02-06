<%@page import="org.apache.log4j.Logger"%>
<%@ page pageEncoding="utf-8"%>
<%@ page import="java.net.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
 <%
	 // 编码，解决中文乱码   
	 String username = URLEncoder.encode(request.getParameter("username"),"utf-8");  
     // 创建一个Cookie
     Cookie cookie = new Cookie("username", username);
    
     // 设置Cookie的生命周期,如果设置为负值的话,关闭浏览器就失效.
     cookie.setMaxAge(60*60*24*365);
     
     // 设置Cookie路径,不设置的话为当前路径(对于Servlet来说为request.getContextPath() + web.xml里配置的该Servlet的url-pattern路径部分)
     //  Cookie url = new Cookie("url", request.getParameter("url"));
   
     // 输出Cookie
     response.addCookie(cookie);
 %>