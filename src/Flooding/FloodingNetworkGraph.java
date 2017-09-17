package Flooding;

import Simulator.NetworkGraph;

/**
 * Specializes the NetworkGraph class for flooding routing scheme
 * @author Christoph Bergmann
 *
 */
public class FloodingNetworkGraph extends NetworkGraph {
	
	
	/**
	 * Create graph with given network width
	 * 
	 * @param width number of nodes in a row
	 */
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


}
