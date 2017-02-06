package com.util.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * word转pdf
 * @author Dang
 *
 */
public class WordTest {

	private Configuration configuration = null;
	
	Calendar a=Calendar.getInstance();
	 int year = a.get(Calendar.YEAR);
	 int month = a.get(Calendar.MONTH)+1;
	 int date = a.get(Calendar.DATE);
	 int hour = a.get(Calendar.HOUR_OF_DAY);
	 int minute = a.get(Calendar.MINUTE);
	 int second = a.get(Calendar.SECOND);
	 
	 
	 
	 public WordTest() {
			configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
		}
	 @SuppressWarnings("deprecation")
	public void createDoc(HttpServletRequest request, HttpServletResponse response) {
			//要填入模本的数据文件
			Map<String,Object> dataMap=new HashMap<String,Object>();
			getData(dataMap);
			//设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
			//这里我们的模板是放置位置
			configuration.setClassForTemplateLoading(this.getClass(), "/config");
			Template t=null;
			try {
				//test.ftl为要装载的模板
				t = configuration.getTemplate("word.ftl");
			} catch (IOException e) {
				e.printStackTrace();
			}
			String path = request.getRealPath("/")+"word";
			File file = new File(path);
			 if (!file.exists()) {
				   file.mkdir();
				}else{
					FileUtil.DeleteFolder(path);
					file.mkdir();
				}
			
			//输出文档路径及名称
			 String path1 = request.getRealPath("/")+"word/outFile1.doc";
			File outFile = new File(path1);
			if (!outFile.exists()) {
				Writer out = null;
				try {
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				 
		        try {
					t.process(dataMap, out);
				} catch (TemplateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				}else{
					FileUtil.DeleteFolder(path1);
					Writer out = null;
					try {
						out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					 
			        try {
						t.process(dataMap, out);
					} catch (TemplateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
		}
	 private void getData(Map<String,Object> dataMap)
	  {
		  dataMap.put("year", String.valueOf(year));
		  dataMap.put("month", month);
		  dataMap.put("date", date);
		  dataMap.put("hour", hour);
		  dataMap.put("minute", minute);
		  dataMap.put("second", second);
	  }
}
