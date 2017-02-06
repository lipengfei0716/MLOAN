package com.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sun.util.logging.resources.logging;
import Decoder.BASE64Decoder;

import com.itextpdf.text.log.Logger;
import com.util.base.ActionBase;
import com.util.base.FileUtil;
import com.util.base.ImageUtil;
import com.util.interceptor.CommonInterceptor;

@Controller
@RequestMapping(value="img")
public class ImageManage extends ActionBase{
	 
	 
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/chart.req", method = RequestMethod.POST)
	public void img(HttpServletRequest request,HttpServletResponse response) {
		
		
		 log.info("图表下载........");
		 String imgname = request.getParameter("imgname");
		 String path1 = request.getRealPath("/")+"Echarts";
			 File file = new File(path1);
			 if (!file.exists()) {
			     System.out.println("文件不存在创建");
				   file.mkdir();
				}else{
					FileUtil.DeleteFolder(path1);
					file.mkdir();
				}
		 if(imgname!=null){
			 log.info(imgname+"下载");
			 String path = path1+"/"+imgname+".png";
			 String data = request.getParameter(imgname);
			 ImageSave(data, path,request, response);}
		 
		 renderData(request, response, null);
	}
	
	/**
	 * 创建文件夹;
	 * @param data
	 * @param path
	 * @param request
	 * @param response
	 */
	public void ImageSave(String data,String path,HttpServletRequest request, HttpServletResponse response){
            
            log.info("图片保存路径........   "+path);
            try {
        String[] url = data.split(",");
        String u = url[1];
        // Base64解码
        byte[] b = new BASE64Decoder().decodeBuffer(u);//图片流
        // 生成图片
        OutputStream out = new FileOutputStream(new File(path));
        out.write(b);
        out.flush();
        out.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    
}
