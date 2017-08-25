package OLSR;

import java.util.LinkedList;

import SimulationNetwork.Message;

public class BasicPacketFormat extends Message{
	
	int packetLength;
	int packetSequenceNumber;
	LinkedList<OlsrMessage> messages;
	
	public BasicPacketFormat(){
		
		this.dataVolume = Message.MESSAGE_SIZE + 2*8;
	}

}
