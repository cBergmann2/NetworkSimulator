package DSDV;

import java.util.LinkedList;

import Simulator.Message;

public class UpdateMessage extends Message{
	
	LinkedList<UpdateMessageEntry> updates;
	
	public UpdateMessage(){
		updates = new LinkedList<UpdateMessageEntry>();
		this.setDataVolume(Message.MESSAGE_SIZE);
	}
	
	public void addUpdate(UpdateMessageEntry update){
		updates.add(update);
		this.setDataVolume(Message.MESSAGE_SIZE + updates.size()*UpdateMessageEntry.SIZE);
	}
	
	public LinkedList<UpdateMessageEntry> getUpdates(){
		return this.updates;
	}

	
	public UpdateMessage clone(){
		UpdateMessage copy = new UpdateMessage();
		copy.dataVolume = dataVolume;
		copy.destinationID = destinationID;
		copy.endTransmissionTime = endTransmissionTime;
		copy.remainingTransmissionTime = remainingTransmissionTime;
		copy.senderID = senderID;
		copy.startTransmissionTime = startTransmissionTime;
		//copy.timeToLive = timeToLive;
		copy.updates = new LinkedList<UpdateMessageEntry>();
		for(UpdateMessageEntry entry: updates){
			copy.updates.add(new UpdateMessageEntry(entry.getDestination(), entry.getMetric(), entry.getSequenceNumber()));
		}
		
		return copy;
	}
}
