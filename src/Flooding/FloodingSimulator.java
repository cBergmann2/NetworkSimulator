package Flooding;

import SimulationNetwork.NetworkGraph;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class FloodingSimulator extends Simulator {

	private int collisions;
	

	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		return time;
	}

	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		FloodingNetworkGraph graph = new FloodingNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		return energyCosts;
	}
/*
	public long energyCostAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId) {

		long consumedEnergy = 0L;
		boolean msgFromAllNodesTransmitted;

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
			
			//Check if all nodes have transmit the message
			msgFromAllNodesTransmitted = true;
			for (int id = 0; id < networkNodes.length; id++) {
				if(id != destinationNodeId){
					if(networkNodes[id].getNumberTransmittedMsg() < 1){
						msgFromAllNodesTransmitted = false;
					}
				}
			}

		} while (!msgFromAllNodesTransmitted);

		for (int id = 0; id < networkNodes.length; id++) {
			consumedEnergy += NetworkNode.NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE
					- networkNodes[id].getAvailableEnery();
		}

		return consumedEnergy;
	}
	*/


	@Override
	public long partitioningAnalysis(int networkWidth, double sendProbability) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCollisions() {
		return collisions;
	}

	private void setCollisions(int collisions) {
		this.collisions = collisions;
	}

}
