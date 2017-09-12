package AODV;

public class TransmittedRREQ {
	int originatorIpAdress;
	int rreqId;
	
	public TransmittedRREQ(int originatorIpAdress, int rreqId){
		this.originatorIpAdress = originatorIpAdress;
		this.rreqId = rreqId;
	}
	
	public int getOriginatorIpAdress() {
		return originatorIpAdress;
	}
	public void setOriginatorIpAdress(int originatorIpAdress) {
		this.originatorIpAdress = originatorIpAdress;
	}
	public int getRreqId() {
		return rreqId;
	}
	public void setRreqId(int rreqId) {
		this.rreqId = rreqId;
	}
}
