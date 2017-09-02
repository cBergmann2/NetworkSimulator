package EADV;

import Simulator.Message;

public class AckMessage extends Message{

	private int hopCount;
	private int cost;
	
	public AckMessage(int hopCount, int cost){
		this.hopCount = hopCount;
		this.cost = cost;
		
		this.dataVolume = Message.MESSAGE_SIZE + 2*8;
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
