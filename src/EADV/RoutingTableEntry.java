package EADV;

/**
 * Datastructure to safe a routing table entry
 * @author Christoph Bergmann
 *
 */
public class RoutingTableEntry {

	private int address;	//address of next hop
	private int hopCount;	//hop count to destination
	private int costs;		//costs
	private long timestamp;	//time stamp of entry
	
	/**
	 * Initialise the routing table entry with the given parameter
	 * @param address
	 * @param hopCount
	 * @param costs
	 * @param timestamp
	 */
	public RoutingTableEntry(int address, int hopCount, int costs, long timestamp){
		this.address = address;
		this.hopCount = hopCount;
		this.costs = costs;
		this.timestamp = timestamp;
	}
	
	/**
	 * Get the address of the next hop
	 * @return
	 */
	public int getAddress() {
		return address;
	}
	
	/**
	 * Set address of the next hop
	 * @param address
	 */
	public void setAddress(int address) {
		this.address = address;
	}
	
	/**
	 * 
	 * @return hop count
	 */
	public int getHopCount() {
		return hopCount;
	}
	
	/**
	 * 
	 * @param hopCount
	 */
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	/**
	 * 
	 * @return Costs
	 */
	public int getCosts() {
		return costs;
	}
	
	/**
	 * Set the costs
	 * @param costs
	 */
	public void setCosts(int costs) {
		this.costs = costs;
	}

	
	/**
	 * Get the time stamp
	 * @return time stamp of this table entry
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * set the time stamp
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
