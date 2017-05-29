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
	
	@Override
	public long partitioningAnalysis(int networkWidth, double sendProbability) {
		// TODO Auto-generated method stub
		return 0;
	}

}
