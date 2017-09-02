package AODVM;


import Simulator.NetworkGraph;
import Simulator.NetworkNode;

public class AodvmNetworkGraph extends NetworkGraph{
	
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
