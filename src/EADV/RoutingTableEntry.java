package EADV;

public class RoutingTableEntry {

	private int address;
	private int hopCount;
	private int costs;
	
	public RoutingTableEntry(int address, int hopCount, int costs){
		this.address = address;
		this.hopCount = hopCount;
		this.costs = costs;
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
	
}
