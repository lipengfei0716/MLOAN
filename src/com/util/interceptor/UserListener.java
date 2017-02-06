package com.util.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.util.base.ActionBase;

public class UserListener extends ActionBase implements HttpSessionListener {

    public void sessionDestroyed(HttpSessionEvent event){
        HttpSession session = event.getSession();
        Object username = session.getAttribute("username");
        if(username != null){
        	String name = username.toString();
        	String sql = "SELECT SESSIONID FROM SYS_USERS WHERE USERNAME = '"+ name + "'";
        	List<Map<String, String>> rs = null;
			try {
				rs = getQueryList(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			String sessionid = null;
        	if(rs !=null && rs.size()!=0)
        		sessionid = rs.get(0).get("sessionid");
    		if(sessionid !=null && sessionid.equals(session.getId()))
    			try {
    				execute("UPDATE SYS_USERS SET SESSIONID=null , STATUS=0 WHERE USERNAME= '"+ username + "' AND SESSIONID='"+sessionid+"'");
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		
    		session.removeAttribute(name);
    		session.invalidate();	//删除所有session中保存的键		
    		
            System.out.println(name + "超时退出。");
        }
    }

	public void sessionCreated(HttpSessionEvent arg0) {}

}