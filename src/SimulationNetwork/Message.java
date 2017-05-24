package SimulationNetwork;

public class Message implements Cloneable{
	
	public static final int TRANSMISSION_TIME_PER_BIT = 4475; //Transmission time per bit in µs
	
	protected int senderID;
	protected int destinationID;
	protected long remainingTransmissionTime;	//Remaining time until message has been transferred in ns
	protected int dataVolume;					//Data volume in bit

	public Message(){
		
	}
	
	public Message(int senderID, int destinationID, long remainingTransmissionTime, int dataVolume){
		this.senderID = senderID;
		this.destinationID = destinationID;
		this.remainingTransmissionTime = remainingTransmissionTime;
		this.dataVolume = dataVolume;
	}
	
	/**
	 * 
	 * @return remaining transmission time of message in ns
	 */
	public long getRemainingTransmissionTime() {
		return remainingTransmissionTime;
	}

	/**
	 * Decrease remaining transmission time
	 * @param timeStep elapsed time in ns
	 */
	public void decreaseRemainingTransmissionTime(long timeStep) {
		this.remainingTransmissionTime -= timeStep;
	}
	
	/**
	 * Set transmission time of this message
	 * @param transmissionTime
	 */
	public void setRemainingTransmissionTime(long transmissionTime){
		this.remainingTransmissionTime = transmissionTime;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public int getDestinationID() {
		return destinationID;
	}

	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}

	public int getDataVolume() {
		return dataVolume;
	}

	public void setDataVolume(int dataVolume) {
		this.dataVolume = dataVolume;
	}

	public Message clone(){
		
		return new Message(senderID, destinationID, remainingTransmissionTime, dataVolume);
	}
}
