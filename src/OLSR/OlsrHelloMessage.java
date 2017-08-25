package OLSR;

import java.util.LinkedList;

public class OlsrHelloMessage extends OlsrMessage{

	int willingness;
	int hTime;										//HELLO emission interval in s
	LinkedList<Integer> neighborInterfaceAddresses;
	
	
	public OlsrHelloMessage(){
		neighborInterfaceAddresses = new LinkedList<Integer>();
		willingness = 3; //WILL_DEFAULT
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
	
}
