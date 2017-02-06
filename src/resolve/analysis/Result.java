package resolve.analysis;


import java.util.ArrayList;
import java.util.Map;

import resolve.decoder.IPPacket;
import resolve.decoder.Packet;
import resolve.decoder.TCPPacket;
import resolve.decoder.UDPPacket;

import java.util.HashMap;

public class Result {
	
	protected Packet[] packets;
	
	protected IPStat ipStat;
	private int  ipPacketCnt;  
	private int ethByteCnt;
	private int ipByteCnt;
	ArrayList<Integer> packetLens;   // IP Length (including IP header)
	protected ArrayList<TCPStream> tcpStreams;
	protected ArrayList<UDPStream> udpStreams;
	protected ArrayList<DNSStream> dnsStreams;
	
	public Result(Packet[] packets) {
		this.packets = packets;
		ipStat = new IPStat(packets);		
		
		packetLens = new ArrayList<>();
		packetStat();
		
		tcpStreams = new ArrayList<TCPStream>();
		udpStreams = new ArrayList<UDPStream>();
		dnsStreams = new ArrayList<DNSStream>();
	}
	
	private void packetStat() {
		
		ipPacketCnt = 0;
		ethByteCnt = 0;
		ipByteCnt = 0;
		
		for(Packet p:packets) {
			if(p instanceof IPPacket) {
				IPPacket ip = (IPPacket) p;
				ipPacketCnt++;
				ethByteCnt += ip.getEthLength();
				ipByteCnt += ip.getIPLength();
				packetLens.add(ip.getIPLength());
			}
		}
	}
	public void assembleStreams() {
		UDPStreamAssemble();
		TCPStreamAssemble();
		DNSStreamAssemble();  // Must be called after UDPStreamAssemble()
		
		udpStreams.forEach(x -> x.calcTime());
		tcpStreams.forEach(x -> x.calcTime());
		dnsStreams.forEach(x -> x.calcTime());
		
		tcpStreams.forEach(x -> x.calRelativeNum());
		tcpStreams.forEach(x -> x.checkHandshake());
		tcpStreams.forEach(x -> x.calcWinSize());
		tcpStreams.forEach(x -> x.calcRTTs());
		tcpStreams.forEach(x -> x.decodeHTTP());	
		tcpStreams.forEach(x -> x.packetStat());

	}
	
	private void UDPStreamAssemble() {
		String s;
		Packet p;
		UDPPacket udp;
		Map<String,Integer> strCount = new HashMap<String,Integer>();
		int cnt = 0;
		Integer idx;
		
		for(int i=0; i<packets.length; i++) {
			p = packets[i];
			if(p instanceof UDPPacket) {
				udp = (UDPPacket) p;
				s = udp.label();
				if(strCount.get(s) == null) {
					strCount.put(s, cnt);
					cnt++;
					if(udp.getLinkDirection() == "UL") {
						udpStreams.add(new UDPStream(udp.getSrcIP(), udp.getDstIP(), udp.getSrcPort(), udp.getDstPort()));
					} else {
						udpStreams.add(new UDPStream(udp.getDstIP(), udp.getSrcIP(), udp.getDstPort(), udp.getSrcPort()));						
					}
					
				}
				idx = strCount.get(s);
				udpStreams.get(idx).addPacket((IPPacket) p);			
			}			
		}		
		
	}
	private void TCPStreamAssemble() {
		String s;
		Packet p;
		TCPPacket tcp;
		Map<String,Integer> strCount = new HashMap<String,Integer>();
		int cnt = 0;
		Integer idx;
		
		for(int i=0; i<packets.length; i++) {
			p = packets[i];
			if(p instanceof TCPPacket) {
				tcp = (TCPPacket) p;
				s = tcp.label();
				if(strCount.get(s) == null) {
					strCount.put(s, cnt);
					cnt++;
					if(tcp.getLinkDirection() == "UL") {
						tcpStreams.add(new TCPStream(tcp.getSrcIP(), tcp.getDstIP(), tcp.getSrcPort(), tcp.getDstPort()));
					} else {
						tcpStreams.add(new TCPStream(tcp.getDstIP(), tcp.getSrcIP(), tcp.getDstPort(), tcp.getSrcPort()));						
					}					
				}
				idx = strCount.get(s);
				
				tcpStreams.get(idx).addPacket((IPPacket) p);			
			}
			
		}
	}
	private void DNSStreamAssemble() {					
		
		for(int i=0; i<udpStreams.size(); i++) {
			ArrayList<IPPacket> pList = udpStreams.get(i).getPackets();
			UDPPacket[] packets = new UDPPacket[pList.size()];	
			pList.toArray(packets);
			if(packets[0].isDNS()) {								
				DNSStream stream = new DNSStream();
				
				for(int j=0; j<packets.length; j++) {
					stream.addPacket(packets[j]);
				}
				dnsStreams.add(stream);
			}
			
		}
		
	}
	
	public int getIPPacketCnt() {
		return ipPacketCnt;
	}
	
	public int getEthByteCnt() {
		return ethByteCnt;
	}
	
	public int getIPByteCnt() {
		return ipByteCnt;
	}
	
	public ArrayList<Integer> getPacketLens() {
		return packetLens;
	}
}
