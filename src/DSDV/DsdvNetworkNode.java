package DSDV;

import java.util.LinkedList;

import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class DsdvNetworkNode extends NetworkNode{
	
	private static final long UPDATE_INTERVAL = 60000;	//in ms
	private long lastUpdate;
	private LinkedList<ForwardTableEntry> forwardTable;

	public DsdvNetworkNode(int id) {
		super(id);
		lastUpdate = 0;
		forwardTable = new LinkedList<ForwardTableEntry>();
	}
	
	protected void performeTimeDependentTasks(long executionTime){
		if(this.lastUpdate >= UPDATE_INTERVAL){
			this.sendForwardTableUpdate();
			this.lastUpdate = 0;
		}
		else{
			lastUpdate += executionTime;
		}
	}

	private void sendForwardTableUpdate() {
		UpdateMessage updateMessage = new UpdateMessage();
		
		for(ForwardTableEntry entry: forwardTable){
			UpdateMessageEntry updateEntry = new UpdateMessageEntry(entry.getDestination(), entry.getMetric(),entry.getSequenceNumber());
			updateMessage.addUpdate(updateEntry);
		}
		
		updateMessage.setDestinationID(-1);
		
		this.sendMsg(updateMessage);
		
	}

	@Override
	public void processRecivedMessage() {
		Message receivedMsg = inputBuffer.removeFirst();
		if (receivedMsg instanceof UpdateMessage) {
			this.receiveUpdateMessage((UpdateMessage)receivedMsg);
		}
		else{
			this.receivePayloadMessage((PayloadMessage)receivedMsg);	
		}
	}

	private void receivePayloadMessage(PayloadMessage msg) {
		if(msg.getPayloadDestinationAdress() == this.id){
			this.numberRecivedPayloadMsg++;
		}
		else{
			this.sendMsg(msg);
		}
	}

	private void receiveUpdateMessage(UpdateMessage msg) {
		for(UpdateMessageEntry entry: msg.getUpdates()){
			this.updateForwardTable(entry, msg.getSenderID());
		}
	}
	
	private void updateForwardTable(UpdateMessageEntry updateMessageEntry, int senderID){
		for(ForwardTableEntry tableEntry: this.forwardTable){
			if(tableEntry.getDestination() == updateMessageEntry.getDestination()){
				
				if(tableEntry.getSequenceNumber() < updateMessageEntry.getSequenceNumber()){
					tableEntry.setSequenceNumber(updateMessageEntry.getSequenceNumber());
					tableEntry.setNextHop(senderID);
					tableEntry.setMetric(updateMessageEntry.getMetric() +1);
				}
				else{
					if((tableEntry.getSequenceNumber() == updateMessageEntry.getSequenceNumber()) && 
							(tableEntry.getMetric() < updateMessageEntry.getMetric())){
						tableEntry.setNextHop(senderID);
						tableEntry.setMetric(updateMessageEntry.getMetric() +1);
					}		
				}	
				return;
			}
		}
		
		//Forwardtable does not contailn destiantion from updateMessageEntry
		ForwardTableEntry tableEntry = new ForwardTableEntry(updateMessageEntry.getDestination(), senderID, 
				updateMessageEntry.getMetric(), updateMessageEntry.getSequenceNumber(), simulator.getNetworkLifetime());
		this.forwardTable.add(tableEntry);
	}
	
	@Override
	public void startSendingProcess(PayloadMessage tmpMsg) {
		// TODO Auto-generated method stub
		
	}

}
