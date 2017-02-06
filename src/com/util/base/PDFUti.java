package com.util.base;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;








import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author 作者 E-mail:lipengfei0716@163.com
 * @date 创建时间：2016年11月29日 下午3:59:48
 * @version 1.0
 * @类的说明:
 * 
 * 
 */
public class PDFUti {
    public  void pdfMethod(HttpServletRequest request, List<List<String>> dataList,
            HttpServletResponse response,  List<List<String>> dataList1,
            List<List<String>> dataList2,String Svalue,String string,String[] parameter2,String object) throws Exception {
        // 模板路径;
        String template = new File(this.getClass().getClassLoader().getResource("").getPath()).toString();
      String  templatePath=template+"//config//word.pdf";
      
      String webpath=request.getRealPath("/"); 
      String yemei=webpath+"design//img//yemei.png";
      String yejiao=webpath+"design//img//yejiao.png";
     // String fonts=webpath+"Fonts";
        
        
        String tupian=webpath+"Echarts";

        // 生成的新文件路径转换的;
        String newPDFPath = "newPdf.pdf";
        //最后的文件名字;
        String newFile=Svalue+".pdf";
        
       /* BaseFont bfChinese = BaseFont.createFont(fonts+"\\SIMSUN.TTC,1", BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);*/
        // 填充表单
        BaseFont bfChinese =BaseFont.createFont("STSong-Light","UniGB-UCS2-H", true);
       
        // 读取模板
        PdfReader reader = new PdfReader(templatePath);
        // 预存
        ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
        // 读取到的模板放入预存中
        PdfStamper stamp = new PdfStamper(reader, tempBaos);
        /////
        PdfContentByte under = stamp.getUnderContent(1);  
//        ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();  
//        fontList.add(bfChinese);
        AcroFields fields = stamp.getAcroFields();  
        //fields.setSubstitutionFonts(fontList); 
        Map<String, String> data = data(string,parameter2,createDate(),object);
        fillData(fields,data); 
        // 获取预存中的文本预
//        AcroFields form = stamp.getAcroFields();
//        fillForm(form);
        stamp.setFormFlattening(true);
       reader.close();
        stamp.close();
        // 可能除了输出模板外，还需要加入其它元素。
        int totalPage = 0;
        Document detailDoc = new Document(reader.getPageSize(1));
        ByteArrayOutputStream detaialBaos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(detailDoc, detaialBaos);
       
        // 页眉页脚图片
        Image headerImage = Image.getInstance(yemei);
        headerImage.scalePercent(70f);
        Chunk chunk = new Chunk(headerImage, -330, 0);
        Image headerImage2 = Image.getInstance(yejiao);
        headerImage2.scalePercent(70f);
        Chunk chunk2 = new Chunk(headerImage2, 300, 0);
        // 页眉
        HeaderFooter header = new HeaderFooter(new Phrase(chunk), false);

        // 设置是否有边框等
        // header.setBorder(Rectangle.NO_BORDER);
        header.setBorder(Rectangle.BOTTOM);
        header.setAlignment(1);
        header.setBorderColor(Color.black);

        // 页脚
        HeaderFooter footer = new HeaderFooter(new Phrase(""), new Phrase(chunk2));
        /**
         * 
         * 0是靠左 1是居中 2是居右
         */
        footer.setAlignment(1);
        footer.setBorderColor(Color.white);
        footer.setBorder(Rectangle.BOTTOM);

        detailDoc.setHeader(header);
        detailDoc.setFooter(footer);
        detailDoc.open();
      //  "C:/WINDOWS/Fonts/SIMSUN.TTC,1"
        
        detailDoc.add(new Chunk("通用指标:", new Font(bfChinese,14f)));
        detailDoc.add(new Chunk("\n\n"));
        genOtherTable(request, dataList, response,  dataList1, dataList2, detailDoc);
        
        
        File mFile = new File(tupian);
        File[] files = mFile.listFiles();
        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                detailDoc.newPage();
                String imgName = null;
                imgName = (files[i].getAbsolutePath());
              long l = files[i].length();
              System.err.println(l);
                System.err.println(imgName);
                if (imgName.indexOf("aipFlow")!=-1) {
                    detailDoc.add(new Chunk("IP流量分布图", new Font(bfChinese,14f)));
                    Image image = Image.getInstance(imgName);
                    image.scaleToFit(1000f, 1000f);
                    image.scalePercent(69f);
                   // image.scaleAbsolute(700, 450);
                    image.setAlignment(Element.ALIGN_CENTER);
                    detailDoc.add(image);
                }else if(imgName.indexOf("bpacketLen")!=-1) {
                    detailDoc.add(new Chunk("包长分布图", new Font(bfChinese,14f)));
                    Image image = Image.getInstance(imgName);
                    image.scaleToFit(1000f, 1000f);
                    image.scalePercent(69f);
                   // image.scaleAbsolute(700, 450);
                    image.setAlignment(Element.ALIGN_CENTER);
                    detailDoc.add(image);
                }else if(imgName.indexOf("cuse")!=-1){
                    detailDoc.add(new Chunk("吞吐曲线图", new Font(bfChinese,14f)));
                    Image image = Image.getInstance(imgName);
                    image.scaleToFit(1000f, 1000f);
                    image.scalePercent(69f);
                   // image.scaleAbsolute(700, 450);
                    image.setAlignment(Element.ALIGN_CENTER);
                    detailDoc.add(image);
                }else if (imgName.indexOf("zlink")!=-1) {
                      if (l>90000) {
                          detailDoc.add(new Chunk("链路图", new Font(bfChinese,14f)));
                          detailDoc.add(new Chunk("\n\n"));
                          Image image = Image.getInstance(imgName);
                          float x = image.getAbsoluteX();
                          System.err.println("zheshi:"+x);
                          float y = image.getAbsoluteY();
                          System.err.println(y);
                          image.scaleToFit(1000f, 1000f);
                          image.scalePercent(30f);
                          image.scaleAbsolute(485, 383);
                          image.setAlignment(Element.ALIGN_CENTER);
                          detailDoc.add(image);
                    } 
              else  {
                      detailDoc.add(new Chunk("链路图", new Font(bfChinese,14f)));
                      detailDoc.add(new Chunk("\n\n"));
                      Image image = Image.getInstance(imgName);
                      image.scaleToFit(1000f, 1000f);
                      image.scalePercent(69f);
                     
                      image.setAlignment(Element.ALIGN_CENTER);
                      detailDoc.add(image);
                    }
                      
                }
            }
        }
        detailDoc.close();
        totalPage = writer.getPageNumber();
        //8
        // 合并模板与其它页，并生成最后报表
        OutputStream outputStream = new FileOutputStream(newPDFPath);
       /* Document doc = new Document(reader.getPageSize(1));
        PdfCopy copy = new PdfCopy(doc, outputStream);

        doc.open();
        doc.newPage();
        int n = reader.getNumberOfPages();
        for (int t = 1; t <= n - 1; t++) {
            doc.newPage();

            PdfImportedPage imported = copy.getImportedPage(reader, t);
            copy.addPage(imported);
        }

        for (int i = 1; i <= totalPage -1; i++) {
            doc.newPage();
            copy.addPage(copy.getImportedPage(new PdfReader(tempBaos.toByteArray()), i));
            doc.resetPageCount();
        }
        doc.close();
        */
        
        List<ByteArrayOutputStream> list=new ArrayList<ByteArrayOutputStream>();
        list.add(tempBaos);
        list.add(detaialBaos);
                
        
        mergePdfFiles(list,outputStream);
        
        addShYin(newPDFPath, Svalue, newFile,request);
        
        File file=new File(newPDFPath);
        file.delete();
        download(response, newFile);
    }

    public static void genOtherTable(HttpServletRequest request,List<List<String>> dataList,HttpServletResponse response,List<List<String>> dataList1, List<List<String>> dataList2 , Document doc) throws Exception {
       /* String webpath=request.getRealPath("/"); 
        String fonts=webpath+"Fonts\\SIMSUN.TTC,1";*/
        //创建一个表格
        Table table=new Table(dataList.get(0).size());
        for (int i = 0; i < dataList.size(); i++) {
                for (int j = 0; j < dataList.get(i).size(); j++) {
                    Paragraph phrase = new  Paragraph(dataList.get(i).get(j).toString(),ChineseFont());
                  phrase.setAlignment(1);
                  phrase.setAlignment(Element.ALIGN_CENTER);
                        Cell cell2=new Cell(phrase);
                        cell2.setWidth(200f);
                        if ( i<1) {
                            cell2.setBackgroundColor(new Color(161, 219, 246, 1)); 
                        }
                        table.addCell(cell2);
                        }
        }
        table.setBorderWidth(1f);
       // table.setAbsWidth(120);
        // 其中1为居中对齐，2为右对齐，3为左对齐
        table.setAlignment(3);
        doc.add(table);
        doc.add(new Phrase());
        doc.add(new Phrase());
        BaseFont bfChinese =BaseFont.createFont("STSong-Light","UniGB-UCS2-H", true);
        doc.add(new Chunk("\n\n"));
        doc.add(new Chunk("详细信息:", new Font(bfChinese,14f)));
        /**
         * 详细信息前8条6行八列;
         */
        if(dataList1!=null){
            Table table1=new Table(8);
            //查看dataList1中的数量;
                for (int i = 0; i < dataList1.size(); i++) {
                                for (int j = 0; j < 8; j++) {
                                        if(dataList1.get(i).size() >j){
                                                Cell cell2=new Cell(new Phrase(dataList1.get(i).get(j).toString(),ChineseFont()));
                                                System.out.println(dataList1.get(i).get(j).toString());
                                                
                                                if ( i<1) {
                                                    cell2.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                                }
                                                
                                                table1.addCell(cell2);
                                        }
                                }
                        }
                
                doc.add(table1);
            /**
             * 详细信息8条之后的数据
             * i为一共多少行
             * j为一共多少列
             */
                int ber;
                //盛夏的字段;3个
                int num =dataList1.get(0).size()%8;
          if(num!=0){
                        if((dataList1.get(0).size()-8)%7!=0){
                                int i = (dataList1.get(0).size()-8)/7;
                                ber=i+1;
                        }else{
                                int i = (dataList1.get(0).size()-8)/7;
                                ber=i;
                        }
                }
           else{
                        if((dataList1.get(0).size()-8)/7!=0){
                                ber=2;
                        }else{
                                ber=1;
                        }
                }
                int nuber = 1;
                
              //ss=2  建立第二组;查看 是
                for (int z = 0; z < ber; z++) {
                    doc.add(new Phrase());
                        Table table2=null;
                        if (z==1) {
                            table2 =new Table(4);
                        }else if (z==0) {
                            table2=new Table(8);
                        }
                        int ss=++nuber;
                        
                        
                /**
                 * 输出第一列        
                 */
                for (int i = 0; i < dataList1.size(); i++) {
                        int s =7;
                     if(dataList1.get(i).size()>1){
                                Cell cell3=new Cell(new Phrase(dataList1.get(i).get(0).toString(),ChineseFont()));
                                System.err.println(dataList1.get(i).get(0).toString());
                                if ( i<1) {
                                    cell3.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                }
                             
                                table2.addCell(cell3);
                        }
                    else{
                                Cell cell3=new Cell(new Phrase("",ChineseFont()));
                                if ( i<1) {
                                    cell3.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                }
                                table2.addCell(cell3);
                        }
                        
                        
                        /**
                         * 详细信息
                         */
                      if(z==0){
                          //第一次 
                                for (int j = 8; j < (ss*8)-1; j++) {
                                                int ta= ++s;
                                                if(dataList1.get(i).size() >= ta){
                                                     if(dataList1.get(i).size()-j==0){
                                                                Cell cell2=new Cell(new Phrase(null,ChineseFont()));
                                                              if ( i<1) {
                                                                  cell2.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                                              }
                                                              table2.addCell(cell2);
                                                    }
                                                    else{
                                                        Cell cell2=new Cell(new Phrase(dataList1.get(i).get(j).toString(),ChineseFont()));
                                                        System.out.println(dataList1.get(i).get(j).toString());
                                                        if ( i<1) {
                                                            cell2.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                                        }
                                                        table2.addCell(cell2);
                                                        }
                                                    }
                                        }
                        }
                   else if (z==1) 
                               {
                                int s1;
                                s1 = ((z+1)*8-1);
                                        for (int j = s1; j <18; j++) {
                                                     if (dataList1.get(i).size()==18) {
                                                         if(dataList1.get(i).size()-j==0){
                                                             Cell cell2=new Cell(new Phrase(null,ChineseFont()));
                                                             table2.addCell(cell2);
                                                     }else{
                                                             Cell cell2=new Cell(new Phrase(dataList1.get(i).get(j).toString(),ChineseFont()));
                                                             System.err.println(dataList1.get(i).get(j).toString());
                                                             if ( i<1) {
                                                                 cell2.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                                             }
                                                             table2.addCell(cell2);
                                                             
                                                     }
                                                    }else if (dataList1.get(i).size()<18) {
                                                        if(dataList1.get(i).size()-j==0){
                                                            Cell cell2=new Cell(new Phrase(null,ChineseFont()));
                                                            table2.addCell(cell2);
                                                    }else{
                                                            Cell cell2=new Cell(new Phrase(dataList1.get(i).get(j).toString(),ChineseFont()));
                                                            System.err.println(dataList1.get(i).get(j-2).toString());
                                                            if ( i<1) {
                                                                cell2.setBackgroundColor(new Color(161, 219, 246, 1)); 
                                                            }
                                                            table2.addCell(cell2);
                                                            
                                                    }
                                                    }
                                            
                                                }
                        }
                  
                      
                      
                      
                                }
                doc.add(table2);
                        }
        }
        
        
        doc.add(new Chunk("无效IP:", new Font(bfChinese,14f)));
        
        if(dataList2!=null){
                Table table3=new Table(2);
                for (int i = 0; i < dataList2.size(); i++) {
                for (int j = 1; j < dataList2.get(i).size(); j++) {
                        Cell cell3=new Cell(new Phrase(dataList2.get(i).get(j).toString(),ChineseFont()));
                        cell3.setWidth(4f);
                        if ( i<1) {
                        cell3.setBackgroundColor(new Color(161, 219, 246, 1)); 
                    }
                        table3.addCell(cell3);
                        }
            }
                doc.add(table3);
        }

        
   
    }
