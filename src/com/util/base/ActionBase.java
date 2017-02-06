package com.util.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.util.interceptor.CommonInterceptor;
import com.util.jdbc.Global;

import net.sf.json.JSONObject;

/**
 * controller常用公共类
 * @author 任宝坤
 *
 */
public class ActionBase extends Global{
	
	/**
	 * log4j日志打印
	 */
	public static Logger log = Logger.getLogger(CommonInterceptor.class);  			//........

	/**
	 * 通过PrintWriter将响应数据写入response，ajax可以接受到这个数据
	 * @param response
	 * @param data json格式(null返回 = "success", "操作成功")
	 * @return ajaxDatas
	 */
	public void renderData(HttpServletRequest request, HttpServletResponse response,String data) {
		
		log.info(request.getServletPath()+"\n通过PrintWriter将响应数据写入response，ajax可以接受到这个数据");
		
		Object username = request.getSession().getAttribute("username");
    	
		Map<String, String> map = new HashMap<String,String>();
		if(data==null){
			map.put("success", "操作成功");
			data = JSON.toJSONString(map);
		}else if(username==null){
	    	map.put("session", "lose");
	    	data = JSON.toJSONString(map);
		}
		PrintWriter out = null;
	    try {
	    	out = response.getWriter();
	    	out.print(data);
	    } catch (IOException e) {
	      System.err.println("PrintWriter写入响应数据时发生异常！！！");
	      e.printStackTrace();
	    } finally {
	      if (null != out)
	    	  out.close();
	      }
	}
	//判断session是否有效
	public void effecTive(HttpServletRequest request,HttpServletResponse response) {

		Object username = request.getSession().getAttribute("username");
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
    	if(username==null && sessionid.equals("")){
    		
    		log.info(request.getServletPath()+"\n会话失效");
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("session", "lose");
    		PrintWriter out = null;
    	    try {
    	    	out = response.getWriter();
    	    	out.print(JSON.toJSONString(map));
    	    } catch (IOException e) {
    	      System.err.println("PrintWriter写入响应数据时发生异常！！！");
    	      e.printStackTrace();
    	    } finally {
    	      if (null != out)
    	    	  out.close();
    	    }
	      }else if(!sessionid.equals(request.getRequestedSessionId())){
    		log.info(request.getServletPath()+"\n用户已在其它地点登录");
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("session", "remote");
    		PrintWriter out = null;
    	    try {
    	    	out = response.getWriter();
    	    	out.print(JSON.toJSONString(map));
    	    } catch (IOException e) {
    	      System.err.println("PrintWriter写入响应数据时发生异常！！！");
    	      e.printStackTrace();
    	    } finally {
    	      if (null != out)
    	    	  out.close();
    	      }
    	}
	}
	/**
	 * HttpSession session缓存会话
	 * @param request	HttpServletRequest
	 * @param Map<String, String> map 参数集合
	 */
	public void session(Map<String, String> map, HttpServletRequest request) throws NumberFormatException, IOException {
		
		log.info("HttpSession session缓存会话");
		int sessiontimeout = Integer.parseInt(config("sessiontimeout"));
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(sessiontimeout);  //会话时间
        for (String key : map.keySet()) {
        	log.info("session : "+key+" = "+map.get(key));
        	session.setAttribute(key,map.get(key));
		}
	}
	/**
	 * 
	 * @param list 存放的list
	 * @param request
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void saveListToSession(List<Map<String, String>> list ,HttpServletRequest request) throws NumberFormatException, IOException{
		log.info("HttpSession session缓存会话");
		int sessiontimeout = Integer.parseInt(config("sessiontimeout"));
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(sessiontimeout);  //会话时间
        session.setAttribute("fileNameList",list);
	}
	/**
	 * 移除指定session中的缓存会话
	 * @param list 指定参数集合
	 * @param request
	 */
	public void removeSession(List<String> list, HttpServletRequest request) {

		log.info("移除指定session中的缓存会话");
        for (String arg : list) {
        	log.info("removeSession : "+arg);
        	request.getSession().removeAttribute(arg);
  		    request.getSession().invalidate();	//删除所有session中保存的键
		}
	}
	/**
	 * response.sendRedirect(path);
	 * @param path		请求地址
	 * @param path_map	地址参数集合
	 * @param path_obj	返回对象集合
	 */
	public HttpServletResponse res(String path,Map<String, String> path_map,HttpServletResponse response) throws IOException{
		
		log.info("response响应地址");
		if(path_map!=null){
			int i=0;
			for (String key : path_map.keySet()) {
				
				path += (i++==0) ? ("?"+key+"=") : ("&"+key+"=");
				path += path_map.get(key);
			}
		}
		log.info("重定向........\n"+path);
		response.sendRedirect(path);
		
		return response;
	} 
	 /**
	   * 加载config.properties配置文件，获取参数值
	   * @param 参数名
	   * @return 参数值
	   */
	public static String config(String arg) throws IOException {
		
		 InputStreamReader in = new InputStreamReader(ActionBase.class.getClassLoader().getResourceAsStream("config/config.properties"),"UTF-8");
         Properties prop = new Properties();
         //加载配置文件
         prop.load(in); 
         String res = prop.getProperty(arg);
         
         return res;
	}
	/**
	 * 从指定路径读取文件
	 */
    public static BufferedReader readFile(String filepath) throws IOException{
    	
    	log.info("从指定路径读取文件  ：   "+ filepath);
        File file = new File(filepath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        return reader;
    }
    /**
	 * String[] 转  List
	 * @return List<String>
	 */
	public List<String>	toList(String[] args) {
		
		List<String> list = Arrays.asList(args);
		
		return list;
	}
	/**
	 * @param ip归属信息   格式：省-市-移动/联通/电信/阿里巴巴
	 *   聚合   -->  百度   -->  新浪
	 * @throws Exception 
	 */
	public String ipInfo(String ip,String baiduKey) throws Exception{
		
		IpFormAddress ipForm = new IpFormAddress();
		String ipInfo = "";
		JSONObject jsonJuhe = ipForm.juheIPs(ip);
		
		if (jsonJuhe != null) {
			JSONObject result = JSONObject.fromObject(jsonJuhe.get("result"));
			String area = result.getString("area");
			String location = result.getString("location");
			if (area != null && !area.equals("")) {
				ipInfo = ipInfo + area;
			}
			if (location != null && !location.equals("")) {
				ipInfo = ipInfo +"/"+ location;
			}
		} else {
			JSONObject jsonBaidu = ipForm.baiduIPs(ip,baiduKey);
			if(jsonBaidu !=null && jsonBaidu.get("errNum").equals(0)){
				JSONObject retData = JSONObject.fromObject(jsonBaidu.get("retData"));
				String province = retData.getString("province");
				String city = retData.getString("city");
				String carrier = retData.getString("carrier");
				if(!province.equals("None"))
					ipInfo = province;
				if(!city.equals("None") && !ipInfo.equals(""))
					ipInfo = ipInfo+"/"+city;
				if(!carrier.equals("None") && !ipInfo.equals(""))
					ipInfo = ipInfo+"/"+carrier;
			}else{
				JSONObject jsonSina = ipForm.sinaIPs(ip);
				if(jsonSina !=null && jsonSina.get("ret").equals(1)){
					String province = jsonSina.getString("province");
					String city = jsonSina.getString("city");
					String isp = jsonSina.getString("isp");
					if(!province.equals(""))
						ipInfo = province;
					if(!city.equals("") && !ipInfo.equals(""))
						ipInfo = ipInfo+"/"+city;
					if(!isp.equals("") && !ipInfo.equals(""))
						ipInfo = ipInfo+"/"+isp;
				}
			}
		}
		String res = "";
		if(ipInfo.length()>25 && ipInfo.contains("(")){
			String[] ipIn = ipInfo.split("\\(");
			for(int i=0;i<ipIn.length;i++){
				if(i==0)
					res += ipIn[i];
				else
					res += "</br>("+ipIn[i];
			}
		}else
			res = ipInfo;
		return res;
	}
	/**
	 * byte[] 转16进制
	 * */
	public static String bytesToHexString(byte[] src){       
        StringBuilder stringBuilder = new StringBuilder();       
        if (src == null || src.length <= 0) {       
            return null;       
        }       
        for (int i = 0; i < src.length; i++) {       
            int v = src[i] & 0xFF;       
            String hv = Integer.toHexString(v);       
            if (hv.length() < 2) {       
                stringBuilder.append(0);       
            }       
            stringBuilder.append(hv);       
        }       
        return stringBuilder.toString();       
    }
	/**
	 * MD5加密      Md5解密网址：http://www.cmd5.com/    解密前请在后面去掉用户名  
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
        //改变过的  如果不改变会被破解  
        char hexDigits[] = { '0', '1', '2', '3', '4', '5',
    			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'  };  
        try {  
            byte[] btInput = s.getBytes();  
     //获得MD5摘要算法的 MessageDigest 对象  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
     //使用指定的字节更新摘要  
            mdInst.update(btInput);  
     //获得密文  
            byte[] md = mdInst.digest();  
     //把密文转换成十六进制的字符串形式  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {  
                byte byte0 = md[i];  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
                str[k++] = hexDigits[byte0 & 0xf];  
            }  
            return new String(str);  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    } 
	
	 /** 
	  * 删除目录及目录下的文件 
	  *  
	  * @param dir 
	  *            要删除的目录的文件路径 
	  * @return 目录删除成功返回true，否则返回false 
	  */  
	 public static boolean deleteDirectory(String dir) {  
	  // 如果dir不以文件分隔符结尾，自动添加文件分隔符  
	  if (!dir.endsWith(File.separator))  
	   dir = dir + File.separator;  
	  File dirFile = new File(dir);  
	  // 如果dir对应的文件不存在，或者不是一个目录，则退出  
	  if ((!dirFile.exists()) || (!dirFile.isDirectory())) {  
	   System.out.println("删除目录失败：" + dir + "不存在！");  
	   return false;  
	  }  
	  boolean flag = true;  
	  // 删除文件夹中的所有文件包括子目录  
	  File[] files = dirFile.listFiles();  
	  for (int i = 0; i < files.length; i++) {  
	   // 删除子文件  
	   if (files[i].isFile()) {  
	    flag = ActionBase.deleteFile(files[i].getAbsolutePath());  
	    if (!flag)  
	     break;  
	   }  
	   // 删除子目录  
	   else if (files[i].isDirectory()) {  
	    flag = ActionBase.deleteDirectory(files[i]  
	      .getAbsolutePath());  
	    if (!flag)  
	     break;  
	   }  
	  }  
	  if (!flag) {  
	   System.out.println("删除目录失败！");  
	   return false;  
	  }  
	  // 删除当前目录  
	  if (dirFile.delete()) {  
	   System.out.println("删除目录" + dir + "成功！");  
	   return true;  
	  } else {  
	   return false;  
	  }  
	 }  
	 /** 
	  * 删除单个文件 
	  *  
	  * @param fileName 
	  *            要删除的文件的文件名 
	  * @return 单个文件删除成功返回true，否则返回false 
	  */  
	 public static boolean deleteFile(String fileName) {  
	  File file = new File(fileName);  
	  // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除  
	  if (file.exists() && file.isFile()) {  
	   if (file.delete()) {  
	    System.out.println("删除单个文件" + fileName + "成功！");  
	    return true;  
	   } else {  
	    System.out.println("删除单个文件" + fileName + "失败！");  
	    return false;  
	   }  
	  } else {  
	   System.out.println("删除单个文件失败：" + fileName + "不存在！");  
	   return false;  
	  }  
	 }
}

