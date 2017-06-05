package SimulationNetwork;

import java.util.LinkedList;

import AODV.AodvNetworkNode;

public abstract class NetworkNode {

	protected final static int TRANSMISSION_MODE_POWER_CONSUMPTION = 74000;
	protected final static int RECIVE_MODE_POWER_CONSUMPTION = 5400;
	protected final static int IDLE_MODE_POWER_CONSUMPTION = 5400;

	public static final long NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE = RECIVE_MODE_POWER_CONSUMPTION * 1000L * 60L
			* 60L * 24L;

	protected int id;
	protected LinkedList<NetworkNode> connectedNodes;
	protected long elapsedTimeSinceLastReception; // Time in ms
	protected Message incommingMsg;
	protected Message outgoingMsg;
	protected boolean nodeAlive;
	protected LinkedList<Message> inputBuffer;
	protected LinkedList<Message> outputBuffer;
	
	//Envergy Variables
	protected long availableEnery; // Available energy in nAs
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
	private long elapsedTimeSinceLastGenerationOfTransmission;	// in milli Sekunden
	protected int numberRecivedPayloadMsg;

	public NetworkNode(int id) {
		this.id = id;
		connectedNodes = new LinkedList<NetworkNode>();
		incommingMsg = null;
		outgoingMsg = null;
		nodeAlive = true;
		inputBuffer = new LinkedList<Message>();
		outputBuffer = new LinkedList<Message>();
		// availableEnery = 4600L*3600L*1000L*1000L;
		availableEnery = NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE;
		idleTime = 0L;
		reciveTime = 0L;
		transmissionTime = 0L;
		waitingTimeForMediumAccesPermission = 0L;
		consumedEnergyInIdleMode = 0L;
		consumedEnergyInReciveMode = 0L;
		consumedEnergyInTransmissionMode = 0L;
		elapsedTimeSinceLastGenerationOfTransmission = 0L;
		numberRecivedPayloadMsg = 0;
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
		if((executionTime < 1) || (executionTime > 2)){
			//It is only possible to use 1 or 2 ms steps
			return false;
		}
		if (nodeAlive) {

			performeTimeDependentTasks();

			if (incommingMsg != null) {
				// Currently resiving a message
				if (incommingMsg.getRemainingTransmissionTime() <= 0) {
					// Transmission complete
					inputBuffer.add(incommingMsg);
					incommingMsg = null;
					// currentlyTransmittingAMessage = false;
					processRecivedMessage();
					availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
					consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
					idleTime += executionTime;
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
			} else {
				// No reciving process at this time
				if (outgoingMsg != null) {
					// Currently transmitting a message
					if (outgoingMsg.remainingTransmissionTime <= 0) {
						// Transmission complete
						outgoingMsg = null;
						// TODO: shedule next action
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
						consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
						idleTime += executionTime;
						elapsedTimeSinceLastReception = 0;
					} else {
						// Transmission is still in progress

						if (outgoingMsg.getRemainingTransmissionTime() < (1000000L * executionTime)) {
							// Transmission takes less than 1 ms
							// TODO: Calculate exact power consumption
							availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
							consumedEnergyInTransmissionMode += TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
							transmissionTime += executionTime;
						} else {
							// Transmission still takes at least 1 ms
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
							outgoingMsg.setRemainingTransmissionTime(Message.calculateTransmissionTime(outgoingMsg.getDataVolume()));
							// System.out.println("Node " + id + "start sending
							// message.");
							for (NetworkNode node : connectedNodes) {
								node.reciveMsg(outgoingMsg.clone());
								availableEnery -= TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
								consumedEnergyInTransmissionMode += TRANSMISSION_MODE_POWER_CONSUMPTION * executionTime;
								transmissionTime += executionTime;
							}
						} else {
							availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
							consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
							idleTime += executionTime;
							elapsedTimeSinceLastReception += executionTime;
							waitingTimeForMediumAccesPermission += executionTime;
						}
					} else {
						availableEnery -= IDLE_MODE_POWER_CONSUMPTION * executionTime;
						consumedEnergyInIdleMode += IDLE_MODE_POWER_CONSUMPTION * executionTime;
						idleTime += executionTime;
						elapsedTimeSinceLastReception += executionTime;
						// currentlyTransmittingAMessage = false;
					}

				}
			}
		}

		if (availableEnery <= 0) {
			nodeAlive = false;
		}
		
		return true;
	}

	protected abstract void performeTimeDependentTasks();

	public abstract void processRecivedMessage();

	/**
	 * Implements the Medium Access Control (MAC)
	 * 
	 * @return
	 */
	private boolean isMediumAccessAllowed() {
		if (elapsedTimeSinceLastReception > (10 + id * 50)) {
			return true;
		}
		return false;
	}

	public void reciveMsg(Message msg) {
		if (incommingMsg == null) {
			incommingMsg = msg;
		} else {
			// collison
			// System.out.println("Collision detected at Node " + this.id);
			graph.addCollision();
		}
	}

	public void sendMsg(Message msg){
		this.outputBuffer.add(msg);
	}

	public void addNeighbor(NetworkNode neighbor) {
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
			int randomDestination = (int) (Math.random() * networkSize);

			// set message payload as simulation time 
			long networkLivetime = simulator.getNetworkLifetime();
			char dataToSend[] = { (char)((networkLivetime >> 8) & 0xFF), (char)((networkLivetime >> 16) & 0xFF), (char)((networkLivetime >> 24) & 0xFF), (char)((networkLivetime >> 32) & 0xFF), (char)((networkLivetime >> 40) & 0xFF), (char)((networkLivetime >> 48) & 0xFF), (char)((networkLivetime >> 56) & 0xFF), (char)((networkLivetime >> 64) & 0xFF) };
			
			PayloadMessage tmpMsg = new PayloadMessage(id, randomDestination, dataToSend);
			tmpMsg.setPayloadHash(networkLivetime);
			tmpMsg.setPayloadSize(payloadSize);
			
			this.startSendingProcess(tmpMsg);
			
		}
		
		this.elapsedTimeSinceLastGenerationOfTransmission += nodeExecutionTime;
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
}
