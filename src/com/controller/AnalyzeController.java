package com.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.service.indexDataServiceImpl;
import com.util.base.ActionBase;
import com.util.base.AnalyzeResult;
import com.util.base.UtilThreads;

import resolve.decoder.Packet;
import resolve.decoder.Pcap;

@Controller
@Scope("prototype")
@RequestMapping("reportOut")
public class AnalyzeController extends ActionBase{
	
	Pcap pcap = new Pcap();
    
	@RequestMapping(value="/oneKey.do",method=RequestMethod.GET)
	public void norm(HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("报告输出........");
		
		effecTive(request, response);
		
		String[] filecheck = request.getParameter("filecheck").split(",");
		String[] norm = request.getParameter("norm").split(",");
		String[] kidney = request.getParameter("kidney").split(",");
		
		List<String> norm_list = toList(norm);
		List<String> kidney_list = toList(kidney);
		
		String serverpath = config("serverpath");
		
		Map<String, Object> list_maps = new HashMap<String, Object>();
		
		list_maps.put("norm", norm);
		list_maps.put("kidney", kidney);
		list_maps.put("filecheck", filecheck);
		for (String arg : filecheck) {
	
			Packet[] packets = pcap.decode(serverpath+arg);  
		    AnalyzeResult result = new AnalyzeResult(packets);
		    result.assembleStreams();
		    Map<String, Object> maps = result.tcp(norm_list,kidney_list);
		    list_maps.put(arg, maps);
		}
		indexDataServiceImpl ids = new indexDataServiceImpl();
		
		String norm_edit = ids.norm_edit(list_maps);
		Map<String, String> norm_data = ids.normSum(list_maps);
		String norm_table = norm_data.get("table");
		String norm_minSumList = norm_data.get("minSumList");
		String norm_norm = norm_data.get("norm");
		String norm_graph =ids.norm_graph(list_maps);
		
		Map<String, String> data_map = new HashMap<String, String>();
		//通用
		data_map.put("norm_edit", norm_edit);         //  编辑项
		data_map.put("norm_norm", norm_norm);    
		data_map.put("norm_table", norm_table);       //  数据展示 
		data_map.put("norm_minSumList", norm_minSumList);   //  业务指标最小值集合  
		data_map.put("norm_graph", norm_graph);       //  图形展示
		data_map.put("filecheck", JSONObject.toJSONString(filecheck));       //  文件名称列表
		
		renderData(request, response, JSON.toJSONString(data_map));
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value="/ipAddr.do",method=RequestMethod.GET)
	public void ipAddr(HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("IP 归属........");
		String ips = request.getParameter("ips");
		String[] ipList = ips.substring(1).split(",");
		List<String> list = Arrays.asList(ipList);
		
		UtilThreads thre = new UtilThreads();
		Map<String, String> map = thre.ipAscription(list.iterator());
		
		renderData(request, response,  JSON.toJSONString(map));
	}
}
