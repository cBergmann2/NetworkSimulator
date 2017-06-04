package SimulationNetwork;

public class PayloadMessage extends Message{
	protected int payloadDestinationAdress;
	protected int payloadSourceAdress;
	protected char payload[];
	protected long payloadHash;
	protected int payloadSize;
		
	public PayloadMessage(int sourceAdress, int destinationAdress, char[] payload){
		this.payloadDestinationAdress = destinationAdress;
		this.payloadSourceAdress = sourceAdress;
		this.payload = payload;
		this.payloadSize = payload.length*8;
		this.dataVolume = this.payloadSize + Message.MESSAGE_SIZE;
	}
	
	public PayloadMessage(int senderID, int destinationID, int payloadSourceAdress, int payloadDestinationAdress, char dataToSend[]){
		super(senderID, destinationID, calculateTransmissionTime(dataToSend.length*8),dataToSend.length*8);
		this.payloadSourceAdress = payloadSourceAdress;
		this.payloadDestinationAdress = payloadDestinationAdress;
		this.payload = dataToSend;
		this.payloadSize = payload.length*8;
		this.dataVolume = this.payloadSize + Message.MESSAGE_SIZE;
	}
	
	public PayloadMessage(int senderID, int destinationID, int payloadSourceAdress, int payloadDestinationAdress,int payloadSize){
		super(senderID, destinationID, calculateTransmissionTime(payloadSize*8),payloadSize*8);
		this.payloadSourceAdress = payloadSourceAdress;
		this.payloadDestinationAdress = payloadDestinationAdress;
		this.payloadSize = payloadSize;
		this.dataVolume = this.payloadSize + Message.MESSAGE_SIZE;
	}

	
	public PayloadMessage() {
		// TODO Auto-generated constructor stub
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
		PayloadMessage copy = new PayloadMessage();
		copy.setDataVolume(dataVolume);
		copy.setDestinationID(destinationID);
		
		//duplicate payload array
		copy.payload = new char[payload.length];
		for(int i=0; i<payload.length; i++){
			copy.payload[i] = payload[i];
		}
		
		copy.setPayloadDestinationAdress(payloadDestinationAdress);
		copy.setPayloadHash(payloadHash);
		copy.setPayloadSize(payloadSize);
		copy.setPayloadSourceAdress(payloadSourceAdress);
		copy.setRemainingTransmissionTime(remainingTransmissionTime);
		copy.setSenderID(senderID);
		copy.setStartTransmissionTime(startTransmissionTime);
		copy.setTimeToLive(timeToLive);
		
		return copy;
	}

	public long getPayloadHash() {
		return payloadHash;
	}

	public void setPayloadHash(long payloadHash) {
		this.payloadHash = payloadHash;
	}

	public int getPayloadSize() {
		return payloadSize;
	}

	public void setPayloadSize(int payloadSize) {
		this.payloadSize = payloadSize;
		this.remainingTransmissionTime = this.calculateTransmissionTime(payloadSize*8);
	}
}
