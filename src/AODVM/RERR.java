package AODVM;

import java.util.LinkedList;

import Simulator.Message;

public class RERR extends Message{

	private int Type;
	private int N;
	private int Reserved;
	private int DestCount;
	private LinkedList<Integer> UnreachableDestinationIpAdresses;
	private LinkedList<Integer> UnreachableDestinationSequenceNumbers;
	
	public void RREP(){
		this.setDataVolume(32);
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return Type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		Type = type;
	}
	/**
	 * @return the n
	 */
	public int getN() {
		return N;
	}
	/**
	 * @param n the n to set
	 */
	public void setN(int n) {
		N = n;
	}
	/**
	 * @return the reserved
	 */
	public int getReserved() {
		return Reserved;
	}
	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(int reserved) {
		Reserved = reserved;
	}
	/**
	 * @return the destCount
	 */
	public int getDestCount() {
		return DestCount;
	}
	/**
	 * @param destCount the destCount to set
	 */
	public void setDestCount(int destCount) {
		DestCount = destCount;
	}
	/**
	 * @return the unreachableDestinationIpAdresses
	 */
	public LinkedList<Integer> getUnreachableDestinationIpAdresses() {
		return UnreachableDestinationIpAdresses;
	}
	/**
	 * @param unreachableDestinationIpAdresses the unreachableDestinationIpAdresses to set
	 */
	public void addUnreachableDestinationIpAdresses(int unreachableDestinationIpAdresses) {
		UnreachableDestinationIpAdresses.add(unreachableDestinationIpAdresses);
		this.setDataVolume(this.getDataVolume() + 32);
	}
	/**
	 * @return the unreachableDestinationSequenceNumbers
	 */
	public LinkedList<Integer> getUnreachableDestinationSequenceNumbers() {
		return UnreachableDestinationSequenceNumbers;
	}
	/**
	 * @param unreachableDestinationSequenceNumbers the unreachableDestinationSequenceNumbers to set
	 */
	public void addUnreachableDestinationSequenceNumbers(int unreachableDestinationSequenceNumbers) {
		UnreachableDestinationSequenceNumbers.add(unreachableDestinationSequenceNumbers);
		this.setDataVolume(this.getDataVolume() + 32);
	}

}
