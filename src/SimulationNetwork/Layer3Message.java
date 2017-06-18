package SimulationNetwork;

public class Layer3Message extends Message implements Cloneable{	
	
	public static final int MESSAGE_SIZE = 3 * 32;	//DestinationID, SenderID, TTL
	

	protected int senderID;
	protected int destinationID;
	protected long remainingTransmissionTime;	//Remaining time until message has been transferred in ns
	protected int dataVolume;					//Data volume in bit
	protected int transmittedDataVolume;
	protected long startTransmissionTime;		//Point in time when message was started at the first network node
	protected long endTransmissionTime;			//Point in time when message reached the destination network node
	protected long startTimeOfCurrentState;			//Elapsed time in current message state
	protected int timeToLive;
	protected boolean MsgIsCompletelyTransferred;
	
	protected MessageState msgState;

	public Layer3Message(){
		msgState = MessageState.TRANSMIT_START_SIGNAL;
		startTimeOfCurrentState = 0;
	}
	
	public Layer3Message(int senderID, int destinationID, long remainingTransmissionTime, int dataVolume){
		this();
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

	public Layer3Message clone(){
		
		return new Layer3Message(senderID, destinationID, remainingTransmissionTime, dataVolume);
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

	public MessageState getMsgState() {
		return msgState;
	}

	public void setMsgState(MessageState msgState) {
		this.msgState = msgState;
	}

	public long getStartTimeOfCurrentState() {
		return startTimeOfCurrentState;
	}

	public void setStartTimeOfCurrentState(long timeInCurrentState) {
		this.startTimeOfCurrentState = timeInCurrentState;
	}

	public boolean isMsgIsCompletelyTransferred() {
		return MsgIsCompletelyTransferred;
	}

	public void setMsgIsCompletelyTransferred(boolean msgIsCompletelyTransferred) {
		MsgIsCompletelyTransferred = msgIsCompletelyTransferred;
	}

	public int getTransmittedDataVolume() {
		return transmittedDataVolume;
	}

	public void addTransmittedDataVolume(int transmittedDataVolume) {
		this.transmittedDataVolume += transmittedDataVolume;
	}
}
