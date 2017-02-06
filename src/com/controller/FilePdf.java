package com.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.util.base.FileUtil;
import com.util.base.ImageUtil;
import com.util.base.PDFUti;
import com.util.base.WordTest;
/**
 * 
 * @author lipengfei
 *
 */
@Controller
@RequestMapping("FilePdf")
public class FilePdf{
	
	@RequestMapping(value="/CreatePDF.do")
	public void CreatePDF(HttpServletRequest request,HttpServletResponse response,String data1,String Svalue,String data2,String data3) throws Exception{
	    String parameter = request.getParameter("Title");
	    String string = parameter.trim();
	    System.err.println(string);
	    String[] parameter2  = request.getParameterValues("Chenck");
	    for (int i = 0; i < parameter2.length; i++) {
                System.err.println(parameter2[i]);
            }
	    String object = (String) request.getSession().getAttribute("username");
	    System.err.println(object);
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
		
		PDFUti fPdfUti=new PDFUti();
		System.err.println(Svalue);
		/**
		 * 详细数据
		 */
		List<List<String>> dataList1 =new ArrayList<>();
		  List<List<String>> dataList2 =new ArrayList<>();
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
			/**
			 * 无效id
			 */
			if(data3!=null){
			  
				String[] dataAttr2 =data3.split("~~");
				for(String data:dataAttr2) {
					String [] minData=data.split(",,");
					List<String> dataMinList= new ArrayList<>();
						for(String min :minData){
							dataMinList.add(min);
						}
						dataList2.add(dataMinList);
				}
			}
			
			fPdfUti.pdfMethod(request, dataList, response,dataList1, dataList2,Svalue,string,parameter2,object);
		}else{
			
		    fPdfUti.pdfMethod(request, dataList, response,null, null,null,null,null,null);
		}
	}
	

}