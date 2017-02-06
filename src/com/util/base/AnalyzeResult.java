package com.util.base;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import resolve.analysis.DNSStream;
import resolve.analysis.HTTP;
import resolve.analysis.Result;
import resolve.analysis.TCPStream;
import resolve.decoder.Packet;
/**
 * 文件 解析结果
 */
public class AnalyzeResult extends Result{

	public AnalyzeResult(Packet[] packets) {
		super(packets);
	}
	public static Logger log = Logger.getLogger(AnalyzeResult.class);  
	
	private boolean a1 = false;
	private boolean a2 = false;
	private boolean a3 = false;
	private boolean a4 = false;
	private boolean a5 = false;
	private boolean a6 = false;
	private boolean a8 = false;
	private boolean a9 = false;
	private boolean a10 = false;
	private boolean a11 = false;
	private boolean a13 = false;
	private boolean a14 = false;
	private boolean a15 = false;
	private boolean a17 = false;
	private boolean a18 = false;
	private boolean a19 = false;
	private boolean a20 = false;
	private boolean a21 = false;
	
	private Map<String, Integer> mapRate = new HashMap<String, Integer>(); //吞吐流量
	private Map<String,Integer> mapIpFlow = new HashMap<String, Integer>();      //IP流量分布图
	private Map<String, Map<String, Object>> mapDnsIP = new HashMap<String, Map<String, Object>>();
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> tcp(List<String> list1,List<String> list2) throws IOException {
		
		log.info("日志分析........");
		target(list1);
		target(list2);
		dns();
		
		Map<String, Object> tcpIPs = new HashMap<String, Object>();
		Map<String, Object> tcpGraph = new HashMap<String, Object>();
		List<List<Integer>> ethLen = new ArrayList<List<Integer>>();
		List<String> others = new ArrayList<>();
		
		Map<String, Object> https = new HashMap<String, Object>();
		double avgRtts = 0;
		for (TCPStream tcp : tcpStreams) {
			
			String addB = tcp.getAddB().toString().substring(1);	//	Server IP
			int portA = tcp.getPortA();
			if(tcp.getPayloadLength()>0 && !tcp.isFirstFin()){
				if(a8){
					if(mapIpFlow.containsKey(addB))
						mapIpFlow.put(addB,mapIpFlow.get(addB)+(int)tcp.getAllByteLen());
					else
						mapIpFlow.put(addB,(int)tcp.getAllByteLen());
					}
			}else{
				if(a8){
					if(mapIpFlow.containsKey("other")){
						mapIpFlow.put("other", mapIpFlow.get("other")+(int)tcp.getAllByteLen());
					}else{
						mapIpFlow.put("other", (int)tcp.getAllByteLen());
					}
				}
				others.add(addB+"("+portA+")");
				continue;
			}
			
			Map<String, Object> tcpIP = new HashMap<String, Object>();
			Map<String, String> tcpInfo = new HashMap<String, String>();
			
			double fillData1Ts = tcp.getFillData1Ts();
			double fillData9Ts = tcp.getFillData9Ts(); 
			double firstFinUpTime = tcp.getFirstFinUpTime(); 
			double laseFinUpTime = tcp.getLaseFinUpTime(); 
			double close33Time = tcp.getClose33Time();
			double setup1Ts = tcp.getSetup1Ts();   //第一次握手时间
			double setup3Ts = tcp.getSetup3Ts();   //第三次握手时间
			double startTs = tcp.getStartTs();    //请求 	开始时间  (单位s)
			double endTs = tcp.getEndTs();    //请求 	结束时间  (单位s)
			int packetCnt = tcp.getPacketCnt();  // 包数
			if(a1)
				ethLen.add(tcp.getTcpEthLen());	// TCP包长集合
			if(a13)
				avgRtts = avgRtts(tcp);
			double eff_time = 0;
			Object rtt = 0;
			if(tcp.isHasSyn() && fillData1Ts>0){
				if(fillData9Ts>firstFinUpTime)
					eff_time = (fillData9Ts-fillData1Ts)/(tcp.endTs-tcp.startTs);
				else
					eff_time = (firstFinUpTime-fillData1Ts)/(tcp.endTs-tcp.startTs);
			}
			if(tcpIPs.containsKey(addB)){
				tcpIP = (Map<String, Object>) tcpIPs.get(addB);
				Map<String, Object> tcpInfoUp = (Map<String, Object>)tcpIP.get(String.valueOf(tcpIP.size()-1));	
				tcpInfo.put("端口号", String.valueOf(portA));
				tcpInfo.put("包数", String.valueOf(packetCnt));
				double dnsStartTs = 0;
				double dnsEndTs = 0;
				if(mapDnsIP.containsKey(addB)){
					dnsStartTs = (double) mapDnsIP.get(addB).get("startTs");
					dnsEndTs = (double) mapDnsIP.get(addB).get("endTs");
				}
				tcpInfo.put("dnsStartTs", String.valueOf(dnsStartTs));
				tcpInfo.put("dnsEndTs", String.valueOf(dnsEndTs));
				tcpInfo.put("startTs", String.valueOf(startTs));
				if(a2 || a20){
					if(fillData9Ts>firstFinUpTime)
						tcpInfo.put("UPendTs", String.valueOf(fillData9Ts));
					else
						tcpInfo.put("UPendTs", String.valueOf(firstFinUpTime));
					double delay = startTs-Double.parseDouble(tcpInfoUp.get("UPendTs").toString());
					if(a2)
						tcpInfo.put("与上条链路间隔时延", String.valueOf(delay));
					if(a20){
						if(delay>0)
							tcpInfo.put("与上条链路串并行关系", String.valueOf(delay));
						else
							tcpInfo.put("与上条链路串并行关系", null);
					}
				}
				if(a4)
					tcpInfo.put("TCP建链时延",String.valueOf(setup3Ts-setup1Ts));
				if(a5){
					if(setup3Ts!=0.0 && fillData1Ts>setup3Ts)
						tcpInfo.put("Time to First Byte", String.valueOf(fillData1Ts-setup3Ts));
					else
						tcpInfo.put("Time to First Byte", String.valueOf(0));
				}
				if(a14){
					if(close33Time!=0){
						if(fillData9Ts>firstFinUpTime && close33Time>firstFinUpTime){
							tcpInfo.put("断链时延", String.valueOf(close33Time-fillData9Ts));
						}else{
							tcpInfo.put("断链时延", String.valueOf(close33Time-firstFinUpTime));
						}
					}else{
						tcpInfo.put("断链时延", "0");
					}
				}
				if(a8)
					tcpInfo.put("链路IP流量", String.valueOf(tcp.getEthLength()));	
				if(a11 || a17)
					tcpInfo.put("链路时间有效率", String.valueOf(eff_time));
				if(a18 && tcp.isHTTP())
					tcpInfo.put("请求类别", JSON.toJSONString(https(tcp)));
				if(a19){
					tcpInfo.put("setup3Ts", String.valueOf(setup3Ts));
					tcpInfo.put("fillData1Ts", String.valueOf(fillData1Ts));
					tcpInfo.put("laseFinUpTime", String.valueOf(laseFinUpTime));
					tcpInfo.put("close33Time", String.valueOf(close33Time));
					tcpInfo.put("endTs", String.valueOf(endTs));
					tcpInfo.put("port", String.valueOf(tcp.getPortA()));
				}
				if(a9){
					if(tcp.isHasSyn())
						tcpInfo.put("交互流量", String.valueOf(tcp.getAllByteLen()-tcp.getHandsEachByteLen()-tcp.getClose33Len()-tcp.getClose44Len())); 
					else
						tcpInfo.put("交互流量", String.valueOf(tcp.getAllByteLen()-tcp.getClose33Len()-tcp.getClose44Len())); 
				}
				if(a10)
					tcpInfo.put("交互时间", String.valueOf(laseFinUpTime-tcp.getTime1Ts())); 
				if(a13)
					tcpInfo.put("平均RTT时间", String.valueOf(avgRtts));
				if(a21)
					tcpInfo.put("重传识别", String.valueOf(tcp.getRetranNum()));
				tcpIP.put(String.valueOf(tcpIP.size()), tcpInfo);	// 根据mapTcpIP.size()将初始调整为1(else中key判断)
			}else{
				tcpInfo.put("端口号", String.valueOf(tcp.getPortA()));
				tcpInfo.put("包数", String.valueOf(packetCnt));
				double dnsStartTs = 0.0;
				double dnsEndTs = 0.0;
				if(mapDnsIP.containsKey(addB)){
					dnsStartTs = (double) mapDnsIP.get(addB).get("startTs");
					dnsEndTs = (double) mapDnsIP.get(addB).get("endTs");
				}
				tcpInfo.put("dnsStartTs", String.valueOf(dnsStartTs));
				tcpInfo.put("dnsEndTs", String.valueOf(dnsEndTs));
				tcpInfo.put("startTs", String.valueOf(startTs));
				if(a2){
					if(fillData9Ts>firstFinUpTime)
						tcpInfo.put("UPendTs", String.valueOf(fillData9Ts));
					else
						tcpInfo.put("UPendTs", String.valueOf(firstFinUpTime));
				}
				if(a4)
					tcpInfo.put("TCP建链时延",String.valueOf(setup3Ts-setup1Ts));
				if(a5){
					if(setup3Ts!=0.0 && fillData1Ts>setup3Ts)
						tcpInfo.put("Time to First Byte", String.valueOf(fillData1Ts-setup3Ts));
					else
						tcpInfo.put("Time to First Byte", String.valueOf(0));
				}
				if(a14){
					if(close33Time!=0){
						if(fillData9Ts>firstFinUpTime && close33Time>firstFinUpTime){
							tcpInfo.put("断链时延", String.valueOf(close33Time-fillData9Ts));
						}else{
							tcpInfo.put("断链时延", String.valueOf(close33Time-firstFinUpTime));
						}
					}else{
						tcpInfo.put("断链时延", "0");
					}
				}
				if(a11 || a17)
					tcpInfo.put("链路时间有效率", String.valueOf(eff_time));  //每一条链路内，T_TimetoFirstByte - Time_dns_query（或Time_tcp_syn）/T_最后一个传输数据非FIN报文 - T_第一个传输数据的报文
				if(a18 && tcp.isHTTP())
					tcpInfo.put("请求类别", JSON.toJSONString(https(tcp)));
				if(a19){
					tcpInfo.put("setup3Ts", String.valueOf(setup3Ts));
					tcpInfo.put("fillData1Ts", String.valueOf(fillData1Ts));
					tcpInfo.put("laseFinUpTime", String.valueOf(laseFinUpTime));
					tcpInfo.put("close33Time", String.valueOf(close33Time));
					tcpInfo.put("endTs", String.valueOf(endTs));
					tcpInfo.put("port", String.valueOf(tcp.getPortA()));
				}
				if(a3)
					tcpInfo.put("DNS时延",rtt.toString());
				if(a9){
					if(tcp.isHasSyn())
						tcpInfo.put("交互流量", String.valueOf(tcp.getAllByteLen()-tcp.getHandsEachByteLen()-tcp.getClose33Len()-tcp.getClose44Len())); 
					else
						tcpInfo.put("交互流量", String.valueOf(tcp.getAllByteLen()-tcp.getClose33Len()-tcp.getClose44Len())); 
				}
				if(a10)
						tcpInfo.put("交互时间", String.valueOf(laseFinUpTime-tcp.getTime1Ts())); 
				if(a13)
					tcpInfo.put("平均RTT时间", String.valueOf(avgRtts));
				tcpInfo.put("链路号", "0");
				tcpIP.put("0", tcpInfo);
				if(a6)
					tcpInfo.put("IP 归属", null);
				if(a21)
					tcpInfo.put("重传识别", String.valueOf(tcp.getRetranNum()));
			}
			
			tcpIPs.put(addB, tcpIP);	
			if(a13)
				avgRtts(tcp);
			if(a15){		//吞吐曲线图 (秒)
				Map<Integer, Integer> getMapRate = tcp.getMapRate();
				if(getMapRate!=null){
					for (Integer key : getMapRate.keySet()) {
						if(mapRate!=null && mapRate.containsKey(key.toString())){
							mapRate.put(key.toString(), mapRate.get(key.toString())+getMapRate.get(key));
						}else{
							mapRate.put(key.toString(), getMapRate.get(key));
						}
					}
				}
			}
		}
		if(a1)
			tcpGraph.put("包长分布图", ethLen);
		if(a8)
			tcpGraph.put("IP流量分布图", mapIpFlow);	
		if(a15)
			tcpGraph.put("吞吐曲线图", mapRate);
		if(a19)
			tcpGraph.put("链路图", https);
		tcpIPs.put("tcpGraph", tcpGraph);
		tcpIPs.put("无效IP", others);
		return tcpIPs;
	}
	/**
	 * dns信息采集
	 */
	public void dns() {
		
		for (DNSStream dns : dnsStreams) {	
			for (InetAddress ip : dns.getIPs()) {
				if(!mapDnsIP.containsKey(ip.toString().substring(1))){
					Map<String, Object> dnsInfo = new HashMap<String, Object>();
					dnsInfo.put("startTs", dns.getStartTs());	//  dns询问时间
					dnsInfo.put("endTs", dns.getEndTs());	//  dns结束时间
					dnsInfo.put("rtt", dns.getDuration());		//  DNS时延
					mapDnsIP.put(ip.toString().substring(1), dnsInfo);
				}
			}
		}
	}
	/**
	 * 平均RTT时间
	 * @return 
	 */
	private double avgRtts(TCPStream tcp) {
		double rtts = 0.0;	// rtts （Time_收到ACK包-Time_发送包）总时间
		double rttsNum = 0.0;	// rtts时间  个数
		double avgNum = 0;
		for (Double arg : tcp.getRTTs()) {
			rtts += arg;
			rttsNum++;
		}
		if(rttsNum!=0.0){
			avgNum = rtts/rttsNum;
		}
		return avgNum;
	}
	/**
	 * 请求类别
	 * @return 
	 */
	private Map<Integer, Object> https(TCPStream tcp) {

		Map<Integer,Object> https = new HashMap<Integer,Object>();
		int i=0;
		for (HTTP http : tcp.getHTTPs()) {
			String info = http.getInfo();
            switch(info.substring(0, 4)){
             case "HTTP": break;
             default: 
        	    Map<String,String> infos = new HashMap<String,String>();
            	infos.put("info",info);
				https.put(i++,infos);
            }
		}
		return https;
	}
	/**
	 * 判断要获取的指标
	 */
	private void target(List<String> list) {
		
		if(list.contains("包长分布图"))
			a1 = true;
		if(list.contains("与上条链路间隔时延"))
			a2 = true;
		if(list.contains("DNS时延"))
			a3 = true;
		if(list.contains("TCP建链时延"))
			a4 = true;
		if(list.contains("Time to First Byte"))
			a5 = true;
		if(list.contains("IP 归属"))
			a6 = true;
		if(list.contains("IP流量分布图"))
			a8 = true;
		if(list.contains("交互流量"))
			a9 = true;
		if(list.contains("交互时间"))
			a10 = true;
		if(list.contains("链路时间有效率"))
			a11 = true;
		if(list.contains("平均RTT时间"))
			a13 = true;
		if(list.contains("断链时延"))
			a14 = true;
		if(list.contains("吞吐曲线图"))
			a15 = true;
		if(list.contains("平均链路时间有效率"))
			a17 = true;
		if(list.contains("请求类别"))
			a18 = true;
		if(list.contains("链路图"))
			a19 = true;
		if(list.contains("与上条链路串并行关系"))
			a20 = true;
		if(list.contains("重传识别"))
			a21 = true;
	}
}
