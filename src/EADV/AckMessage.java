package EADV;

import Simulator.Message;

/**
 * Datastructure for ACK messages
 * 
 * @author Christoph Bergmann
 *
 */
public class AckMessage extends Message{

	private int hopCount;
	private int cost;
	
	/**
	 * Initialize the ACK message with hop count and costs
	 * @param hopCount
	 * @param cost
	 */
	public AckMessage(int hopCount, int cost){
		this.hopCount = hopCount;
		this.cost = cost;
		
		this.dataVolume = Message.MESSAGE_SIZE + 2*8;
	}
	
	/**
	 * 
	 * @return hop count
	 */
	public int getHopCount() {
		return hopCount;
	}
	
	/**
	 * Set the hop count value
	 * @param hopCount
	 */
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	/**
	 * 
	 * @return costs
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * 
	 * @param cost
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
}
