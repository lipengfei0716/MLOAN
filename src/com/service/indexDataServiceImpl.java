package com.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.util.base.ActionBase;

import net.sf.json.JSONObject;

public class indexDataServiceImpl extends ActionBase {

	private indexDataService ids = new indexDataService();
    private Map<String, String> minSumList = new HashMap<String, String>();

    private Map<String, Double> compareList = new HashMap<String, Double>();

    private DecimalFormat dfAfter = new DecimalFormat("0");
    private DecimalFormat dfAfter2 = new DecimalFormat("0.00");
    
    private BigDecimal perc = new BigDecimal(100);
    /* BigDecimal.compareTo(args) -1表示小于,0是等于,1是大于*/
    /**
     * 编辑项（通用）
     * 
     * @return
     */
    public String norm_edit(Map<String, Object> map) {

        log.info("........编辑项--通用");
        List<String> list = ids.detailTitle(map);
        String edit = "";
        int i = 1;
        int no = 0;
        for (String arg : list) {
        	switch (arg) {
			case "Server IP": i++; break;
			case "端口号": i++; break;
			default: edit += "<input type='checkbox' id='comm" + i
					 + "' class='boxrule' checked='true' onclick=\"changeTd('tag1','tb1Detail'," + i + "," + no++ + ")\"/>";
					 edit += "<label for='comm" + i++ + "'></label>" + arg + "<br/>";
			}
        }
        return edit;
    }

    /**
     * TODO 按业务-汇总数据 --通用 response 展示数据
     * <table>
     * <tr>
     * <td></td>...
     * </tr>
     * ...
     * </table>
     * 最小值集合 Map<String, String> minSumList
     */
    @SuppressWarnings("unchecked")
   
