package OLSR;

import java.util.LinkedList;

/**
 * Dastructur to safe the network topology
 * @author Christoph Bergmann
 *
 */
public class TopologyTableEntry {

	int destinationAddress;
	LinkedList<Integer> neighbors;
	boolean routeAvailable;
	
	public TopologyTableEntry(){
		neighbors = new LinkedList<Integer>();
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
	 * @return the neighbors
	 */
	public LinkedList<Integer> getNeighbors() {
		return neighbors;
	}

	/**
	 * @param neighbors the neighbors to set
	 */
	public void addNeighbor(int neighbor) {
		this.neighbors.add(neighbor);
	}
	
	public void clearNeighborList(){
		this.neighbors.clear();
	}

	/**
	 * @return the routeAvailable
	 */
	public boolean isRouteAvailable() {
		return routeAvailable;
	}

	/**
	 * @param routeAvailable the routeAvailable to set
	 */
	public void setRouteAvailable(boolean routeAvailable) {
		this.routeAvailable = routeAvailable;
	}

	public boolean isTopologyChanged(TopologyDiscoveryMessage msg) {
		if(msg.getAdvertiesdNeighborList().size() != this.neighbors.size()){
			return true;
		}
		
		for(int neighbor: msg.getAdvertiesdNeighborList()){
			if(!neighbors.contains(neighbor)){
				return true;
			}
		}
		
		for(int neighbor: neighbors){
			if(!msg.getAdvertiesdNeighborList().contains(neighbor)){
				return true;
			}
		}
		
		return false;
	}
	
	
}
