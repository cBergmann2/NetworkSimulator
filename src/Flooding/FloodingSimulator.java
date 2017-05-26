package Flooding;

import SimulationNetwork.Simulator;

public class FloodingSimulator extends Simulator{
	
	private int collisions;

	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {
		
		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);
		
		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		return time;
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


	public int getCollisions() {
		return collisions;
	}


	private void setCollisions(int collisions) {
		this.collisions = collisions;
	}

}
