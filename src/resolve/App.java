package resolve;

import resolve.analysis.DNSStream;
import resolve.analysis.HTTP;
import resolve.analysis.Result;
import resolve.analysis.TCPStream;
import resolve.analysis.UDPStream;
import resolve.decoder.Packet;
import resolve.decoder.Pcap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

//import com.util.base.ActionBase;
//import com.util.base.Threads;

/**
 * Hello world!
 */
public class App  extends Result
{
	public App(Packet[] packets) {
		super(packets);
	}
	DecimalFormat df = new DecimalFormat("#.##############");
    public static void main(String[] args ) throws HttpException, IOException, InterruptedException
    {    
    	
    	/*Map<Integer, Integer> a = new HashMap<Integer, Integer>();
    	
    	a.put(1, 11);
    	a.put(2, 22);
    	a.put(4, 33);
    	
    	System.out.println(a.get("3"));
    	*/
    	/*List<String> list = new ArrayList<>();
    	
		list.add("112.91.31.33");
		list.add("42.156.140.84");
		list.add("42.156.140.209");
		list.add("106.120.167.99");
		list.add("121.18.239.245");
		list.add("42.156.167.82");
		list.add("124.95.157.240");
		
		Threads thre = new Threads();
		Map<String, String> map = thre.ipAscription(list.iterator());*/
    	
    	/*
    	double v1 = 16.76286482810974;
    	double v2 = 16.762778997421265;
    	System.out.println((v1-v2));
    	
    	System.out.println(df.format((v1-v2)));
    	System.out.println(Double.parseDouble(df.format((v1-v2))));*/
    	
    	/*String ip = "114.112.103.9";
    	String appkey = "8aa46d6cba98b535304ca24f73c9749a";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://apis.juhe.cn/ip/ip2addr?ip="+ip+"&key="+appkey);
        httpClient.executeMethod(getMethod);
		String res = getMethod.getResponseBodyAsString();
        System.out.println(res);*/
    	
        /*Pcap pcap = new Pcap();
        Packet[] packets = pcap.decode("F:\\pcap\\aiqiyi_qiehuan_02.pcap");   // aiqiyi_taitaiwansui-02
        App result = new App(packets);
        result.assembleStreams();
        result.tcp0();*/
       /* String srcImgPath = "C:/Users/renbaokun/Desktop/aa.png";
        String distImgPath = "C:/Users/renbaokun/Desktop/a.png";
        resizeImage(srcImgPath, distImgPath, 205, 568);*/
    	System.out.println("测试1".length());
    }
    /*** 
     * 功能 :调整图片大小 开发：wuyechun 2011-7-22 
     * @param srcImgPath 原图片路径 
     * @param distImgPath  转换大小后图片路径 
     * @param width   转换后图片宽度 
     * @param height  转换后图片高度 
     */  
    public static void resizeImage(String srcImgPath, String distImgPath,  
            int width, int height) throws IOException {  
  
        File srcFile = new File(srcImgPath);  
        Image srcImg = ImageIO.read(srcFile);  
        BufferedImage buffImg = null;  
        buffImg = new BufferedImage(width, height, BufferedImage.SCALE_AREA_AVERAGING);  
        buffImg.getGraphics().drawImage(  
                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,  
                0, null);  
        
        ImageIO.write(buffImg, "JPEG", new File(distImgPath));  
  
    }  
  
public void tcp0() {			//tcp 测试
		for (TCPStream tcp : tcpStreams) {
				System.out.println("****"
					+ "\n ip		" + tcp.getAddB()
					+ "\n getPortA		" + tcp.getPortA()
					
					+ "\n getFillData1Ts		" + tcp.getFillData1Ts()
					+ "\n startTs		" + tcp.startTs
					+ "\n endTs		" + tcp.endTs
		);
		}
	}
public void udp0() {
	
	for (UDPStream tcp : udpStreams) {
		System.out.println("***********************************************************"
				+ "\n getAddB()		" + tcp.getAddB()	//  接收地址
				+ "\n getPacketCnt		" + tcp.getPacketCnt()	
				+ "\n getInPacketCnt		" + tcp.getInPacketCnt()
				+ "\n getOutEthByteCnt		" + tcp.getOutEthByteCnt()
	);}
}
public void dns0() {
	
	for (DNSStream dns : dnsStreams) {	
			System.out.println("***********************************************************"
					+ "\n getStartTs		    " + dns.getStartTs()
					+ "\n getEndTs		    " + dns.getEndTs()
					+ "\n getHost		" + dns.getHost()
					+ "\n getIPs		" + dns.getIPs()
		);
	}
}
    public void tcp() {
		
		tcpStreams.forEach(x -> {	
			if(x.hasHTTP()) {
				System.out.println("--------------------------------------------");
				ArrayList<HTTP> https = x.getHTTPs();
				https.forEach(y -> {
					System.out.println("getInfo()		" + y.getInfo());
					System.out.println("getMethod()	      " + y.getMethod());
					System.out.println("getPacketIDs()	      " + y.getPacketIDs());
					System.out.println("getReasonPhrase()	      " + y.getReasonPhrase());
					System.out.println("getStartID()		" + y.getStartID());
					System.out.println("getStatus()		" + y.getStatus());
					System.out.println("getURL()		" + y.getURL());
					System.out.println("getVersion()		" + y.getVersion());
				});
			}
			System.out.println("getPackets()		" + x.getPacketCnt());  //  包数		getEthByteCnt
			System.out.println("getAddA()		" + x.getAddA());			//  本地地址
			System.out.println("getAddB()		" + x.getAddB());			//  接收地址
			System.out.println("getPortA()		" + x.getPortA());
			System.out.println("getPortB()		" + x.getPortB());
		});
		}
public void dns() {
		
		dnsStreams.forEach(x -> {			
			System.out.println("--------------------------------------------");
			System.out.println("host		" + x.getHost());		//
			System.out.println("req_ips		" + x.getReq_ips());
			System.out.println("res_ips		" + x.getRes_ips());
			System.out.println("ips		" + x.getIPs());
		});
	}
	/**
	 * 识别Get、Post等关键字，目的IP地址（源IP和目的IP）
	 */
	public void serverIP() {
		
		dnsStreams.forEach(x -> {			
			System.out.println("--------------------------------------------");
			System.out.println("host		" + x.getHost());		//
			System.out.println("req_ips		" + x.getReq_ips());
			System.out.println("res_ips		" + x.getRes_ips());
			System.out.println("getStartTs()		" + x.getStartTs());
			System.out.println("getPacketCnt()	      " + x.getPacketCnt());
			System.out.println("getStartTs()		" + x.getStartTs());
			System.out.println("startTs	        " + x.startTs);
			System.out.println("endTs	        " + x.endTs);
			System.out.println("rtt		        " + x.getDuration());
			//deltaTs  时间
		});
	}
}