    public Map<String, String> normSum(Map<String, Object> map) {

        log.info("........汇总数据拼接--通用");
        List<String> filecheck = toList((String[]) map.get("filecheck"));
        List<String> list = ids.tagAll(map);
        String th = "<tr style='white-space: nowrap;'><td style='text-align: center;background:#a1dbf6;'>业务名</td>"
                + "<td style='text-align: center;border-left: solid 1px #eee;background:#a1dbf6;'>Server IP</td>";
        for (String title : list) {
            switch (title) {
            case "链路数":
                th += "<td style='text-align: center;border-left: solid 1px #eee;background:#a1dbf6;'>"
                        + title + "</td>";
                break;
            case "包数":
                th += "<td style='text-align: center;border-left: solid 1px #eee;background:#a1dbf6;'>"
                        + title + "(个)</td>";
                break;
            case "交互流量":
                th += "<td style='text-align: center;border-left: solid 1px #eee;background:#a1dbf6;'>"
                        + title + "(byte)</td>";
                break;
            case "平均链路时间有效率":
                th += "<td style='text-align: center;border-left: solid 1px #eee;background:#a1dbf6;'>"
                        + title + "(%)</td>";
                break;
            default:
                th += "<td style='text-align: center;border-left: solid 1px #eee;background:#a1dbf6;'>"
                        + title + "(ms)</td>";
            }
        }
        th += "</tr>";
        // data
        String td = "";
        String norm = "";
        Map<String, String> sumBusi = new HashMap<String, String>();
        for (String file : filecheck) {
        	
            Map<String, Object> tcpIPs = (Map<String, Object>) map.get(file);
            List<String> ipSort = ids.ipSort(map, file);
            boolean firstIp = true;
            String ipStr = "";
            for (String ip : ipSort) { // ip 集合拼接

                  if (firstIp) {
                      ipStr += ip;
                      firstIp = false;
                  } else
                      ipStr += "、" + ip;
            }
            int links = 0;
            for (String ip : ipSort) { // 该业务中数据求和

                Map<String, Object> tcpIP = (Map<String, Object>) tcpIPs.get(ip);
                // 数据过滤，并汇总有效数据(判断无效链路)
                for (int i = 0; i < tcpIP.size(); i++) {

                	Map<String, String> tcpInfo = (Map<String, String>) tcpIP.get(String.valueOf(i));
					if(links==0) {
						for (String arg : list) {
							switch (arg) {
								case "链路数":	break;
								case "平均链路时间有效率":	sumBusi.put(arg, tcpInfo.get("链路时间有效率")); break;
								case "DNS时延":	if(i==0)
													sumBusi.put(arg, String.valueOf(Double.parseDouble(tcpInfo.get(arg))));
												break;
								default:	sumBusi.put(arg, tcpInfo.get(arg));
							}
						}
					}else{
						for (String arg : list) {
							double ti = 0;
							double sb = 0;
							switch (arg) {
								case "链路数": break;
								case "平均链路时间有效率":	ti = Double.parseDouble(tcpInfo.get("链路时间有效率"));
															sb = Double.parseDouble(sumBusi.get(arg));
															sumBusi.put(arg, String.valueOf(ti+sb));
															break;
								case "DNS时延":	if(i==0)
													sumBusi.put(arg, String.valueOf(Double.parseDouble(tcpInfo.get(arg))+Double.parseDouble(sumBusi.get(arg))));
												break;
								default: sumBusi.put(arg, String.valueOf(Double.parseDouble(tcpInfo.get(arg))+Double.parseDouble(sumBusi.get(arg))));
							}
						}
					}
					links++;
				}
			}
			td +="<tr><td style='border-top: solid 1px #eee;'>"+file+"</td><td style='border-top: solid 1px #eee;border-left: solid 1px #eee;'>"+ipStr+"</td>";
			for (String key : list) {		//四舍五入   保留两位
				String value = sumBusi.get(key);
				String tdId  = file+key;
				if(key.equals("链路数")){
					td +="<td id='"+tdId+"' style='border-left: solid 1px #eee;border-top: solid 1px #eee;'>"+links+"</td>";
					minList(key,links,tdId);
					continue;
				}
				if(value==null){
					td +="<td id='"+tdId+"' style='border-left: solid 1px #eee;border-top: solid 1px #eee;'></td>";
				}else{
					double 	valueDb = Double.parseDouble(value);
					switch (key) {
						case "包数":	 
						case "交互流量":	td +="<td id='"+tdId+"' style='border-left: solid 1px #eee;border-top: solid 1px #eee;'>"+dfAfter.format(valueDb)+"</td>";
										break;
						case "平均链路时间有效率":	BigDecimal timeFill = new BigDecimal(sumBusi.get("平均链路时间有效率"));
													BigDecimal num = new BigDecimal(links);
													BigDecimal avgFill  = timeFill.multiply(perc).divide(num,2,BigDecimal.ROUND_HALF_UP);
													td +="<td id='"+tdId+"' style='border-left: solid 1px #eee;border-top: solid 1px #eee;'>"+dfAfter2.format(Double.parseDouble(avgFill.toString()))+"</td>";
													break;
						default:  td +="<td id='"+tdId+"' style='border-left: solid 1px #eee;border-top: solid 1px #eee;'>"+dfAfter2.format(valueDb*1000)+"</td>";
					}
					minList(key,valueDb,tdId);
				}
			}
			td +="</tr>";
		}
        Map<String, String> detail = normDetail(map);
		norm +=detail.get("fill");
		norm +="<tr><td id=\"tb1DetailTen\" style='border-top: solid 1px #cccccc;cursor: pointer;color: blue;' colspan='"+(detail.get("DataSize")+1)+"' onclick=\"nullIP()\">&nbsp;&nbsp;&nbsp;+&nbsp;无效IP</td></tr>";
		norm +="<tr id='nullIp' style='white-space: normal;border-top: solid 1px #eee;display:none'><td colspan='"+(detail.get("DataSize")+1)+"' >"+detail.get("effec")+"</td></tr>";
		Map<String, String> res = new HashMap<String, String>();
		res.put("norm", norm);
		res.put("table", th+td);
		res.put("minSumList", JSON.toJSONString(minSumList));
		return res;
	}
    // 最小值数据
    public void minList(String key,double vlue,String keyTemp){
    	
    	if(compareList==null || !compareList.containsKey(key)){
			compareList.put(key, vlue);
			minSumList.put(key, keyTemp);
		}else{
			if(compareList.get(key)>vlue){
				compareList.put(key, vlue); 
				minSumList.put(key, keyTemp);
			}
		}
    }
	/**
	 * TODO 详细数据	--通用
	 * response			展示数据		<table> <tr><td></td>...</tr>... </table>
	 * 					最小值集合	Map<String, String> minSumList
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> normDetail(Map<String, Object> map){
		
		log.info("........详细数据拼接--通用");
		
		List<String> filecheck = toList((String[])map.get("filecheck"));
		List<String> list = ids.detailTitle(map);
		//  title   
		String th = "<tr><td style='text-align: center;padding-top:5px;padding-bottom:5px;background:#a1dbf6;font-12px'>业务名</td>";
		for (String title : list) {
			switch(title){
				case "测试地归属":th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>"+title+"</td>"; break;
				case "Server IP":
				case "端口号":
				case "请求类别":th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>"+title+"</td>"; break;
				case "TCP建链时延": th +="text-align: center;<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>TCP建链<br/> 时延(ms)</td>"; break;
				case "包数":th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>"+title+"(个)</td>"; break;
				case "交互流量":th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>"+title+"<br>(byte)</td>";break;
				case "Time to First Byte": th +="text-align: center;<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>Time to First<br/> Byte(ms)</td>"; break;
				case "链路时间有效率": th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>链路时间有<br/>效率(%)</td>"; break;
				case "重传识别": th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>重传识别<br/>(次)</td>"; break;
				case "与上条链路串并行关系": th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>与上条链路<br/>串并行关系</td>";break;
				case "与上条链路间隔时延": th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>与上条链路<br/>间隔时延(ms)</td>"; break;
				case "串行链路频繁拆建链判断": th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>串行链路频繁拆<br/>建链判断(0~2s)</td>"; break;
				case "IP 归属": th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>IP 归属</td>"; break;
				default:th +="<td style='text-align: center;padding-top:5px;padding-bottom:5px;border-left: solid 1px #ffffff;background:#a1dbf6;'>"+title+"(ms)</td>";
			}
		}
		th +="</tr>";
		//  detailData
		String fill = th;	// 有效链路
		String effec = "";  // 无效链路
		boolean fileNo = true;
		int fi = 0;
		for (String file : filecheck) {
	           
			int fiNo = 1;
			Map<String, Object> tcpIPs = (Map<String, Object>) map.get(file);
			 List<String> ipSort = ids.ipSort(map, file);
			for (String ip : ipSort) {

				Map<String, Object> tcpIP = (Map<String, Object>) tcpIPs.get(ip);
				//  data
				boolean ip1No = true;
				int n=0;
				for (int i = 0; i < tcpIP.size(); i++) {
					Map<String, Object> tcpInfo = (Map<String, Object>) tcpIP.get(String.valueOf(i));
					switch (fiNo) {
						case 1:    fill +="<tr><td style='padding-top:5px;padding-bottom:5px;border-top: solid 1px #cccccc;'>"+file+"</td>"; fiNo++; break;
						default:   fill +="<tr><td style='padding-top:5px;padding-bottom:5px;'></td>"; 
					}
					for (String arg : list) {
						Object a = "";
						double time = 0;
						if(i==0){
							switch(arg){
							case "DNS时延":	time = Double.valueOf(tcpInfo.get(arg).toString());
											if(time==0)
												fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>N/A</td>";
											else
												fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(time*1000)+"</td>";
											break;
							case "与上条链路串并行关系": fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'></td>";
											  break;
							case "与上条链路间隔时延":  fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'></td>";
													 break;
							case "串行链路频繁拆建链判断": 	fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'></td>";
															break;
							case "IP 归属": a = tcpInfo.get(arg);
											if(a==null)
												fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;' name='ipAddr' id='"+String.valueOf(fi++)+"-"+ip+"'>N/A</td>";
											else
												fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+a+"</td>";
											break;
							}
						}
						if(i>0){
							switch(arg){
							case "DNS时延": fill +="<td  style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;'></td>";
											break;
							case "与上条链路串并行关系": a = tcpInfo.get(arg);
											  if(a == null){
												fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>并行</td>";
												n=0;
											  }else{
												time = Double.valueOf(a.toString())*1000;
												fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>串行</td>";
												if(time<=2000 && time>=0)
													n++;
												else
													n=0;
											  }
											 break;
							case "与上条链路间隔时延":  	time = Double.valueOf(tcpInfo.get(arg).toString())*1000;
													 	fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(time)+"</td>";
													 	break;
							case "串行链路频繁拆建链判断": 	if(n>=2){
																fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>Y</td>";
															}else
																fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>N</td>";
															break;
							case "IP 归属": fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;'></td>";
											break;
							}
						}
						switch(arg){
						case "测试地归属": fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'></td>";
										  break;
						case "Server IP": if(ip1No){
												fill +="<td  style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+ip+"</td>";
												ip1No = false;
										  }else
												fill +="<td  style='border-left: solid 1px #cccccc;'></td>";
										  break;
						case "端口号":  
						case "包数":		
						case "交互流量":	  fill +="<td  style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter.format(Double.valueOf(tcpInfo.get(arg).toString()))+"</td>";
									      break;
						case "交互时间":	  a = tcpInfo.get(arg).toString();
										  fill +="</td><td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(Double.valueOf(tcpInfo.get(arg).toString())*1000)+"</td>";
										  break;
						case "链路时间有效率": 	time = Double.valueOf(tcpInfo.get(arg).toString());
												if(time==0)
													fill +="<td  style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>N/A</td>";
												else
													fill +="<td  style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(time*100)+"</td>";
												break;
						case "重传识别": fill +="<td  style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+tcpInfo.get(arg)+"</td>";
										 break;
						case "断链时延":  double chain = Double.valueOf(tcpInfo.get(arg).toString());
										 if(chain>0){
										 	fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(chain*1000)+"</td>";
										 }else
									 		fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>N/A</td>";
					      				 break;
						case "请求类别": Map<String, String> httpInf = httpInfo(tcpInfo);
										fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;' title='"+httpInf.get("infos")+"'>"+httpInf.get("part")+"</td>";
										break;
						case "TCP建链时延": 
						case "Time to First Byte": 	time = Double.valueOf(tcpInfo.get(arg).toString());
													if(time==0)
														fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>N/A</td>";
													else
														fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(time*1000)+"</td>";
													break;
						case "平均RTT时间": fill +="<td style='padding-top:5px;padding-bottom:5px;border-left: solid 1px #cccccc;border-top: solid 1px #cccccc;'>"+dfAfter2.format(Double.valueOf(tcpInfo.get(arg).toString())*1000)+"</td>";
						}
					}
					fill += "</tr>";
				}
			}
			String effecIp = tcpIPs.get("无效IP").toString();
			if(fileNo){
				effec += effecIp.toString().substring(1, effecIp.length()-1);
				fileNo = false;
			}else{
				effec = effec+","+effecIp.toString().substring(1, effecIp.length()-1);
			}
		}
		Map<String, String> res = new HashMap<String, String>();
		res.put("effec",  effec);
		res.put("fill", fill);
		res.put("DataSize", String.valueOf(list.size()));
		return res;
	}
	public Map<String, String> httpInfo(Map<String, Object> tcpInfo){
		Object https = tcpInfo.get("请求类别");
		Map<String,String> httpIf = new HashMap<String,String>();
		JSONObject httpsInfo =  JSONObject.fromObject(https);
		String infos = "";
		String part = "";
		for(int j=0;j<httpsInfo.size();j++){
			if(j==0){
				String in1 = JSONObject.fromObject(httpsInfo.get(String.valueOf(j))).get("info").toString();
				if(in1.length()>30){
					part = in1.substring(0, 25);
				}else
					part = in1;
			}
			String info = JSONObject.fromObject(httpsInfo.get(String.valueOf(j))).get("info").toString();
			String txt= "";
			int k=0;
			int len = info.length();
			while(k<len){
				if(k>0)
					txt += "<br/>";
				for(int k1=0;k1<100;k1++){
					if(k++<len){
						txt += info.substring(k-1, k);
					}else
						break;
				}
			}
		    if(j==0){
		    	infos += txt;
		    }else
		    	infos += "<br/>"+txt;
		}
		httpIf.put("infos", infos);
		httpIf.put("part", part);
		return httpIf;
	}
	
	/**
	 * TODO 图形数据
	 */
	public String norm_graph(Map<String, Object> map){
		
		log.info("........图形数据");
		
		Map<String, String> graph = new HashMap<String, String>();
		List<String> norm = toList((String[])map.get("norm"));
		if(norm.contains("包长分布图"))
			graph.put("包长分布图", packetLenData(map));
		if(norm.contains("吞吐曲线图"))
			graph.put("吞吐曲线图", hufPufFlow(map));
		if(norm.contains("IP流量分布图"))
			graph.put("IP流量分布图", ipFlowData(map));
		if(norm.contains("链路图"))
			graph.put("链路图", ipLinkData(map));
		return JSON.toJSONString(graph);
	}
	/**
	 * @param 包长分布图
	 */
	@SuppressWarnings("unchecked")
	public String packetLenData(Map<String, Object> map){
		
		log.info("........包长分布图");
		//包长分布图
		List<String> filecheck = toList((String[])map.get("filecheck"));
		List<String> res = new ArrayList<>();
		
		Iterator<String> files = filecheck.iterator();
	    for (int i = 0; i < filecheck.size(); i++) {
	    	
	    	int x1 = 0;
			int x2 = 0;
			int x3 = 0;
			int small = 9999;
			int max   = 0;
		    String file = files.next();
			Map<String, Object> tcpGraph = (Map<String, Object>) ((Map<String, Object>) map.get(file)).get("tcpGraph");
			List<Object> packetLenS = (List<Object>) tcpGraph.get("包长分布图");
			Iterator<Object> plsKeys = packetLenS.iterator();
			while (plsKeys.hasNext()) {
				String arrStr = plsKeys.next().toString();
				List<String> arrs = toList(arrStr.substring(0, arrStr.length()-1).split(","));
				Iterator<String> lens = arrs.iterator();
				
				int len = 0;
				while (lens.hasNext()) {
					String lenStr = lens.next().substring(1);
					if(lenStr.equals(""))
						continue;
					len = Integer.parseInt(lenStr);
					if(len<small)
						small = len;
					if(len>max)
						max = len;
					if(0<=len && len<=100){
						x1++;
					}else if(100<len && len<=1000){
						x2++;
					}else{
						x3++;
					}
				}
			}
			Map<String, Integer> data = new HashMap<String,Integer>();
			data.put("0-100", x1);
			data.put("101-1000", x2);
			data.put("1001-"+String.valueOf(max), x3);
		
			List<String> part = new ArrayList<>();
			List<String> x = new ArrayList<>();
			part.add("0-100");
			part.add("101-1000");
			part.add("1001-"+String.valueOf(max));
			int ii = i+1;
			String id = "bpacketLen"+ii;	
			String list_y = "[";
			int z =0;
			for(String key:part){
				x.add("'"+key+"Byte'");
				Integer value = data.get(key);
				if(z!=0)
					list_y+=",";
				if(value==null)
					list_y+="{value:"+0+",name:'"+key+"Byte'}";
				else
					list_y+="{value:"+value+",name:'"+key+"Byte'}";
				z++;
			}
			list_y+="]";
			String subtext = "最小:"+small+"最大:"+max;
			Map<String, String> pld = new HashMap<String, String>();
			pld.put("id", id);
			pld.put("x", x.toString());
			pld.put("list_y", list_y);
			pld.put("title", file);
			pld.put("subtext", subtext);
			res.add(JSON.toJSONString(pld));
		}
		return JSON.toJSONString(res);
	}
	/**
	 * @param 吞吐曲线图
	 *  中文排序
	 *  Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
    	Collections.sort(list, cmp);
    	倒序
    	Collections.reverse(list);
	 */
	@SuppressWarnings("unchecked")
	public String hufPufFlow(Map<String, Object> map){		//吞吐曲线图

        log.info("........吞吐曲线图");

        String id = "cuse2"; // 折线图堆叠

        List<Integer> x = new ArrayList<Integer>();
        List<String> filecheck = toList((String[]) map.get("filecheck"));

        Iterator<String> files = filecheck.iterator();
        while (files.hasNext()) {

            Map<String, Object> tcpGraph = (Map<String, Object>) ((Map<String, Object>) map.get(files.next()))
                    .get("tcpGraph");
            Map<String, Integer> flowInfo = (Map<String, Integer>) tcpGraph.get("吞吐曲线图");
            Set<String> keys = flowInfo.keySet();
            for (String key : keys) {
                int k = Integer.parseInt(key);
                if (!x.contains(k))
                    x.add(k);
            }
        }
        Collections.sort(x);
        Map<String, String> y = new HashMap<String, String>();
        files = filecheck.iterator();
        while (files.hasNext()) {

            String file = files.next();
            List<Integer> list_y = new ArrayList<Integer>();
            Map<String, Object> tcpGraph = (Map<String, Object>) ((Map<String, Object>) map.get(file))
                    .get("tcpGraph");
            Map<Integer, Integer> flowInfo = (Map<Integer, Integer>) tcpGraph.get("吞吐曲线图");

            for (Integer key : x) {
            	Integer value = flowInfo.get(key.toString());
            	if(value!=null)
            		list_y.add(value);
            	else
            		list_y.add(0);
            }
            y.put(file, list_y.toString());
        }
        String list = "[";
        String list_y = "[";
        boolean first = true;
        files = filecheck.iterator();
        while (files.hasNext()) {
            String file = files.next();
            if (first) {
                list += "'" + file + "'";
                list_y += "{name:'" + file + "',type:'line',data:" + y.get(file) + ""
                        + ",markPoint: {data: [{type: 'max', name: '最大值'},{type: 'min', name: '最小值'}]}}";
                first = false;
            } else {
                list += ",'" + file + "'";
                list_y += ",{name:'" + file + "',type:'line',data:" + y.get(file) + ""
                        + ",markPoint: {data: [{type: 'max', name: '最大值'},{type: 'min', name: '最小值'}]}}";
            }
        }
        list += "]";
        list_y += "]";
        Map<String, String> res = new HashMap<String, String>();
        res.put("id", id);
        res.put("list", list);
        res.put("x_str", x.toString());
        res.put("list_y", list_y);
        return JSON.toJSONString(res);
    }

