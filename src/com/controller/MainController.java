package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
@RequestMapping("/resolve")
public class MainController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@RequestMapping("/test.do")
	public void findAllUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		System.err.println(request.getParameter("name"));

        List<Object> pcapArray = new ArrayList<>();
		Map<String, String> pcapMap = new HashMap<String, String>();

		pcapMap.put("no", "序号");
		pcapMap.put("time", "时间");
		pcapMap.put("fromAddress", "原地址");
		pcapMap.put("goAddress", "目的地址");
		pcapMap.put("formPort", "原端口");
		pcapMap.put("goPost", "目的端口");
		
		pcapArray.add(pcapMap);
		pcapArray.add(pcapMap);
		Gson gson = new Gson();
		String pcapList = gson.toJson(pcapArray);

        PrintWriter out = response.getWriter();
		out.println(pcapList);
		System.err.println(pcapList);
		out.flush();
		out.close();
	}
	@RequestMapping("/ajaxget.do")
	 protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
             throws ServletException, IOException {
         try{
           httpServletResponse.setContentType("text/html;charset=utf-8"); 
           PrintWriter out= httpServletResponse.getWriter();
             
            Integer inte = (Integer)httpServletRequest.getSession().getAttribute("total"); //用来测试缓存的；在IE下如果多次请求的地址相同，则他不会再去服务器端交互读数据，而是直接读取缓存
               int temp=0;
             if(inte==null){
                 temp=1;
             }else{
                 temp=inte.intValue()+1;
             }
                httpServletRequest.getSession().setAttribute("total",temp);
            //1.取参数
        String old =httpServletRequest.getParameter("name");
            // String name=new String(old.getBytes("iso8859-1"),"utf-8"); //处理中文乱码1，需和前台js文件中的encodeURI配合
            String name= URLDecoder.decode(old,"utf-8"); //处理中文乱码2
           //2.检查参数是否有问题
       if(old==null||old.length()==0){
           out.println("用户名不能为空！");
       } else{
           //String name=old;
           //3.校验操作
           if(name.equals("123")){
                 //4.与传统应用不同的是，这一步将用户感兴趣的数据返回给页面端。而不是将一个新的页面返回给页面端
               //写法没有改变，本质变化了
               out.println("用户名【"+name+"】已经存在，请使用其他的用户名！,"+temp);
           } else{
               out.println("用户名【"+name+"】未存在，您可以使用该用户名！,"+temp);
           }
       }
     }catch(Exception e){
            e.printStackTrace();
         }
     }
	 @RequestMapping("/ajaxpost.do")
     protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
         throws ServletException, IOException {
        doGet(httpServletRequest,httpServletResponse);
     }
	 /**
	  	List<Object> pcapArray = new ArrayList<>();
		Map<String, String> pcapMap = new HashMap<String, String>();

		pcapMap.put("no", "序号");
		pcapMap.put("time", "时间");
		pcapMap.put("fromAddress", "原地址");
		pcapMap.put("goAddress", "目的地址");
		pcapMap.put("formPort", "原端口");
		pcapMap.put("goPost", "目的端口");
		
		pcapArray.add(pcapMap);
		pcapArray.add(pcapMap);
		Gson gson = new Gson();
		String pcapList = gson.toJson(pcapArray);
		System.err.println(pcapList);
	  	  */
}