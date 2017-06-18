package AODV_RFC;

import SimulationNetwork.Layer3Message;

public class RREP_ACK extends Layer3Message{

	private int Type;
	private int Reserved;
	
	public RREP_ACK(){
		this.setDataVolume(15);
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
	
}
