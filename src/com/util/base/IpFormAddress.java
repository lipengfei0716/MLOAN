package com.util.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.util.interceptor.CommonInterceptor;

import net.sf.json.JSONObject;

public class IpFormAddress {
	
	/**
	 * log4j日志打印
	 */
	public static Logger log = Logger.getLogger(CommonInterceptor.class);  			//........
	
	/**
	 * 地址查询   -- 百度
	 *  See http://apistore.baidu.com/apiworks/servicedetail/114.html	IP归属地查询
	 *  k1 cb4b9b822ef517861e0392b5b727631d
	 *  k2 442b4cd117fb3636794603d67b8fcfd8
	 *  查询次数限制     200次/秒
	 * @throws Exception 
	 */
	public JSONObject baiduIPs(String ip,String apikey) throws Exception {
		
			if(apikey==null)
				apikey = "cb4b9b822ef517861e0392b5b727631d";

		    BufferedReader reader = null;
		    String jsonStr = null;
		    StringBuffer sbf = new StringBuffer();
		    try {
		        URL url = new URL("http://apis.baidu.com/apistore/iplookupservice/iplookup?ip="+ip);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setRequestMethod("GET");
		        connection.setRequestProperty("apikey", apikey);// 填入apikey到HTTP header
		        connection.connect();
		        
		        InputStream is = connection.getInputStream();
		        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		        String strRead = null;
		        while ((strRead = reader.readLine()) != null) {
		            sbf.append(strRead);
		            sbf.append("\r\n");
		        }
		        reader.close();
		        jsonStr = sbf.toString();
		    } catch (Exception e) {
		    	log.info("百度查询IP 归属 ： 网络异常");
		        throw e;
		    }
			JSONObject json = JSONObject.fromObject(jsonStr);
			log.info(ip+" 百度  ："+json);
			System.out.println(ip+" 百度  ："+json);
			return json;
	}
	/**
	 * 地址查询   -- 新浪
	 */
	public JSONObject sinaIPs(String ip) throws IOException {
		
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="+ip);
        httpClient.executeMethod(getMethod);
        Integer statusCode = getMethod.getStatusCode();
        JSONObject json = null;
        if(statusCode != null && statusCode == HttpStatus.SC_OK){
        	String res = getMethod.getResponseBodyAsString();
            String asc = res.substring(21,res.length()-1);
            json = JSONObject.fromObject(asc);
            log.info(ip+" 新浪 : "+json);
        }else
        	log.info(ip+" 新浪 : 网络异常");        	
        return json;
	}
	/**
	 * 地址查询   -- 聚合数据
	 */
	public JSONObject juheIPs(String ip) throws IOException {
		String appkey = "8aa46d6cba98b535304ca24f73c9749a";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://apis.juhe.cn/ip/ip2addr?ip="+ip+"&key="+appkey);
        httpClient.executeMethod(getMethod);
		String res = convertStreamToString(getMethod.getResponseBodyAsStream());
		JSONObject json = null;
        log.info(ip+"聚合数据"+res);
        if (res != null && !res.equals("")) {
        	json = JSONObject.fromObject(res);
		} 
        
        return json;
	}

	public String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}   
}