    /**
     * @param IP流量分布图
     */
    @SuppressWarnings("unchecked")
    public String ipFlowData(Map<String, Object> map) { // IP流量分布图

        log.info("........IP流量分布图");

        List<String> res = new ArrayList<String>();
        List<String> filecheck = toList((String[]) map.get("filecheck"));

        Iterator<String> files = filecheck.iterator();
        for (int i = 0; i < filecheck.size(); i++) {

        	Map<String, Integer> flowArry = new HashMap<String, Integer>();
            String fileName = files.next();
            Map<String, Object> tcpGraph = (Map<String, Object>) ((Map<String, Object>) map.get(fileName))
                    .get("tcpGraph");
            Map<String, Integer> mapIpFlow = (Map<String, Integer>) tcpGraph.get("IP流量分布图");
            Set<String> ips = mapIpFlow.keySet();
            for (String ip : ips) {
                flowArry.put(ip, mapIpFlow.get(ip));
            }

            List<String> x = ids.ipSort(map, fileName);
            String list_y = "[";
            List<Integer> y = new ArrayList<Integer>();
            for (String ip : x) {
                y.add(flowArry.get(ip));
            }
            x.add("other");
            y.add(flowArry.get("other"));
            list_y += "{name:'IP流量分布图',type:'bar',barWidth: '10%',data:" + y.toString() + "}";
            // list_y += "{name:'IP流量分布图',type:'bar',barWidth: '10%',data:" + y.toString() + ",label: {normal: {show: true,position: 'top'}}}";
            list_y += "]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("title",fileName);
            int ii = i + 1;
            data.put("id", "aipFlow" + ii);
            data.put("x", JSON.toJSONString(x));
            data.put("list_y", list_y);
            res.add(JSON.toJSONString(data));
        }
        return JSON.toJSONString(res);
    }

