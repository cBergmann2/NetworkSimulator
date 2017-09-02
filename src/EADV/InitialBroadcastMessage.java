package EADV;

import Simulator.Message;

public class InitialBroadcastMessage extends Message {
	
	int address;
	int hopCount;
	int costs;
	long timestampWhenReceivedIBM;
	
	public InitialBroadcastMessage(int address, int hopCount, int costs){
		this.address = address;
		this.hopCount = hopCount;
		this.costs = costs;
		
		this.dataVolume = Message.MESSAGE_SIZE + 3*8;
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
	public void incrementHopCount(int hopCount) {
		this.hopCount++;
	}
	public int getCosts() {
		return costs;
	}
	public void addCosts(int costs) {
		this.costs += costs;
	}
	
	public InitialBroadcastMessage clone(){
		InitialBroadcastMessage copy = new InitialBroadcastMessage(address, hopCount, costs);
		copy.setDataVolume(dataVolume);
		copy.setDestinationID(destinationID);
		copy.setEndTransmissionTime(endTransmissionTime);
		copy.setRemainingTransmissionTime(remainingTransmissionTime);
		copy.setSenderID(senderID);
		copy.setStartTransmissionTime(startTransmissionTime);
		//copy.setTimeToLive(timeToLive);
		
		return copy;
	}
	
	public String toString(){
		return ("address: " + address + ", hop count: " + hopCount + ", costs: " + costs);
	}

	/**
	 * @return the timestampWhenReceivedIBM
	 */
	public long getTimestampWhenReceivedIBM() {
		return timestampWhenReceivedIBM;
	}

	/**
	 * @param timestampWhenReceivedIBM the timestampWhenReceivedIBM to set
	 */
	public void setTimestampWhenReceivedIBM(long timestampWhenReceivedIBM) {
		this.timestampWhenReceivedIBM = timestampWhenReceivedIBM;
	}
}
