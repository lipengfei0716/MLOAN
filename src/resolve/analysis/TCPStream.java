package resolve.analysis;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import resolve.util;
import resolve.decoder.Packet;
import resolve.decoder.TCPPacket;

public class TCPStream extends Stream {
	
	private final static int HTTP_PORT = 80;
    private DecimalFormat dfAfter = new DecimalFormat("0");
    
	private ArrayList<DNSStream> dns;
	private ArrayList<Double> rtts;
	private double meanRTT;
	private int setupSteps;
	private int closeSteps;
	
	private TCPPacket setup1;   // Three-way handshake (for the set-up)  三次握手
	private TCPPacket setup2;
	private TCPPacket setup3;	
	private int handsEachByteLen;	//交互流量	SUM（每一条有效链路IP流量，去除三次握手）
	
	private double setup1Ts;	   // 第一次握手时间
	private double setup3Ts;	   // 第三次握手时间
	private double time1Ts;	   // 建链后第一次交互时间
	private double fillData1Ts;	   // 第一个数据包时间
	private double fillData9Ts;	   // 最后一个数据包时间
	private double data1Ts;	   // 第一个携带数据的包的时间
	private double firstFinUpTime;// 第一个FIN前的时间
	private double laseFinUpTime; // 最后一个FIN前的时间
	private double close1Time;	// 第一次断链时间
	private double close33Time;	// 倒数第二次断链时间
	private double close4Time;	// 第四次断链时间
	private double close44Time;	// 最后一次断链时间
	private double close11Len;
	private double close22Len;
	private double close33Len;	
	private double close44Len;	
	private double allByteLen;
	private TCPPacket close1,close2,close3,close4,close33,close44;  // Modified three-way handshake (for the close)
	private double setupRTT;
	int retranNum;
	
	private boolean hasData;
	private boolean hasResp;
	private boolean hasSyn;
	private boolean firstFin; 
	private boolean inclSYN;  //是否含SYN
	private boolean inclFIN;  //是否含FIN
	private boolean inclHTTP; //是否含HTTP
	private double firstDataTs;
	private double lastDataTs;
	private double dataDuration;  // excluding the set-up and closing procedures
	private double duration;  
	private double idleDuration;
	private double firstRespTs;
	private int payloadLength = 0; //数据和
	private String dataType;    //协议类型
	private int moreBytesTemp = 0;
	private InetAddress ipTemp = null;
	private ArrayList<HTTP> https;
	private Map<Integer, Integer> mapRate = new HashMap<Integer, Integer>(); //吞吐流量
	private List<Integer> tcpEthLen = new ArrayList<Integer>(); //所有包长/流量
	
	public TCPStream(InetAddress addA, InetAddress addB,int portA, int portB) {
		super(addA,addB,portA,portB);
		dns = new ArrayList<DNSStream>();
		rtts = new ArrayList<Double>();
		setupSteps = 0;
		closeSteps = 0;
	}
	public void setDNSStream(ArrayList<DNSStream> dnsStreams) {		
		dnsStreams.forEach(x -> { if(x.getIPs().contains(addB) && (x.getStartTs() < startTs)) dns.add(x); });	
	}
	
	public ArrayList<DNSStream> getDNSStream() {
		return dns;
	}
	
	public void calcRTTs() {
		meanRTT = 0;
		for(Packet packet1:ulPackets) {
			TCPPacket p1 = (TCPPacket) packet1;
			if(p1.getPayloadLength() == 0) {
				continue;
			}
			for(Packet packet2:dlPackets) {
				TCPPacket p2 = (TCPPacket) packet2;				
				if(p2.isACK() && (p2.getAckNum() == p1.getNextSeqNum()) ) {
					double rtt = p2.getDeltaTs() -  p1.getDeltaTs();
					if(rtt > 0) {
						rtts.add(rtt);
						meanRTT += rtt;
						break;
					}
				}
			}
			
		}
		
		if(rtts.size()>0) {
			meanRTT /= rtts.size();
		}
	}
	
