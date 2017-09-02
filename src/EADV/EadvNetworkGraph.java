package EADV;

import Simulator.NetworkGraph;
import Simulator.NetworkNode;

public class EadvNetworkGraph extends NetworkGraph{
	
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