/**
 * 字体控制;
 * @return
 */
    public static Font ChineseFont() {
       
        
        BaseFont baseFont=null;
        try {
            baseFont=BaseFont.createFont("STSong-Light","UniGB-UCS2-H", true);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }                                               //bfChinese, 10, Font.BOLD
        Font chineseFont=new Font(baseFont,10f,0,Color.black);
        
        return chineseFont;
    }
      
    

    
    /**
     * 下载问题;
     * @param response
     * @param filePath
     */
    public static void download(HttpServletResponse response,String filePath) {
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
     * pdf增加水印;
     * @param filePath
     * @param value
     * @throws Exception
     * @throws IOException
     */
    public static void addShYin(String filePath,String value,String newFile,HttpServletRequest request) throws Exception, IOException{
        /*String webpath=request.getRealPath("/"); 
        String fonts=webpath+"Fonts\\SIMSUN.TTC,1";*/
        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;
        try
        {
            pdfReader = new PdfReader(filePath);
            pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(newFile));
            int total = pdfReader.getNumberOfPages() + 1;
            Rectangle psize = pdfReader.getPageSize(1);
            
            float width = psize.getWidth();
            float height = psize.getHeight();
            com.lowagie.text.pdf.PdfContentByte content;
            BaseFont bfChinese =BaseFont.createFont("STSong-Light","UniGB-UCS2-H", true);
            PdfGState gs = new PdfGState();
            for (int i = 1; i < total; i++) 
            {
                content = pdfStamper.getOverContent(i);//在内容上方加水印
                //content = pdfStamper.GetUnderContent(i);//在内容下方加水印
                //透明度
                gs.setFillOpacity(0.1f);
                gs.setStrokeOpacity(0.1f);
                content.saveState();
                // set Transparency
                content.setGState(gs);
                content.beginText();
               
                content.setFontAndSize(bfChinese, 60);
                content.setTextMatrix(0, 0);
                content.showTextAligned(Element.ALIGN_CENTER, value, width / 2 - 50,height / 2 - 50, 55);
                //content.SetColorFill(BaseColor.BLACK);
                //content.SetFontAndSize(font, 8);
                //content.ShowTextAligned(Element.ALIGN_CENTER, waterMarkName, 0, 0, 0);
                content.endText();
            }
        }catch (Exception ex)
        {
            throw ex;
        }
        finally
        {

            if (pdfStamper != null)
                pdfStamper.close();

            if (pdfReader != null)
                pdfReader.close();
        }
        
    
    }
    
    
   /**
    * 设置文本域; 
    * @param fields
    * @param data
    * @throws IOException
    * @throws DocumentException
    */
    public static void fillData(AcroFields fields, Map<String, String> data)  
            throws IOException, DocumentException {  
        for (String key : data.keySet()) {  
            String value = data.get(key);  
            fields.setField(key, value); // 为字段赋值,注意字段名称是区分大小写的  
        }  
    }  
  /**
   * 获取数据;
   * @return
   */
    private static Map<String, String> data(String value,String[] string,String date,String object) {  
        Map<String, String> data = new HashMap<String, String>();  
        data.put("Text1", date); 
        data.put("Text2", value);
       
        data.put("Text3", object);  
        for (int i = 0; i < string.length; i++) {
            data.put("Text4", string[i]);  
        }
        
        return data;  
    }  
    /**
     * 合并pdf流;移动互联网业务品质智能测评系统
     * @param osList
     * @param os
     */
    public static void mergePdfFiles(List<ByteArrayOutputStream> osList,  
            OutputStream os) {  
        try {  
            Document document = new Document(new PdfReader(osList.get(0)  
                    .toByteArray()).getPageSize(1));  
            PdfCopy copy = new PdfCopy(document, os);  
            document.open();  
            for (int i = 0; i < osList.size(); i++) {  
                PdfReader reader = new PdfReader(osList.get(i).toByteArray());  
                int n = reader.getNumberOfPages();  
                for (int j = 1; j <= n; j++) {  
                    document.newPage();  
                    PdfImportedPage page = copy.getImportedPage(reader, j);  
                    copy.addPage(page);  
                }  
            }  
            document.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        }  
    }
    /**
     * 创建当前时间;
     * @return
     */
    private static String createDate(){
        long l = System.currentTimeMillis();
        //new日期对象
        Date date = new Date(l);
        //转换提日期输出格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    
    
    
}
