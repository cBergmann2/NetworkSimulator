package DSDV;

import java.util.LinkedList;

import Simulator.Message;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;

public class DsdvNetworkNode extends NetworkNode{
	
	private static final long UPDATE_INTERVAL = 9*60000;	//in ms
	//private static final long UPDATE_INTERVAL = 30000;	//in ms -> every 30 seconds
	private long lastUpdate;
	private LinkedList<ForwardTableEntry> forwardTable;
	private long numberTransmittedUpdateMsg;
	private LinkedList<PayloadMessage> msgWaintingBuffer;
	private long sequenceNumber;
	private boolean propagateNetworkStructure;
	UpdateMessage incrementalUpdateMessage;
	
	
	public DsdvNetworkNode(int id) {
		super(id);
		lastUpdate = UPDATE_INTERVAL;
		forwardTable = new LinkedList<ForwardTableEntry>();
		numberTransmittedUpdateMsg = 0L;
		msgWaintingBuffer = new LinkedList<PayloadMessage>();
		this.sequenceNumber = 0;
		this.forwardTable.add(new ForwardTableEntry(this.id,-1, 0, this.sequenceNumber, 0L));
		this.propagateNetworkStructure = true;
	}
	
	protected void performeTimeDependentTasks(long executionTime){
		if(propagateNetworkStructure){
			if(this.lastUpdate >= UPDATE_INTERVAL){
	
				this.sendForwardTableUpdate();
				this.lastUpdate = 0;
			}
			else{
				lastUpdate += executionTime;
			}
		}
		
		if(simulator.getNetworkLifetime() % 2000 == 0){
			if(this.isNeighborTurnedOff()){
				//new neighbor is turned off
				this.sendMsg(incrementalUpdateMessage);
			}
		}
		
	}
	
	private boolean isNeighborTurnedOff(){
		boolean newTurnedOffNodeDetected = false;
		
		incrementalUpdateMessage = new UpdateMessage();
		
		for(ForwardTableEntry entry: forwardTable){
			if(entry.getMetric() == 1){
				if(entry.getInstallTime() < simulator.getNetworkLifetime() - 2 * UPDATE_INTERVAL){
					entry.setMetric(Integer.MAX_VALUE);
					entry.setSequenceNumber(entry.getSequenceNumber() + 1);
					newTurnedOffNodeDetected = true;
					
					
					
					for(ForwardTableEntry destEntry: forwardTable){
						if(destEntry.getNextHop() == entry.getDestination()){
							destEntry.setMetric(Integer.MAX_VALUE);
							destEntry.setSequenceNumber(destEntry.getSequenceNumber()+1);
							destEntry.setNextHop(-1);
							
							UpdateMessageEntry updateEntry = new UpdateMessageEntry(destEntry.getDestination(), destEntry.getMetric(), destEntry.getSequenceNumber());
							incrementalUpdateMessage.addUpdate(updateEntry);
						}
					}
				}
			}
		}
		
		return newTurnedOffNodeDetected;
	}

