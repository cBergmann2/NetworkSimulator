package SimulationNetwork;

public class IR_Receiver {
	
	public static final int RECEIVER_NORTH = 0;
	public static final int RECEIVER_NORTH_EAST = 1;
	public static final int RECEIVER_EAST = 2;
	public static final int RECEIVER_SOUTH_EAST = 3;
	public static final int RECEIVER_SOUTH = 4;
	public static final int RECEIVER_SOUTH_WEST = 5;
	public static final int RECEIVER_WEST = 6;
	public static final int RECEIVER_NORTH_WEST = 7;
	
	
	
	private int id;
	private Message incommingMessage;
	private boolean messageReceived;
	
	public IR_Receiver(int id){
		this.id = id;
		this.messageReceived = false;
		this.incommingMessage = null;
	}
	
	public boolean isRecevingAMessage(){
		if(incommingMessage != null){
			return true;
		}
		else{
			return false;
		}
	}
	
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
	
	public Message getReceivedMessage(){
		Message tmpMsg = this.incommingMessage;
		this.messageReceived = false;
		this.incommingMessage = null;
		return tmpMsg;
		
	}
	
	public boolean isMessageReceived(){
		return this.messageReceived;
	}
	
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
