package resolve.analysis;


import java.net.InetAddress;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;

import java.util.HashMap;
import com.google.gson.Gson;
import com.util.interceptor.CommonInterceptor;

import net.sf.json.JSONObject;
import resolve.decoder.IPPacket;
import resolve.decoder.Packet;

public class IPStat {
	
	/**
	 * log4j日志打印
	 */
	public static Logger log = Logger.getLogger(CommonInterceptor.class);  			//........
	
	HashSet<InetAddress> localIPv4s;
	HashSet<InetAddress> remoteIPv4s;
	HashSet<InetAddress> localIPv6s;
	HashSet<InetAddress> remoteIPv6s;
	Map<InetAddress, BaiduIPInfo> ipInfos;
	InetAddress ueIP;
	
	public IPStat(Packet[] packets) {
		
		localIPv4s = new HashSet<InetAddress>();
		remoteIPv4s = new HashSet<InetAddress>();
		localIPv6s = new HashSet<InetAddress>();
		remoteIPv6s = new HashSet<InetAddress>();
		
		ipInfos = new HashMap<InetAddress, BaiduIPInfo>();
		
		Map<InetAddress,Integer> ipCount = new HashMap<InetAddress, Integer>();
		
		
		for(int i=0; i<packets.length; i++) {
			IPPacket p ;
			InetAddress ip1, ip2;
		
			if (packets[i] instanceof IPPacket) {
				p = (IPPacket) packets[i];
				ip1 = p.getSrcIP();
				ip2 = p.getDstIP();
				
				// Set the link direction (UL or DL?)
				if(isLocal(ip1) && !isLocal(ip2)) {
					p.setLinkDirection("UL");
				} else if(isLocal(ip2) && !isLocal(ip1)) {
					p.setLinkDirection("DL");
				} else {
					p.setLinkDirection("Unknown");
				}
				
				groupIP(ipCount,ip1);
				groupIP(ipCount,ip2);
				
			}
		}
		
		findUeIP(ipCount);
		doubleCheckDirection(packets);		
		
		//baiduIPs();
		
//		for(Map.Entry<InetAddress, BaiduIPInfo> entry:ipInfos.entrySet()) {
//			InetAddress ip = entry.getKey();
//			BaiduIPInfo info = entry.getValue();
//			logger.debug("{} {} {} {} {} {} {}", ip.getHostAddress(), info.getIP(), info.getCountry(),info.getProvince(),info.getCity(),info.getCarrier(),info.getErrMsg());
//		}
	}
	
	private boolean isLocal(InetAddress ip) {
		if(ip.isLinkLocalAddress() || ip.isSiteLocalAddress()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void groupIP(Map<InetAddress,Integer> ipCount, InetAddress ip) {
		if(isLocal(ip)) {
					
			if(ipCount.get(ip) == null) {
				ipCount.put(ip, 1);
			} else {
				ipCount.put(ip,ipCount.get(ip) + 1);
			}
			
			if(ip instanceof Inet4Address) {
				localIPv4s.add(ip);
			} else {
				localIPv6s.add(ip);
			}
			
		} else {
			if(ip instanceof Inet4Address) {
				remoteIPv4s.add(ip);
			} else {
				remoteIPv6s.add(ip);
			}
		}
	}
	
	private void findUeIP(Map<InetAddress,Integer> ipCount) {
		Integer cnt = 0;
		
		for(Map.Entry<InetAddress, Integer> entry: ipCount.entrySet()) {
			if(entry.getValue() > cnt) {
				ueIP = entry.getKey();
			}			
		}
	}
	
	private void doubleCheckDirection(Packet[] packets) {
		for(int i=0; i<packets.length; i++) {
			IPPacket p ;
		
			if (packets[i] instanceof IPPacket) {
				p = (IPPacket) packets[i];
				if (p.getLinkDirection() == "Unknown") {
					if(ueIP.equals(p.getSrcIP())) {
						p.setLinkDirection("UL");
					}
					if(ueIP.equals(p.getDstIP())) {
						p.setLinkDirection("DL");
					}
				}				
			}
			
		}
	}
	
	// See http://apistore.baidu.com/apiworks/servicedetail/114.html	IP归属地查询
	public void baiduIPs() {
		
		String httpUrl = "http://apis.baidu.com/apistore/iplookupservice/iplookup?ip=";
		String httpArg;
		String json;
		Gson gson = new Gson();
		BaiduIPInfo ipInfo;
		for(InetAddress ip:remoteIPv4s) {
			httpArg = ip.getHostAddress();
			json = request(httpUrl, httpArg);
			
			JSONObject jsonObject = JSONObject.fromObject(json);
			int errNum = (int)jsonObject.get("errNum");
			if(errNum==0){
				ipInfo = gson.fromJson(json,BaiduIPInfo.class);
				ipInfos.put(ip, ipInfo);
			}else{
				log.error(httpArg+" = "+jsonObject);
			}
		}
	}

	/**
	 * 
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	private String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl +  httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "cb4b9b822ef517861e0392b5b727631d");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	public Map<InetAddress, BaiduIPInfo> getIpInfos() {
		return ipInfos;
	}
}