	private void sendForwardTableUpdate() {
		UpdateMessage updateMessage = new UpdateMessage();

		//update old table entry for this node
		for(ForwardTableEntry entry: forwardTable){
			if(entry.getDestination() == this.id){
				//increment sequenceNumber of this node by 2
				entry.setSequenceNumber(entry.getSequenceNumber() +2);
				break;
			}
		}
		
		
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
				//System.out.println(simulator.getNetworkLifetime() + " - Node " + id + ": recive Payloadmsg from Node " + receivedMsg.getSenderID() + ". Source: " + ((PayloadMessage)receivedMsg).getPayloadSourceAdress() + " Sink: " + ((PayloadMessage)receivedMsg).getPayloadDestinationAdress() + " NextHop: "+ receivedMsg.getDestinationID()
				if(((PayloadMessage)receivedMsg).getPayloadDestinationAdress() == this.id){
					//System.out.println(simulator.getNetworkLifetime() + " - Node " + id + ": recive Payloadmsg from Node " + receivedMsg.getSenderID() + ". Source: " + ((PayloadMessage)receivedMsg).getPayloadSourceAdress() + " TransmissionTime: " + (simulator.getNetworkLifetime() - receivedMsg.getStartTransmissionTime()));
				}
				this.receivePayloadMessage((PayloadMessage)receivedMsg);
			}
		}
	}

	private void receivePayloadMessage(PayloadMessage msg) {
		if(msg.getPayloadDestinationAdress() == this.id){
			this.numberRecivedPayloadMsg++;
			msg.setEndTransmissionTime(simulator.getNetworkLifetime());
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
		boolean brokenLinkDetected = false;
		boolean newDestinationDetected = false;
		incrementalUpdateMessage = new UpdateMessage();
		
		
		
		
		for(UpdateMessageEntry entry: msg.getUpdates()){
			
			if(checkForNewDestination(entry)){
				UpdateMessageEntry updateMessageEntry = new UpdateMessageEntry(entry.getDestination(), entry.getMetric()+1, entry.getSequenceNumber());
				incrementalUpdateMessage.addUpdate(updateMessageEntry);
				newDestinationDetected = true;
			}
			
			if(this.updateForwardTable(entry, msg.getSenderID())){
				brokenLinkDetected = true;
				UpdateMessageEntry updateMessageEntry = new UpdateMessageEntry(entry.getDestination(), entry.getMetric(), entry.getSequenceNumber());
				incrementalUpdateMessage.addUpdate(updateMessageEntry);
			}
		}
		
		//if (this.propagateNetworkStructure){
			if(brokenLinkDetected || newDestinationDetected){
				
				this.sendMsg(incrementalUpdateMessage);
			}
		//}
	}
	
	private boolean checkForNewDestination(UpdateMessageEntry updateMsgEntry){
		for(ForwardTableEntry forwartTableEntry: this.forwardTable){
			if((forwartTableEntry.getDestination() == updateMsgEntry.getDestination()) && 
					(forwartTableEntry.getMetric() < Integer.MAX_VALUE)){
				return false;
				
			}
		}
		return true;
	}
	
	private boolean updateForwardTable(UpdateMessageEntry updateMessageEntry, int senderID){
		
		boolean brokenLinkDetected = false;
		
		for(ForwardTableEntry tableEntry: this.forwardTable){
			if(tableEntry.getDestination() == updateMessageEntry.getDestination()){
				
				if(tableEntry.getSequenceNumber() < updateMessageEntry.getSequenceNumber()){
					tableEntry.setSequenceNumber(updateMessageEntry.getSequenceNumber());
					tableEntry.setNextHop(senderID);
					tableEntry.setMetric(updateMessageEntry.getMetric() +1);
					tableEntry.setInstallTime(simulator.getNetworkLifetime());
					
					if(tableEntry.getMetric() == Integer.MAX_VALUE){
						brokenLinkDetected = true;
						UpdateMessageEntry updateEntry = new UpdateMessageEntry(tableEntry.getDestination(), tableEntry.getMetric(), tableEntry.getSequenceNumber());
						incrementalUpdateMessage.addUpdate(updateEntry);
					}
					else{
						findMessageToSend(updateMessageEntry.getDestination(), updateMessageEntry.getMetric());
					}
				}
				else{
					if((tableEntry.getSequenceNumber() == updateMessageEntry.getSequenceNumber()) && 
							(tableEntry.getMetric() > (updateMessageEntry.getMetric() +1))){
						tableEntry.setNextHop(senderID);
						tableEntry.setMetric(updateMessageEntry.getMetric() +1);
						findMessageToSend(updateMessageEntry.getDestination(), updateMessageEntry.getMetric());
					}		
				}	
				return brokenLinkDetected;
			}
		}
		
		//Forwardtable does not contailn destiantion from updateMessageEntry
		ForwardTableEntry tableEntry = new ForwardTableEntry(updateMessageEntry.getDestination(), senderID, 
				updateMessageEntry.getMetric()+1, updateMessageEntry.getSequenceNumber(), simulator.getNetworkLifetime());
		this.forwardTable.add(tableEntry);
		findMessageToSend(updateMessageEntry.getDestination(), updateMessageEntry.getMetric());
		
		return brokenLinkDetected;
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
		tmpMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
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
					
					if(((PayloadMessage)msg).getPayloadSourceAdress() == this.id){
						//System.out.println(simulator.getNetworkLifetime() + " Node: " +this.id + " send msg to node " + ((PayloadMessage)msg).getPayloadDestinationAdress() + ". Distance: " + entry.getMetric());
					}
					else{
						//System.out.println(simulator.getNetworkLifetime() + " Node: " +this.id + " forward msg to node " + msg.getDestinationID() + ". SinkNode: " + ((PayloadMessage)msg).getPayloadDestinationAdress() +  " Distance: " + entry.getMetric() + " TransmissionTime: " + (simulator.getNetworkLifetime() - msg.getStartTransmissionTime()));
					}
					
					this.outputBuffer.add(msg);
					//this.numberTransmittedPayloadMsg++;
					
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

	public LinkedList<ForwardTableEntry> getForwardTable() {
		return forwardTable;
	}

	public boolean isPropagateNetworkStructure() {
		return propagateNetworkStructure;
	}

	public void setPropagateNetworkStructure(boolean propagateNetworkStructure) {
		this.propagateNetworkStructure = propagateNetworkStructure;
	}

	public void forceTheNodeToSendPeriodicUpdateMessage() {
		this.lastUpdate = Long.MAX_VALUE;
	}

}
