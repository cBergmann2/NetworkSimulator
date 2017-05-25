package SimulationNetwork;

public class PayloadMessage extends Message{
	int payloadDestinationAdress;
	int payloadSourceAdress;
	char payload[];
		
	public PayloadMessage(int sourceAdress, int destinationAdress, char[] payload){
		this.payloadDestinationAdress = destinationAdress;
		this.payloadSourceAdress = sourceAdress;
		this.payload = payload;
	}
	
	public PayloadMessage(int senderID, int destinationID, int payloadSourceAdress, int payloadDestinationAdress, char dataToSend[]){
		super(senderID, destinationID, calculateTransmissionTime(dataToSend.length*8),dataToSend.length*8);
		this.payloadSourceAdress = payloadSourceAdress;
		this.payloadDestinationAdress = payloadDestinationAdress;
		this.payload = dataToSend;
	}

	
	public int getPayloadDestinationAdress() {
		return payloadDestinationAdress;
	}
	
	public void setPayloadDestinationAdress(int payloadDestinationAdress) {
		this.payloadDestinationAdress = payloadDestinationAdress;
	}
	
	public int getPayloadSourceAdress() {
		return payloadSourceAdress;
	}


	public void setPayloadSourceAdress(int payloadSourceAdress) {
		this.payloadSourceAdress = payloadSourceAdress;
	}


	public char[] getPayload() {
		return payload;
	}

	public void setPayload(char[] payload) {
		this.payload = payload;
	}

	public PayloadMessage clone(){
		PayloadMessage copy = new PayloadMessage(senderID, destinationID, payloadSourceAdress, payloadDestinationAdress, payload);
		copy.setStartTransmissionTime(startTransmissionTime);
		return copy;
	}
}
