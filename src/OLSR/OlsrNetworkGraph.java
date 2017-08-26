package OLSR;


import SimulationNetwork.NetworkGraph;
import SimulationNetwork.NetworkNode;

public class OlsrNetworkGraph extends NetworkGraph{
	
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
