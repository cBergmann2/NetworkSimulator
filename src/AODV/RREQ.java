package AODV;

public class RREQ  implements Cloneable{
	
	private int source_addr;
	private int source_sequence_number;
	private int broadcast_id;
	private int dest_addr;
	private int dest_sequenc_number;
	private int hop_cnt;
	
	public RREQ(int source_addr, int source_sequence_number, int broadcast_id, int dest_addr, int dest_sequence_number){
		this.source_addr = source_addr;
		this.source_sequence_number = source_sequence_number;
		this.broadcast_id = broadcast_id;
		this.dest_addr = dest_addr;
		this.dest_sequenc_number = dest_sequence_number;
		this.hop_cnt = 0;
	}
	
	public int getSource_addr() {
		return source_addr;
	}
	public int getSource_sequence_number() {
		return source_sequence_number;
	}
	public int getBroadcast_id() {
		return broadcast_id;
	}
	public int getDest_addr() {
		return dest_addr;
	}
	public int getDest_sequenc_number() {
		return dest_sequenc_number;
	}
	public int getHop_cnt() {
		return hop_cnt;
	}
	
	public void incrementHopCount(){
		this.hop_cnt++;
	}
	
	public RREQ clone(){
		return new RREQ(this.source_addr, this.source_sequence_number, this.broadcast_id, this.dest_addr, this.dest_sequenc_number);
	}

}
