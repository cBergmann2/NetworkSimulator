package AODV_RFC;

import java.util.LinkedList;

public class RouteTableEntry {

	private int destinationAdress;
	private int destinationSequenceNumber;
	private int validDestinationSequenceNumberFlag;
	private int hopCount;
	private int nextHop;
	private int lifetime;
	private boolean valid;
	private LinkedList<Integer> precursorList;
	
	public RouteTableEntry(){
		precursorList = new LinkedList<Integer>();
	}
	
	
	public int getDestinationAdress() {
		return destinationAdress;
	}
	
	public void setDestinationAdress(int destinationAdress) {
		this.destinationAdress = destinationAdress;
	}
	
	public int getDestinationSequenceNumber() {
		return destinationSequenceNumber;
	}
	
	public void setDestinationSequenceNumber(int destinationSequenceNumber) {
		this.destinationSequenceNumber = destinationSequenceNumber;
	}
	
	public int getValidDestinationSequenceNumberFlag() {
		return validDestinationSequenceNumberFlag;
	}
	
	public void setValidDestinationSequenceNumberFlag(int validDestinationSequenceNumberFlag) {
		this.validDestinationSequenceNumberFlag = validDestinationSequenceNumberFlag;
	}
	
	public int getHopCount() {
		return hopCount;
	}
	
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	public int getNextHop() {
		return nextHop;
	}
	
	public void setNextHop(int nextHop) {
		this.nextHop = nextHop;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}


	public boolean isValid() {
		return valid;
	}


	public void setValid(boolean valid) {
		this.valid = valid;
	}


	public LinkedList<Integer> getPrecursorList() {
		return precursorList;
	}


	public void addPrecursor(Integer precursor) {
		this.precursorList.add(precursor);
	}
	
}
