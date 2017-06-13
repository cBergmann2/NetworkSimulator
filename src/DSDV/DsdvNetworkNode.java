package DSDV;

import java.util.LinkedList;

import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class DsdvNetworkNode extends NetworkNode{
	
	private static final long UPDATE_INTERVAL = 600000;	//in ms
	private long lastUpdate;
	private LinkedList<ForwardTableEntry> forwardTable;
	private long numberTransmittedUpdateMsg;
	private LinkedList<PayloadMessage> msgWaintingBuffer;
	private long sequenceNumber;
	
	
	public DsdvNetworkNode(int id) {
		super(id);
		lastUpdate = 600000;
		forwardTable = new LinkedList<ForwardTableEntry>();
		numberTransmittedUpdateMsg = 0L;
		msgWaintingBuffer = new LinkedList<PayloadMessage>();
		this.sequenceNumber = 0;
		this.forwardTable.add(new ForwardTableEntry(this.id,-1, 0, this.sequenceNumber, 0L));
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
			//System.out.println(simulator.getNetworkLifetime() + " - Node " + id + ": recive UpdateMsg from Node " + receivedMsg.getSenderID());
			this.receiveUpdateMessage((UpdateMessage)receivedMsg);
		}
		else{
			if(receivedMsg instanceof PayloadMessage){
				//this.receivePayloadMessage((PayloadMessage)receivedMsg);
				System.out.println(simulator.getNetworkLifetime() + " - Node " + id + ": recive Payload from Node " + receivedMsg.getSenderID());
			}
		}
	}

	private void receivePayloadMessage(PayloadMessage msg) {
		if(msg.getPayloadDestinationAdress() == this.id){
			this.numberRecivedPayloadMsg++;
			this.lastRecivedPayloadMessage = msg;
		}
		else{
			if(msg.getDestinationID() == this.id){
				PayloadMessage copyMsg = msg.clone();
				this.sendMsg(copyMsg);
			}
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
					findMessageToSend(updateMessageEntry.getDestination(), updateMessageEntry.getMetric());
				}
				else{
					if((tableEntry.getSequenceNumber() == updateMessageEntry.getSequenceNumber()) && 
							(tableEntry.getMetric() < updateMessageEntry.getMetric())){
						tableEntry.setNextHop(senderID);
						tableEntry.setMetric(updateMessageEntry.getMetric() +1);
						findMessageToSend(updateMessageEntry.getDestination(), updateMessageEntry.getMetric());
					}		
				}	
				return;
			}
		}
		
		//Forwardtable does not contailn destiantion from updateMessageEntry
		ForwardTableEntry tableEntry = new ForwardTableEntry(updateMessageEntry.getDestination(), senderID, 
				updateMessageEntry.getMetric(), updateMessageEntry.getSequenceNumber(), simulator.getNetworkLifetime());
		this.forwardTable.add(tableEntry);
		findMessageToSend(updateMessageEntry.getDestination(), updateMessageEntry.getMetric());
	}
	
	private void findMessageToSend(int destinationID, int metric){
		
		LinkedList<PayloadMessage> msgToSend = new LinkedList<PayloadMessage>();
		
		if(metric < Integer.MAX_VALUE){
			for(PayloadMessage msg: this.msgWaintingBuffer){
				if(msg.getPayloadDestinationAdress() == destinationID){
					msgToSend.add(msg);
				}
			}
			
			for(PayloadMessage msg: msgToSend){
				this.msgWaintingBuffer.remove(msg);
				this.sendMsg(msg);
			}
			
		}
	}
	
	@Override
	public void startSendingProcess(PayloadMessage tmpMsg) {
		this.sendMsg(tmpMsg);
		
	}
	
	public void sendMsg(Message msg){
		
		msg.setSenderID(this.id);
		
		if(msg instanceof UpdateMessage){
			//System.out.println(simulator.getNetworkLifetime() + " - Node " + id + ": send UpdateMsg");
			this.numberTransmittedUpdateMsg++;
			
			msg.setDestinationID(-1);
			
			this.outputBuffer.add(msg);
			
		}
		else{
			for(ForwardTableEntry entry: forwardTable){
				if((entry.getDestination() == ((PayloadMessage)msg).getPayloadDestinationAdress()) 
						&& (entry.getMetric() < Integer.MAX_VALUE)){
					
					msg.setDestinationID(entry.getNextHop());
					
					this.outputBuffer.add(msg);
					this.numberTransmittedPayloadMsg++;
					
					return;
				}
			}
			//No Route for destination available
			msgWaintingBuffer.add((PayloadMessage) msg);
		}	
	}

	public long getNumberTransmittedUpdateMsg() {
		return numberTransmittedUpdateMsg;
	}

}
