package OLSR;

/**
 * Datastructure for olsr messages
 * @author Christoph Bergmann
 *
 */
public abstract class OlsrMessage {
	
	public static final int MESSAGE_SIZE = 2 * 8;

	int mesageType;
	int messageSize;
	int originatorAddress;
	int ttl;
	int hopCount;
	int messageSequenceNumber;
	
	/**
	 * @return the mesageType
	 */
	public int getMesageType() {
		return mesageType;
	}
	
	/**
	 * @param mesageType the mesageType to set
	 */
	public void setMesageType(int mesageType) {
		this.mesageType = mesageType;
	}
	
	/**
	 * @return the messageSize
	 */
	public int getMessageSize() {
		return messageSize;
	}
	
	/**
	 * @param messageSize the messageSize to set
	 */
	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}
	
	/**
	 * @return the originatorAddress
	 */
	public int getOriginatorAddress() {
		return originatorAddress;
	}
	
	/**
	 * @param originatorAddress the originatorAddress to set
	 */
	public void setOriginatorAddress(int originatorAddress) {
		this.originatorAddress = originatorAddress;
	}
	
	/**
	 * @return the ttl
	 */
	public int getTtl() {
		return ttl;
	}
	
	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	
	/**
	 * @return the hopCount
	 */
	public int getHopCount() {
		return hopCount;
	}
	
	/**
	 * @param hopCount the hopCount to set
	 */
	public void setHopCount(int hopCount) {
		this.hopCount = hopCount;
	}
	
	/**
	 * @return the messageSequenceNumber
	 */
	public int getMessageSequenceNumber() {
		return messageSequenceNumber;
	}
	
	/**
	 * @param messageSequenceNumber the messageSequenceNumber to set
	 */
	public void setMessageSequenceNumber(int messageSequenceNumber) {
		this.messageSequenceNumber = messageSequenceNumber;
	}

	public abstract OlsrMessage getCopy();
	
	
}
