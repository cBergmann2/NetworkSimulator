package OLSR;

public class OlsrMessage {
	
	public static final int MESSAGE_SIZE = 2 * 8;

	int mesageType;
	int messageSize;
	int originatorAddress;
	int ttl;
	int hopCount;
	int messageSequenceNumber;
	
}
