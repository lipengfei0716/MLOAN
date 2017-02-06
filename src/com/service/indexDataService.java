package com.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.util.base.ActionBase;

public class indexDataService extends ActionBase{
	
	/**
	 * @descriotion 升序
	 * @param list
	 * @return
	 */
	public Map<Integer, Map<Integer, Integer>> startAsc(List<BigDecimal> list) {
		
		Map<Integer, Map<Integer, Integer>> ipNew = new HashMap<>();
		int ls = list.size();
		for(int i=0;i<ls;i++){
			Map<Integer, Integer> News = new HashMap<>();
			for(int j=1;j<ls;j++){
				BigDecimal curr1 = list.get(j-1);
				BigDecimal curr2= list.get(j);

				if(curr1.compareTo(curr2)==1){
					News.put(j-1, j);
					list.set(j-1, curr2);
					list.set(j, curr1);
				}
			}
			ipNew.put(i, News);
		}
        return ipNew;
	}
	public List<String> ListStrAsc(List<String> list,Map<Integer, Map<Integer, Integer>> ipNew) {
		
		String t1 = "";
		String t2 = "";
		for(Entry<Integer, Map<Integer, Integer>> entry:ipNew.entrySet()){    
			
			for(Entry<Integer, Integer> entrys:entry.getValue().entrySet()){    
				
				 int key = entrys.getKey();
				 int value = entrys.getValue();
				 t1 = list.get(key);  
			     t2 = list.get(value);  
			     list.set(key, t2);
				 list.set(value, t1);
			}   
		}   
        return list;
	}
	/**
	 * @descriotion 降序
	 * @param list
	 * @param fIN 
	 * @param delay 
	 * @param interactive 
	 * @param fIRST_DATA 
	 * @param sYN 
	 * @param interval 
	 * @param dNS 
	 * @param y 
	 * @return
	 */
	public Map<String, Object> startDesc(List<BigDecimal> START, List<String> y, List<BigDecimal> DNS, List<BigDecimal> Interval, 
							List<BigDecimal> SYN, List<BigDecimal> FIRST_DATA, List<BigDecimal> Interactive, 
							List<BigDecimal> Delay, List<BigDecimal> FIN) {
		Map<String, Object> strS = new HashMap<String, Object>();
		
		START 		= listBgDesc(START);
		y 	  		= listStrDesc(y);
		DNS	   		= listBgDesc(DNS);
		Interval 	= listBgDesc(Interval);
		SYN		 	= listBgDesc(SYN);
		FIRST_DATA	= listBgDesc(FIRST_DATA);
		Interactive	= listBgDesc(Interactive);
		Delay		= listBgDesc(Delay);
		FIN			= listBgDesc(FIN);
		
		String str1,str2;
		BigDecimal curr1,curr2;
		int ls = START.size();
		for(int i=0;i<ls;i++){
			for(int j=0;j<ls-1;j++){
				
				curr1 = START.get(j);
				curr2= START.get(j+1);

				if(curr1.compareTo(curr2)==-1){

					START.set(j, curr2);
					START.set(j+1, curr1);
					
					str1 = y.get(j);
					str2= y.get(j+1);
					y.set(j, str2);
					y.set(j+1, str1);

					curr1 = DNS.get(j);
					curr2= DNS.get(j+1);
					DNS.set(j, curr2);
					DNS.set(j+1, curr1);

					curr1 = Interval.get(j);
					curr2= Interval.get(j+1);
					Interval.set(j, curr2);
					Interval.set(j+1, curr1);

					curr1 = SYN.get(j);
					curr2= SYN.get(j+1);
					SYN.set(j, curr2);
					SYN.set(j+1, curr1);

					curr1 = FIRST_DATA.get(j);
					curr2= FIRST_DATA.get(j+1);
					FIRST_DATA.set(j, curr2);
					FIRST_DATA.set(j+1, curr1);

					curr1 = Interactive.get(j);
					curr2= Interactive.get(j+1);
					Interactive.set(j, curr2);
					Interactive.set(j+1, curr1);

					curr1 = Delay.get(j);
					curr2= Delay.get(j+1);
					Delay.set(j, curr2);
					Delay.set(j+1, curr1);

					curr1 = FIN.get(j);
					curr2= FIN.get(j+1);
					FIN.set(j, curr2);
					FIN.set(j+1, curr1);
				}
			}
		}
		
		
		 String list_y = "[{name: '开始时间(ms)',type: 'bar',stack:  '总量',"
                 + "itemStyle: {normal: {barBorderColor: 'rgba(0,0,0,0)',color: 'rgba(0,0,0,0)'},"
                 + "emphasis: {barBorderColor: 'rgba(0,0,0,0)',color: 'rgba(0,0,0,0)'}}," + "data: "
                 + JSON.toJSONString(START) + ",barWidth: '50%'},";
         list_y += " {name: 'DNS Lookup(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(DNS) + "},";
         list_y += " {name: 'Interval(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(Interval) + "},";
         list_y += " {name: 'SYN(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(SYN) + "},";
         list_y += " {name: 'Time to First Byte(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(FIRST_DATA) + "},";
         list_y += " {name: 'Interactive(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(Interactive) + "},";
         list_y += " {name: 'Delay(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(Delay) + "},";
         list_y += " {name: 'FIN(ms)',type: 'bar',stack: '总量',label: {normal: {position: ''}},"
                 + "data:  " + JSON.toJSONString(FIN) + "}]";
		
         strS.put("list_y", list_y);
         strS.put("y", y);
        return strS;
	}
	/**
	 * @descripton list逆序
	 * @param list
	 * @return
	 */
	public List<BigDecimal> listBgDesc(List<BigDecimal> list) {
		
		List<BigDecimal> listDesc = new ArrayList<BigDecimal>();
		
		for(int i=(list.size()-1);i>=0;i--){
			listDesc.add(list.get(i));
		}
		return listDesc;
	}
	public List<String> listStrDesc(List<String> list) {
		
		List<String> listDesc = new ArrayList<String>();
		
		for(int i=(list.size()-1);i>=0;i--){
			listDesc.add(list.get(i));
		}
		return listDesc;
	}
	 /**
	 * IP 地址排序
	 * @return List<String>
	 */
	public List<String>	ipList(List<String> list) {
		
		list = listStrDesc(list);
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<String> ipList = new ArrayList<String>();
		for (String ip : list) {
			if(ip.equals("other"))
				continue;
			String[] ipArr = ip.split("\\.");
			ipList = toList(ipArr);
			String first = ipList.get(0);
			map.put(ip, Integer.parseInt(first));
		}
		Set<String> keys = map.keySet();
		List<String> ips = new ArrayList<String>();
		ips.addAll(keys);
		return ips;
	}
	/**
	 * @description ip按开始时间降序
	 * @param map
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> ipSort(Map<String, Object> map,String file) {

		BigDecimal ms = new BigDecimal(1000);
        BigDecimal zero = new BigDecimal(0);

        Map<String, Object> tcpIPs = (Map<String, Object>) map.get(file);
        Set<String> ips = tcpIPs.keySet();
        List<String> y = new ArrayList<String>();
        List<BigDecimal> START = new ArrayList<BigDecimal>();
        for (String ip : ips) {
            if (ip.equals("tcpGraph") || ip.equals("无效IP"))
                continue;
            Map<String, Object> tcpIP = (Map<String, Object>) tcpIPs.get(ip);
        	
            Map<String, String> tcpInfo = (Map<String, String>) tcpIP.get(String.valueOf(0));
            BigDecimal dnsStartTs = new BigDecimal(tcpInfo.get("dnsStartTs"));
            BigDecimal dnsEndTs = new BigDecimal(tcpInfo.get("dnsEndTs"));
            BigDecimal startTs = new BigDecimal(tcpInfo.get("startTs"));
            
            if (startTs.compareTo(dnsEndTs) == 1 && dnsStartTs.compareTo(zero) == 1)
        		START.add(dnsStartTs.multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
            else
            	START.add(startTs.multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));

            y.add(ip);
        }
        Map<Integer, Map<Integer, Integer>> ipNew = startAsc(START);
        ListStrAsc(y,ipNew);
        return y;
	}
	/**
     * TODO 共用配置
     * @depcription 汇总title --通用
     * @return List<String>
     */
    public List<String> tagAll(Map<String, Object> map) {

        log.info("........编辑项，汇总title   --通用");
        List<String> norm = toList((String[]) map.get("norm"));
        List<String> list = new ArrayList<String>();
        if (norm.contains("DNS时延"))
            list.add("DNS时延");
        if (norm.contains("链路数"))
            list.add("链路数");
        if (norm.contains("包数"))
            list.add("包数");
        if (norm.contains("交互流量"))
            list.add("交互流量");
        if (norm.contains("交互时间"))
            list.add("交互时间");
        if (norm.contains("平均链路时间有效率"))
            list.add("平均链路时间有效率");
        return list;
    }

    /**
     * @depcription 明细title 、编辑项 --通用
     * @return List<String>
     */
    public List<String> detailTitle(Map<String, Object> map) {

        log.info("........明细title    --通用");
        List<String> list = new ArrayList<String>();
        List<String> norm = toList((String[]) map.get("norm"));
        if (norm.contains("测试地归属"))
            list.add("测试地归属");
        if (norm.contains("DNS时延"))
            list.add("DNS时延");

        list.add("Server IP");
        list.add("端口号");

        if (norm.contains("TCP建链时延")) // tcpInfoSum
            list.add("TCP建链时延");
        if (norm.contains("请求类别"))
            list.add("请求类别");
        if (norm.contains("Time to First Byte"))
            list.add("Time to First Byte");
        if (norm.contains("包数"))
            list.add("包数");
        if (norm.contains("交互流量"))
            list.add("交互流量");
        if (norm.contains("交互时间"))
            list.add("交互时间");
        if (norm.contains("链路时间有效率"))
            list.add("链路时间有效率");
        if (norm.contains("重传识别"))
            list.add("重传识别");
        if (norm.contains("平均RTT时间"))
            list.add("平均RTT时间");
        if (norm.contains("断链时延"))
            list.add("断链时延");
        if (norm.contains("与上条链路串并行关系"))
            list.add("与上条链路串并行关系");
        if (norm.contains("与上条链路间隔时延"))
            list.add("与上条链路间隔时延");
        if (norm.contains("串行链路频繁拆建链判断"))
            list.add("串行链路频繁拆建链判断");
        if (norm.contains("IP 归属"))
            list.add("IP 归属");
        return list;
    }
}
