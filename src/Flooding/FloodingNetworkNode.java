package Flooding;

import java.util.LinkedList;
import SimulationNetwork.Layer3Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

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

	@Override
	protected void performeTimeDependentTasks(long executionTime) {
		long currentTime = simulator.getNetworkLifetime();
		LinkedList<PayloadMessage> msgToDelete = new LinkedList<PayloadMessage>();
		for(PayloadMessage msg: recivedMessages){
			if(msg.getStartTransmissionTime()-currentTime > 3600000){
				msgToDelete.add(msg);			}
		}
		
		for(PayloadMessage msg: msgToDelete){
			recivedMessages.remove(msg);
		}
		
	}

	@Override
	public void processRecivedMessage() {
		Layer3Message recivedMsg = inputBuffer.removeFirst();
		if(recivedMsg instanceof PayloadMessageWithRoute){
			//System.out.println("Node " + this.id + ": recive msg from node " + recivedMsg.getSenderID() + ", transmission time: " + (simulator.getNetworkLifetime() - recivedMsg.getStartTransmissionTime()));	
			
			PayloadMessageWithRoute msg = new PayloadMessageWithRoute(((PayloadMessage) recivedMsg).getPayloadSourceAdress(), ((PayloadMessage) recivedMsg).getPayloadDestinationAdress(), null);
			msg.setPayloadHash(((PayloadMessage)recivedMsg).getPayloadHash());
			
			
			if(!doMessageAlreadyExists(msg)){
				this.recivedMessages.add(msg);
				if(msg.getPayloadDestinationAdress() == this.id){
					
					this.numberRecivedPayloadMsg++;
					this.lastRecivedPayloadMessage = (PayloadMessage) recivedMsg;
					recivedMsg.setEndTransmissionTime(simulator.getNetworkLifetime());
					
					//System.out.println("Node " + this.id + ": recive payload msg");
					//System.out.print("Message path: ");
					//msg = (PayloadMessageWithRoute) recivedMsg;
					//LinkedList<Integer> msgRoute = msg.getMesageRoute();
					//for(int i=0; i<msgRoute.size(); i++){
					//	System.out.print(" " + msgRoute.get(i) + ",");
					//}
					//System.out.println();
					
				}else{
					//System.out.println("Node "+ this.id + ": forward msg");
					msg = (PayloadMessageWithRoute) recivedMsg;
					msg.addNodeToRoute(this.id);
					this.sendMessage(msg);
				}
			}
		}
	}
	
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

	@Override
	public int getNumberOfRecivedPayloadMessages() {
		return this.numberRecivedPayloadMsg;
	}
	
	public void sendMessage(PayloadMessageWithRoute msg){
		PayloadMessageWithRoute msgCopy = msg.clone();
		msgCopy.setSenderID(this.id);
		msgCopy.setDestinationID(-1);
		
		this.outputBuffer.add(msgCopy);
		
		this.numberTransmittedMsg++;
		this.numberTransmittedPayloadMsg++;
			
	}

	@Override
	public void startSendingProcess(PayloadMessage msg) {
		//System.out.println("DataVolume of msg to send: "+ msg.getDataVolume());
		
		msg.setStartTransmissionTime(simulator.getNetworkLifetime());
		
		PayloadMessageWithRoute newMsg = new PayloadMessageWithRoute(msg.getPayloadSourceAdress(), msg.getPayloadDestinationAdress(), msg.getPayload());
		newMsg.setStartTransmissionTime(this.simulator.getNetworkLifetime());
		newMsg.addNodeToRoute(this.id);
		newMsg.setPayloadSize(msg.getPayloadSize());
		newMsg.setDataVolume(msg.getPayloadSize()*8);
		newMsg.setPayloadHash(msg.getPayloadHash());
		this.sendMessage(newMsg);
	}
	
	@Override
	public void reciveMsg(Layer3Message msg){
		if(incommingMsg == null){
			incommingMsg = msg;
		}
		else{
			//collison
			//System.out.println("Collision detected at Node " + this.id);
			((FloodingNetworkGraph)graph).addCollision();
		}
	}

	public int getNumberTransmittedMsg() {
		return numberTransmittedMsg;
	}

}
