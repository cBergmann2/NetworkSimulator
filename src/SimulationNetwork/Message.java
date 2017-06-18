package SimulationNetwork;

public class Message {
	
	public static final long TRANSMISSION_TIME_0_BIT 		= 2983680; 	//Transmission time per bit in ns
	public static final long TRANSMISSION_TIME_1_BIT 		= 5967360L; 	//Transmission time per bit in ns
	public static final long TRANSMISSION_TIME_PER_BIT 		= 4475520L; 	//Transmission time per bit in ns
	public static final long TRANSMISSION_TIME_START_SIGNAL = 8951040L; 	//Transmission time for start signal in ns
	public static final long TRANSMISSION_TIME_STOP_SIGNAL 	= 11934720L; 	//Transmission time for stop signal in ns
	
	public static final long WAITING_TIME_FOR_ACK_START_SIGNAL = 16;		//Waiting time for ACK START-SIGNAL in ms
	public static final long WAITING_TIME_FOR_ACK = 12;						//Waiting time for ACK in ms


}
