package AODV_RFC;

import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class AodvSimulator extends Simulator{
	
	private long msgTransmissionTime; 

	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}
	
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
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
		
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
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

		char dataToSend[] = { 'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D' };

		PayloadMessage msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;
		}while(((AodvNetworkNode)networkNodes[sourceNodeId]).getNumberRecivedRREPdMsg() == 0);
		
		//Reset transmission units and battery of all nodes
		this.resetTransmissionUnitFromAllNodes();
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

		} while (transmissionInNetworkDetected);

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
		

		return (consumedEnergyInIdleMode + consumedEnergyInReciveMode + consumedEnergyInTransmissionMode);
		
	}
	
	public long lifetimeAnalysisStaticSendBehavior(int networkWidth, int transmissionPeriod,int payloadSIze){
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		return this.lifetimeAnalysisStaticBehavior(graph, networkWidth, transmissionPeriod, payloadSIze);
	}
	
	public long partitioningAnalysis(int networkWidth, int transmissionPeriod, int payloadSize) {
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
		
		return this.partitioningAnalysis(graph, networkWidth, transmissionPeriod, payloadSize);
	}

	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

}
