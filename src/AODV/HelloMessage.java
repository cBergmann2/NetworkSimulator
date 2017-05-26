package AODV;

public class HelloMessage extends RREP{

	public HelloMessage(int senderID, int destinationID, int source_addr, int dest_addr, int dest_sequence_number,
			long lifetime, int hop_cnt) {
		super(senderID, destinationID, source_addr, dest_addr, dest_sequence_number, lifetime, hop_cnt);
		// TODO Auto-generated constructor stub
	}
	
}
