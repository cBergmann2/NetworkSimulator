package SimulationNetwork;

import AODV.AodvNetworkGraph;
import AODV.AodvNetworkNode;

public class Simulator {
	long networkLifetime;
	
	public long lifetimeAnalysis(int networkWidth, double sendProbability){
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		NetworkGraph graph = new AodvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for(int id=0; id<networkNodes.length; id++){
			networkNodes[id].setSimulator(this);
		}
		
		
		char dataToSend[] = {'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D'}; 
		
		/*
		PayloadMessage msg = new PayloadMessage(0, (networkWidth*networkWidth-1), dataToSend);
		((AodvNetworkNode)networkNodes[0]).sendMessage(msg);
		*/
		
		do{
			
			
			if(networkLifetime % 60000 == 0){
				//every 10 seconds
				for(int id=0; id<networkNodes.length; id++){
					double random = Math.random();
					if(random <= sendProbability){
						
						//find random destination
						int randomDestination = (int)(Math.random()*networkNodes.length);
						
						PayloadMessage tmpMsg = new PayloadMessage(id , randomDestination, dataToSend);
						((AodvNetworkNode)networkNodes[id]).sendMessage(tmpMsg);
					}
				}
			}
			
			
			// TODO: performe 1 msec
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].performAction();
			}
			
			networkLifetime++;
			
			
			if(networkLifetime % (3600000) == 0){
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}
			
			if(networkLifetime % (86400000) == 0){
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}
		
		//}while(networkNodes[networkWidth*networkWidth-1].getNumberOfRecivedPayloadMessages() == 0);
		}while(allNodesAlive(networkNodes));//while(networkLifetime < 3600000);//
		
		System.out.println("Network Lifetime:" + networkLifetime/1000/60/60/24 + " Tage bzw "+ networkLifetime/1000 + " Sekunden.");
		
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

	public long getNetworkLifetime() {
		return networkLifetime;
	}
}
