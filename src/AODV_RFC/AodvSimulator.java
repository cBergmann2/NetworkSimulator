package AODV_RFC;

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
