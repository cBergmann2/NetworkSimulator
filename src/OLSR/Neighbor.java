package OLSR;

import java.util.LinkedList;

/**
 * Datastructure to save information about a neighbor
 * @author Christoph Bergmann
 *
 */
public class Neighbor {

	int id;								// Id of the neighbor
	long lastHelloMessage;				// Timestamp from last hello message
	long expectedTimeNextHelloMessage;	// Expected time when received  a hello message from this neighbor 
	
	LinkedList<Integer> reachableTwoHopNeighbors;
	
	public Neighbor(){
		reachableTwoHopNeighbors = new LinkedList<Integer>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getLastHelloMessage() {
		return lastHelloMessage;
	}
	public void setLastHelloMessage(long lastHelloMessage) {
		this.lastHelloMessage = lastHelloMessage;
	}
	public long getExpectedTimeNextHelloMessage() {
		return expectedTimeNextHelloMessage;
	}
	public void setExpectedTimeNextHelloMessage(long expectedTimeNextHelloMessage) {
		this.expectedTimeNextHelloMessage = expectedTimeNextHelloMessage;
	}
	
	public LinkedList<Integer> getReachableTwoHopNeighbors(){
		return this.reachableTwoHopNeighbors;
	}
	
	
	public void addReachableTwoHopNeighbor(int twoHopNeihborId){
		reachableTwoHopNeighbors.add(twoHopNeihborId);
	}
}
