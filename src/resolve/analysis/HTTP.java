package resolve.analysis;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import resolve.decoder.TCPPacket;

public class HTTP {
	
	private Map<String, String> header;
	private ArrayList<TCPPacket> tcps;
	private int packetType;   // 0: not the first packet of HTTP req/resp;  1: Response; 2:Request
	private String version;
	private String url;
	private String method;
	private String status;
	private String reason;
	private int contentLen;
	private int moreBytes;
	private String info;
	
	public HTTP(TCPPacket tcp) {
		
		header = new HashMap<>();
		tcps = new ArrayList<>();
		packetType = 0;
		moreBytes = 0;
		
		int idx = parse(tcp.getPayload());
		
		if(packetType > 0) {
			tcps.add(tcp);		
		} else {
			return;
		}
		
		String s = header.get("Content-Length");
		if(s != null) {
			contentLen = Integer.parseInt(s);
			moreBytes = contentLen -  ( tcp.getPayloadLength() - idx);	
		} else {
			contentLen = 0;
			moreBytes = 0;
		}
	}
	
	public int parse(byte[] bytes) {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		InputStreamReader reader = new InputStreamReader(in);  
        BufferedReader br = new BufferedReader(reader);
        int cnt = 0;
        
        String line;
        try {
        	line = br.readLine();
        	info = line;
        	cnt += line.length() + 2;        	
        	String[] s = line.split(" ");
        	if(!isFirstLine(s) ) {
        		return cnt;
        	}
        	
        	// read HTTP header
        	line = br.readLine();
        	cnt += line.length() + 2;
        	while( (!line.isEmpty()) && (line != null) ) { 
        		s = line.split(": ");
        		if(s.length>1)
        			header.put(s[0], s[1]);
        		line = br.readLine();
        		cnt += line.length() + 2;
        	}
        	
        } catch(Exception e) {
        	 e.printStackTrace();  
        }
        
        return cnt;
        
	}
	
	private boolean isFirstLine(String[] s) {
		if(s.length < 3) {
			return false;
		}
		
		if(s[0].startsWith("HTTP/")) {
			packetType = 2;
			version = s[0];
			status = s[1];
			reason = String.join(" ", Arrays.copyOfRange(s, 2, s.length));
			return true;
		} 
		
		if(isMethod(s[0])) {
			packetType = 1;
			method = s[0];
			url = s[1];
			version = s[2];
			return true;
		} else {
			return false;
		}		
		
	}
	
	private boolean isMethod(String s) {
		switch(s) {
		case "GET":
		case "HEAD":
		case "POST":
		case "PUT":
		case "TRACE":
		case "OPTIONS":
		case "DELETE":
			return true;
		default: 
			return false;
		}
	}
	
	public int moreBytes() {
		return moreBytes;
	}
	
	public boolean isValid() {
		if(packetType > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addPacket(TCPPacket tcp) {
		tcps.add(tcp);
	}
	
	public String getInfo() {
		return info;
	}
	
	public String getMethod() {
		return method;
	}
	
	public int getStatus() {
		if (packetType == 2) {
			return Integer.parseInt(status);
		} else {
			return 0;
		}
	}
	
	public int getStartID() {
		return tcps.get(0).getID();
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getURL() {
		return header.get("Host")+url;
	}
	
	public String getPacketIDs() {
		String s = "";
		for(TCPPacket tcp:tcps) {
			s += "  " + tcp.getID();
		}
		return s;
	}
	public String getReasonPhrase() {
		return reason;
	}
}
