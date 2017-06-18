package DSDV;

import java.util.LinkedList;

import SimulationNetwork.Layer3Message;

public class UpdateMessage extends Layer3Message{
	
	LinkedList<UpdateMessageEntry> updates;
	
	public UpdateMessage(){
		updates = new LinkedList<UpdateMessageEntry>();
		this.setDataVolume(Layer3Message.MESSAGE_SIZE);
	}
	
	public void addUpdate(UpdateMessageEntry update){
		updates.add(update);
		this.setDataVolume(Layer3Message.MESSAGE_SIZE + updates.size()*UpdateMessageEntry.SIZE);
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
		copy.timeToLive = timeToLive;
		copy.updates = new LinkedList<UpdateMessageEntry>();
		for(UpdateMessageEntry entry: updates){
			copy.updates.add(new UpdateMessageEntry(entry.getDestination(), entry.getMetric(), entry.getSequenceNumber()));
		}
		
		return copy;
	}
}
