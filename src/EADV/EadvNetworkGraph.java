package EADV;

import Simulator.NetworkGraph;
import Simulator.NetworkNode;

/**
 * Specializes the NetworkGraph class for EADV routing scheme
 * @author Christoph Bergmann
 *
 */
public class EadvNetworkGraph extends NetworkGraph{
	
	/**
	 * Create graph with given network width
	 * 
	 * @param width number of nodes in a row
	 */
	public EadvNetworkGraph(int width) {
		nodes = new NetworkNode[width*width];
		this.networkWidth = width;
		
		for(int id=0; id<nodes.length; id++){
			nodes[id] = new EadvNetworkNode(id);
			nodes[id].setGraph(this);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}
	


}
