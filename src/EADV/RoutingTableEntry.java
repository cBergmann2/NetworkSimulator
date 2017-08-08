package EADV;

public class RoutingTableEntry {

	private int address;
	private int hopCount;
	private int costs;
	private long timestamp;
	
	public RoutingTableEntry(int address, int hopCount, int costs, long timestamp){
		this.address = address;
		this.hopCount = hopCount;
		this.costs = costs;
		this.timestamp = timestamp;
	}
	
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public int getHopCount() {
		return hopCount;
	}
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	public int getCosts() {
		return costs;
	}
	public void setCosts(int costs) {
		this.costs = costs;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
