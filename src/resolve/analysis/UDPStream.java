package resolve.analysis;

import java.net.InetAddress;

public class UDPStream extends Stream {
	
	public UDPStream(InetAddress addA, InetAddress addB,int portA, int portB) {
		super(addA,addB,portA,portB);
	}	

}
