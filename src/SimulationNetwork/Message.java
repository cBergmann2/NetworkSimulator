package SimulationNetwork;

public class Message implements Cloneable{
	
	public static final long TRANSMISSION_TIME_PER_BIT 		= 4475520L; 			//Transmission time per bit in ns
	public static final long TRANSMISSION_TIME_START_SIGNAL = 8951040L; 	//Transmission time for start signal in ns
	public static final long TRANSMISSION_TIME_STOP_SIGNAL 	= 11934720L; 	//Transmission time for stop signal in ns
	
	public static final int MESSAGE_SIZE = 4 * 32;
	
	
	protected int senderID;
	protected int destinationID;
	protected long remainingTransmissionTime;	//Remaining time until message has been transferred in ns
	protected int dataVolume;					//Data volume in bit
	protected long startTransmissionTime;
	protected long endTransmissionTime;
	protected int timeToLive;

	public Message(){
		
	}
	
	public Message(int senderID, int destinationID, long remainingTransmissionTime, int dataVolume){
		this.senderID = senderID;
		this.destinationID = destinationID;
		this.remainingTransmissionTime = remainingTransmissionTime;
		this.dataVolume = dataVolume;
	}
	
	/**
	 * Calculates transmission time for a given message size
	 * @param sizeOfMessage	Size of message in bit
	 * @return transmission Time for message in ns
	 */
	public static long calculateTransmissionTime(long sizeOfMessage) {
		long transmissionTime = TRANSMISSION_TIME_START_SIGNAL + sizeOfMessage*TRANSMISSION_TIME_PER_BIT + TRANSMISSION_TIME_STOP_SIGNAL;
		return transmissionTime;
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

	public long getStartTransmissionTime() {
		return startTransmissionTime;
	}

	public void setStartTransmissionTime(long startTransmissionTime) {
		this.startTransmissionTime = startTransmissionTime;
	}

	public long getEndTransmissionTime() {
		return endTransmissionTime;
	}

	public void setEndTransmissionTime(long endTransmissionTime) {
		this.endTransmissionTime = endTransmissionTime;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
}