	public void calRelativeNum() {
		if(ulPackets.size()>0) {
			TCPPacket tcp = (TCPPacket) ulPackets.get(0);
			long SeqNum0 = tcp.getSeqNum0();
			ulPackets.forEach( x -> { 
				TCPPacket t = (TCPPacket) x;
				long seqNum = util.uintSubtract(t.getSeqNum0(), SeqNum0);
				t.setSeqNum(seqNum);
			});
			dlPackets.forEach( x -> { 
				TCPPacket t = (TCPPacket) x;
				long ackNum = util.uintSubtract(t.getAckNum0(), SeqNum0);
				t.setAckNum(ackNum);
			});
		}
		if(dlPackets.size()>0) {
			TCPPacket tcp = (TCPPacket) dlPackets.get(0);
			long SeqNum0 = tcp.getSeqNum0();
			dlPackets.forEach( x -> { 
				TCPPacket t = (TCPPacket) x;
				long seqNum = util.uintSubtract(t.getSeqNum0(), SeqNum0);
				t.setSeqNum(seqNum);
			});
			ulPackets.forEach( x -> { 
				TCPPacket t = (TCPPacket) x;
				long ackNum = util.uintSubtract(t.getAckNum0(), SeqNum0);
				t.setAckNum(ackNum);
			});
		}
		
	}
	public void checkHandshake() {
		if(packets.size() < 3) {
			return;
		} 
		int cnt = 0;
		int cnt1 = 1;
		int cnt2 = 1;
		int deltaTs = 0;
		int upDeltaTs = 0;
		int diffTs = 0;
		int upEthLength = 0;
		int currEthLength = 0;
		int avgLen = 0;
		int finNo = 0;
		int fisrtFinNo = 0;
		double close33_up = 0;
		boolean first = true;
		boolean fillNo = true;
		boolean data1No = true;
		String retKey = "";
		int retValue = 0;
		Map<String, Integer> retran = new HashMap<String, Integer>(); //重传识别   key=ACK，value=SEQ
		
		for(Packet packet:packets) {
			
			TCPPacket p = (TCPPacket) packet;
			
			if(first){
				if(p.isSYN())
					inclSYN = true;
				first = false;
			}
			if(data1No && p.getPayloadLength()>0){
				data1Ts = p.getDeltaTs();
				data1No = false;
			}
			retKey = Long.toString(p.getAckNum0())+","+Long.toString(p.getSeqNum0())+","+p.getWinSize()+","+p.isFIN();
			retValue = p.getPayloadLength();
			if(inclSYN){
				if(cnt1==4 && retran.containsKey(retKey)){
					if(retran.get(retKey)>=retValue)
						retranNum++;
				}else{
					retran.put(retKey, retValue);
					if(p.isFIN() && p.isACK()) {
						close33 = p;
						finNo++;
					}
				}
			}else{
				if(retran.containsKey(retKey)){
					if(retran.get(retKey)>=retValue)
						retranNum++;
				}else{
					retran.put(retKey, retValue);
					if(p.isFIN() && p.isACK()) {
						close33 = p;
						finNo++;
					}
				}
			}
			if(cnt==1 && p.getPayloadLength()>0){
				fillData1Ts = p.getDeltaTs(); 
				cnt++;
			}
			
			if(isHttp(p)){	// 当前报文是否为 HTTP
				cnt++;
				inclHTTP = true;
			}	
			
			if(fisrtFinNo==0){
				if(p.isFIN())
					firstFin = true;
				fisrtFinNo++;
			}
			tcpEthLen.add(p.getEthLength());
			allByteLen += p.getEthLength();

			int getPayloadLength = p.getPayloadLength();
			if(getPayloadLength>0){
				payloadLength += getPayloadLength;
				fillData9Ts = p.getDeltaTs(); 
			}
			if(cnt1==4 && fillNo){
				time1Ts = p.getDeltaTs();
				fillNo = false;
			}
			if(cnt1==4 && cnt2==1 && !p.isSYN() && (p.getEthLength()-54)>0 && !p.isFIN()){	// 吞吐曲线（不含DNS、SYN、FIN）
				
				deltaTs = Integer.parseInt(dfAfter.format(new BigDecimal(p.getDeltaTs())));//  时间向下取整(s)
				currEthLength = p.getEthLength();
				if(deltaTs == 0){
					mapRate.put(deltaTs, p.getEthLength());
					upDeltaTs = deltaTs;
					upEthLength = p.getEthLength();
				}else if(mapRate.containsKey(deltaTs)){
					mapRate.put(deltaTs, mapRate.get(deltaTs) + currEthLength);
				}else{
					diffTs = deltaTs - upDeltaTs;
					if(diffTs==0){
						mapRate.put(deltaTs, currEthLength);
					}else{
						avgLen = (currEthLength - upEthLength)/diffTs;
						for(int i=diffTs;i>0;i--){
							if(avgLen == 0 || i==1)
								mapRate.put(deltaTs-i, currEthLength);
							else
								mapRate.put(deltaTs-i, currEthLength+avgLen*i);
						}
					}
					upDeltaTs = deltaTs;
					upEthLength = currEthLength;
				}
			}
			
			if((cnt1==1) && p.isSYN() && (!p.isACK())) {
				setup1 = p;
				setup1Ts = setup1.getDeltaTs();
				handsEachByteLen += p.getEthLength();
				cnt1++;
			}
			if((cnt1==2) && p.isSYN() && p.isACK() && (p.getAckNum() == 1)) {
				setup2 = p;
				handsEachByteLen += p.getEthLength();
				cnt1++;
			}
			
			if((cnt1 == 3) && (p.getSeqNum() == 1) && (p.getAckNum() == 1) && p.isACK() ) {
				setup3 = p;
				setup3Ts = setup3.getDeltaTs();
				hasSyn = true;
				handsEachByteLen += p.getEthLength();
				cnt1++;
			}
				
			if((cnt2 == 1) && p.isFIN()  && p.isACK() ) {
				close1 = p;
				close1Time = close1.getDeltaTs();
				inclFIN = true;
				cnt2++;
			}
			if((cnt2 == 2) && p.isACK() && (p.getSeqNum() == close1.getAckNum())  && (util.uintSubtract(p.getAckNum(), close1.getSeqNum()) == 1) ) {
				close2 = p;
				close11Len = close1.getEthLength();
				close22Len = p.getEthLength();
				cnt2++;
			}
			if((cnt2 == 3) && p.isFIN() && p.isACK() && (p.getSeqNum() == close2.getSeqNum())  && (p.getAckNum() == close2.getAckNum()) ) {
				close3 = p;
				cnt2++;
			}
			if((cnt2 == 4) && p.isACK() && (close3.getAckNum() == p.getSeqNum())  && (util.uintSubtract(p.getAckNum(),close3.getSeqNum()) == 1) ) {
				close4 = p;
				close4Time = p.getDeltaTs();
				cnt2++;
			}	
			
			if(finNo==0)
				firstFinUpTime = p.getDeltaTs();
			if(p.isFIN())
				laseFinUpTime = close33_up;  
			
			if(finNo>0 && p.isACK() && (close33.getAckNum() == p.getSeqNum())  && (util.uintSubtract(p.getAckNum(),close33.getSeqNum()) == 1)) {
				close44 = p;
				close33Time = close33.getDeltaTs();
				close44Time = p.getDeltaTs();
				close33Len = close33.getEthLength();
				close44Len = p.getEthLength();
			}else if(finNo>0 && p.isRST()){
				close44 = p;
				close33Time = close33.getDeltaTs();
				close44Time = p.getDeltaTs();
				close33Len = close33.getEthLength();
				close44Len = p.getEthLength();
			}else if(!p.isFIN())
				close33_up = p.getDeltaTs();  // FIN前一条数据时间、且非fin的响应报文时间	
		}
		if(cnt1>3) {
			setupSteps = 3;			
			setupRTT = setup2.getDeltaTs() - setup1.getDeltaTs();
		} else {
			setupSteps = cnt1 -1 ;
		}
		
		closeSteps = cnt2 - 1;
		
		checkDataBurst();
	}
	
