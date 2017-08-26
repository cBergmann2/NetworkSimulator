package OLSR;

import java.util.LinkedList;

public class TopologyDiscoveryMessage extends OlsrMessage{
	
	int ansn;
	LinkedList<Integer> advertisedNeighborList;
	
	public TopologyDiscoveryMessage(){
		advertisedNeighborList = new LinkedList<Integer>();
	}
	
	public void addAdvertisedNeighbor(int negihborAddress){
		advertisedNeighborList.add(negihborAddress);
	}
	
	public LinkedList<Integer> getAdvertiesdNeighborList(){
		return this.advertisedNeighborList;
	}

	@Override
	public TopologyDiscoveryMessage getCopy() {
		
		TopologyDiscoveryMessage copy = new TopologyDiscoveryMessage();
		
		for(int advertisedNeighbor: advertisedNeighborList){
			copy.addAdvertisedNeighbor(advertisedNeighbor);
		}
		copy.ansn = ansn;
		copy.hopCount = hopCount;
		copy.mesageType = mesageType;
		copy.messageSequenceNumber = messageSequenceNumber;
		copy.messageSize = messageSize;
		copy.originatorAddress = originatorAddress;
		copy.ttl = ttl;
		
		
		return copy;
	}

	/**
	 * @return the ansn
	 */
	public int getAnsn() {
		return ansn;
	}

	/**
	 * @param ansn the ansn to set
	 */
	public void setAnsn(int ansn) {
		this.ansn = ansn;
	}

}
