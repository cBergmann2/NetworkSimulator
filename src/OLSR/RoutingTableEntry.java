package OLSR;

public class RoutingTableEntry {
	
	int destinationAddress;
	int nextHop;
	int distance;
	
	public RoutingTableEntry(int destinationAddress, int nextHop, int distance){
		this.destinationAddress = destinationAddress;
		this.nextHop = nextHop;
		this.distance = distance;
	}
	
	/**
	 * @return the destinationAddress
	 */
	public int getDestinationAddress() {
		return destinationAddress;
	}
	/**
	 * @param destinationAddress the destinationAddress to set
	 */
	public void setDestinationAddress(int destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	/**
	 * @return the nextHop
	 */
	public int getNextHop() {
		return nextHop;
	}
	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(int nextHop) {
		this.nextHop = nextHop;
	}
	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

}
