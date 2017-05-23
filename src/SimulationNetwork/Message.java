package SimulationNetwork;

public class Message {
	
	protected long remainingTime;	//Remaining time until message has been transferred

	public long getRemainingTime() {
		return remainingTime;
	}

	/**
	 * 
	 * @param timeStep elapsed time in msec
	 */
	public void decreaseRemainingTime(long timeStep) {
		this.remainingTime -= timeStep;
		
	}

}
