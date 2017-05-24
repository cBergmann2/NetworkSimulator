package SimulationNetwork;

import AODV.AodvNetworkGraph;
import AODV.AodvNetworkNode;

public class Simulator {

	
	public long lifetimeAnalysis(int networkWidth){
		NetworkGraph graph = new AodvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		long networkLifetime = 0;
		int simulatedDays = 0;
		
		Message msg = new Message(0, 2, 20, 400);
		((AodvNetworkNode)networkNodes[0]).addMessageToSent(msg);
		
		do{
			// TODO: Choose node(s) to send a message
			
			// TODO: performe 1 msec
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].performAction();
			}
			
			networkLifetime++;
			
			if(networkLifetime % (86400000) == 0){
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}
			
		}while(networkLifetime < 3600000);
		
		System.out.println("Network Lifetime:" + networkLifetime/1000/60/60/24 + " Tage");
		
		return networkLifetime;
	}
	
	private boolean allNodesAlive(NetworkNode networkNodes[]){
		for(NetworkNode node: networkNodes){
			if(!node.isNodeAlive()){
				return false;
			}
		}
		return true;
	}
}
