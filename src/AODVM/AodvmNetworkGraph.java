package AODVM;


import Simulator.NetworkGraph;
import Simulator.NetworkNode;

/**
 * Specializes the NetworkGraph class for AODVM routing scheme
 * @author Christoph Bergmann
 *
 */
public class AodvmNetworkGraph extends NetworkGraph{
	
	/**
	 * Create graph with given network width
	 * 
	 * @param width 	number of nodes in a row
	 */
	public AodvmNetworkGraph(int width) {
		nodes = new NetworkNode[width*width];
		this.networkWidth = width;
		
		for(int id=0; id<nodes.length; id++){
			nodes[id] = new AodvmNetworkNode(id);
			nodes[id].setGraph(this);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}
	

}
