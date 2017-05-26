package Flooding;

import java.util.LinkedList;

import SimulationNetwork.PayloadMessage;

public class PayloadMessageWithRoute extends PayloadMessage{
	
	private LinkedList<Integer> route;

	public PayloadMessageWithRoute(int sourceAdress, int destinationAdress, char[] payload) {
		super(sourceAdress, destinationAdress, payload);
		route = new LinkedList<Integer>();
	}
	
	public PayloadMessageWithRoute(int senderID, int destinationID, int payloadSourceAdress,
			int payloadDestinationAdress, char[] payload) {
		super( senderID, destinationID, payloadSourceAdress, payloadDestinationAdress, payload);
		route = new LinkedList<Integer>();
	}

	public void addNodeToRoute(int nodeID){
		this.route.add(nodeID);
	}
	
	public LinkedList<Integer> getMesageRoute(){
		return this.route;
	}
	
	public PayloadMessageWithRoute clone(){
		
		PayloadMessageWithRoute copy = new PayloadMessageWithRoute(senderID, destinationID, payloadSourceAdress, payloadDestinationAdress, payload);
		copy.setStartTransmissionTime(startTransmissionTime);
		
		//duplicate payload array
		copy.payload = new char[payload.length];
		for(int i=0; i<payload.length; i++){
			copy.payload[i] = payload[i];
		}
		
		for(int i=0; i<route.size(); i++){
			copy.route.add(this.route.get(i).intValue());
		}		

		return copy;
	}

}
