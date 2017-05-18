package AODV;

public class RoutingTableEntry {
	int zielknotenID;
	int dest_sequence_number;
	int nextHop;
	int routeRequestExpirationTime;
	int tableEntryExpirationTime;
	int lifetime;
	
	public RoutingTableEntry(int zielknotenID, int dest_sequence_number, int nextHop){
		this.zielknotenID = zielknotenID;
		this.dest_sequence_number = dest_sequence_number;
		this.nextHop = nextHop;
		this.lifetime = 0;
	}
	
	public void updateTableEntryExpirationTime(){
		//tableEntryExpirationTime = currentTime + active_route_timeout;
	}

	public int getZielknotenID() {
		return zielknotenID;
	}

	public int getTableEntryExpirationTime() {
		return tableEntryExpirationTime;
	}

	public int getDest_sequence_number() {
		return dest_sequence_number;
	}
	
	public int getLifetime(){
		return lifetime;
	}
	
	public int getNextHop(){
		return nextHop;
	}
	
}
