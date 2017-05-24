package SimulationNetwork;

import java.util.LinkedList;

public abstract class NetworkNode {
	
	protected final static int TRANSMISSION_MODE_POWER_CONSUMPTION = 74000;
	protected final static int RECIVE_MODE_POWER_CONSUMPTION = 5400;
	protected final static int IDLE_MODE_POWER_CONSUMPTION = 5400;
	
	protected int id;
	protected LinkedList<NetworkNode> connectedNodes;
	protected long elapsedTimeSinceLastReception;	//Time in ms
	protected Message incommingMsg;
	protected Message outgoingMsg;
	protected boolean nodeAlive;
	protected LinkedList<Message> inputBuffer;
	protected LinkedList<Message> outputBuffer;
	protected long availableEnery;				//Available energy in nAs
	
	
	public NetworkNode(int id){
		this.id = id;
		connectedNodes = new LinkedList<NetworkNode>();
		incommingMsg = null;
		outgoingMsg = null;
		nodeAlive = true;
		inputBuffer = new LinkedList<Message>();
		outputBuffer = new LinkedList<Message>();
		availableEnery = 4600L*3600L*1000L*1000L;	
	}
	
	/**
	 * Runs this NetworkNode for 1 ms
	 */
	public void performAction(){
		if(nodeAlive){
			if(incommingMsg != null){
				//Currently resiving a message
				if(incommingMsg.getRemainingTransmissionTime() <= 0){
					//Transmission complete
					inputBuffer.add(incommingMsg);
					incommingMsg = null;
					processRecivedMessage();
					availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
					elapsedTimeSinceLastReception = 0;
				}
				else{
					//Transmission is still in progress
					if(incommingMsg.getRemainingTransmissionTime() < 1000000L){
						//Transmission takes less than 1 ms
						// TODO: Calculate exact power consumption
						availableEnery -= RECIVE_MODE_POWER_CONSUMPTION;
					}
					else{
						//Transmission still takes at least 1 ms
						availableEnery -= RECIVE_MODE_POWER_CONSUMPTION;
					}
					incommingMsg.decreaseRemainingTransmissionTime(1000000L);	
					elapsedTimeSinceLastReception = 0;
				}
			}
			else{
				//No reciving process at this time
				if(outgoingMsg != null){
					//Currently transmitting a message
					if(outgoingMsg.remainingTransmissionTime <= 0){
						//Transmission complete
						outgoingMsg = null;
						// TODO: shedule next action
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
						elapsedTimeSinceLastReception = 0;
					}
					else{
						//Transmission is still in progress
						
						if(outgoingMsg.getRemainingTransmissionTime() < 1000000L){
							//Transmission takes less than 1 ms
							// TODO: Calculate exact power consumption
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION;
						}
						else{
							//Transmission still takes at least 1 ms
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION;
						}
						outgoingMsg.decreaseRemainingTransmissionTime(1);
					}
				}
				else{
					//Currently not transmitting a message
					if(outputBuffer.size() != 0){
						if(isMediumAccessAllowed()){
							//Start message transmission
							outgoingMsg = outputBuffer.removeFirst();
							for(NetworkNode node: connectedNodes){
								node.reciveMsg(outgoingMsg.clone());
								availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION;
							}
						}
						else{
							availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
							elapsedTimeSinceLastReception++;
						}
					}else{						
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
						elapsedTimeSinceLastReception++;
					}
					
				}
			}
		}
		
		if(availableEnery <= 0){
			nodeAlive = false;
		}
	}
	
	public abstract void processRecivedMessage();

	/**
	 * Implements the Medium Access Control (MAC)
	 * @return
	 */
	private boolean isMediumAccessAllowed() {
		if(elapsedTimeSinceLastReception > (10 + id*50)){
			return true;
		}
		return false;
	}

	public void reciveMsg(Message msg){
		if(incommingMsg == null){
			incommingMsg = msg;
		}
		else{
			//collison
			System.out.println("Collision detected at Node " + this.id);
		}
	}
	
	public void sendMsg(Message msg){
		
		for(NetworkNode node: connectedNodes){
			node.reciveMsg(msg);
		}
	}

	public void addNeighbor(NetworkNode neighbor){
		connectedNodes.add(neighbor);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public boolean isNodeAlive() {
		return nodeAlive;
	}

}
