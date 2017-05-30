package AODV;


import Flooding.FloodingNetworkGraph;
import SimulationNetwork.Simulator;

public class AodvSimulator extends Simulator
{

	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		return time;
	}
	
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		return energyCosts;
	}
	
	@Override
	public long partitioningAnalysis(int networkWidth, double sendProbability) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long lifetimeAnalysisStaticSendBehavior(int networkWidth, int transmissionPeriod,int payloadSIze){
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		return this.lifetimeAnalysisStaticBehavior(graph, networkWidth, transmissionPeriod, payloadSIze);
	}

}
