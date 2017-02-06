package com.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.util.base.FileUtil;

@Controller
@RequestMapping("file")
public class FileManage extends FileUtil{
	
	/**
	 * @description client上传至server
	 */
	@RequestMapping(value = "/upload.do")
	public void doGetUpload(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request, HttpServletResponse response,ModelMap model) throws ServletException, IOException {
		
			System.out.println("开始");  
	        String path = "E:\\upload";  
	        String fileName = file.getOriginalFilename();  
	        //String fileName = new Date().getTime()+".jpg";  
	        System.out.println(path);  
	        File targetFile = new File(path, fileName);  
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();  
	        }  
	        //保存  
	        try {  
	        	if(!"".equals(fileName)){
		            file.transferTo(targetFile);  
		            model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName); 
	        	}
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	/**
	 * @description server下载至client
	 */
	@RequestMapping(value = { "/download.do" },method = { RequestMethod.GET })
	public void doGetDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String temp_fileName = request.getParameter("downloadFileName");
		if (temp_fileName == null || temp_fileName == "")
			return;
		byte[] temp_t = temp_fileName.getBytes("UTF-8");
		String fileName = new String(temp_t, "GBK");
		SmartUpload mySmartUpload = new SmartUpload();
		//mySmartUpload.initialize(config, request, response);
		mySmartUpload.setContentDisposition(null);
		/*
		 * 原型：public void setContentDisposition(String contentDisposition)
		 * 其中，contentDisposition为要添加的数据。
		 * 如果contentDisposition为null，则组件将自动添加"attachment;"，
		 * 以表明将下载的文件作为附件，结果是IE浏览器将会提示另存文件，而不是自动打开这个文件
		 * （IE浏览器一般根据下载的文件扩展名决定执行什么操作，扩展名为doc的将用word程序打开，
		 * 扩展名为pdf的将用acrobat程序打开，等等）。
		 */
		try {
			mySmartUpload.downloadFile("/upload/" + fileName);
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断文档文档
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileFormatList.do")
	public void fileFormatList(HttpServletRequest request, HttpServletResponse response,String format) throws IOException {
		
		log.info("读取server指定文件夹中文件列表........");
		List<Map<String, String>> fileName = getFileName(config("serverpath"));
		/*saveListToSession(fileName, request);//保存fileName集合
		List<Map<String, String>> fileName =(List<Map<String, String>>) request.getSession().getAttribute("fileNameList");*/
		List<Map<String, String>> fileNameFormat=new ArrayList<>();
		for (Map<String, String> map :fileName) {
			String pathLast =map.get("name").substring(map.get("name").lastIndexOf(".")+1);
			if(format.equals("all")){
				fileNameFormat.add(map);
			}else if(pathLast.equals(format)) {
				fileNameFormat.add(map);
			}
		}
		String data = JSON.toJSONString(fileNameFormat);
		renderData(request, response, data);
	}
	@RequestMapping(value = "/filelist.do")
	public void filelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		log.info("读取server指定文件夹中文件列表........");
		List<Map<String, String>> fileName = getFileName(config("serverpath"));
		
		List<Map<String, String>> fileNameFormat=new ArrayList<>();
		for (Map<String, String> map :fileName) {
			String pathLast =map.get("name").substring(map.get("name").lastIndexOf(".")+1);
			if(pathLast.equals("pcap")) {
				fileNameFormat.add(map);
			}
		}
		String data = JSON.toJSONString(fileNameFormat);
		renderData(request, response, data);
	}

	
	
