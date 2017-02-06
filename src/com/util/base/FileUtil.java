package com.util.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfStamper;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 *	文件生成  PPT、excel、txt等
 */
public class FileUtil extends ActionBase{
	
	
	/**
	 * @description 获取指定路径下文件名列表
	 * @param path
	 * @return String[] filelist
	 */
	public static List<Map<String, String>> getFileName(String path){
		
		log.info("获取指定路径下文件名列表........");
		log.info("path : "+path);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    File file = new File(path);
	    File[] list = file.listFiles();
	    List<Map<String, String>> filelist = new ArrayList<Map<String,String>>();
	    
	    for(File name:list){
	    	if(name.isDirectory())	//目录类型   name.isDirectory()			文件类型		name.isFile()
	    		continue;
	    	Map<String, String> map = new HashMap<String, String>();
	    	
		    DecimalFormat df = new DecimalFormat("#.##");//格式化小数   
		    String size = "";
	    	long length = name.length();
	    	if(length>=1024){
	    		long siKb = length/1024;
	    		if(siKb>=1024){
		    		size = df.format((float)siKb/1024)+"MB";//返回的是String类型 
		    	}else{
		    		size = String.valueOf(siKb)+"KB";
		    	}
	    	}else{
	    		size = String.valueOf(name.length())+"byte";
	    	}
	    	String last_date = sdf.format(name.lastModified());
	    	map.put("name", String.valueOf(name.getName()));	//文件名
	    	map.put("absolute_path", name.getAbsolutePath());	//文件绝对路径
	    	map.put("can_read", String.valueOf(name.canRead()));	//是否可读
	    	map.put("can_write", String.valueOf(name.canWrite()));	//是否可写
	    	map.put("parent_path", name.getParent());	//文件上级目录
	    	map.put("path", name.getPath());	//文件所在目录（含文件名）
	    	map.put("size", size);	//文件长度
	    	map.put("last_date", last_date);	//最后修改
	    	map.put("is_hidden", String.valueOf(name.isHidden()));	//是否被隐藏
	    	filelist.add(map);
	    }
	    
	    return filelist;
	}
	/**
	 * @description 获取指定路径下文件名列表及子目录
	 * @param file，resultFileName
	 * @return List<String> filelist
	 */
	public List<String> ergodic(File file,List<String> resultFileName){
		
	    File[] list = file.listFiles();
	    List<File> filelist = new ArrayList<File>();
	    for(File name:list){
	    	filelist.add(name);
	    }
		for (File f : filelist) {
		    if(f.isDirectory()){
		        resultFileName.add(f.getPath());
		        ergodic(f,resultFileName);
		    }else
		        resultFileName.add(f.getPath());
		}
		return resultFileName;
	}
	/**
	 * @description 文件名列表排序
	 * @param fileName，srot = desc 降序(默认升序)
	 * @return filelist
	 */
	public Map<String, Map<String, String>> sorting(Map<String, Map<String, String>> fileName,String sort){

		Map<String, Map<String, String>> sortlist = new HashMap<String, Map<String, String>>();

		Object[] key_arr =  fileName.keySet().toArray();    
		if(sort.equalsIgnoreCase("desc")){
			Arrays.sort(key_arr, Collections.reverseOrder());
		}else{
			Arrays.sort(key_arr);
		}
		for  (Object key : key_arr) {     
			sortlist.put(key.toString(), fileName.get(key.toString()));     
		} 
		return sortlist;
	} 
	/**
	 * @description 支持中英文混合字符串长度
	 * @param arg
	 * @param fixed
	 * @return
	 */
	public static String polishing(String arg,int fixed) {

		 int valueLength = 0;  
	        char arr[] = arg.toCharArray();  
	        for(int i=0;i<arr.length;i++)  
	        {  
	            char c = arr[i];  
	            if((c >= 0x0391 && c <= 0xFFE5))  //中文字符  
	            {  
	            	valueLength += 2;  
	            }  
	            else if((c>=0x0000 && c<=0x00FF)) //英文字符  
	            {  
	            	valueLength += 1;  
	            }  
	        }  
		for (int i = 0; i < (fixed-valueLength); i++) {
			arg+=" ";
		}
		return arg;
	}

	public void session(Map<String, String> map, HttpServletRequest request) throws NumberFormatException, IOException {
		
		log.info("HttpSession session缓存会话");
		int sessiontimeout = Integer.parseInt(config("sessiontimeout"));
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(sessiontimeout);  //会话时间
        for (String key : map.keySet()) {
        	log.info("session : "+key+" = "+map.get(key));
        	session.setAttribute(key,map.get(key));
		}
	}
public void download(HttpServletResponse response,String filePath) {
    try {
        File file = new File(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
        fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename="+fileName);
        String len = String.valueOf(file.length());
        response.setHeader("Content-Length", len);
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(file);
        byte[] b = new byte[1024];
        int n;
        while((n=in.read(b))!=-1){
            out.write(b, 0, n);
        }
        in.close();
        out.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

/**
 * word转pdf
 */
public static void wordToPDF(HttpServletRequest request, HttpServletResponse response/*String docfile, String toFile,int type*/) {    
    ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word    
    String path1 = request.getRealPath("/")+"word\\outFile1.doc";
    String path2 = request.getRealPath("/")+"word\\outFile1";
    try {    
        app.setProperty("Visible", new Variant(false));    
        Dispatch docs = app.getProperty("Documents").toDispatch();    
        Dispatch doc = Dispatch.invoke(    
                docs,    
                "Open",    
                Dispatch.Method,    
                new Object[] { path1, new Variant(false),    
                        new Variant(true) }, new int[1]).toDispatch();    
        //new Variant(type)，这里面的type的决定另存为什么类型的文件  
        Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {    
        		path2, new Variant(17) }, new int[1]);    
        Variant f = new Variant(false);    
        Dispatch.call(doc, "Close", f);    
    } catch (Exception e) {    
        e.printStackTrace();    
    } finally {    
        app.invoke("Quit", new Variant[] {});    
    }    
} 

/**
 * PDF插入水印
 * @param pdfStamper
 * @param waterMarkName
 */
protected static void addWatermark(PdfStamper pdfStamper, String waterMarkName){
	 PdfContentByte content = null;
     BaseFont base = null;
     Rectangle pageRect = null;
     PdfGState gs = new PdfGState();
     try {
         // 设置字体
     base = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
     } catch (DocumentException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     }
     try {
         if (base == null || pdfStamper == null) {
             return;
         }
         // 设置透明度为0.4
         gs.setFillOpacity(0.1f);
         gs.setStrokeOpacity(0.1f);
         int toPage = pdfStamper.getReader().getNumberOfPages();
         for (int i = 1; i <= toPage; i++) {
             pageRect = pdfStamper.getReader().
                getPageSizeWithRotation(i);
             // 计算水印X,Y坐标
             float x = pageRect.getWidth() / 2;
             float y = pageRect.getHeight() / 2;
             //获得PDF最顶层
             content = pdfStamper.getOverContent(i);
             content.saveState();
             // set Transparency
             content.setGState(gs);
             content.beginText();
             content.setColorFill(BaseColor.GRAY);
             content.setFontAndSize(base, 60);
             // 水印文字成45度角倾斜
             content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, x,y, 45);
             content.endText(); 
         }
     } catch (Exception ex) {
         ex.printStackTrace();
     } finally {
         content = null;
         base = null;
         pageRect = null;
     }
 }
public static void DeleteFolder(String sPath) {
	File file = new File(sPath);
	// 路径为文件且不为空则进行删除
	if (file.isFile() && file.exists()) {
	file.delete();
		}
	}
}