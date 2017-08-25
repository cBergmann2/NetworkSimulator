package SimulationNetwork;

public class CommunicationPair {

	
	int source;
	int destination;
	long startTransmissionTime;
	int numberTransmittedMessages;

	
	public CommunicationPair(int source, int destination, long startTransmissionTime){
		this.source = source;
		this.destination = destination;
		this.startTransmissionTime = startTransmissionTime;
		numberTransmittedMessages = 0;
	}


	public int getSource() {
		return source;
	}


	public void setSource(int source) {
		this.source = source;
	}


	public int getDestination() {
		return destination;
	}


	public void setDestination(int destination) {
		this.destination = destination;
	}


	public long getStartTransmissionTime() {
		return startTransmissionTime;
	}


	public void setStartTransmissionTime(long startTransmissionTime) {
		this.startTransmissionTime = startTransmissionTime;
	}


	public void incrementTransmittedMessages() {
		numberTransmittedMessages++;
	}


	public int getNumberTransmittedMessages() {
		// TODO Auto-generated method stub
		return numberTransmittedMessages;
	}
}
