package OLSR;

import java.util.LinkedList;

import Simulator.Message;
import Simulator.PayloadMessage;

public class BasicPacketFormat extends Message{
	
	int packetLength;
	int packetSequenceNumber;
	LinkedList<OlsrMessage> messages;
	
	public BasicPacketFormat(){
		this.messages = new LinkedList<OlsrMessage>();
		this.dataVolume = Message.MESSAGE_SIZE + 2*8;
		this.destinationID = -1;
	}
	
	public void addMessage(OlsrMessage msg){
		this.messages.add(msg);
		this.dataVolume += msg.getMessageSize();
	}
	
	public LinkedList<OlsrMessage> getMessages(){
		return messages;
	}
	
	public BasicPacketFormat clone(){
		BasicPacketFormat copy = new BasicPacketFormat();
		copy.dataVolume = dataVolume;
		copy.destinationID = destinationID;
		copy.endTransmissionTime = endTransmissionTime;
		
		//TODO: copy messages
		for(OlsrMessage msg: messages){
			OlsrMessage msgCopy = msg.getCopy();
			copy.addMessage(msgCopy);
		}
		
		copy.packetLength = packetLength;
		copy.packetSequenceNumber = packetSequenceNumber;
		copy.remainingTransmissionTime = remainingTransmissionTime;
		copy.senderID = senderID;
		copy.startTransmissionTime = startTransmissionTime;

		
		return copy;
	}

}
