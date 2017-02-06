package resolve.decoder;


import java.nio.ByteBuffer;
import java.util.Arrays;

public class UDPPacket extends IPPacket {	

	
	public static final int DNS_PORT = 53;
	
	private String protocal;
	private String dataType;
	private int dataStart;
	
	protected int srcPort;
	protected int dstPort;
	
	DNSPacket dns;
	
	UDPPacket(int id, long second, long ms, byte[] data) {
		super(id, second,ms,data);
		protocal = "UDP";
		dataStart = super.getDataStart() + 8;
		readHeader();	
		dataType = checkDNS();
		
		if(dataType == "DNS") {
			dns = new DNSPacket(super.getID(), Arrays.copyOfRange(data,dataStart,data.length));
		}
		
		
	}
	
	private void readHeader() {
		int offset = super.getDataStart();
		ByteBuffer b = ByteBuffer.wrap(bytes);
		srcPort = b.getShort(offset) & 0xFFFF;
		dstPort = b.getShort(offset+2) & 0xFFFF;		
	}
	
	private String checkDNS() {
		if (dstPort == DNS_PORT || srcPort == DNS_PORT) {
			return "DNS";
		} else {
			return "Unknown";
		}
	}
	
	public boolean isDNS() {
		if (dataType == "DNS") {
			return true;
		} else {
			return false;
		}
	}
	
	public int getPayloadLength() {
		return bytes.length - dataStart;
	}
	
	public int getLen() {
		return bytes.length - super.getDataStart();
	}
	
	public String getProtocal() {
		return protocal;
	}
	
	public int getSrcPort() {
		return srcPort;
	}
	
	public int getDstPort() {
		return dstPort;
	}
	
	public int getDataStart() {
		return dataStart;
	}
	
	public String getDataType(){
		return dataType;
	}
	
	public DNSPacket getDNS() {
		return dns;
	}
	
	public String label() {
		String s1 = super.getSrcIP().getHostAddress() + ":" + srcPort;
		String s2 = super.getDstIP().getHostAddress() + ":" + dstPort;
		
		if(s1.compareTo(s2) > 0) {
			return s2 + "-" + s1;
		} else {
			return s1 + "-" + s2;
		}
	}

}
