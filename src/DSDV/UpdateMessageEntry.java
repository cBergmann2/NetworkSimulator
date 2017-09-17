package DSDV;

/**
 * Datastructure for an entry of a update message
 * @author Christoph Bergmann
 *
 */
public class UpdateMessageEntry{
	public static final int SIZE = 8 + 8 + 8;
	
	private int destination;
	private int metric;
	private long sequenceNumber;
	
	public UpdateMessageEntry(int destination, int metric, long sequenceNumber){
		this.destination = destination;
		this.metric = metric;
		this.sequenceNumber = sequenceNumber;
	}
	
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public int getMetric() {
		return metric;
	}
	public void setMetric(int metric) {
		this.metric = metric;
	}
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	
}
