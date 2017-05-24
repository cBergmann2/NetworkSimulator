package AODV;

public class ReversePathRoutingTableEntry {
	public static final int ENTRY_EXPIRATION_TIME = 30000;	//entry expiration time in ms
	
	private int source_addr;
	private int source_sequence_number;
	private int broadcast_id;
	private int dest_addr;
	private int route_request_expiration_timer;
	private int nextHop;
	private long expirationTime;							//expiration time in ms
	
	public ReversePathRoutingTableEntry(int source_addr, int source_sequence_number, int broadcast_id, int dest_addr, int route_request_expiration_timer, int nextHop){
		this.source_addr = source_addr;
		this.source_sequence_number = source_sequence_number;
		this.broadcast_id = broadcast_id;
		this.dest_addr = dest_addr;
		this.route_request_expiration_timer = route_request_expiration_timer;
		this.nextHop = nextHop;
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

	public int getRoute_request_expiration_timer() {
		return route_request_expiration_timer;
	}

	public int getNextHop() {
		return nextHop;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public void decrementExpirationTime(int decrement){
		this.expirationTime -= decrement;
	}
}
