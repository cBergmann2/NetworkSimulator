package Simulator;

/**
 * Dastructur to calculate if the network is partitioned.
 * This class is used by the simulator class for paritioning analysis
 * @author Christoph Bergmann
 *
 */
public class AliveNode {
	protected int nodeID;			//Node ID
	protected boolean reachable;	//Ture if the node is reachable
	protected boolean processed;	//True if the node is already processed 
	
	public AliveNode(int nodeID){
		this.nodeID = nodeID;
		reachable = false;
		processed = false;
	}
	
	/**
	 * Set the object back to initialization state. Keeps the nodeID. Set reachable and processed back to false.
	 */
	public void resetObject(){
		reachable = false;
		processed = false;
	}

	public boolean isReachable() {
		return reachable;
	}

	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public int getNodeID() {
		return nodeID;
	}
}
