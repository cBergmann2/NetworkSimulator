package AODV;

import SimulationNetwork.NetworkGraph;
import SimulationNetwork.NetworkNode;

public class AodvNetworkGraph extends NetworkGraph{

	public AodvNetworkGraph(int width) {
		nodes = new NetworkNode[width*width];
		this.networkWidth = width;
		
		for(int id=0; id<nodes.length; id++){
			nodes[id] = new AodvNetworkNode(id);
		}
		lifetime = 0;
		initializeNetworkStructure();
	}

}