	private boolean isHttp(TCPPacket p) {
		
		HTTP http = null;
		boolean firstHttp = false;
		if(isHTTP()){
			if(p.getPayloadLength() != 0) {
			
				if(moreBytesTemp > 0) {	
					if((ipTemp!=null) && ipTemp.equals(p.getSrcIP())) {
						moreBytesTemp -= p.getPayloadLength();
						if (moreBytesTemp == 0) 
							firstHttp = true;
					}
				}else{
					http = new HTTP(p);	
					if(http.isValid()) {				
						moreBytesTemp = http.moreBytes();
						if(moreBytesTemp <= 0) {
							firstHttp = true;
						}else
							ipTemp = p.getSrcIP();
					}			
				}
			}
		}
		return firstHttp;
	}
	
	public void calcWinSize() {
		if (setupSteps < 3) {
			return;
		}
		
		int shift1 = setup1.getWSOPT();
		int shift2 = setup2.getWSOPT();
		
		if( (shift1> 0) && (shift2 > 0) ) {
			ulPackets.forEach( x -> {
				TCPPacket p = (TCPPacket) x;
				if(!p.isSYN()) {
					p.calcWinSize(shift1);
					p.setWSOPT(shift1);
				}
				
			});
			dlPackets.forEach( x -> {
				TCPPacket p = (TCPPacket) x;
				if(!p.isSYN()) {
					p.calcWinSize(shift2);
					p.setWSOPT(shift2);
				}
				
			});
		}
	}
	
	public int getSetupSteps() {
		return setupSteps;
	}
	
	public int getCloseSteps() {
		return closeSteps;
	}
	
	public double getSetupRTT() {
		return setupRTT;
	}
	
	public double getMeanRTT() {
		return meanRTT;
	}
	
	public ArrayList<Double> getRTTs() {
		return rtts;
	}
	

	public boolean isHTTP() {
		if ((portB == HTTP_PORT) || (portA == HTTP_PORT)) {
			return true;
		}
		return false;
	}
	
	public boolean hasHTTP() {
		if(https.size() > 0) {
			return true;
		}
		return false;
	}
	
