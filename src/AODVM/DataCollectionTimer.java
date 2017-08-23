package AODVM;

import java.util.LinkedList;

public class DataCollectionTimer {

	private long exprationTime;
	private long originatorID;
	private LinkedList<RREQ> receivedRREQs;
	private boolean rrepSend;
	
	public DataCollectionTimer(){
		setRrepSend(false);
		receivedRREQs = new LinkedList<RREQ>();
	}
	
	public long getExprationTime() {
		return exprationTime;
	}
	
	public void setExprationTime(long exprationTime) {
		this.exprationTime = exprationTime;
	}
	
	public long getOriginatorID() {
		return originatorID;
	}
	
	public void setOriginatorID(long originatorID) {
		this.originatorID = originatorID;
	}
	
	public LinkedList<RREQ> getReceivedRREQs() {
		return receivedRREQs;
	}
	
	public void addReceivedRREQ(RREQ receivedRREQ) {
		
		receivedRREQ.calculateAlpha();
		this.receivedRREQs.add(receivedRREQ);
	}
	
	public RREQ getRreqWithMaxAlpha(){
		double maxAlpha = 0;
		RREQ tempRreq = null;
		
		System.out.println("Number of Received RREQs: " + receivedRREQs.size());
		
		for(RREQ rreq :receivedRREQs){
			if(rreq.getAlpha() > maxAlpha){
				maxAlpha = rreq.getAlpha();
				tempRreq = rreq;
			}
		}
		
		return tempRreq;
	}

	public boolean isRrepSend() {
		return rrepSend;
	}

	public void setRrepSend(boolean rrepSend) {
		this.rrepSend = rrepSend;
	}
	
	
}
