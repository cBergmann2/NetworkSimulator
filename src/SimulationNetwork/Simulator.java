package SimulationNetwork;

public class Simulator {

	
	public long lifetimeAnalysis(int networkWidth){
		NetworkGraph graph = new NetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		long networkLifetime = 0;
		int simulatedDays = -1;
		
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
			
		}while(allNodesAlive(networkNodes));
		
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
