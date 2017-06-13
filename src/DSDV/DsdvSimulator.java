package DSDV;

import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class DsdvSimulator extends Simulator{
	
	private long msgTransmissionTime; 
	
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}
	
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

}
