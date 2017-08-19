package Flooding;


import SimulationNetwork.NetworkGraph;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class FloodingSimulator extends Simulator {


	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		return time;
	}

	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		return energyCosts;
	}

	/**
	 * Calculates energy costs for all nodes in the network to deliver a payload
	 * message.
	 * 
	 * @param graph
	 * @param networkWidth
	 * @param sourceNodeID	
	 * @param destinationNodeID
	 * @return consumed energy in nAs;
	 */
	public long energyCostAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId){
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
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;
			
			//Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {

					if(networkNodes[id].getNumberOfRecivedPayloadMessages() == 0){
						transmissionInNetworkDetected = true;
						break;
					}
			}

		} while (transmissionInNetworkDetected);

		for (int id = 0; id < networkNodes.length; id++) {
			consumedEnergyInIdleMode += networkNodes[id].getConsumedEnergyInIdleMode();
			//System.out.println("Node " + id + ": consumedEnergy idle: " + networkNodes[id].getConsumedEnergyInIdleMode() + ", time in idleMode " + (networkNodes[id].getIdleTime() +networkNodes[id].getWaitingTimeForMediumAccesPermission()) + ", time in recive mode "+ networkNodes[id].getReciveTime() + ", time in transmit mode " + networkNodes[id].getTransmissionTime() );
			consumedEnergyInReciveMode += networkNodes[id].getConsumedEnergyInReciveMode();
			consumedEnergyInTransmissionMode += networkNodes[id].getConsumedEnergyInTransmissionMode();
		}
		
		calculateAverageNodeTimes(graph.getNetworkNodes());
		
		//System.out.println("Consumed energy idle mode: " + consumedEnergyInIdleMode + ", time in idle mode: " + (this.averageTimeInIdleMode + this.averageTimeWaitingForMediumAccesPermission));
		//System.out.println("Consumed energy recive mode: " + consumedEnergyInReciveMode + ", time in recive mode: " + this.averageTimeInTransmissionMode);
		//System.out.println("Consumed energy transmit mode: " + consumedEnergyInTransmissionMode + ", time in transmit mode: " + this.averageTimeInReciveMode);

		return (consumedEnergyInIdleMode + consumedEnergyInReciveMode + consumedEnergyInTransmissionMode);
	}

	public long lifetimeAnalysisStochasitcSendBehavior(int networkWidth, double sendProbability){
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);
		
		return this.lifetimeAnalysisStochasticBehavior(graph, networkWidth, sendProbability);
	}

	public long lifetimeAnalysisStaticSendBehavior(int networkWidth, int transmissionPeriod, int payloadSize){
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);

		return this.lifetimeAnalysisStaticBehavior(graph, networkWidth, transmissionPeriod, payloadSize);
	}

	public long partitioningAnalysis(int networkWidth, int transmissionPeriod, int payloadSize) {
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);
		
		
		return this.partitioningAnalysis(graph, networkWidth, transmissionPeriod, payloadSize);
	}

	public long lifetimeAnalysisStaticSendBehaviorOneDestination(int networkWidth, int transmissionPeriod,int payloadSize){
		
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);
		
		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod, payloadSize));
	}

	public long partitioningAnalysisOnePayloadmessageDestination(int networkWidth, int transmissionPeriod, int payloadSize) {
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);
		
		
		return this.partitioningAnalysisOnePayloadmessageDestination(graph, networkWidth, transmissionPeriod, payloadSize);
	}


}
