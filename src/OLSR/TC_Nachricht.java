package OLSR;
import java.util.LinkedList;

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:30
 */
public class TC_Nachricht extends OlsrNachricht{
	
	private int originatorID;
	private int senderID;

	private LinkedList<Integer> advertisedNeighbors;

	public TC_Nachricht(int originatorID, int hopCount){
		this.originatorID = originatorID;
		this.hopCount = hopCount;
		this.advertisedNeighbors = new LinkedList<Integer>();
	}
	
	
	
	public void addAdvertisedNeighbor(int advertisedNeighborID){
		if(!advertisedNeighbors.contains(advertisedNeighborID)){
			advertisedNeighbors.add(advertisedNeighborID);
		}
	}
	
	public int getOriginatorID(){
		return this.originatorID;
	}
	
	public LinkedList<Integer> getAdvertisedNeighbors(){
		return this.advertisedNeighbors;
	}

	public int getSenderID() {
		return senderID;
	}
	
	
	
	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public void finalize() throws Throwable {

	}
}//end TC_Nachricht