package Flooding;

import AODV.AodvNetworkNode;
import SimulationNetwork.NetworkGraph;
import SimulationNetwork.NetworkNode;

public class FloodingNetworkGraph extends NetworkGraph {

	public FloodingNetworkGraph(int width) {
		nodes = new NetworkNode[width * width];
		this.networkWidth = width;

		for (int id = 0; id < nodes.length; id++) {
			nodes[id] = new FloodingNetworkNode(id);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}

}
