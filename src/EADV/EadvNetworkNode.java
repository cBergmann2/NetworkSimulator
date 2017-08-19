package EADV;

import java.util.LinkedList;

import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class EadvNetworkNode extends NetworkNode{
	
	LinkedList<RoutingTableEntry> routingTable;
	int nodeHopCount;
	int xValue;
	boolean nodeIsDataSink;
	private LinkedList<PayloadMessage> msgWaintingBuffer;
	long lastInitialBroadcast = 0;

	public EadvNetworkNode(int id) {
		super(id);
		routingTable = new LinkedList<RoutingTableEntry>();
		nodeHopCount = Integer.MAX_VALUE;
		xValue = 0;
		nodeIsDataSink = false;
		msgWaintingBuffer = new LinkedList<PayloadMessage>();
	}

	@Override
	protected void performeTimeDependentTasks(long executionTime) {
		
		if(nodeIsDataSink){
			if(lastInitialBroadcast < simulator.getNetworkLifetime() - 9*60*1000){
				sendInitialBroadcast();
			}
		}
		
		//TODO: Check if current routing table is up to date
		
	}

	@Override
	public void processRecivedMessage() {
		Message receivedMsg = inputBuffer.removeFirst();
		if(receivedMsg.getDestinationID() == -1 || receivedMsg.getDestinationID() == this.id){
			if (receivedMsg instanceof InitialBroadcastMessage) {
				InitialBroadcastMessage ibm = (InitialBroadcastMessage)receivedMsg;
				//System.out.println("Node " + this.id + " receive IBM: " + ibm);
				if(nodeIsDataSink){
					//System.out.println("Node ist data sink. Node will not forward data to other nodes.");
				}
				else{
					
					//updateRoutingTable
					this.updateRoutingTable(ibm);
					
					//System.out.println("Node " + this.id + " hopCount: " + nodeHopCount);
					if(ibm.getHopCount() < this.nodeHopCount + xValue){
						//forward the IBM
						InitialBroadcastMessage forwardIBM = new InitialBroadcastMessage(this.id, ibm.getHopCount() +1, ibm.getCosts() + this.getCostValue());
						forwardIBM.setSenderID(this.id);
						forwardIBM.setDestinationID(-1);
						//System.out.println("Node "+ this.id + " forward IBM");
						this.sendMsg(forwardIBM);
					}
					
					//update node hop count
					nodeHopCount = Integer.MAX_VALUE;
					for(RoutingTableEntry tableEntry: routingTable){
						if(tableEntry.getHopCount() < nodeHopCount){
							nodeHopCount = tableEntry.getHopCount();
						}
					}
				}
				
				//add all waiting payload messages to send list
				LinkedList<PayloadMessage> msgToSend = new LinkedList<PayloadMessage>();

				for (PayloadMessage msg : this.msgWaintingBuffer) {
					msgToSend.add(msg);
				}

				for (PayloadMessage msg : msgToSend) {
					this.msgWaintingBuffer.remove(msg);
					this.sendMsg(msg);
				}
				
			}
			else{
				if(receivedMsg instanceof PayloadMessage){
					//System.out.println(simulator.getNetworkLifetime() + ": Node " + this.id + " receive payloadmsg");
					PayloadMessage msg = (PayloadMessage)receivedMsg;
					if(msg.getPayloadDestinationAdress() != this.id){
						//send ACK back to transmitting node
						this.sendAck(receivedMsg);
						
						//forward msg to payload destination
						int nextHop = this.getNextHop();		//get next hop from routing table					
						msg.setDestinationID(nextHop);			//set next hop
						//System.out.println(simulator.getNetworkLifetime() + ": Node " + this.id + " forward payloadmsg to node " + nextHop);
						this.sendMsg(msg);						//forward message
						
					}
					else{
						msg.setEndTransmissionTime(simulator.getNetworkLifetime());
						this.numberRecivedPayloadMsg++;
						this.lastRecivedPayloadMessage = msg;
					}
				}
				else{
					if(receivedMsg instanceof AckMessage){
						//System.out.println("Node " + this.id + " receive ACK message");
						this.updateRoutingTable((AckMessage)receivedMsg);
					}
				}
			}
		}
	}
	
	private RoutingTableEntry getTableEntryWithMinimalCosts(){
		RoutingTableEntry minimalCostEntry = routingTable.getFirst();
		for(RoutingTableEntry tableEntry: routingTable){
			if(tableEntry.getCosts() < minimalCostEntry.getCosts()){
				minimalCostEntry = tableEntry;
			}
		}
		return minimalCostEntry;
	}
	
	private void sendAck(Message msg){
		RoutingTableEntry minimalCostEntry = this.getTableEntryWithMinimalCosts();
		AckMessage ackMsg = new AckMessage(minimalCostEntry.getHopCount(), minimalCostEntry.getCosts());
		ackMsg.setDestinationID(msg.getSenderID());
		this.sendMsg(ackMsg);
	}
	
	private int getNextHop(){
		int nextHop = Integer.MAX_VALUE;
		int currentCosts = Integer.MAX_VALUE;
		
		for(RoutingTableEntry tableEntry: routingTable){
			if(tableEntry.getCosts() < currentCosts){
				nextHop = tableEntry.getAddress();
				currentCosts = tableEntry.getCosts();
			}
		}		
		return nextHop;
	}
	
	private void updateRoutingTable(InitialBroadcastMessage ibm){
		//update routing table
		boolean entryFound = false;
		
		
		for(RoutingTableEntry tableEntry: routingTable){
			if(tableEntry.getAddress() == ibm.getAddress()){
				
				if(ibm.getHopCount() < tableEntry.getHopCount()){
					tableEntry.setCosts((int) (ibm.getCosts()) );
					tableEntry.setHopCount((int) (ibm.getHopCount()));
					System.out.println("Node " + this.id + " update routing table");
				}
				
				if(ibm.getCosts() <= tableEntry.getCosts()){
					tableEntry.setCosts((int) (ibm.getCosts()) );
					tableEntry.setHopCount((int) (ibm.getHopCount()));
				}
				else{
					if((ibm.getHopCount() > tableEntry.getHopCount())
							&& (tableEntry.getTimestamp() < simulator.getNetworkLifetime() - 5000)){
						tableEntry.setCosts(ibm.getCosts());
						tableEntry.setHopCount(ibm.getHopCount());
					}
				}
				
				entryFound = true;
			}
			
		}
		
		if(!entryFound){
			routingTable.add(new RoutingTableEntry(ibm.getAddress(), (int) (ibm.getHopCount()) , (int)(ibm.getCosts()), simulator.getNetworkLifetime()));
		}
	}
	
	private void updateRoutingTable(AckMessage msg){
		//update routing table
		for(RoutingTableEntry tableEntry: routingTable){
			if(tableEntry.getAddress() == msg.getSenderID()){
				tableEntry.setCosts((int) (msg.getCost()) );
				tableEntry.setHopCount((int) (msg.getHopCount()));
			}
		}
		
		//update node hop count
		int nodeHopcount = Integer.MAX_VALUE;
		for(RoutingTableEntry tableEntry: routingTable){
			if(tableEntry.getHopCount() < nodeHopcount){
			nodeHopCount = tableEntry.getHopCount();
			}
		}
	}

	@Override
	public void startSendingProcess(PayloadMessage tmpMsg) {
		//System.out.println("Node " + this.id + " send payload msg");
		tmpMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
		this.sendMsg(tmpMsg);
	}
	
	public void sendMsg(Message msg){
			
			msg.setSenderID(this.id);
			
			if(msg instanceof AckMessage){
				
			}
			else{
				if(msg instanceof InitialBroadcastMessage){
					msg.setDestinationID(-1);
				}
				else{
					if(msg instanceof PayloadMessage){
						
						if(routingTable.size() > 0){
							
							int nextHop = -1;
							while(-1 == nextHop){
								//Get next hop with minimal costs
								int costs = Integer.MAX_VALUE;
								for(RoutingTableEntry tableEntry: routingTable){
									if(tableEntry.getCosts() < costs){
										costs = tableEntry.getCosts();
										nextHop = tableEntry.getAddress();
									}
								}
								
								if(nextHop != -1){
									//check if next hop selection is alive
									if(!graph.getNetworkNodes()[nextHop].isNodeAlive()){
										//next hop selection is not alive
										
										//delete all routingtable entrys with next hop address == nextHop
										LinkedList<RoutingTableEntry> toDeleteRouteTable = new LinkedList<RoutingTableEntry>();
										for (RoutingTableEntry entry : routingTable) {
											if(entry.getAddress() == nextHop){
												toDeleteRouteTable.add(entry);
											}
										}
										for (RoutingTableEntry entry : toDeleteRouteTable) {
											routingTable.remove(entry);
										}
	
										
										nextHop = -1;	//erase next hop selection
									}
								}
								else{
									msgWaintingBuffer.add((PayloadMessage) msg);
									return;
								}
								
							}
							
							
							msg.setDestinationID(nextHop);
						}
						else{
							msgWaintingBuffer.add((PayloadMessage) msg);
						}
					}					
				}
					
			}
			
			this.outputBuffer.add(msg);
	}
	
	private int getCostValue(){
		double batterieLoad = this.availableEnery*1.0 / this.startEnergy*1.0;
		if(batterieLoad >= 80.0){
			return (int)(100.0 - batterieLoad);
		}
		else{
			if(batterieLoad >= 10.0){
				return (int)(Math.pow((100.0 - batterieLoad), 2));
			}
			else{
				return (int)(Math.pow((100.0 - batterieLoad), 3));
			}
		}
	}

	public int getXValue() {
		return xValue;
	}

	public void setXValue(int xValue) {
		this.xValue = xValue;
	}

	
	public void sendInitialBroadcast() {
		InitialBroadcastMessage newIbm = new InitialBroadcastMessage(this.id, 0, 0);
		newIbm.setDestinationID(-1);
		this.sendMsg(newIbm);
		this.lastInitialBroadcast = simulator.getNetworkLifetime(); 
	}

	public boolean isNodeIsDataSink() {
		return nodeIsDataSink;
	}

	public void setNodeIsDataSink(boolean nodeIsDataSink) {
		this.nodeIsDataSink = nodeIsDataSink;
	}
	
	
}
