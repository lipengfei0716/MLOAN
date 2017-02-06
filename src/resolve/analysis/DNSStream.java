package resolve.analysis;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import resolve.decoder.DNSPacket;
import resolve.decoder.IPPacket;
import resolve.decoder.UDPPacket;

public class DNSStream extends Stream {
	
	private int id = -1;		
	private String host;
	private Set<InetAddress> ips;
	private InetAddress req_ips;
	private InetAddress res_ips;
	private Set<String> cnames;
	private boolean isMalformed;
	

	
	public DNSStream() {	
		super();
		ips = new HashSet<InetAddress>();
		cnames = new HashSet<String>();	
		isMalformed = true;

	}
	
	public Set<InetAddress> getIPs() {
		return ips;
	}
	
	public Set<String> getCnames() {
		return cnames;
	}
	
	public boolean isMalformed() {
		return isMalformed;
	}
	
	public int getId() {
		return id;
	}

	public String getHost() {
		return host;
	}

	public InetAddress getReq_ips() {
		return req_ips;
	}

	public InetAddress getRes_ips() {
		return res_ips;
	}

	public void addPacket(UDPPacket p) {
		super.addPacket((IPPacket) p);
		DNSPacket dns = p.getDNS();
		if(!dns.isMalformed()) {
			isMalformed = true;
		}
		
		if(dns.getQR() == 0) {
			id = dns.getID();
			host = dns.getHost();
			req_ips = p.getSrcIP();
			ips = dns.getIPs();
			cnames = dns.getCnames();
		} else {
			id = dns.getID();
			host = dns.getHost();
			res_ips = p.getDstIP();
			ips = dns.getIPs();
			cnames = dns.getCnames();
		}		
	}
}
