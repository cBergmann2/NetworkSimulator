package OLSR;


import AODVM.AodvmNetworkGraph;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class OlsrSimulator extends Simulator{

	private long msgTransmissionTime; 
	
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return this.networkLifetime;
	}

	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		//calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return energyCosts;
	}
	
	/**
	 * @return the msgTransmissionTime
	 */
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}
	
}
