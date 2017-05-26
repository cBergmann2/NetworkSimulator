package Flooding;

import SimulationNetwork.Simulator;

public class FloodingSimulator extends Simulator{

	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {
		
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);
		
		return this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
	}

	@Override
	public long energyCostAnalysis(int distance) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long lifetimeAnalysis(int networkWidth, double sendProbability) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long partitioningAnalysis(int networkWidth, double sendProbability) {
		// TODO Auto-generated method stub
		return 0;
	}

}
