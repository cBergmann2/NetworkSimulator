package Simulator;

import java.util.LinkedList;


public abstract class NetworkNode {

	protected final static int TRANSMISSION_MODE_POWER_CONSUMPTION = 16067;	//nA
	protected final static int RECIVE_MODE_POWER_CONSUMPTION = 5400; //nA
	protected final static int IDLE_MODE_POWER_CONSUMPTION = 5400; //nA

	public static final long NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE = RECIVE_MODE_POWER_CONSUMPTION * 1000L * 60L
			* 60L * 24L;

	public static final long NODE_BATTERY_ENERGY_FOR_ONE_HOUR_IN_IDLE_MODE = RECIVE_MODE_POWER_CONSUMPTION * 1000L * 60L
			* 60L;

	
	protected int id;
	protected LinkedList<NetworkNode> connectedNodes;
	protected LinkedList<IR_Receiver> destinationIrReceiver;
	protected long elapsedTimeSinceLastReception; // Time in ms
	protected Message incommingMsg;
	protected Message outgoingMsg;
	protected boolean nodeAlive;
	protected LinkedList<Message> inputBuffer;
	protected LinkedList<Message> outputBuffer;
	
	//Envergy Variables
	protected long availableEnery; // Available energy in nAs
	protected long startEnergy;
	protected long consumedEnergyInTransmissionMode;
	protected long consumedEnergyInReciveMode;
	protected long consumedEnergyInIdleMode;

	protected long idleTime;
	protected long reciveTime;
	protected long transmissionTime;
	protected long waitingTimeForMediumAccesPermission;

	protected NetworkGraph graph;
	protected Simulator simulator;

	protected PayloadMessage lastRecivedPayloadMessage;
	protected long elapsedTimeSinceLastGenerationOfTransmission;	// in milli Sekunden
	protected int numberRecivedPayloadMsg;
	protected int numberTransmittedPayloadMsg;
	
	protected long csmaWaitingTime;
	
	protected int destinationNode;
	
	protected boolean batteryPowered;
	
	protected IR_Receiver irReceiver[];

	public NetworkNode(int id) {
		this.id = id;
		connectedNodes = new LinkedList<NetworkNode>();
		destinationIrReceiver = new LinkedList<IR_Receiver>();
		incommingMsg = null;
		outgoingMsg = null;
		nodeAlive = true;
		inputBuffer = new LinkedList<Message>();
		outputBuffer = new LinkedList<Message>();
		// availableEnery = 4600L*3600L*1000L*1000L;
		availableEnery = 5 * NODE_BATTERY_ENERGY_FOR_ONE_HOUR_IN_IDLE_MODE;
		startEnergy = availableEnery;
		idleTime = 0L;
		reciveTime = 0L;
		transmissionTime = 0L;
		waitingTimeForMediumAccesPermission = 0L;
		consumedEnergyInIdleMode = 0L;
		consumedEnergyInReciveMode = 0L;
		consumedEnergyInTransmissionMode = 0L;
		elapsedTimeSinceLastGenerationOfTransmission = 0L;
		numberRecivedPayloadMsg = 0;
		numberTransmittedPayloadMsg = 0;
		
		//csmaWaitingTime = 10 + this.id * 50 ;
		csmaWaitingTime = 10;
		
		destinationNode = this.id;
		
		batteryPowered = true;
		
		irReceiver = new IR_Receiver[8];
		for(int i=0; i<irReceiver.length; i++){
			irReceiver[i] = new IR_Receiver(i);
		}
	}

