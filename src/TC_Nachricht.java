import java.util.LinkedList;

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:30
 */
public class TC_Nachricht {
	
	private int originatorID;
	private LinkedList<Integer> advertisedNeighbors;

	public TC_Nachricht(int originatorID){
		this.originatorID = originatorID;
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

	public void finalize() throws Throwable {

	}
}//end TC_Nachricht