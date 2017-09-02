package DSDV;

import Simulator.NetworkGraph;
import Simulator.NetworkNode;

public class DsdvNetworkGraph extends NetworkGraph{
	
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
