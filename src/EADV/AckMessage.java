package EADV;

import SimulationNetwork.Message;

public class AckMessage extends Message{

	private int hopCount;
	private int cost;
	
	public AckMessage(int hopCount, int cost){
		this.hopCount = hopCount;
		this.cost = cost;
	}
	
	public int getHopCount() {
		return hopCount;
	}
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
}
