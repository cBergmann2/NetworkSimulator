package DSDV;

import Simulator.NetworkGraph;
import Simulator.NetworkNode;

/**
 * Specializes the NetworkGraph class for DSDV routing scheme
 * @author Christoph Bergmann
 *
 */
public class DsdvNetworkGraph extends NetworkGraph{
	
	/**
	 * Create graph with given network width
	 * 
	 * @param width number of nodes in a row
	 */
	public DsdvNetworkGraph(int width) {
		nodes = new NetworkNode[width*width];
		this.networkWidth = width;
		
		for(int id=0; id<nodes.length; id++){
			nodes[id] = new DsdvNetworkNode(id);
			nodes[id].setGraph(this);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}
	


}
