package SimulationNetwork;

import java.util.LinkedList;

import AODV.AodvNetworkNode;

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
	
	protected long idleTime;
	protected long reciveTime;
	protected long transmissionTime;
	protected long waitingTimeForMediumAccesPermission;
	
	protected Simulator simulator;
	
	public NetworkNode(int id){
		this.id = id;
		connectedNodes = new LinkedList<NetworkNode>();
		incommingMsg = null;
		outgoingMsg = null;
		nodeAlive = true;
		inputBuffer = new LinkedList<Message>();
		outputBuffer = new LinkedList<Message>();
		//availableEnery = 4600L*3600L*1000L*1000L;	
		availableEnery = RECIVE_MODE_POWER_CONSUMPTION*1000L*60L*60L*24L;
		idleTime = 0L;
		reciveTime = 0L;
		transmissionTime = 0L;
		waitingTimeForMediumAccesPermission = 0L;
	}
	
	/**
	 * Runs this NetworkNode for 1 ms
	 */
	public void performAction(){
		if(nodeAlive){
			
			performeTimeDependentTasks();
			
			if(incommingMsg != null){
				//Currently resiving a message
				if(incommingMsg.getRemainingTransmissionTime() <= 0){
					//Transmission complete
					inputBuffer.add(incommingMsg);
					incommingMsg = null;
					processRecivedMessage();
					availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
					idleTime++;
					elapsedTimeSinceLastReception = 0;
				}
				else{
					//Transmission is still in progress
					if(incommingMsg.getRemainingTransmissionTime() < 1000000L){
						//Transmission takes less than 1 ms
						// TODO: Calculate exact power consumption
						availableEnery -= RECIVE_MODE_POWER_CONSUMPTION;
						reciveTime++;
					}
					else{
						//Transmission still takes at least 1 ms
						availableEnery -= RECIVE_MODE_POWER_CONSUMPTION;
						reciveTime++;
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
						idleTime++;
						elapsedTimeSinceLastReception = 0;
					}
					else{
						//Transmission is still in progress
						
						if(outgoingMsg.getRemainingTransmissionTime() < 1000000L){
							//Transmission takes less than 1 ms
							// TODO: Calculate exact power consumption
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION;
							transmissionTime++;
						}
						else{
							//Transmission still takes at least 1 ms
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION;
							transmissionTime++;
						}
						outgoingMsg.decreaseRemainingTransmissionTime(1000000L);
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
								transmissionTime++;
							}
						}
						else{
							availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
							idleTime++;
							elapsedTimeSinceLastReception++;
							waitingTimeForMediumAccesPermission++;
						}
					}else{						
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION;
						idleTime++;
						elapsedTimeSinceLastReception++;
					}
					
				}
			}
		}
		
		if(availableEnery <= 0){
			nodeAlive = false;
		}
	}
	
	protected abstract void performeTimeDependentTasks();

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
			//System.out.println("Collision detected at Node " + this.id);
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


	public abstract int getNumberOfRecivedPayloadMessages();

	public void setSimulator(Simulator simulator){
		this.simulator = simulator;
	}

	public long getIdleTime() {
		return idleTime;
	}

	public long getReciveTime() {
		return reciveTime;
	}

	public long getTransmissionTime() {
		return transmissionTime;
	}

	public long getWaitingTimeForMediumAccesPermission() {
		return waitingTimeForMediumAccesPermission;
	}
	
	public void generateRandomTransmissionLoad(double sendProbability, int networkSize){
		if(outputBuffer.size() == 0){
			double random = Math.random();
			if(random <= sendProbability){
				
				//find random destination
				int randomDestination = (int)(Math.random()*networkSize);
				
				char dataToSend[] = {'M', 's', 'g'}; 
				
				PayloadMessage tmpMsg = new PayloadMessage(id , randomDestination, dataToSend);
				this.sendMessage(tmpMsg);
			}
		}
	}

	protected abstract void sendMessage(PayloadMessage tmpMsg);
}
