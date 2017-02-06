package resolve.decoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class IPPacket extends Packet {
	
	private static final Logger logger = LogManager.getLogger();
	
	private String protocal;
	private String dataType;
	private int dataStart;
	
	protected InetAddress srcIP;
	protected InetAddress dstIP;
	
	String linkDirection;   // UL  or DL	上传或下载地址

	public IPPacket(int id, long second, long ms, byte[] data) {
		super(id, second,ms,data);
		readHeader();
		
	}
	
	private void readHeader(){
		int headLen;
		int offset = super.getDataStart();
		byte v = (byte) ((bytes[offset] & 0xf0 ) >> 4);

		try {
			switch (v) {
			case 4: 
				protocal = "IPv4";
				headLen = (bytes[offset] & 0x0f ) * 4;
				dataStart = headLen + offset;
				dataType = getType(bytes[offset+9]);
				
				srcIP = InetAddress.getByAddress(Arrays.copyOfRange(bytes, offset+12, offset+16));			
				dstIP = InetAddress.getByAddress(Arrays.copyOfRange(bytes, offset+16, offset+20));
				break;
			case 6:
				protocal = "IPv6";
				dataStart = offset + 40;  
				dataType = getType(bytes[offset+6]);
				srcIP = InetAddress.getByAddress(Arrays.copyOfRange(bytes, offset+8, offset+24));
				dstIP = InetAddress.getByAddress(Arrays.copyOfRange(bytes, offset+24, offset+40));
				break;
			default:
				logger.error("The packet {} is not IPv4 or IPv6 ", id);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getType(byte b) {
		switch(b) {
		case 6: return "TCP";
		case 17: return "UDP";
		case 41: return "SIP";
		case 58: return "ICMPv6";
		default: return "Unkown";
		}
	}
	
	public int getPayloadLength() {
		return bytes.length - dataStart;
	}
	public int getEthLength() {
		return super.getEthLength();
	}
	public String getProtocal() {
		return protocal;
	}
	
	public InetAddress getSrcIP() {
		return srcIP;
	}
	
	public InetAddress getDstIP() {
		return dstIP;
	}
	
	public int getDataStart() {
		return dataStart;
	}
	
	public String getDataType(){
		return dataType;
	}
	
	public void setLinkDirection(String direction) {
		linkDirection = direction;
	}
	
	public String getLinkDirection() {
		return linkDirection;
	}

}
