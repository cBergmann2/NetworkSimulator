package AODVM;

import SimulationNetwork.Message;

public class RREQ extends Message{

	private int Type;
	private int J;
	private int R;
	private int G;
	private boolean destinationOnly;
	private int U;
	private int Reserved;
	private int Hop_Count;
	private int RREQ_ID;
	private int Destination_IP_Address;
	private int Destination_Sequence_Number;
	private int Originator_IP_Adress;
	private int Originator_Sequence_Number;
	private int timeToLive;
	private int Min_RE;
	private double alpha;
	
	public RREQ(){
		this.setDataVolume(8*8);	
		this.Hop_Count = 0;
		this.Min_RE = 100;
	}
	
	public RREQ(int originatorIpAdress, int originatorSequenceNumber, int destinationIpAddress, int destinationSequnceNumber, int rreqId){
		this();
		this.Originator_IP_Adress = originatorIpAdress;
		this.Originator_Sequence_Number = originatorSequenceNumber;
		this.Destination_IP_Address = destinationIpAddress;
		this.Destination_Sequence_Number = destinationSequnceNumber;
		this.RREQ_ID = rreqId;
	}
	
	public RREQ clone(){
		
		RREQ copy = new RREQ();
		copy.setDataVolume(dataVolume);
		copy.setDestination_IP_Addresse(Destination_IP_Address);
		copy.setDestination_Sequence_Number(Destination_Sequence_Number);
		copy.setDestinationID(Destination_IP_Address);
		copy.setDestinationOnly(destinationOnly);
		copy.setG(G);
		copy.setHop_Count(Hop_Count);
		copy.setJ(J);
		copy.setOriginator_IP_Adress(Originator_IP_Adress);
		copy.setOriginator_Sequence_Number(Originator_Sequence_Number);
		copy.setR(R);
		copy.setRemainingTransmissionTime(remainingTransmissionTime);
		copy.setReserved(Reserved);
		copy.setRREQ_ID(RREQ_ID);
		copy.setSenderID(senderID);
		copy.setStartTransmissionTime(startTransmissionTime);
		copy.setTimeToLive(timeToLive);
		copy.setType(Type);
		copy.setU(U);
		copy.setMin_RE(Min_RE);
		
		return copy;
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
	 * @return the j
	 */
	public int getJ() {
		return J;
	}
	/**
	 * @param j the j to set
	 */
	public void setJ(int j) {
		J = j;
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
	 * @return the g
	 */
	public int getG() {
		return G;
	}
	/**
	 * @param g the g to set
	 */
	public void setG(int g) {
		G = g;
	}
	/**
	 * @return the d
	 */
	public boolean isDestinationOnly() {
		return destinationOnly;
	}
	/**
	 * @param d the d to set
	 */
	public void setDestinationOnly(boolean d) {
		destinationOnly = d;
	}
	/**
	 * @return the u
	 */
	public int getU() {
		return U;
	}
	/**
	 * @param u the u to set
	 */
	public void setU(int u) {
		U = u;
	}
	/**
	 * @return the recerved
	 */
	public int getRecerved() {
		return Reserved;
	}
	/**
	 * @param recerved the recerved to set
	 */
	public void setReserved(int recerved) {
		Reserved = recerved;
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
	 * @return the rREQ_ID
	 */
	public int getRREQ_ID() {
		return RREQ_ID;
	}
	/**
	 * @param rREQ_ID the rREQ_ID to set
	 */
	public void setRREQ_ID(int rREQ_ID) {
		RREQ_ID = rREQ_ID;
	}
	/**
	 * @return the destination_IP_Addresse
	 */
	public int getDestination_IP_Addresse() {
		return Destination_IP_Address;
	}
	/**
	 * @param destination_IP_Addresse the destination_IP_Addresse to set
	 */
	public void setDestination_IP_Addresse(int destination_IP_Addresse) {
		Destination_IP_Address = destination_IP_Addresse;
	}
	/**
	 * @return the destination_Sequence_Number
	 */
	public int getDestination_Sequence_Number() {
		return Destination_Sequence_Number;
	}
	/**
	 * @param destination_Sequence_Number the destination_Sequence_Number to set
	 */
	public void setDestination_Sequence_Number(int destination_Sequence_Number) {
		Destination_Sequence_Number = destination_Sequence_Number;
	}
	/**
	 * @return the originator_IP_Adress
	 */
	public int getOriginator_IP_Adress() {
		return Originator_IP_Adress;
	}
	/**
	 * @param originator_IP_Adress the originator_IP_Adress to set
	 */
	public void setOriginator_IP_Adress(int originator_IP_Adress) {
		Originator_IP_Adress = originator_IP_Adress;
	}
	/**
	 * @return the originator_Sequence_Number
	 */
	public int getOriginator_Sequence_Number() {
		return Originator_Sequence_Number;
	}
	/**
	 * @param originator_Sequence_Number the originator_Sequence_Number to set
	 */
	public void setOriginator_Sequence_Number(int originator_Sequence_Number) {
		Originator_Sequence_Number = originator_Sequence_Number;
	}
	
	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	public int getMin_RE() {
		return Min_RE;
	}

	public void setMin_RE(int min_RE) {
		if(min_RE > 0){
			if(this.Min_RE == -1 || this.Min_RE > min_RE){
				Min_RE = min_RE;
			}
		}
	}

	/**
	 * Set alpha equal to Min_RE/Hop_Count, when Hop_Count is greater than 0
	 * Otherwise set alpha equal to 100.
	 */
	public void calculateAlpha() {
		if(Hop_Count > 0){
			this.alpha = ((double)Min_RE*1.0)/((double)Hop_Count*1.0);
		}
		else{
			this.alpha = 100;
		}
	}
	
	public double getAlpha(){
		return this.alpha;
	}
}
