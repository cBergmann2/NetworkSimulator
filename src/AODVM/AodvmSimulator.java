package AODVM;

import Simulator.IR_Receiver;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;
import Simulator.Simulator;

/**
 * Simulator for AODVM routing scheme
 * @author Christoph
 *
 */
public class AodvmSimulator extends Simulator{
	
	private double percentageTransmittedRREQMsg;
	private double percentageTransmittedRREPMsg;
	private double percentageTransmittedPayloadMsg;
	private double routeDistance;
	
	private long msgTransmissionTime; 

	/**
	 * Performs the speed analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return this.networkLifetime;
	}
	
	/**
	 * Performs the energy cost analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return energyCosts;
	}
	
	/**
	 * Energy cost analysis without route discovery process
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysisWithoutRDP(int networkWidth, int sourceNodeId, int destinationNodeId) {
		
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);
		
		this.graph = graph;
		
		long consumedEnergy = 0L;
		boolean transmissionInNetworkDetected;
		
		consumedEnergyInIdleMode = 0L;
		consumedEnergyInReciveMode = 0L;
		consumedEnergyInTransmissionMode = 0L;

		networkLifetime = 0;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		char dataToSend[] = { 'A' };

		PayloadMessage msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;
		}while(((AodvmNetworkNode)networkNodes[sourceNodeId]).getNumberRecivedRREPdMsg() == 0);
		
		//Reset transmission units and battery of all nodes
		//this.resetTransmissionUnitFromAllNodes();
		
		this.routeDistance = ((AodvmNetworkNode)networkNodes[sourceNodeId]).getRouteTableEntry(destinationNodeId).getHopCount();
		
		do {
			
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
						
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
				if (id != destinationNodeId) {
					if (networkNodes[id].getOutgoingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					if (networkNodes[id].getIncomingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					if (networkNodes[id].getOutputBufferSize() > 0) {
						transmissionInNetworkDetected = true;
						break;
					}
				}
			}

		} while (transmissionInNetworkDetected);
		
		
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}
		
		msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);
		
		
		do{
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;
			
			//Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
				if(id != destinationNodeId){
					if(networkNodes[id].getOutgoingMessage() != null){
						transmissionInNetworkDetected = true;
						break;
					}
					if(networkNodes[id].getIncomingMessage() != null){
						transmissionInNetworkDetected = true;
						break;
					}
					if(networkNodes[id].getOutputBufferSize() > 0){
						transmissionInNetworkDetected = true;
						break;
					}
				}
			}

		} while (networkNodes[destinationNodeId].getNumberOfRecivedPayloadMessages() == 0);

		for (int id = 0; id < networkNodes.length; id++) {
			/*consumedEnergy += NetworkNode.NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE
					- networkNodes[id].getAvailableEnery();
			*/
			
			
			consumedEnergyInIdleMode += networkNodes[id].getConsumedEnergyInIdleMode();
			//System.out.println("Node " + id + ": consumedEnergy idle: " + networkNodes[id].getConsumedEnergyInIdleMode());
			consumedEnergyInReciveMode += networkNodes[id].getConsumedEnergyInReciveMode();
			consumedEnergyInTransmissionMode += networkNodes[id].getConsumedEnergyInTransmissionMode();
			//System.out.println("Node " + id + ": time in transmissionMode: " + networkNodes[id].getTransmissionTime()  + ", consumedEnergy transmission: " + networkNodes[id].getConsumedEnergyInTransmissionMode());
			
		}
		

		return (consumedEnergyInTransmissionMode);
		
	}
	
	/**
	 * Energy cost analysis only for the route discovery process
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public double energyCostAnalysisRouteDiscoveryProcess(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);

		this.graph = graph;

		boolean transmissionInNetworkDetected;

		consumedEnergyInIdleMode = 0L;
		consumedEnergyInReciveMode = 0L;
		consumedEnergyInTransmissionMode = 0L;

		networkLifetime = 0;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		char dataToSend[] = { 'A' };

		PayloadMessage msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				if((id == sourceNodeId) 
						&&(((AodvmNetworkNode)networkNodes[sourceNodeId]).getNumberRecivedRREPdMsg() > 0)){
					
				}else{
					networkNodes[id].performAction(NODE_EXECUTION_TIME);					
				}
			}

			networkLifetime += NODE_EXECUTION_TIME;

			
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
				
				if(((AodvmNetworkNode)networkNodes[sourceNodeId]).getNumberRecivedRREPdMsg() == 0){
					transmissionInNetworkDetected = true;
				}

				if(id != sourceNodeId){
				
					if (networkNodes[id].getOutgoingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					
					for(IR_Receiver irReceiver: networkNodes[id].getIrReceiver()){
						if(irReceiver.isRecevingAMessage()){
							transmissionInNetworkDetected = true;
						}
					}
					
					if (networkNodes[id].getIncomingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					
					if (networkNodes[id].getOutputBufferSize() > 0) {
						transmissionInNetworkDetected = true;
						break;
					}
				}

			}

		} while (transmissionInNetworkDetected);
		
		for (int id = 0; id < networkNodes.length; id++) {
			consumedEnergyInIdleMode += networkNodes[id].getConsumedEnergyInIdleMode();
			consumedEnergyInReciveMode += networkNodes[id].getConsumedEnergyInReciveMode();
			consumedEnergyInTransmissionMode += networkNodes[id].getConsumedEnergyInTransmissionMode();
			//System.out.println(id + ": consumed Energy in transmission mode: " + networkNodes[id].getConsumedEnergyInTransmissionMode());
		}

		return (consumedEnergyInTransmissionMode);
			
	}
	
	/**
	 * Lifetime analysis
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long lifetimeAnalysisStaticSendBehavior(int networkWidth, int transmissionPeriod,int payloadSize){
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);
		
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;
		
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for(int id=0; id<networkNodes.length; id++){
			networkNodes[id].setSimulator(this);
		}
			
		do{
					
			for(int id=0; id<networkNodes.length; id++){
				//Generate static transmission of data
				((AodvmNetworkNode)networkNodes[id]).generateTransmissionEveryTSecondsChangingDestination(transmissionPeriod, NODE_EXECUTION_TIME, networkNodes.length, payloadSize);
			}
			

			// performe network nodes
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
			
			networkLifetime += NODE_EXECUTION_TIME;
			
			
			if(networkLifetime % (3600000) == 0){
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}
			
			
			if(networkLifetime % (86400000) == 0){
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}
		

		}while(allNodesAlive(networkNodes));
		
		calculateAverageNodeTimes(graph.getNetworkNodes());
		
		int recivedPayloadMsg = 0;
		for(int id=0; id<networkNodes.length; id++){
			recivedPayloadMsg += networkNodes[id].getNumberOfRecivedPayloadMessages();
		}
		
	
		System.out.println("Network Lifetime:" + networkLifetime/1000/60/60/24 + " Tage bzw "+ networkLifetime/1000 + " Sekunden. Recived PayloadMsg: " + recivedPayloadMsg);
	
		
		int sendPayloadMsg = 0;
		int sendRoutingMsg = 0;
		for(int id=0; id<networkNodes.length; id++){
			sendPayloadMsg += ((AodvmNetworkNode)networkNodes[id]).getNumberTransmittedPayloadMsg();
			sendRoutingMsg += ((AodvmNetworkNode)networkNodes[id]).getNumberTransmittedRREPMsg() + ((AodvmNetworkNode)networkNodes[id]).getNumberTransmittedRREQMsg();
		}
		System.out.println("Number send payload msg: " + sendPayloadMsg + ", number send routing msg: " + sendRoutingMsg);
		
		
		calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return networkLifetime;
	}
	
	/**
	 * Lifetime analysis with changing destination
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSIze
	 * @return
	 */
	public long lifetimeAnalysisStaticSendBehaviorChangingDestination(int networkWidth, int transmissionPeriod,int payloadSIze){
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);
		
		long lifetime = this.lifetimeAnalysisStaticBehavior(graph, networkWidth, transmissionPeriod, payloadSIze); 

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		
		int sendPayloadMsg = 0;
		int sendRoutingMsg = 0;
		for(int id=0; id<networkNodes.length; id++){
			sendPayloadMsg += ((AodvmNetworkNode)networkNodes[id]).getNumberTransmittedPayloadMsg();
			sendRoutingMsg += ((AodvmNetworkNode)networkNodes[id]).getNumberTransmittedRREPMsg() + ((AodvmNetworkNode)networkNodes[id]).getNumberTransmittedRREQMsg();
		}
		System.out.println("Number send payload msg: " + sendPayloadMsg + ", number send routing msg: " + sendRoutingMsg);
		
		
		calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return lifetime;
	}

	/**
	 * Lifetime analysis, every Node sends its messages to one destination
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long lifetimeAnalysisStaticSendBehaviorOneDestination(int networkWidth, int transmissionPeriod,int payloadSize){
		
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);
		
		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod, payloadSize));
	}
	
	/**
	 * Lifetime analysis, message source and destination is changing after every message transfer
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @param maxPairs
	 * @return
	 */
	public long lifetimeAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod,
			int payloadSize, int maxPairs) {
		
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);

		return this.lifetimeAnalysisRandomSorceAndDest(graph, networkWidth, transmissionPeriod, payloadSize, maxPairs);
	}
	
	/**
	 * Partitioning analysis,  every Node sends its messages to one destination
	 * 
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long partitioningAnalysis(int networkWidth, int transmissionPeriod, int payloadSize) {
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);
		
		
		return this.partitioningAnalysisOnePayloadmessageDestination(graph, networkWidth, transmissionPeriod, payloadSize);
	}
	
	/**
	 * Partitioning analysis, message source and destination is changing after every message transfer
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @param maxPairs
	 * @return
	 */
	public long partitioningAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod, int payloadSize,
			int maxPairs) {
		AodvmNetworkGraph graph = new AodvmNetworkGraph(networkWidth);

		return this.partitioningAnalysisRandomSorceAndDest(graph, networkWidth, transmissionPeriod, payloadSize,
				maxPairs);
	}

	/**
	 * 
	 * @return message transmission time
	 */
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

	
	private void calculatSpreadOfTransmittedMessages(NetworkNode networkNodes[]){
		int numberTransmittedMsg = 0;
		int numberTransmittedRREQMsg = 0;
		int numberTransmittedRREPMsg = 0;
		int numberTransmittedPayloadMsg = 0;
		this.percentageTransmittedRREQMsg = 0.0;
		this.percentageTransmittedRREPMsg = 0.0;
		this.percentageTransmittedPayloadMsg = 0.0;

		for(NetworkNode node: networkNodes){
			numberTransmittedRREQMsg += ((AodvmNetworkNode)node).getNumberTransmittedRREQMsg();
			numberTransmittedRREPMsg += ((AodvmNetworkNode)node).getNumberTransmittedRREPMsg();
			numberTransmittedPayloadMsg += ((AodvmNetworkNode)node).getNumberTransmittedPayloadMsg();
			numberTransmittedMsg += ((AodvmNetworkNode)node).getNumberTransmittedRREQMsg() + ((AodvmNetworkNode)node).getNumberTransmittedRREPMsg() + ((AodvmNetworkNode)node).getNumberTransmittedPayloadMsg();
		}
		
		percentageTransmittedRREQMsg = numberTransmittedRREQMsg / (double)numberTransmittedMsg;
		percentageTransmittedRREPMsg = numberTransmittedRREPMsg / (double)numberTransmittedMsg;
		percentageTransmittedPayloadMsg = numberTransmittedPayloadMsg / (double)numberTransmittedMsg;
	}

	public double getPercentageTransmittedRREQMsg() {
		return percentageTransmittedRREQMsg;
	}

	public double getPercentageTransmittedRREPMsg() {
		return percentageTransmittedRREPMsg;
	}

	public double getPercentageTransmittedPayloadMsg() {
		return percentageTransmittedPayloadMsg;
	}

	public double getRouteDistance() {
		return routeDistance;
	}

	public void setRouteDistance(double routeDistance) {
		this.routeDistance = routeDistance;
	}


	
}
