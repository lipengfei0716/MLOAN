package resolve.analysis;

import java.net.InetAddress;
import java.util.ArrayList;

import resolve.decoder.IPPacket;

public class Stream {
	
	ArrayList<IPPacket> packets;			
	ArrayList<IPPacket> ulPackets;
	ArrayList<IPPacket> dlPackets;
	
	InetAddress addA;		// local IP
	InetAddress addB;		// server IP
	int portA;
	int portB;
	
	int inPacketCnt;
	int outPacketCnt;
	int inEthByteCnt;		//上传包数
	int outEthByteCnt;		//下载包数
	int inIPByteCnt;
	int outIPByteCnt;
	
	public double startTs;	//请求 	开始时间  (单位s)
	public double endTs;	//请求	结束时间
	private double duration;		//请求	时长   (DNS的rtt)
	
	int ethByteCnt;			//上传+下载包数=有效链路总包数
	int ethLength;			//上传+下载字节数=有效链路总字节数
	int ipByteCnt;
	int packetCnt;			//有效链路总包数=ethByteCnt
		
	public Stream(InetAddress addA, InetAddress addB,int portA, int portB) {
		
		this();
		
		this.addA = addA;
		this.addB = addB;
		this.portA = portA;
		this.portB = portB;		
		
	}
	
	public Stream() {
		inPacketCnt = 0;
		outPacketCnt = 0;
		inEthByteCnt = 0;
		outEthByteCnt = 0;
		inIPByteCnt = 0;
		outIPByteCnt = 0;
		
		startTs = 0;
		endTs = 0;
		duration = 0;
		
		packets = new ArrayList<IPPacket>();
		ulPackets = new ArrayList<IPPacket>();
		dlPackets = new ArrayList<IPPacket>();
	}
	
	public void addPacket(IPPacket packet) {
		packets.add(packet);
		IPPacket p = (IPPacket) packet;
		
		if(p.getLinkDirection() == "UL") {			
			outPacketCnt++;			
			outEthByteCnt++;		
			outIPByteCnt++;
			ulPackets.add(packet);
		} else {
			inPacketCnt++;		
			inEthByteCnt++;		
			inIPByteCnt++;	
			dlPackets.add(packet);
		}	
		ethLength = p.getEthLength();
	}
	
	public void calcTime() {
		 startTs = packets.get(0).getDeltaTs();
		 endTs = packets.get(packets.size()-1).getDeltaTs();
		 duration = endTs - startTs;
	}
	
	public void packetStat() {		
		
		ethByteCnt = inEthByteCnt + outEthByteCnt;
		ipByteCnt = inIPByteCnt + outIPByteCnt;
			
	}
	public InetAddress getAddA() {
		return addA;
	}
	public InetAddress getAddB() {
		return addB;
	}
	public int getPortA() {
		return portA;
	}
	public int getPortB() {
		return portB;
	}
	public ArrayList<IPPacket> getPackets() {
		return packets;
	}
	public int getPacketCnt() {
		return packets.size();
	}
	public double getStartTs() {
		return startTs;
	}
	public double getEndTs() {
		return endTs;
	}
	public double getDuration() {
		return duration;
	}
	public int getInPacketCnt() {
		return inPacketCnt;
	}
	public int getOutPacketCnt() {
		return outPacketCnt;
	}
	public int getInEthByteCnt() {
		return inEthByteCnt;
	}
	public int getOutEthByteCnt() {
		return outEthByteCnt;
	}
	public int getInIPByteCnt() {
		return inIPByteCnt;
	}
	public int getOutIPByteCnt() {
		return outIPByteCnt;
	}
	public int getEthByteCnt(){
		return ethByteCnt;
	}
	public int getIPByteCnt(){
		return ipByteCnt;
	}
	public int getEthLength() {
		return ethLength;
	}
}
