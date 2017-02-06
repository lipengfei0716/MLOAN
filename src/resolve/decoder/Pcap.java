package resolve.decoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.nio.ByteBuffer;

public class Pcap {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final int ETH = 1;  // EtherNet (data link type)
	private static final int DLT = 113;
	
	private boolean swap;
	private int dataLink;
	
	public  Packet[] decode (String file) {
		
		Packet[] packets = null;
		
		try (FileInputStream f = new FileInputStream(file)) {
			readGlobalHeader(f);
			switch(dataLink) {
			case ETH: 				
				packets = readRecordEth(f);
				break;
			case DLT: 		
				packets = readRecordDLT(f);
				break;
			default: logger.error("Only support the datalink of 1、113 (EtherNet)!");
			}
		} catch (Exception e) {
			logger.error("Cannot read the pcap file {}", file);
		}
		return packets;
		
	}
	
	private void readGlobalHeader(FileInputStream f) throws Exception {
        byte[] unit32 = new byte[4];

        f.read(unit32);
        checkSwap(unit32);

        f.read(unit32);  // Skip version_major; and version_minor
        f.read(unit32);  // Skip gint32  thiszone;   - GMT to local correction
        f.read(unit32);  // Skip guint32 sigfigs;    - accuracy of timestamps
        f.read(unit32);  // guint32 snaplen;         - max length of captured packets, in octets

        // Read guint32 network;        -  data link type
        f.read(unit32);
        dataLink =  (int) unit32ToLong(unit32,swap);
          
    }
	
	private void checkSwap(byte[] unit32) {

        long magicNumber = unit32ToLong(unit32, false);

        if(magicNumber == 0xa1b2c3d4) {
            swap = false;
        } else if(magicNumber == 0xd4c3b2a1) {
            swap = true;
        } else {
            logger.error("The file is illegal since I cannot identify the magiNumber!");
        }
    }


    private long unit32ToLong(byte[] unit32, boolean swap) {

        if (swap) {
            return (long) ((unit32[0] & 0xFF) | ((unit32[1] & 0xFF) << 8) | ((unit32[2] & 0xFF) << 16) | ((unit32[3] & 0xFF) <<24));
        } else {
            return (long) ((unit32[3] & 0xFF) | ((unit32[2] & 0xFF) << 8) | ((unit32[1] & 0xFF) << 16) | ((unit32[0] & 0xFF) <<24));
        }
    }
    
    private Packet[] readRecordEth(FileInputStream f) throws Exception {
    	
    	ArrayList<Packet> packetList = new ArrayList<Packet>();
    	
    	byte[] uint32 = new byte[4];
    	long second;
    	long ms;
    	int len;
    	int num = 0;   	
    	
    	do {
    		// guint32 ts_sec;    - timestamp seconds
            if (f.read(uint32) < 4 )  {
            	break;
            }
            second = unit32ToLong(uint32,swap);

            // guint32 ts_usec;      - timestamp microseconds
            if (f.read(uint32) < 4 )  {
            	break;
            }
            ms = unit32ToLong(uint32,swap);

            // guint32 incl_len;   - number of octets of packet saved in file
            if (f.read(uint32) < 4 )  {
            	break;
            }
            len = (int) unit32ToLong(uint32,swap);

            // guint32 orig_len;       -  actual length of packet
            if (f.read(uint32) < 4 )  {
            	break;
            }

            // read the record data
            byte[] data = new byte[len];

            if (f.read(data) < len)  {
            	break;
            }
            
            num += 1;              
            
            
            packetList.add(creatPacket(num, second, ms, data));                    
                      

    	} while(true);
    	
    	Packet[] packets = (Packet[]) packetList.toArray(new Packet[packetList.size()]);
    	    	
    	double startTs = packets[0].getTs();
    	
    	
    	for (int i=0; i<packets.length; i++) {
    		packets[i].setDeltaTs(startTs);  
    	}
    	
    	return packets;
    }
    /**
     * DLT 协议
     * */
 private Packet[] readRecordDLT(FileInputStream f) throws Exception {
    	
    	ArrayList<Packet> packetList = new ArrayList<Packet>();
    	
    	byte[] uint32 = new byte[4];
    	long second;
    	long ms;
    	int len;
    	int num = 0;   	
    	
    	do {
    		// guint32 ts_sec;    - timestamp seconds
            if (f.read(uint32) < 4 )  {
            	break;
            }
            second = unit32ToLong(uint32,swap);

            // guint32 ts_usec;      - timestamp microseconds
            if (f.read(uint32) < 4 )  {
            	break;
            }
            ms = unit32ToLong(uint32,swap);

            // guint32 incl_len;   - number of octets of packet saved in file
            if (f.read(uint32) < 4 )  {
            	break;
            }
            len = (int) unit32ToLong(uint32,swap);

            // guint32 orig_len;       -  actual length of packet
            if (f.read(uint32) < 4 )  {
            	break;
            }

            // read the record data
            byte[] data = new byte[len];

            if (f.read(data) < len)  {
            	break;
            }
            
            num += 1;              
            
            
            packetList.add(creatPacket(num, second, ms, data));                    
                      

    	} while(true);
    	
    	Packet[] packets = (Packet[]) packetList.toArray(new Packet[packetList.size()]);
    	    	
    	double startTs = packets[0].getTs();
    	
    	
    	for (int i=0; i<packets.length; i++) {
    		packets[i].setDeltaTs(startTs);  
    	}
    	
    	return packets;
    }
    private Packet creatPacket(int id, long second, long ms, byte[] data) { 
    	
    	final int ETH_HEAD_LEN = 14;
    	final int DLT_HEAD_LEN = 16;
    	final int MAC_ADDR_LEN = 6;  // 6 bytes for the MAC address
    	final short IPv4 = 0x0800;   // Type field in the Ethernet Frame
    	final short IPv6 = (short) 0x86DD;
    	final short TCP = 6;
    	final short UDP = 17;
    	
    	ByteBuffer b = ByteBuffer.wrap(data);
    	short dataType = b.getShort(2 * MAC_ADDR_LEN);
    	short dataType1 = b.getShort(14);
    	if (dataType == IPv4) {
    		dataType = b.get(ETH_HEAD_LEN + 9);
    	} else if (dataType == IPv6) {
    		dataType = b.get(ETH_HEAD_LEN + 6);
    	} else if (dataType1 == IPv4) {
    		dataType = b.get(DLT_HEAD_LEN + 9);
    	} else if (dataType1 == IPv6) {
    		dataType = b.get(DLT_HEAD_LEN + 6);
    	} else {
    		return new Packet(id, second, ms, data);
    	}
    	
    	if (dataType == UDP) {
    		return new UDPPacket(id, second, ms, data);
    	}
    	
    	if (dataType == TCP) {
    		return new TCPPacket(id, second, ms, data);
    	}

    	return new IPPacket(id, second, ms, data);
    	
    }
    

}
	
