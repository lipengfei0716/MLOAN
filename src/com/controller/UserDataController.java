package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.util.base.ActionBase;

@Controller
@RequestMapping(value="data")
public class UserDataController extends ActionBase{
	
	@RequestMapping(value="/norm.do",method=RequestMethod.GET)
	 public void norm(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		log.info("备份指标........");
		String listname = request.getParameter("listname");
		String list = request.getParameter("list");
		Object username = request.getSession().getAttribute("username");
	    String query = "SELECT * FROM SYS_USERDATA WHERE USERNAME='"+username+"' AND LISTNAME='"+listname+"'";
		int num = getQueryNumber(query);
	    if(num==0){
		    String sql = "INSERT INTO SYS_USERDATA VALUES('"+username+"','"+ listname + "','"+list+"')";
		    int rs = execute(sql);
		    System.out.println(rs);
		    renderData(request, response, null);
	    }else {
	    	Map<String, String> map = new HashMap<String,String>();
			map.put("errormsg", "文件名已存在！！！");
			String data = JSON.toJSONString(map);
	    	renderData(request, response, data);
		}
	}
	
	@RequestMapping(value="/selectinfo.do",method=RequestMethod.GET)
	 public void selectinfo(HttpServletRequest request,HttpServletResponse response) throws Exception {

		log.info("查询指定用户........");
		String username = request.getParameter("username");
	    String sql = "SELECT * FROM SYS_USERDATA WHERE USERNAME='"+username+"'";
		
	    List<Map<String, String>> rs = getQueryList(sql);
	    String data = JSON.toJSONString(rs);
	    renderData(request, response, data);
	}
	@RequestMapping(value="/usrsinfo.do",method=RequestMethod.GET)
	 public void usrsinfo(HttpServletRequest request,HttpServletResponse response) throws Exception {

		log.info("查询所有用户信息........");
	    String sql1 = "SELECT * FROM SYS_USERS";
	    List<Map<String, String>> rs1 = getQueryList(sql1);
	    
	    String sql2 = "SELECT * FROM SYS_USERDATA";
	    List<Map<String, String>> rs2 = getQueryList(sql2);
	    
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("users", rs1);
	    map.put("userdata", rs2);
	    String data = JSON.toJSONString(map);
	    
	    renderData(request, response, data);
	}
	
	@RequestMapping(value="/Pcenter.do")
	public void Pcenter(HttpServletRequest request,HttpServletResponse response,String username) throws Exception{
		log.info("查询当前用户信息........");
		String sql = "SELECT * FROM SYS_USERS WHERE USERNAME='"+username+"'";
		List<Map<String, String>>  rs = getQueryList(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username",rs.get(0).get("username"));
		map.put("usermail",rs.get(0).get("usermail"));
	    ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("data", map);  
        String data = JSON.toJSONString(map);
        renderData(request, response, data);
	}
	
	@RequestMapping(value="/test.do")
	public String  test(HttpServletRequest request,HttpServletResponse response,String username) throws Exception{
		String sql = "SELECT * FROM SYS_USERS WHERE USERNAME='"+username+"'";
		List<Map<String, String>>  rs = getQueryList(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username",rs.get(0).get("username"));
		map.put("usermail",rs.get(0).get("usermail"));
		request.setAttribute("map", map);
		return "jsp/admin/setup";
	}

}
