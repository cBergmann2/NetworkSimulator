package OLSR;


import Simulator.NetworkGraph;
import Simulator.NetworkNode;

/**
 * Specializes the NetworkGraph class for OLSR routing scheme
 * @author Christoph Bergmann
 *
 */
public class OlsrNetworkGraph extends NetworkGraph{
	
	/**
	 * Create graph with given network width
	 * 
	 * @param width number of nodes in a row
	 */
	public OlsrNetworkGraph(int width) {
		nodes = new NetworkNode[width*width];
		this.networkWidth = width;
		
		for(int id=0; id<nodes.length; id++){
			nodes[id] = new OlsrNetworkNode(id);
			nodes[id].setGraph(this);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}
	

}
