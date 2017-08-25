package AODV_RFC;

import SimulationNetwork.Message;

public class RREP extends Message{
	private int Type;
	private int R;
	private int A;
	private int Reserved;
	private int Prefix_Size;
	private int Hop_Count;
	private int Destination_IP_Adress;
	private int Destination_Sequence_Number;
	private int Orignator_IP_Adress;
	private long Lifetime;
	private int timeToLive;
	
	public RREP(){
		this.setDataVolume(6*8);
	}
	
	public RREP clone(){
		RREP clone = new RREP();
		clone.setA(A);
		clone.setDataVolume(dataVolume);
		clone.setDestination_IP_Adress(Destination_IP_Adress);
		clone.setDestinationID(destinationID);
		clone.setDestination_Sequence_Number(Destination_Sequence_Number);
		clone.setHop_Count(Hop_Count);
		clone.setLifetime(Lifetime);
		clone.setOrignator_IP_Adress(Orignator_IP_Adress);
		clone.setPrefix_Size(Prefix_Size);
		clone.setR(R);
		clone.setReserved(Reserved);
		clone.setSenderID(senderID);
		clone.setStartTransmissionTime(startTransmissionTime);
		clone.setTimeToLive(timeToLive);
		clone.setType(Type);
		
		return clone;
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
	 * @return the r
	 */
	public int getR() {
		return R;
	}
	/**
	 * @param r the r to set
	 */
	public void setR(int r) {
		R = r;
	}
	/**
	 * @return the a
	 */
	public int getA() {
		return A;
	}
	/**
	 * @param a the a to set
	 */
	public void setA(int a) {
		A = a;
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
	 * @return the prefix_Size
	 */
	public int getPrefix_Size() {
		return Prefix_Size;
	}
	/**
	 * @param prefix_Size the prefix_Size to set
	 */
	public void setPrefix_Size(int prefix_Size) {
		Prefix_Size = prefix_Size;
	}
	/**
	 * @return the hop_Count
	 */
	public int getHop_Count() {
		return Hop_Count;
	}
	/**
	 * @param hop_Count the hop_Count to set
	 */
	public void setHop_Count(int hop_Count) {
		Hop_Count = hop_Count;
	}
	/**
	 * @return the destination_IP_Adress
	 */
	public int getDestination_IP_Adress() {
		return Destination_IP_Adress;
	}
	/**
	 * @param destination_IP_Adress the destination_IP_Adress to set
	 */
	public void setDestination_IP_Adress(int destination_IP_Adress) {
		Destination_IP_Adress = destination_IP_Adress;
	}
	public int getDestination_Sequence_Number() {
		return Destination_Sequence_Number;
	}

	public void setDestination_Sequence_Number(int destination_Sequence_Number) {
		Destination_Sequence_Number = destination_Sequence_Number;
	}

	/**
	 * @return the orignator_IP_Adress
	 */
	public int getOrignator_IP_Adress() {
		return Orignator_IP_Adress;
	}
	/**
	 * @param orignator_IP_Adress the orignator_IP_Adress to set
	 */
	public void setOrignator_IP_Adress(int orignator_IP_Adress) {
		Orignator_IP_Adress = orignator_IP_Adress;
	}
	/**
	 * @return the lifetime
	 */
	public long getLifetime() {
		return Lifetime;
	}
	/**
	 * @param lifetime the lifetime to set
	 */
	public void setLifetime(long lifetime) {
		Lifetime = lifetime;
	}
	
	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
}