	@RequestMapping(value="/ExportExcel")
	public void ExportExcel(HttpServletResponse response,String data1,String data2,String data3)throws Exception{
		HSSFWorkbook hssf = new HSSFWorkbook();
		String[] dataAttr =data1.split("~~");
		List<List<String>> dataList =new ArrayList<>();
		for(String data:dataAttr) {
			String [] minData=data.split(",,");
			List<String> dataMinList= new ArrayList<>();
				for(String min :minData){
					dataMinList.add(min);
				}
			dataList.add(dataMinList);
		}
		List<List<String>> dataList1 =new ArrayList<>();
		if(data2!=null){
			String[] dataAttr1 =data2.split("~~");
			for(String data:dataAttr1) {
				String [] minData=data.split(",,");
				List<String> dataMinList= new ArrayList<>();
					for(String min :minData){
						dataMinList.add(min);
					}
				dataList1.add(dataMinList);
			}
			List<List<String>> dataList2 =new ArrayList<>();
			if(data3!=null){
				String[] dataAttr2 =data3.split("~~");
				for(String data:dataAttr2) {
					String [] minData=data.split(",,");
					List<String> dataMinList1= new ArrayList<>();
						for(String min :minData){
							dataMinList1.add(min);
						}
					dataList2.add(dataMinList1);
				}
		}
		HSSFSheet hsset = hssf.createSheet("汇总数据");
		//设置单元格大小celle
		hsset.setColumnWidth(1, 5000);
		hsset.setColumnWidth(2, 5000);
		hsset.setColumnWidth(3, 5000);
		hsset.setColumnWidth(4, 5000);
		hsset.setColumnWidth(5, 5000);
		hsset.setColumnWidth(6, 5000);
		hsset.setColumnWidth(7, 5000);
		hsset.setColumnWidth(8, 5000);
		hsset.setColumnWidth(9, 5000);
		hsset.setColumnWidth(10, 5000);
		hsset.setColumnWidth(11, 5000);
		hsset.setColumnWidth(12, 5000);
		hsset.setColumnWidth(13, 5000);
		hsset.setColumnWidth(14, 5000);
		hsset.setColumnWidth(15, 5000);
		hsset.setColumnWidth(16, 5000);
		hsset.setColumnWidth(17, 5000);
		hsset.setColumnWidth(18, 5000);
		hsset.setColumnWidth(19, 5000);
		Integer b=-1;
		for (int i = 0; i < dataList.size(); i++) {
			HSSFRow row=hsset.createRow(++b);
			Integer aa = -1;
			for(int y = 0 ; y<dataList.get(i).size() ; y++){
				row.createCell(++aa).setCellValue(dataList.get(i).get(y).toString());
			}
		}
		if(dataList1.size()!=0){
		HSSFSheet hsset1 = hssf.createSheet("详细数据");
		//设置单元格大小celle
		hsset1.setColumnWidth(1, 5000);
		hsset1.setColumnWidth(2, 5000);
		hsset1.setColumnWidth(3, 5000);
		hsset1.setColumnWidth(4, 5000);
		hsset1.setColumnWidth(5, 5000);
		hsset1.setColumnWidth(6, 5000);
		hsset1.setColumnWidth(7, 5000);
		hsset1.setColumnWidth(8, 5000);
		hsset1.setColumnWidth(9, 5000);
		hsset1.setColumnWidth(10, 5000);
		hsset1.setColumnWidth(11, 5000);
		hsset1.setColumnWidth(12, 5000);
		hsset1.setColumnWidth(13, 5000);
		hsset1.setColumnWidth(14, 5000);
		hsset1.setColumnWidth(15, 5000);
		hsset1.setColumnWidth(16, 5000);
		hsset1.setColumnWidth(17, 5000);
		hsset1.setColumnWidth(18, 5000);
		hsset1.setColumnWidth(19, 5000);
		int c = -1;
			for (int i = 0; i < dataList1.size(); i++) {
				HSSFRow row=hsset1.createRow(++c);
				Integer aa = -1;
				for(int y = 0 ; y<dataList1.get(i).size() ; y++){
					row.createCell(++aa).setCellValue(dataList1.get(i).get(y).toString());
				}
			}
			for(int i = 0; i < dataList2.size(); i++){
				HSSFRow row=hsset1.createRow(++c);
				Integer aa = -1;
				for(int y = 0 ; y<dataList2.get(i).size() ; y++){
					row.createCell(++aa).setCellValue(dataList2.get(i).get(y).toString());
				}
			}
			}
		}
		
		 ByteArrayOutputStream os = new ByteArrayOutputStream();
		  try {
			  hssf.write(os);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		  byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("CMCC指标分析" + ".xls").getBytes(), "iso-8859-1"));

	        ServletOutputStream out = response.getOutputStream();

	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;

	        try {

	            bis = new BufferedInputStream(is);
	            bos = new BufferedOutputStream(out);

	            byte[] buff = new byte[2048];
	            int bytesRead;

	            // Simple read/write loop.
	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                bos.write(buff, 0, bytesRead);
	            }

	        } catch (final IOException e) {
	            throw e;
	        } finally {
	            if (bis != null)
	                bis.close();
	            if (bos != null)
	                bos.close();
	        }
	}
}