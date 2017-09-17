package Simulator;

/**
 * Class represents one IR receiver that is used from a network node
 * @author Christoph Bergmann
 *
 */
public class IR_Receiver {
	
	public static final int RECEIVER_NORTH = 0;
	public static final int RECEIVER_NORTH_EAST = 1;
	public static final int RECEIVER_EAST = 2;
	public static final int RECEIVER_SOUTH_EAST = 3;
	public static final int RECEIVER_SOUTH = 4;
	public static final int RECEIVER_SOUTH_WEST = 5;
	public static final int RECEIVER_WEST = 6;
	public static final int RECEIVER_NORTH_WEST = 7;
	
	
	
	private int id;						//ID of the receiver
	private Message incommingMessage;	//incoming message
	private boolean messageReceived;	//is true if a message was received
	
	public IR_Receiver(int id){
		this.id = id;
		this.messageReceived = false;
		this.incommingMessage = null;
	}
	
	/**
	 * 
	 * @return true if the IR receiver is receiving a message
	 */
	public boolean isRecevingAMessage(){
		if(incommingMessage != null){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Performe the receiving process
	 * If the transmission time is elapsed, set the variable messageReceived to true
	 * @param executionTime
	 */
	public void performeReceivingProcess(long executionTime){
		if(incommingMessage != null){
			if(incommingMessage.getRemainingTransmissionTime() <= 0){
				this.messageReceived = true;
			}
			else{
				incommingMessage.decreaseRemainingTransmissionTime(1000000L * executionTime);
			}
		}
	}
	
	/**
	 * Return the received message and reset the IR receiver
	 * @return
	 */
	public Message getReceivedMessage(){
		Message tmpMsg = this.incommingMessage;
		this.messageReceived = false;
		this.incommingMessage = null;
		return tmpMsg;
		
	}
	
	/**
	 * 
	 * @return true if a message was received
	 */
	public boolean isMessageReceived(){
		return this.messageReceived;
	}
	
	/**
	 * Receive a message from a IR sender
	 * This method should be called from a IR sender to start a transmission
	 * @param msg message that should be transmitted
	 */
	public void receiveMessage(Message msg){
		
		if(this.incommingMessage == null){
			this.incommingMessage = msg;
		}
		else{
			long remeinaingTransmissionTime;
			CollisionMessage collisionMsg = new CollisionMessage();
			if(msg.getRemainingTransmissionTime() > incommingMessage.getRemainingTransmissionTime()){
				remeinaingTransmissionTime = msg.getRemainingTransmissionTime();
			}
			else{
				remeinaingTransmissionTime = incommingMessage.getRemainingTransmissionTime();
			}
			collisionMsg.setRemainingTransmissionTime(remeinaingTransmissionTime);
			this.incommingMessage = collisionMsg;
		}
	}

}
