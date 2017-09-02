package AODV_RFC;

import java.util.LinkedList;

public class RouteTableEntry {

	private int destinationAdress;
	private int destinationSequenceNumber;
	private int validDestinationSequenceNumberFlag;
	private int hopCount;
	private int nextHop;
	private long expirationTime;
	private boolean valid;
	private LinkedList<Integer> precursorList;
	private long timeOfLastMessage;
	
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
	
	public long getExpirationTime() {
		return expirationTime;
	}
	
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
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
	
	public boolean isRouteValid(long currentTime){
		if(this.expirationTime > currentTime){
			return true;
		}
		this.valid = false;
		return false;
	}


	/**
	 * @return the timeOfLastMessage
	 */
	public long getTimeOfLastMessage() {
		return timeOfLastMessage;
	}


	/**
	 * @param timeOfLastMessage the timeOfLastMessage to set
	 */
	public void setTimeOfLastMessage(long timeOfLastMessage) {
		this.timeOfLastMessage = timeOfLastMessage;
	}
}
