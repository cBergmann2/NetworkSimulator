package Flooding;

import java.util.LinkedList;
import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class FloodingNetworkNode extends NetworkNode{

	LinkedList<PayloadMessage> messagesToBeSent;
	LinkedList<PayloadMessage> recivedMessages;
	int numberRecivedPayloadMsg;
	
	public FloodingNetworkNode(int id) {
		super(id);
		messagesToBeSent = new LinkedList<PayloadMessage>();
		recivedMessages = new LinkedList<PayloadMessage>();
		
		numberRecivedPayloadMsg = 0;
	}

	@Override
	protected void performeTimeDependentTasks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processRecivedMessage() {
		Message recivedMsg = inputBuffer.removeFirst();
		if(recivedMsg instanceof PayloadMessage){
			
			PayloadMessage msg = new PayloadMessage(((PayloadMessage) recivedMsg).getPayloadSourceAdress(), ((PayloadMessage) recivedMsg).getPayloadDestinationAdress(), ((PayloadMessage) recivedMsg).getPayload());
			
			if(!this.recivedMessages.contains(msg)){
				this.recivedMessages.add( msg);
				if(msg.getPayloadDestinationAdress() == this.id){
					this.numberRecivedPayloadMsg++;
					this.lastRecivedPayloadMessage = (PayloadMessage) recivedMsg;
					recivedMsg.setEndTransmissionTime(simulator.getNetworkLifetime());
				}else{
					this.startSendingProcess((PayloadMessage) msg);
				}
			}
		}
	}

	@Override
	public int getNumberOfRecivedPayloadMessages() {
		return this.numberRecivedPayloadMsg;
	}
	
	public void sendMessage(PayloadMessage msg){
		PayloadMessage msgCopy = msg.clone();
		msgCopy.setSenderID(this.id);
		msgCopy.setDestinationID(-1);
		
		this.outputBuffer.add(msgCopy);
		
	}

	@Override
	protected void startSendingProcess(PayloadMessage msg) {
		msg.setStartTransmissionTime(simulator.getNetworkLifetime());
		this.sendMessage(msg);
	}
	
	@Override
	public void reciveMsg(Message msg){
		if(incommingMsg == null){
			incommingMsg = msg;
		}
		else{
			//collison
			System.out.println("Collision detected at Node " + this.id);
			if(msg instanceof PayloadMessage){
				
			}
		}
	}

}
