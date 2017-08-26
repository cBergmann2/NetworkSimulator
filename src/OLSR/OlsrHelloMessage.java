package OLSR;

import java.awt.TrayIcon.MessageType;
import java.util.LinkedList;

public class OlsrHelloMessage extends OlsrMessage{

	int willingness;
	int hTime;										//HELLO emission interval in s
	LinkedList<Integer> neighborInterfaceAddresses;
	
	
	public OlsrHelloMessage(){
		neighborInterfaceAddresses = new LinkedList<Integer>();
		willingness = 3; 							//WILL_DEFAULT
		hTime = 60;									//emit a HELLO Message every second
		ttl = 1;
	}
	
	public int getWillingness() {
		return willingness;
	}
	
	public void setWillingness(int willingness) {
		this.willingness = willingness;
	}
	
	public void addNeighborInterfaceAddress(int address){
		neighborInterfaceAddresses.add(address);
		this.messageSize += 8;
	}
	
	public LinkedList<Integer> getNeighborInterfaceAdresses(){
		return this.neighborInterfaceAddresses;
	}
	
	public int getHTime(){
		return hTime;
	}

	@Override
	public OlsrHelloMessage getCopy() {
		OlsrHelloMessage copy = new OlsrHelloMessage();
		copy.hopCount = hopCount;
		copy.hTime = hTime;
		copy.mesageType = mesageType;
		copy.messageSequenceNumber = messageSequenceNumber;
		copy.messageSize = messageSize;
		
		for(int neighborAddress: neighborInterfaceAddresses){
			copy.addNeighborInterfaceAddress(neighborAddress);
		}
		
		copy.originatorAddress = originatorAddress;
		copy.ttl = ttl;
		copy.willingness = willingness;
		
		return copy;
	}
	
}