	/**
	 * Perform network node for executionTime
	 * 
	 * @param executionTime
	 *            Time the network node has to be executed
	 *            
	 * @return Return true, if the simulation was possible with the given parameter. Otherwise false.
	 */
	public boolean performAction(long executionTime) {
		int numberOfIncommingMsg;

		if((executionTime < 1) || (executionTime > 2)){
			//It is only possible to use 1 or 2 ms steps
			return false;
		}
		if (nodeAlive) {

			numberOfIncommingMsg = 0;
			for(IR_Receiver receiver: irReceiver){
				if(receiver.isRecevingAMessage()){
					numberOfIncommingMsg++;
				}
			}
			
			performeTimeDependentTasks(executionTime);
			

			if (numberOfIncommingMsg > 0) {
				// Currently resiving at least one message
				
				for(IR_Receiver receiver: irReceiver){
					if(receiver.isMessageReceived()){
						this.inputBuffer.add(receiver.getReceivedMessage());
					}
					else{
						receiver.performeReceivingProcess(executionTime);
					}
				}
				
				if(this.inputBuffer.size() > 0){
					processRecivedMessage();
				}
				
				elapsedTimeSinceLastReception = 0;
				availableEnery -= RECIVE_MODE_POWER_CONSUMPTION * executionTime;
				consumedEnergyInReciveMode += RECIVE_MODE_POWER_CONSUMPTION * executionTime;
				reciveTime += executionTime;
				
				/*
				if (incommingMsg.getRemainingTransmissionTime() <= 0) {
					// Transmission complete
					inputBuffer.add(incommingMsg);
					incommingMsg = null;
					// currentlyTransmittingAMessage = false;
					processRecivedMessage();
					availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
					consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
					reciveTime += executionTime;
					elapsedTimeSinceLastReception = 0;
				} else {
					// Transmission is still in progress
					if (incommingMsg.getRemainingTransmissionTime() < (1000000L * executionTime)) {
						// Transmission takes less than 1 ms
						// TODO: Calculate exact power consumption
						availableEnery -= RECIVE_MODE_POWER_CONSUMPTION * executionTime;
						consumedEnergyInReciveMode += RECIVE_MODE_POWER_CONSUMPTION * executionTime;
						reciveTime += executionTime;
					} else {
						// Transmission still takes at least 1 ms
						availableEnery -= RECIVE_MODE_POWER_CONSUMPTION * executionTime;
						consumedEnergyInReciveMode += RECIVE_MODE_POWER_CONSUMPTION * executionTime;
						reciveTime += executionTime;
					}
					incommingMsg.decreaseRemainingTransmissionTime(1000000L * executionTime);
					elapsedTimeSinceLastReception = 0;
				}
				*/
			} else {
				// No reciving process at this time
				if (outgoingMsg != null) {
					// Currently transmitting a message
					if (outgoingMsg.remainingTransmissionTime <= 0) {
						// Transmission complete
						//System.out.println(simulator.getNetworkLifetime() + " Knoten " + this.id + ": Send process complete, transmsmission energy: " + this.consumedEnergyInTransmissionMode);
						outgoingMsg = null;
						// TODO: shedule next action
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
						consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
						transmissionTime += executionTime;
						elapsedTimeSinceLastReception = 0;
					} else {
						// Transmission is still in progress

						if (outgoingMsg.getRemainingTransmissionTime() < (1000000L * executionTime)) {
							// Transmission takes less than one execution unit
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION * (outgoingMsg.getRemainingTransmissionTime()*1.0/(1000000L *executionTime));
							availableEnery -= IDLE_MODE_POWER_CONSUMPTION * ((1000000L *executionTime - outgoingMsg.getRemainingTransmissionTime())*1.0/(1000000L *executionTime));
							consumedEnergyInTransmissionMode += TRANSMISSION_MODE_POWER_CONSUMPTION * (outgoingMsg.getRemainingTransmissionTime()*1.0/(1000000L *executionTime));
							consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * ((1000000L *executionTime - outgoingMsg.getRemainingTransmissionTime())*1.0/(1000000L *executionTime));
							transmissionTime += executionTime;
						} else {
							// Transmission still takes at least one node execution unit
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
							consumedEnergyInTransmissionMode += TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
							transmissionTime += executionTime;
						}
						outgoingMsg.decreaseRemainingTransmissionTime(1000000L * executionTime);
					}
				} else {
					// Currently not transmitting a message
					if (outputBuffer.size() != 0) {
						if (isMediumAccessAllowed()) {
							// Start message transmission
							outgoingMsg = outputBuffer.removeFirst();
							if((outgoingMsg instanceof PayloadMessage) && ((PayloadMessage)outgoingMsg).getPayloadSourceAdress() == this.id){
								//outgoingMsg.setStartTransmissionTime(simulator.getNetworkLifetime());								
							}
							outgoingMsg.setRemainingTransmissionTime(Message.calculateTransmissionTime(outgoingMsg.getDataVolume()));
							//System.out.println(simulator.getNetworkLifetime() + ": Node " + id + " start sending message to destination " + outgoingMsg.getDestinationID());
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
							consumedEnergyInTransmissionMode += TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
							transmissionTime += executionTime;

							
							/*
							for (NetworkNode node : connectedNodes) {
								node.reciveMsg(outgoingMsg.clone());
							}*/
							
							for(IR_Receiver irReceiver:destinationIrReceiver){
								irReceiver.receiveMessage(outgoingMsg.clone());
							}
							
							outgoingMsg.decreaseRemainingTransmissionTime(1000000L * executionTime);
							//System.out.println(simulator.getNetworkLifetime() + " Knoten " + this.id +": start send process, data volume: " + outgoingMsg.getDataVolume() + ", remaining transmission time: " + outgoingMsg.getRemainingTransmissionTime());
						} else {
							availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
							consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
							//idleTime += executionTime;
							elapsedTimeSinceLastReception += executionTime;
							waitingTimeForMediumAccesPermission += executionTime;
						}
					} else {
						//Node in idle mode
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
						consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
						idleTime += executionTime;
						elapsedTimeSinceLastReception += executionTime;
						// currentlyTransmittingAMessage = false;
					}

				}
			}
		}
		if(batteryPowered){
			if (availableEnery <= 0) {
				nodeAlive = false;
				batteryPowered = false;
				System.out.println(simulator.getNetworkLifetime() + ": Node " + this.id + " is down");
			}
		}
		return true;
	}

