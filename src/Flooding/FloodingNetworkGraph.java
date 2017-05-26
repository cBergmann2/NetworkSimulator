package Flooding;

import SimulationNetwork.NetworkGraph;


public class FloodingNetworkGraph extends NetworkGraph {
	
	private int collisions;

	public FloodingNetworkGraph(int width) {
		nodes = new FloodingNetworkNode[width * width];
		this.networkWidth = width;

		for (int id = 0; id < nodes.length; id++) {
			nodes[id] = new FloodingNetworkNode(id);
			nodes[id].setGraph(this);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}

	public int getCollisions() {
		return collisions;
	}

	public void addCollision() {
		this.collisions++;
	}

}
