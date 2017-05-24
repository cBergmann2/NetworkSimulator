package SimulationNetwork;

import AODV.AodvNetworkGraph;
import AODV.AodvNetworkNode;

public class Simulator {

	
	public long lifetimeAnalysis(int networkWidth, double sendProbability){
		NetworkGraph graph = new AodvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		long networkLifetime = 0;
		int simulatedDays = 0;
		
		char dataToSend[] = {'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D'}; 
		/*
		PayloadMessage msg = new PayloadMessage(0, (networkWidth*networkWidth-1), dataToSend);
		((AodvNetworkNode)networkNodes[0]).addMessageToSent(msg);
		*/
		
		do{
			
			for(int id=0; id<networkNodes.length; id++){
				double random = Math.random();
				if(random <= sendProbability){
					
					//find random destination
					int randomDestination = (int)Math.random()*networkNodes.length;
					
					PayloadMessage tmpMsg = new PayloadMessage(id , randomDestination, dataToSend);
					((AodvNetworkNode)networkNodes[id]).addMessageToSent(tmpMsg);
				}
			}
			
			// TODO: performe 1 msec
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].performAction();
			}
			
			networkLifetime++;
			
			if(networkLifetime % (86400000) == 0){
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}
			
		}while(networkLifetime < 3600000);//while(allNodesAlive(networkNodes));
		
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
