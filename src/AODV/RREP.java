package AODV;

import javax.sound.midi.Sequence;

import SimulationNetwork.Message;

public class RREP extends Message {
	
	public static final int RREP_UEBERTRAGUNGSZEIT = 378927000; //Übertragungszeit in nsec
	
	private int source_addr;
	private int dest_addr;
	private int dest_sequence_number;
	private int hop_cnt;
	private long lifetime;
	private int uebertragungszeit;
	
	
	private RREP(int senderID, int destinationID, long remainingTransmissionTime, int dataVolume){
		super(senderID, destinationID, remainingTransmissionTime, dataVolume);
	}
	
	public RREP(int senderID, int destinationID, int source_addr, int dest_addr, int dest_sequence_number, long lifetime, int hop_cnt){
		this(senderID, destinationID, RREP_UEBERTRAGUNGSZEIT, 80);
		this.source_addr = source_addr;
		this.dest_addr = dest_addr;
		this.dest_sequence_number = dest_sequence_number;
		this.lifetime = lifetime;
		this.hop_cnt = 0;
		this.uebertragungszeit = 0;
	}
	
	public int getSource_addr() {
		return source_addr;
	}
	public int getDest_addr() {
		return dest_addr;
	}
	public int getDest_sequence_number() {
		return dest_sequence_number;
	}
	
	public void incrementHopCnt(){
		this.hop_cnt++;
	}
	
	public int getHop_cnt() {
		return hop_cnt;
	}
	public long getLifetime() {
		return lifetime;
	}

	public int getUebertragungszeit() {
		return uebertragungszeit;
	}

	public void addUebertragungszeit(int uebertragungszeit) {
		this.uebertragungszeit += uebertragungszeit;
	}
	
	public RREP clone(){
		return new RREP(senderID, destinationID, source_addr, dest_addr, dest_sequence_number, lifetime, hop_cnt);
	}

	public void setLifetime(long lifetime) {
		this.lifetime = lifetime;
	}
	
	public void decrementLifetime(long decrement){
		this.lifetime -= decrement;
	}

}
