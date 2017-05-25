package SimulationNetwork;

public abstract class Simulator {
	protected long networkLifetime;

	/**
	 * Calculates time between start and end of transmission process
	 * @param distance Distance between source and destination
	 * @return Time between start and end of transmission process
	 */
	public abstract long speedAnalysis(int networkWidth, int sourceNodeId, int destionationNodeId);
	
	/**
	 *  Calculates energy costs for all nodes in the network to deliver a payload message.
	 * @param distance	Distance between source and destination
	 * @return consumed energy in nAs;
	 */
	public abstract long energyCostAnalysis(int distance);
	
	/**
	 * Run network with given parameter to determine duration until first node is out of power
	 * @param networkWidth		sqrt(Number of Nodes)
	 * @param sendProbability	Probability to send Data, when node is in idle mode
	 * @return duration until first node is out of power
	 */
	public abstract long lifetimeAnalysis(int networkWidth, double sendProbability);
	
	/**
	 * Run network with given parameter to determine duration until the network is partitioned
	 * @param networkWidth
	 * @param sendProbability
	 * @return duration until network is partitioned
	 */
	public abstract long partitioningAnalysis(int networkWidth, double sendProbability);
	
	
	protected boolean allNodesAlive(NetworkNode networkNodes[]){
		for(NetworkNode node: networkNodes){
			if(!node.isNodeAlive()){
				System.out.println("Node " + node.getId() + " is down: " + ((double)node.getIdleTime()/(double)networkLifetime) + "% idle time" + ((double)node.getReciveTime()/(double)networkLifetime) + "% recive time" + ((double)node.getTransmissionTime()/(double)networkLifetime) + "% transmit time" + ((double)node.getWaitingTimeForMediumAccesPermission()/(double)networkLifetime) + "% waiting for medium access time" );
				return false;
			}
		}
		return true;
	}

	public long getNetworkLifetime() {
		return networkLifetime;
	}
}
