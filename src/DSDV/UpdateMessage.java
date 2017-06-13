package DSDV;

import java.util.LinkedList;

import SimulationNetwork.Message;

public class UpdateMessage extends Message{
	
	LinkedList<UpdateMessageEntry> updates;
	
	public UpdateMessage(){
		updates = new LinkedList<UpdateMessageEntry>();
	}
	
	public void addUpdate(UpdateMessageEntry update){
		updates.add(update);
		this.setDataVolume(Message.MESSAGE_SIZE + updates.size()*UpdateMessageEntry.SIZE);
	}
	
	public LinkedList<UpdateMessageEntry> getUpdates(){
		return this.updates;
	}

}
