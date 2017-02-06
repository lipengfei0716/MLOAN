package com.util.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.util.base.ActionBase;
/**
* @ClassName: AuthFilter
* @Description:filter的三种典型应用：
*                     1、可以在filter中根据条件决定是否调用chain.doFilter(request, response)方法，
*                        即是否让目标资源执行
*                     2、在让目标资源执行之前，可以对request\response作预处理，再让目标资源执行
*                     3、在目标资源执行之后，可以捕获目标资源的执行结果，从而实现一些特殊的功能
* @author: 任宝坤
* @date: 2014-8-31 下午10:09:24
*/ 
public class AuthFilter extends ActionBase implements Filter{
	
	//1.从web.xml中获取userSessionKey,redirectPage,uncheckdUrl
	private String uncheckdUrl;
    public void init(FilterConfig filterConfig) throws ServletException {
       
    	log.info("----过滤器初始化----");
    	ServletContext servletContext = filterConfig.getServletContext();
    	uncheckdUrl = servletContext.getInitParameter("uncheckdUrl");
    	System.err.println("web.xml中配置了>>>>"+uncheckdUrl);
    	
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        log.info("AuthFilter执行前！！！");
        //对request和response进行一些预处理

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        //1.获取请求的servletpath
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse respon = (HttpServletResponse)response;
        String servletPath = req.getServletPath();
       
        //2.检查1获取的servletPath 是否为要不检查的URL中的一个
        List<String> list = Arrays.asList(uncheckdUrl.split(","));
        if(list.contains(servletPath)){
        	chain.doFilter(request, response);  //让目标资源执行，放行
        	return;
        }
        String url = "";
        url = request.getScheme() +"://" + request.getServerName()  
                        + ":" +request.getServerPort();
        if (((HttpServletRequest) request).getQueryString() != null){
        }
        String path = url+"/MLOAN/jsp/admin/login.jsp";
        //3.从session中获取用户名，若值不存在，则重定向到redirectPage
        Object username = req.getSession().getAttribute("username");
        if(username==null){
        	
            PrintWriter out = respon.getWriter();
            out.flush();
            out.println("<script language>");
            out.println("alert('请登录！…………');");
            out.println("top.location.href='"+path+"'");
            out.println("</script>");
        	chain.doFilter(request, response);  //让目标资源执行，放行   respon.sendRedirect(url);
        	return;
        }
        
		String sql = "SELECT SESSIONID FROM SYS_USERS WHERE USERNAME = '"+ username + "'";
    	List<Map<String, String>> rs = null;
		try {
			rs = getQueryList(sql);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	String sessionid="";
    	if(rs!=null && rs.size()!=0)
        	sessionid = rs.get(0).get("sessionid");
    	if(!sessionid.equals(((HttpServletRequest) request).getRequestedSessionId())){
    		PrintWriter out = respon.getWriter();
            out.flush();
            out.println("<script language>");
            out.println("alert('用户已在其它地点登录，请重新登录！');");
            out.println("top.location.href='"+path+"'");
            out.println("</script>");
         	chain.doFilter(request, response);  //让目标资源执行，放行   respon.sendRedirect(url);
         	return;
    	}
        chain.doFilter(request, response);
        log.info("AuthFilter执行后！！！");
    }

    public void destroy() {
        log.info("----过滤器销毁----");
    }
    
    private final Logger log = Logger.getLogger(AuthFilter.class);
}