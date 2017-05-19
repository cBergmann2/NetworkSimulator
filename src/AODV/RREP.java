package AODV;

public class RREP {
	
	public static final int RREP_UEBERTRAGUNGSZEIT = 378927; //Übertragungszeit in µsec
	
	private int source_addr;
	private int dest_addr;
	private int dest_sequence_number;
	private int hop_cnt;
	private int lifetime;
	private int uebertragungszeit;
	
	public RREP(int source_addr, int dest_addr, int dest_sequence_number, int lifetime, int hop_cnt){
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
	public int getLifetime() {
		return lifetime;
	}

	public int getUebertragungszeit() {
		return uebertragungszeit;
	}

	public void addUebertragungszeit(int uebertragungszeit) {
		this.uebertragungszeit += uebertragungszeit;
	}

}
