package com.util.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 资源共享多线程
 * @author 任宝坤
 */
public class UtilThreads extends ActionBase implements Runnable{  
	
	private static Map<String, String> IPS;
    private String baiduKey = "442b4cd117fb3636794603d67b8fcfd8";
    private static String ip;
    static boolean ipSta;
    
	public void run() {
		log.info(Thread.currentThread().getName()+"  启动");
		ipSta = true;
		if(baiduKey.equals("cb4b9b822ef517861e0392b5b727631d"))
			  baiduKey = "442b4cd117fb3636794603d67b8fcfd8";
		else 
			  baiduKey = "cb4b9b822ef517861e0392b5b727631d";
		try {
			IPS.put(ip, ipInfo(Arrays.asList(ip.split("-")).get(1),baiduKey));
		} catch (Exception e) {
			log.info(Thread.currentThread().getName()+"  处理异常");
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		log.info(Thread.currentThread().getName()+"  结束");
	}

	public static Map<String, String> ipAscription(Iterator<String> iter) throws InterruptedException {
    	
		IPS = new HashMap<String, String>();
    	UtilThreads my = new UtilThreads();
    	int i=0;
    	while (iter.hasNext()) {
    		
    		Thread a = new Thread(my);
        	Thread b = new Thread(my);
        	Thread c = new Thread(my);
        	
    		if(i==0){
        		ip = iter.next();
        		a.setName(ip+"   线程号：   " +i++);
    			a.start();	
        	}else if(iter.hasNext() && ipSta){   
    			ip = iter.next();
    			a.setName(ip+"   线程号：   " +i++);
    			a.start();	
        		ipSta=false;
	    	}
    		List<String> list = new ArrayList<>();
    		// NEW  RUNNABLE  TERMINATED
    		if(b.getState().toString().equals("NEW"))
    			list.add("b");
    		if(c.getState().toString().equals("NEW"))
    			list.add("c");
    		while (list.size()>0 && iter.hasNext()) {
    			
    			if(iter.hasNext() && ipSta && b.getState().toString().equals("NEW")){
    				list.remove("b");
        			ip = iter.next();
        			b.setName(ip+"   线程号：   " +i++);
        			b.start();
        			ipSta=false;
        		}
        		if(iter.hasNext() && ipSta && c.getState().toString().equals("NEW")){
        			list.remove("c");
        			ip = iter.next();
        			c.setName(ip+"   线程号：   " +i++);
        			c.start();
        			ipSta=false;
        		}
			}
    		a.join();
    		b.join();
    		c.join();
		}
    	log.info("IP 归属 查询完毕，线程结束");
		Thread f = new Thread(my);
		f.setName(ip+"   线程号：   " +i++);
		f.start();
    	return IPS;
    }
}

