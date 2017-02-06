package com.util.base;
import java.io.*;
/**
 * json相关
 */
public class JsonUtils {
	
	public static void main(String[] args) {
		
		//String path = "E:\\pcap.txt";
		//readJson(path);
	}
    //从给定位置读取Json文件
    public static String readJson(String path){
        //从给定位置获取文件
        File file = new File(path);
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new FileReader(file));
            //每次读取文件的缓存
            String temp1 = null;
            String temp2 = null;
            while((temp1 = reader.readLine()) != null){
            	
            	if(!" TCP Streams ".equals(temp1))
            		continue;
            	if(temp1.contains("TCP Streams")){
            		System.out.println(temp1);
            	}
            	 while((temp2 = reader.readLine()) != null){
                 		
            		if(" UDP Streams ".equals(temp2)){
            			System.out.println(temp2);
            			break;}
             		System.out.println(temp2);
             		data.append(temp2);
            	 }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        return data.toString();
    }
    //给定路径与Json文件，存储到硬盘
    public static void writeJson(String path,Object json,String fileName){
        BufferedWriter writer = null;
        File file = new File(path + fileName + ".json");
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("文件写入成功！");
    }
}