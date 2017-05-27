package SimulationNetwork;

import AODV.AodvNetworkGraph;
import AODV.AodvNetworkNode;
import Flooding.FloodingNetworkNode;

public abstract class Simulator {
	protected long networkLifetime;

	/**
	 * Calculates time between start and end of transmission process
	 * 
	 * @param distance
	 *            Distance between source and destination
	 * @return Time between start and end of transmission process
	 */
	public long speedAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId) {

		networkLifetime = 0;

		
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		char dataToSend[] = { 'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D' };

		PayloadMessage msg = new PayloadMessage(0, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe 1 ms every iteration
			
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction();
			}

			networkLifetime++;

		} while (networkNodes[destinationNodeId].getNumberOfRecivedPayloadMessages() == 0);

		long transmissionTime = networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getEndTransmissionTime()
				- networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getStartTransmissionTime();

		return transmissionTime;
	}

	/**
	 * Calculates energy costs for all nodes in the network to deliver a payload
	 * message.
	 * 
	 * @param graph
	 * @param networkWidth
	 * @param sourceNodeID	
	 * @param destinationNodeID
	 * @return consumed energy in nAs;
	 */
	public long energyCostAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId){
		long consumedEnergy = 0L;
		boolean transmissionInNetworkDetected;

		networkLifetime = 0;

		FloodingNetworkNode networkNodes[] = (FloodingNetworkNode[])graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		char dataToSend[] = { 'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D' };

		PayloadMessage msg = new PayloadMessage(0, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction();
			}

			networkLifetime++;
			
			//Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
				if(id != destinationNodeId){
					if(networkNodes[id].getOutgoingMessage() != null){
						transmissionInNetworkDetected = true;
						break;
					}
					if(networkNodes[id].getIncomingMessage() != null){
						transmissionInNetworkDetected = true;
						break;
					}
					if(networkNodes[id].getOutputBufferSize() > 0){
						transmissionInNetworkDetected = true;
						break;
					}
				}
			}

		} while (transmissionInNetworkDetected);

		for (int id = 0; id < networkNodes.length; id++) {
			consumedEnergy += NetworkNode.NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE
					- networkNodes[id].getAvailableEnery();
		}

		return consumedEnergy;
	}

	/**
	 * Run network with given parameter to determine duration until first node
	 * is out of power
	 * 
	 * @param networkWidth
	 *            sqrt(Number of Nodes)
	 * @param sendProbability
	 *            Probability to send Data, when node is in idle mode
	 * @return duration until first node is out of power
	 */
	public long lifetimeAnalysis(NetworkGraph graph, int networkWidth, double sendProbability){
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for(int id=0; id<networkNodes.length; id++){
			networkNodes[id].setSimulator(this);
		}
		
		
		char dataToSend[] = {'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D'}; 
		
		
		PayloadMessage msg = new PayloadMessage(0, (networkWidth*networkWidth-1), dataToSend);
		//((AodvNetworkNode)networkNodes[0]).sendMessage(msg);
		
		
		do{
					
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].generateRandomTransmissionLoad(sendProbability, networkNodes.length);
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
		
		
		
		//System.out.println("Network Lifetime:" + networkLifetime/1000/60/60/24 + " Tage bzw "+ networkLifetime/1000 + " Sekunden.");
		
		return networkLifetime;
	}

	/**
	 * Run network with given parameter to determine duration until the network
	 * is partitioned
	 * 
	 * @param networkWidth
	 * @param sendProbability
	 * @return duration until network is partitioned
	 */
	public abstract long partitioningAnalysis(int networkWidth, double sendProbability);

	protected boolean allNodesAlive(NetworkNode networkNodes[]) {
		for (NetworkNode node : networkNodes) {
			if (!node.isNodeAlive()) {
				System.out.println("Node " + node.getId() + " is down: "
						+ ((double) node.getIdleTime() / (double) networkLifetime) + "% idle time"
						+ ((double) node.getReciveTime() / (double) networkLifetime) + "% recive time"
						+ ((double) node.getTransmissionTime() / (double) networkLifetime) + "% transmit time"
						+ ((double) node.getWaitingTimeForMediumAccesPermission() / (double) networkLifetime)
						+ "% waiting for medium access time");
				return false;
			}
		}
		return true;
	}

	public long getNetworkLifetime() {
		return networkLifetime;
	}
}
