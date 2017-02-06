package resolve.decoder;

import java.nio.ByteBuffer;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DNSPacket {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final short TYPE_A = 1;
	private static final short TYPE_CNAME = 5;
	private static final short TYPE_AAAA = 28;
	//private static final short QR_QUERY = 0;
	private static final short QR_RESPONSE = 1;
	
	private int id;   // Transaction ID (16 bits)
	private int qr;     // QR: Query(0)/Response(1)
	private short queryCount; // 
	private short answerCount;
	private String host;
	private short qType;
	private short qClass;
	
	Set<InetAddress> ips = new HashSet<InetAddress>();
	Set<String> cnames = new HashSet<String>();
	
	
	
	DNSPacket(int packetID, byte[] data) {
		ByteBuffer b = ByteBuffer.wrap(data);
		id = b.getShort() & 0xFFFF;
		qr = (b.get() & 0x80) >> 7;
		b.get();
		queryCount = b.getShort();
		if(queryCount > 1) {
			logger.warn("DNS packet (#{}) with more than one query. MalFormed!!!", packetID);
		} else {
			answerCount = b.getShort();
			b.getShort();    // Authority Record count
			b.getShort();    // Additional Information Count
			StringBuffer s = new StringBuffer();
			b.position(parseName(s, data,b.position()));
			host = s.toString();
			qType = b.getShort();
			qClass = b.getShort();
			if ( ( qClass != 1) || (qType != TYPE_A && qType != TYPE_AAAA)) {
				logger.warn("Don't support the DNS query:  qType=" + qType + ", qClass=" + qClass);				
			} else if (qr == QR_RESPONSE) {				
				readAnswers(data, b);
			}			
			
		}
		
	}
	
	@SuppressWarnings("unused")
	private void readAnswers(byte[] data, ByteBuffer b) {
		short aType;  // Answer Type
		short aClass;  // Answer Class
		int ttl;     // TTL
		short len;   // RDLENGTH
		
		for (int i=0; i<answerCount;i++) {
			StringBuffer s = new StringBuffer();			
			b.position(parseName(s, data,b.position()));			
			aType = b.getShort();
			aClass = b.getShort();
			ttl = b.getInt();
			len = b.getShort();
			if(aClass != 1) {
				logger.warn("Don't support the DNS answer:  aClass=" + aClass);
				continue;
			}
			switch(aType) {
			case TYPE_A:
			case TYPE_AAAA:
				byte[] rdata = new byte[len];
				b.get(rdata,0,len);
				try{
					ips.add(InetAddress.getByAddress(rdata));
				} catch (Exception e) {
					logger.warn("Din't get the IP address");
				}
				break;
			case TYPE_CNAME:
				s.delete(0, s.length());				
				b.position(parseName(s, data,b.position()));
				cnames.add(s.toString());
				break;
				default:
					logger.warn("Don't support the DNS answer:  aType=" + aType);
			}
			
		}
		
	}
	
	private int parseName(StringBuffer s, byte[] data, int start) {		
		
		
		int idx = start;
		ByteBuffer b = ByteBuffer.wrap(data);
		
		while (data[idx] !=0 ) {
			boolean compress = (data[idx] & 0xc0) == 0xc0;
			if (compress) {
				parseName(s, data, b.getShort(idx)&0x3fff);
				idx += 1;
				break;
			} else {
				if (s.length() > 0) {
					s.append(".");					
				}
				s.append(new String(data, idx + 1, data[idx]));
				idx += (data[idx] + 1);
			}
			
		}
		
		return ++idx;
	}
	
	
	
	public int getID() {
		return id;
	}
	
	public int getQR() {
		return qr;
	}
	
	public String getHost() {
		return host;
	}
	
	public Set<InetAddress> getIPs() {
		return ips;
	}
	
	public Set<String> getCnames() {
		return cnames;
	}
	
	public boolean isMalformed() {
		if(queryCount > 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
}
