package EADV;

import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class EadvSimulator extends Simulator{
	
	private long msgTransmissionTime; 
	private long energyCostsForPropagationNetworkStructure;
	
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		EadvNetworkGraph graph = new EadvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		boolean transmissionInNetworkDetected = false;
		
		this.networkLifetime = 0L;
		
		for (int id = 0; id < networkNodes.length; id++) {
			((EadvNetworkNode)networkNodes[id]).setSimulator(this);
		}
		
		//set data sink
		((EadvNetworkNode)networkNodes[destinationNodeId]).setNodeIsDataSink(true);
		//start initial broadcast
		((EadvNetworkNode)networkNodes[destinationNodeId]).sendInitialBroadcast();
		
		// Propagate network structure
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			// Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
					if (networkNodes[id].getOutgoingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					if (networkNodes[id].getOutputBufferSize() > 0) {
						transmissionInNetworkDetected = true;
						break;
					}
				
			}
		} while (transmissionInNetworkDetected);
		
		// network structure known in all nodes
		// start transmitting msg from source to destination
		
		System.out.println("Send payload message");
		
		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}
	
	/*
	public long speedAnalysisWhenNetworkStarts(int networkWidth, int sourceNodeId, int destinationNodeId) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}
	
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		this.networkLifetime = 0L;
		
		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode)networkNodes[id]).setSimulator(this);
		}
		
		//Propagate network structure
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
		}while(((DsdvNetworkNode)networkNodes[networkNodes.length-1]).getForwardTable().size() < networkNodes.length);
		
		//network structure known in all nodes
		//suspend propagation of network structure
		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode)networkNodes[id]).setPropagateNetworkStructure(false);
		}
		
		//Sum energy costs for propagation of network structure
		long energyCostsPropagation = 0L;
		for (int id = 0; id < networkNodes.length; id++) {
			energyCostsPropagation += networkNodes[id].getConsumedEnergyInIdleMode();
			energyCostsPropagation += networkNodes[id].getConsumedEnergyInReciveMode();
			energyCostsPropagation += networkNodes[id].getConsumedEnergyInTransmissionMode();
		}
		this.energyCostsForPropagationNetworkStructure = energyCostsPropagation;

		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}
		
		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		return energyCosts;
	}
	
	public long lifetimeAnalysisStaticSendBehaviorOneDestination(int networkWidth, int transmissionPeriod,int payloadSize){
		
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		
		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod, payloadSize));
	}
	
	public long partitioningAnalysis(int networkWidth, int transmissionPeriod, int payloadSize) {
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		
		
		return this.partitioningAnalysisOnePayloadmessageDestination(graph, networkWidth, transmissionPeriod, payloadSize);
	}

 */
	
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

	public long getEnergyCostsForPropagationNetworkStructure() {
		return energyCostsForPropagationNetworkStructure;
	}
}