	public void decodeHTTP() {
		
		if(!isHTTP()) {
			return;
		}
		
		https = new ArrayList<>();
		int moreBytes = 0;
		InetAddress ip = null;
		HTTP http = null;
		
		for(int i=setupSteps; i<packets.size()-closeSteps; i++) {	
			TCPPacket tcp = (TCPPacket) packets.get(i);		
			
			if(tcp.getPayloadLength() == 0) {
				continue;
			}
			
			if(moreBytes > 0) {	
				if((ip!=null) && ip.equals(tcp.getSrcIP())) {
					moreBytes -= tcp.getPayloadLength();
					http.addPacket(tcp);
					if (moreBytes == 0) {
						https.add(http);
					}					
				}
				continue;
			}
			
			http = new HTTP(tcp);	
			
			if(http.isValid()) {				
				moreBytes = http.moreBytes();
				if(moreBytes > 0) {
					ip = tcp.getSrcIP();
				} else {
					https.add(http);
				}
			}			
			
		}
		
		
	}
	public ArrayList<HTTP> getHTTPs() {
		return https;
	}
	private void checkDataBurst() {
		
		hasResp = false;
		
		if(packets.size()>setupSteps+closeSteps) {
			hasData = true;
			firstDataTs = packets.get(setupSteps).getDeltaTs();
			lastDataTs = packets.get(packets.size()-closeSteps-1).getDeltaTs();
			dataDuration = lastDataTs - firstDataTs;
			
			if(closeSteps > 0) {
				idleDuration = packets.get(packets.size()-closeSteps).getDeltaTs() - lastDataTs;
			} else {
				idleDuration = 0;
			}
			
			int idx = setupSteps;
			TCPPacket tcp = (TCPPacket) packets.get(idx);
			while((tcp.getLinkDirection() == "UL") && idx < packets.size()) {
				idx++;
			}
			if(idx < packets.size()) {
				hasResp = true;
				firstRespTs = packets.get(idx).getTs();
			}
			
		} else {
			hasData = false;
			firstDataTs = 0;
			lastDataTs = 0;
			dataDuration = 0;
			idleDuration = 0;
		}
		duration = packets.get(packets.size()-1).getDeltaTs() - packets.get(0).getDeltaTs();
	}
	public boolean hasData() {
		return hasData;
	}
	public boolean hasResp(){
		return hasResp;
	}
	public double getFirstRespTs() {
		return firstRespTs;
	}
	public double getFirstDataTs() {
		return firstDataTs;
	}
	public double getLastDataTs() {
		return lastDataTs;
	}
	public double getDataDuration() {
		return dataDuration;
	}
	public double getDuration() {
		return duration;
	}
	public double getIdleDuration() {
		return idleDuration;
	}
	public double getSetup1Ts() {
		return setup1Ts;
	}
	public double getSetup3Ts() {
		return setup3Ts;
	}
	public double getFillData1Ts() {
		if(!isInclSYN() || !isInclHTTP())
			fillData1Ts = data1Ts;
		return fillData1Ts;
	}
	public double getFillData9Ts() {
		return fillData9Ts;
	}
	public double getLaseFinUpTime() {
		if(!isInclFIN())
			laseFinUpTime = endTs;
		return laseFinUpTime;
	}
	public Map<Integer, Integer> getMapRate() {
		return mapRate;
	}
	public List<Integer> getTcpEthLen() {
		return tcpEthLen;
	}
	public double getClose1Time() {
		return close1Time;
	}
	public double getClose4Time() {
		return close4Time;
	}
	public double getClose33Time() {
		return close33Time;
	}
	public double getClose44Time() {
		return close44Time;
	}
	public String getDataType() {
		return dataType;
	}
	public double getTime1Ts() {
		if(!isInclSYN())
			time1Ts = startTs;
		return time1Ts;
	}
	public int getPayloadLength() {
		return payloadLength;
	}
	public boolean isHasSyn() {
		return hasSyn;
	}

	public boolean isFirstFin() {
		return firstFin;
	}

	public double getClose11Len() {
		return close11Len;
	}

	public double getClose22Len() {
		return close22Len;
	}
	public double getClose33Len() {
		return close33Len;
	}

	public double getClose44Len() {
		return close44Len;
	}
	public double getAllByteLen() {
		return allByteLen;
	}
	public TCPPacket getClose4() {
		return close4;
	}
	public TCPPacket getClose44() {
		return close44;
	}
	public int getRetranNum() {
		return retranNum;
	}
	public boolean isInclSYN() {
		return inclSYN;
	}
	public boolean isInclFIN() {
		return inclFIN;
	}
	public boolean isInclHTTP() {
		return inclHTTP;
	}
	public double getFirstFinUpTime() {
		return firstFinUpTime;
	}
	public int getHandsEachByteLen() {
		return handsEachByteLen;
	}
}


