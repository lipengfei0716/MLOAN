package resolve.decoder;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TCPPacket extends IPPacket{
	
	private String protocal;
	private String dataType;
	private int dataStart;
	
	protected int srcPort;
	protected int dstPort;
	protected int dataLen;
	private long seqNum0;
	private long seqNum;
	private long nextSeqNum;
	private long ackNum0;
	private long ackNum;
	
	private boolean urg; // URG
	private boolean ack; // ACK
	private boolean psh; // PSH
	private boolean rst; // RST
	private boolean syn; // SYN
	private boolean fin; // FIN
	private int winSize0;
	private int winSize;
	
	private int wsopt; // window size scaling factor (shifted by n bits)
	private boolean sack;  // SACK permitted ?
	private int mss;  // Maximum Segment Size
	
	private boolean ssl;
	@SuppressWarnings("unused")
	private boolean sslHandshake;
	@SuppressWarnings("unused")
	private boolean sslData;
	
	TCPPacket(int id, long second, long ms, byte[] data) {
		super(id, second,ms,data);
		protocal = "TCP";
		wsopt = -1;   // No WSOPT options
		sack = false;     // No SACK options
		mss = -1;     // No MSS options
		readHeader();	
	}
	
	private void readHeader() {
		int offset = super.getDataStart();
		ByteBuffer b = ByteBuffer.wrap(bytes);
		srcPort = b.getShort(offset) & 0xFFFF;
		dstPort = b.getShort(offset+2) & 0xFFFF;
		seqNum0 = b.getInt(offset+4) & 0xFFFFFFFFL;
		ackNum0 = b.getInt(offset+8) & 0xFFFFFFFFL;
		seqNum = seqNum0;
		nextSeqNum = seqNum + getPayloadLength();
		ackNum = ackNum0;
		
		int hLen = (b.get(offset+12) & 0xF0 ) >> 2;
		dataStart = hLen + offset;
		
		byte tmp = b.get(offset+13);
		urg = (tmp & 0x20) != 0;
		ack = (tmp & 0x10) != 0;
		psh = (tmp & 0x08) != 0;
		rst = (tmp & 0x04) != 0;
		syn = (tmp & 0x02) != 0;
		fin = (tmp & 0x01) != 0;
		
		winSize0 = b.getShort(offset+14) & 0xFFFF;
		winSize = winSize0;
		
		if (hLen > 20) {
			checkOptions(Arrays.copyOfRange(bytes, offset+20, offset+hLen));
		}
		
		offset = dataStart;
		do {
			offset = checkSSL(bytes, offset);
		} while (offset >= 0);
		
		
	}
	
	private void checkOptions(byte[] data) {
		
		for(int i=0; i < data.length; ) {		
			
			byte b = data[i];
			switch(b) {
				case 0:
					return;
				case 1:
					i++;
					break;
				case 2:
					mss = (data[i+2] & 0xFF) << 8 | (data[i+3] & 0xFF);
					i += 4;
					break;
				case 3:
					wsopt = data[i+2];
					i += 3;
					break;
				case 4:
					sack = true;
					i += 2;
					break;
				default: 
					i += ( data[i+1] & 0xFF);					
			}
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
	
	public boolean isURG() {
		return urg;
	}
	
	public boolean isACK() {
		return ack;
	}
	
	public boolean isPSH() {
		return psh;
	}
	
	public boolean isRST() {
		return rst;
	}
	
	public boolean isSYN() {
		return syn;
	}
	
	public boolean isFIN() {
		return fin;
	}
	
	public int getWSOPT() {
		return wsopt;
	}
	
	public boolean isSACK() {
		return sack;
	}
	
	public int getMSS() {
		return mss;
	}
	
	public long getSeqNum0() {
		return seqNum0;
	}
	
	public long getAckNum0() {
		return ackNum0;
	}
	
	public long getSeqNum() {
		return seqNum;
	}
	
	public long getAckNum() {
		return ackNum;
	}
	
	public void setSeqNum(long i) {
		seqNum = i; 
		nextSeqNum = seqNum + getPayloadLength();
	}
	
	public long getNextSeqNum() {
		return nextSeqNum;
	}
	
	public void setAckNum(long i) {
		if(ack) {		
			ackNum = i; 
		} else {
			ackNum = 0;
		}
		
	}
	
	public void calcWinSize(int shift) {
		winSize = winSize << shift;
	}
	
	public int getWinSize() {
		return winSize;
	}
	
	public void setWSOPT(int wsopt) {
		this.wsopt = wsopt;
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
	
	
	
	
	private int checkSSL(byte[] data, int offset) {
		
		final byte TLS_CHANGE_CIPHER_SPEC = 20;
		final byte TLS_ALERT = 21;
		final byte TLS_HANDSHAKE = 22;
		final byte TLS_APPLICATION = 23;

		if (data.length >= offset + 5) {			
			
			byte contentType = data[offset];
			byte majorVersion = data[offset+1];
			byte minorVersion = data[offset+2];
			dataLen = (data[offset+3] & 0xFF) << 8 + (data[offset+4] & 0xFF);
			int result = offset + 5 + dataLen;
			if (majorVersion == 3
					&& (minorVersion == 1 || minorVersion == 2 || minorVersion == 3)
					&& (contentType == TLS_CHANGE_CIPHER_SPEC
							|| contentType == TLS_ALERT
							|| contentType == TLS_HANDSHAKE || contentType == TLS_APPLICATION)
					&& data.length >= result) {
				this.ssl = true;
				this.dataType = "SSL";
				if (contentType == TLS_HANDSHAKE) {
					this.sslHandshake = true;
				} else if (contentType == TLS_APPLICATION) {
					this.sslData = true;
				}
				
				return result;
			}
		}
		return -1;
	}	
	
	public byte[] getPayload() {
		return Arrays.copyOfRange(bytes, dataStart, bytes.length);
	}
	public boolean isSsl() {
		return ssl;
	}
}