	protected abstract void performeTimeDependentTasks(long executionTime);

	public abstract void processRecivedMessage();

	/**
	 * Implements the Medium Access Control (MAC)
	 * 
	 * @return
	 */
	private boolean isMediumAccessAllowed() {
		if (elapsedTimeSinceLastReception > csmaWaitingTime) {
			return true;
		}
		return false;
	}

	/*
	public void reciveMsg(Message msg) {
		if (incommingMsg == null) {
			incommingMsg = msg;
		} else {
			// collison
			// System.out.println("Collision detected at Node " + this.id);
			graph.addCollision();
		}
	}
	*/

	public void sendMsg(Message msg){
		this.outputBuffer.add(msg);
		
	}

	public void addNeighbor(NetworkNode neighbor) {
		connectedNodes.add(neighbor);
	}
	
	public void addDestinationIrReceiver(IR_Receiver irReceiver){
		this.destinationIrReceiver.add(irReceiver);
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

	public int getNumberOfRecivedPayloadMessages(){
		return this.numberRecivedPayloadMsg;
	}

	public void setSimulator(Simulator simulator) {
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

	public void generateRandomTransmissionLoad(double sendProbability, int networkSize) {
		if (outputBuffer.size() == 0) {
			double random = Math.random();
			if (random <= sendProbability) {

				// find random destination
				int randomDestination = (int) (Math.random() * networkSize);

				
				// set message payload as simulation time 
				long networkLivetime = simulator.getNetworkLifetime();
				char dataToSend[] = { (char)((networkLivetime >> 8) & 0xFF), (char)((networkLivetime >> 16) & 0xFF), (char)((networkLivetime >> 24) & 0xFF), (char)((networkLivetime >> 32) & 0xFF), (char)((networkLivetime >> 40) & 0xFF), (char)((networkLivetime >> 48) & 0xFF), (char)((networkLivetime >> 56) & 0xFF), (char)((networkLivetime >> 64) & 0xFF) };
				
				PayloadMessage tmpMsg = new PayloadMessage(id, randomDestination, dataToSend);
				tmpMsg.setPayloadHash(networkLivetime);

				this.startSendingProcess(tmpMsg);
			}
		}
	}
	
	/**
	 * Generates a transmission every t seconds
	 * @param seconds
	 * @param nodeExecutionTime
	 * @param networkSize
	 */
	public void generateTransmissionEveryTSeconds(int seconds, int nodeExecutionTime, int networkSize, int payloadSize){
		if(this.elapsedTimeSinceLastGenerationOfTransmission/1000 >= seconds){
			this.elapsedTimeSinceLastGenerationOfTransmission = 0L; //Reset timer

			// find random destination
			//int randomDestination = (int) (Math.random() * networkSize);

			//send message to inverse node
			//int destination = graph.getNetworkNodes().length - this.id -1;
			
			// set message payload as simulation time 
			if(destinationNode != this.id){
			
				long networkLivetime = simulator.getNetworkLifetime();
				char dataToSend[] = { (char)((networkLivetime >> 8) & 0xFF), (char)((networkLivetime >> 16) & 0xFF), (char)((networkLivetime >> 24) & 0xFF), (char)((networkLivetime >> 32) & 0xFF), (char)((networkLivetime >> 40) & 0xFF), (char)((networkLivetime >> 48) & 0xFF), (char)((networkLivetime >> 56) & 0xFF), (char)((networkLivetime >> 64) & 0xFF) };
				
				PayloadMessage tmpMsg = new PayloadMessage(id, destinationNode, dataToSend);
				tmpMsg.setPayloadHash(networkLivetime);
				tmpMsg.setPayloadSize(payloadSize);
				
				this.startSendingProcess(tmpMsg);
			}
			
		}
		
		this.elapsedTimeSinceLastGenerationOfTransmission += nodeExecutionTime;
	}
	
	public void setDestinationNode(int nodeID){
		this.destinationNode = nodeID;
	}

	public abstract void startSendingProcess(PayloadMessage tmpMsg);

	public PayloadMessage getLastRecivedPayloadMessage() {
		return lastRecivedPayloadMessage;
	}

	public void setGraph(NetworkGraph graph) {
		this.graph = graph;
	}

	public void setAvailableEnergy(long availableEnergy){
		this.availableEnery = availableEnergy;
	}
	
	public long getAvailableEnery() {
		return availableEnery;
	}

	public int getOutputBufferSize() {
		return outputBuffer.size();
	}

	public Message getOutgoingMessage() {
		if (outgoingMsg != null) {
			return outgoingMsg.clone();
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Message getIncomingMessage() {
		if (incommingMsg != null) {
			return incommingMsg.clone();
		}
		return null;
	}

	public long getConsumedEnergyInTransmissionMode() {
		return consumedEnergyInTransmissionMode;
	}

	public long getConsumedEnergyInReciveMode() {
		return consumedEnergyInReciveMode;
	}

	public long getConsumedEnergyInIdleMode() {
		return consumedEnergyInIdleMode;
	}
	
	public LinkedList<NetworkNode> getConnectedNodes(){
		return this.connectedNodes;
	}
	
	/**
	 * Reset the transmission unit
	 * All incoming and outgoing messages will be deleted
	 */
	public void resetTransmissionUnit(){
		this.outgoingMsg = null;
		this.outputBuffer.clear();
		this.incommingMsg = null;
	}

	public void resetBattery() {
		this.availableEnery = NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE;
		consumedEnergyInIdleMode = 0L;
		consumedEnergyInReciveMode = 0L;
		consumedEnergyInTransmissionMode = 0L;
	}
	

	public int getNumberTransmittedPayloadMsg() {
		return numberTransmittedPayloadMsg;
	}

	public boolean isBatteryPowered() {
		return batteryPowered;
	}

	public void setBatteryPowered(boolean batteryPowered) {
		this.batteryPowered = batteryPowered;
	}
	
	public IR_Receiver getIrReceiver(int id){
		return this.irReceiver[id];
	}

	public LinkedList<Message> getInputBuffer() {
		return inputBuffer;
	}

	/**
	 * @return the irReceiver
	 */
	public IR_Receiver[] getIrReceiver() {
		return irReceiver;
	}
}
