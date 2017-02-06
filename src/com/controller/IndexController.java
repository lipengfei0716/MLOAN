package com.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.util.base.ActionBase;

@Controller
@RequestMapping(value="index")
public class IndexController extends ActionBase{

	@RequestMapping(value = "chart.do", method = RequestMethod.GET)
	public void chart(HttpServletRequest request, HttpServletResponse response){

		log.info("图标数据保存........");
		//设置map，并设值
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		BufferedReader reader = readFile();
		//每次读取文件的缓存
        String temp1 = null;
        String temp2 = null;
        try {
			while((temp1 = reader.readLine()) != null){
				
				if(!" TCP Streams ".equals(temp1))
					continue;
				if(temp1.contains("TCP Streams")){
					//System.out.println(temp1);
				}
				 while((temp2 = reader.readLine()) != null){
					
					Map<String, Object> map = new HashMap<String, Object>();
					int key1 = 0;
					if(" UDP Streams ".equals(temp2)){
						//System.out.println(temp2);
						break;}
					if(temp2.length()==143){
				 		map.put(Integer.toString(key1++), temp2.substring(5, 24));
				 		map.put(Integer.toString(key1++), temp2.substring(58, 59));
				 		list.add(map);
					}
				 }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
            //关闭文件流
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		String data = JSON.toJSONString(list);
		//System.out.println("data------"+data);
	    renderData(request, response,data);
	}
	//从给定位置读取Json文件
    public static BufferedReader readFile(){
    	
    	String path = "E:\\pcap.txt";
        //从给定位置获取文件
        File file = new File(path);
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        return reader;
    }
}
