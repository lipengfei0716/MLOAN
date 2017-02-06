package resolve.decoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.nio.ByteBuffer;


public class Packet {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final int DLT_HEAD_LEN = 16;
	private static final int ETH_HEAD_LEN = 14;
	private static final int MAC_ADDR_LEN = 6;  // 6 bytes for the MAC address
	private static final int DLT_ADDR_LEN = 7; 
	
	protected int id;    // The packet index
	private long second;
	private long ms;
	private double ts;
	private double deltaTs;	
	private int dataStart;
	private String protocal;
	private String dataType;
	
	byte[] bytes;
		
	private byte[] srcMAC;
	private byte[] dstMAC;
	
	public Packet(int index, long seconds, long microSeconds, byte[] data) {
		id = index;
		second = seconds;
		ms = microSeconds;
		bytes = data;
		ts = calcTs();
		dstMAC = Arrays.copyOfRange(bytes, 0, MAC_ADDR_LEN);
		srcMAC = Arrays.copyOfRange(bytes, MAC_ADDR_LEN, 2 * MAC_ADDR_LEN);
		String dataType1 = readType(data,2 * DLT_ADDR_LEN);
		String dataType2 = readType(data,2 * MAC_ADDR_LEN);
		if(!dataType1.equals("")){
			dataStart = DLT_HEAD_LEN;
			protocal = "DLT";
			dataType = dataType1;
		}else if(!dataType2.equals("")){
			dataStart = ETH_HEAD_LEN;
			protocal = "ETH";
			dataType = dataType2;
		}else{
			logger.error("Don't support the type {} of Ethernet frames.", dataType );
		}
	}
	
	private String readType(byte[] data, int position) {
		final short IPv4 = 0x0800;   // Type field in the Ethernet Frame	-----	16bit 协议类型 IPv4
		final short IPv6 = (short) 0x86DD;		
		final short ARP = 0x0806;	// 
		
		ByteBuffer b = ByteBuffer.wrap(data);
		
		switch(b.getShort(position)) {
		case IPv4: 
			return "IPv4"; 
		case IPv6: 
			return  "IPv6"; 
		case ARP:
			return "ARP";
		}
		return "";
	}
	private double calcTs() {
		return second + ms / 1000000.0;
	}
	
	public void setDeltaTs(double startTs) {
		deltaTs = ts - startTs;		
	}
	
	public double getDeltaTs() {
		return deltaTs;		
	}
	
	public double getTs(){
		return ts;
	}
	
	public int getID() {
		return id;
	}
	
	public int getEthLength() {
		return bytes.length;   // 流量
	}
	
	public int getIPLength() {
		return bytes.length - ETH_HEAD_LEN; 
	}
	
	public int getPayloadLength() {
		return bytes.length - dataStart;
	}
	
	public String getProtocal() {
		return protocal;
	}
	
	public byte[] getSrcMAC() {
		return srcMAC;
	}
	
	public byte[] getDstMAC() {
		return dstMAC;
	}
	
	public int getDataStart() {
		return dataStart;
	}
	
	public String getDataType(){
		return dataType;
	}
	

}
