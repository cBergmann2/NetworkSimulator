package Flooding;

import java.util.LinkedList;

import Simulator.Message;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;

/**
 * Specializes the NetworkNode class for flooding routing scheme
 * 
 * @author Christoph Bergmann
 */
public class FloodingNetworkNode extends NetworkNode{

	LinkedList<PayloadMessage> messagesToBeSent;
	LinkedList<PayloadMessage> recivedMessages;
	int numberRecivedPayloadMsg;
	int numberTransmittedMsg;
	
	public FloodingNetworkNode(int id) {
		super(id);
		messagesToBeSent = new LinkedList<PayloadMessage>();
		recivedMessages = new LinkedList<PayloadMessage>();
		
		numberRecivedPayloadMsg = 0;
		numberTransmittedMsg = 0;
		//currentlyTransmittingAMessage = false;
	}

	/**
	 * Performs time dependent tasks. Delete old received message
	 */
	protected void performeTimeDependentTasks(long executionTime) {
		long currentTime = simulator.getNetworkLifetime();
		
		if(currentTime % 60000 == 1){	//Perform cleaning task every minute
			
			LinkedList<PayloadMessage> msgToDelete = new LinkedList<PayloadMessage>();
			for(PayloadMessage msg: recivedMessages){
				if(msg.getStartTransmissionTime()-currentTime > 3600000){
					msgToDelete.add(msg);			}
			}
			
			for(PayloadMessage msg: msgToDelete){
				recivedMessages.remove(msg);
			}
		}
		
	}

	/**
	 * Process received message
	 */
	public void processRecivedMessage() {
		Message recivedMsg = inputBuffer.removeFirst();
		if(recivedMsg instanceof PayloadMessageWithRoute){
			//System.out.println("Node " + this.id + ": recive msg from node " + recivedMsg.getSenderID() + ", transmission time: " + (simulator.getNetworkLifetime() - recivedMsg.getStartTransmissionTime()));	
			
			PayloadMessageWithRoute msg = new PayloadMessageWithRoute(((PayloadMessage) recivedMsg).getPayloadSourceAdress(), ((PayloadMessage) recivedMsg).getPayloadDestinationAdress(), null);
			msg.setPayloadHash(((PayloadMessage)recivedMsg).getPayloadHash());
			
			this.numberRecivedPayloadMsg++;
			
			if(!doMessageAlreadyExists(msg)){
				this.recivedMessages.add(msg);
				if(msg.getPayloadDestinationAdress() == this.id){
					
					this.numberRecivedPayloadMsg++;
					this.lastRecivedPayloadMessage = (PayloadMessage) recivedMsg;
					recivedMsg.setEndTransmissionTime(simulator.getNetworkLifetime());
					/*
					System.out.print("Node " + this.id + ": recive payload msg. ");
					System.out.print("Message path: ");
					msg = (PayloadMessageWithRoute) recivedMsg;
					LinkedList<Integer> msgRoute = msg.getMesageRoute();
					for(int i=0; i<msgRoute.size(); i++){
						System.out.print(" " + msgRoute.get(i) + ",");
					}
					System.out.println();
					*/
					
				}else{
					//System.out.println("Node "+ this.id + ": forward msg");
					msg = (PayloadMessageWithRoute) recivedMsg;
					msg.addNodeToRoute(this.id);
					this.sendMessage(msg);
				}
			}
		}
	}
	
	/**
	 * Check if given message was already received
	 * @param msg message to check
	 * @return true if message was already received
	 */
	private boolean doMessageAlreadyExists(PayloadMessageWithRoute msg){
		for(PayloadMessage tmpMsg: recivedMessages){
			if((tmpMsg.getPayloadSourceAdress() == msg.getPayloadSourceAdress()) &&
					(tmpMsg.getPayloadDestinationAdress() == msg.getPayloadDestinationAdress()) &&
					(tmpMsg.getPayloadHash() == msg.getPayloadHash())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Get number of received payload messages
	 */
	public int getNumberOfRecivedPayloadMessages() {
		return this.numberRecivedPayloadMsg;
	}
	
	/**
	 * Send message
	 * @param msg
	 */
	public void sendMessage(PayloadMessageWithRoute msg){
		PayloadMessageWithRoute msgCopy = msg.clone();
		msgCopy.setSenderID(this.id);
		msgCopy.setDestinationID(-1);
		
		this.outputBuffer.add(msgCopy);
		
		this.numberTransmittedMsg++;
		//this.numberTransmittedPayloadMsg++;
			
	}

	/**
	 * Send message
	 */
	public void startSendingProcess(PayloadMessage msg) {
		//System.out.println("DataVolume of msg to send: "+ msg.getDataVolume());
		
		msg.setStartTransmissionTime(simulator.getNetworkLifetime());
		
		PayloadMessageWithRoute newMsg = new PayloadMessageWithRoute(msg.getPayloadSourceAdress(), msg.getPayloadDestinationAdress(), msg.getPayload());
		newMsg.setStartTransmissionTime(this.simulator.getNetworkLifetime());
		newMsg.addNodeToRoute(this.id);
		newMsg.setPayloadSize(msg.getPayloadSize());
		newMsg.setDataVolume(msg.getDataVolume());
		newMsg.setPayloadHash(msg.getPayloadHash());
		this.sendMessage(newMsg);
	}
	

	/**
	 * Get number of transmitted messages
	 * @return
	 */
	public int getNumberTransmittedMsg() {
		return numberTransmittedMsg;
	}

}
