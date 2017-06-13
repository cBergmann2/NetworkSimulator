package DSDV;

public class ForwardTableEntry {
	
	private int destination;
	private int nextHop;
	private int metric;
	private long sequenceNumber;
	private long install;
	private int flag;
	private int stalbeData;
	
	public ForwardTableEntry(int destination, int nextHop, int metric, long sequenceNumber, long install){
		this.destination = destination;
		this.nextHop = nextHop;
		this.metric = metric;
		this.sequenceNumber = sequenceNumber;
		this.install = install;
	}
	
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public int getNextHop() {
		return nextHop;
	}
	public void setNextHop(int nextHop) {
		this.nextHop = nextHop;
	}
	public int getMetric() {
		return metric;
	}
	public void setMetric(int metric) {
		this.metric = metric;
	}
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public long getInstall() {
		return install;
	}
	public void setInstall(long install) {
		this.install = install;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getStalbeData() {
		return stalbeData;
	}
	public void setStalbeData(int stalbeData) {
		this.stalbeData = stalbeData;
	}

}