    /**
     * @param 链路图
     */
    @SuppressWarnings("unchecked")
    public String ipLinkData(Map<String, Object> map) { // 链路图

        log.info("........链路图");
        List<String> res = new ArrayList<String>();
        BigDecimal ms = new BigDecimal(1000);

        List<String> filecheck = toList((String[]) map.get("filecheck"));
        Iterator<String> files = filecheck.iterator();
        BigDecimal zero = new BigDecimal(0);
        StringBuffer ipPort = new StringBuffer();
        
        for (int i = 0; i < filecheck.size(); i++) {

            String file = files.next();
            Map<String, Object> tcpIPs = (Map<String, Object>) map.get(file);
            Set<String> ips = tcpIPs.keySet();
            BigDecimal endTsLast = new BigDecimal(0); 
            for (String ip : ips) {
            	switch (ip) {
				case "tcpGraph": break;
				case "无效IP": break;
				default:
					 Map<String, Object> tcpIP = (Map<String, Object>) tcpIPs.get(ip);

		                for (int j = 0; j < tcpIP.size(); j++) {

		                    Map<String, String> tcpInfo = (Map<String, String>) tcpIP.get(String.valueOf(j));
		                    if (Integer.parseInt((tcpInfo.get("包数").toString())) > 3){
		                        BigDecimal endTs = new BigDecimal(tcpInfo.get("endTs"));
		                    	if(endTsLast.compareTo(endTs)==-1)
		                    		endTsLast = new BigDecimal(tcpInfo.get("endTs"));
		                    }
		                }
				}
            }
            List<String> y = new ArrayList<String>();
            List<BigDecimal> START = new ArrayList<BigDecimal>();
            List<BigDecimal> DNS = new ArrayList<BigDecimal>();
            List<BigDecimal> Interval = new ArrayList<BigDecimal>();
            List<BigDecimal> SYN = new ArrayList<BigDecimal>();
            List<BigDecimal> FIRST_DATA = new ArrayList<BigDecimal>();
            List<BigDecimal> Interactive = new ArrayList<BigDecimal>();
            List<BigDecimal> Delay = new ArrayList<BigDecimal>();
            List<BigDecimal> FIN = new ArrayList<BigDecimal>();
            for (String ip : ips) {
                if (ip.equals("tcpGraph") || ip.equals("无效IP"))
                    continue;
                Map<String, Object> tcpIP = (Map<String, Object>) tcpIPs.get(ip);
                for (int j = 0; j < tcpIP.size(); j++) {
                	
                    Map<String, String> tcpInfo = (Map<String, String>) tcpIP.get(String.valueOf(j));
                    BigDecimal dnsStartTs = new BigDecimal(tcpInfo.get("dnsStartTs"));
                    BigDecimal dnsEndTs = new BigDecimal(tcpInfo.get("dnsEndTs"));
                    BigDecimal startTs = new BigDecimal(tcpInfo.get("startTs"));
                    BigDecimal setup3Ts = new BigDecimal(tcpInfo.get("setup3Ts"));
                    BigDecimal fillData1Ts = new BigDecimal(tcpInfo.get("fillData1Ts"));
                    BigDecimal laseFinUpTime = new BigDecimal(tcpInfo.get("laseFinUpTime"));
                    BigDecimal close33Time = new BigDecimal(tcpInfo.get("close33Time"));
                    BigDecimal endTs = new BigDecimal(tcpInfo.get("endTs"));
                    
                    if (startTs.compareTo(dnsEndTs) == 1 && dnsStartTs.compareTo(zero) == 1 && j==0)
                		START.add(dnsStartTs.multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                    	START.add(startTs.multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));

                    if(j==0 && startTs.compareTo(dnsEndTs) == 1){
                    	DNS.add(dnsEndTs.subtract(dnsStartTs).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    	Interval.add(startTs.subtract(dnsEndTs).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }else{
                    	DNS.add(zero);
                    	Interval.add(zero);
                    }
                    	
                    
                	if(setup3Ts.compareTo(startTs) == 1)
                		SYN.add(setup3Ts.subtract(startTs).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                	else
                		SYN.add(zero);
                	
                    if(fillData1Ts.compareTo(setup3Ts)==1)
                    	FIRST_DATA.add(fillData1Ts.subtract(setup3Ts).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else if(fillData1Ts.compareTo(startTs)==1)
                    	FIRST_DATA.add(fillData1Ts.subtract(startTs).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                    	FIRST_DATA.add(zero);
                    
                    if(laseFinUpTime.compareTo(fillData1Ts)==1)
                    	Interactive.add(laseFinUpTime.subtract(fillData1Ts).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else if(laseFinUpTime.compareTo(setup3Ts)==1)
                    	Interactive.add(laseFinUpTime.subtract(setup3Ts).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else if(laseFinUpTime.compareTo(startTs)==1)
                    	Interactive.add(laseFinUpTime.subtract(startTs).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                    	Interactive.add(zero);
                    
                	if(close33Time.compareTo(zero)==1){
                		if( close33Time.compareTo(laseFinUpTime)==1)
                			Delay.add(close33Time.subtract(laseFinUpTime).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                		else if( close33Time.compareTo(fillData1Ts)==1)
                			Delay.add(close33Time.subtract(fillData1Ts).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                		else if( close33Time.compareTo(setup3Ts)==1)
                			Delay.add(close33Time.subtract(setup3Ts).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                		else if( close33Time.compareTo(startTs)==1)
                			Delay.add(close33Time.subtract(startTs).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                	}else
                		Delay.add(zero);
                		
                    if(close33Time.compareTo(zero)==1)
                    	FIN.add(endTs.subtract(close33Time).multiply(ms).setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                    	FIN.add(zero);
                    String ipY = ip+"("+tcpInfo.get("port")+")";
                    y.add(ipY);
                }
            }
            Map<String, Object> maps = ids.startDesc(START,y,DNS,Interval,SYN,FIRST_DATA,Interactive,Delay,FIN);
            y = (List<String>) maps.get("y");
            for(int k=y.size()-1;k>=0;k--){
            	
            	 ipPort.append(y.get(k)+"<br/>");
            }
            int ii = i + 1;
            String id = "zlink" + ii;
           
            Map<String, String> link = new HashMap<String, String>();
            link.put("title", file);
            link.put("id", id);
            link.put("ipPort", ipPort.toString());
            ipPort.delete(0, ipPort.length());
            link.put("y", JSON.toJSONString(y));
            link.put("list_y", maps.get("list_y").toString());
            link.put("height", String.valueOf(y.size()*12));
            link.put("topLeg",String.valueOf(25));
            link.put("topGr",String.valueOf(45));
            res.add(JSON.toJSONString(link));
        }
        return JSON.toJSONString(res);
    }
}
