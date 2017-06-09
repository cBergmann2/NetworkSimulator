package AODV_RFC;

import java.util.LinkedList;

import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class AodvNetworkNode extends NetworkNode{
	
	private static final long HELLO_INTERVAL = 5*60000;	//HELLO_INTERVAL in ms
	private static final long MAX_ROUTE_LIFETIME = 9*60*1000;
	
	private LinkedList<RouteTableEntry> routingTable;
	private LinkedList<Message> waitingForRouteBuffer;
	private LinkedList<TransmittedRREQ> transmittedRREQs;
	private LinkedList<TransmittedRREQ> recivedRREQs;
	private int sequenceNumber;
	private int rreqID;
	private int numberRecivedRREPdMsg;
	
	private long helloInvervalCounter;
	private boolean sendBroadcastMessageInCurrentHelloInterval;

	public AodvNetworkNode(int id) {
		super(id);
		this.routingTable = new LinkedList<RouteTableEntry>();
		this.waitingForRouteBuffer = new LinkedList<Message>();
		this.transmittedRREQs = new LinkedList<TransmittedRREQ>();
		this.recivedRREQs = new LinkedList<TransmittedRREQ>();
		this.sequenceNumber = 1;
		this.rreqID = 1;
		this.numberRecivedRREPdMsg = 0;
		this.helloInvervalCounter = 0L;
		this.sendBroadcastMessageInCurrentHelloInterval = false;
	}

	@Override
	protected void performeTimeDependentTasks(long executionTime) {
		this.helloInvervalCounter += executionTime;
		if(helloInvervalCounter >= HELLO_INTERVAL){
			if(!sendBroadcastMessageInCurrentHelloInterval){
				//Generate and send hello message
				generateHelloMessage();
			}
			//reset helloInvervalCounter and msg send flag
			this.helloInvervalCounter = 0L;
			this.sendBroadcastMessageInCurrentHelloInterval = false;
		}
		
	}
	
	private void generateHelloMessage(){
		boolean nodeHasPrecursor = false;
		
		//Search for precursor
		for(RouteTableEntry routeTableEntry: routingTable){
			if(routeTableEntry.getPrecursorList().size() > 0){
				nodeHasPrecursor = true;
				break;
			}
		}
		
		if(nodeHasPrecursor){
			//Node has at least one precursor -> generate and send hello message
			RREP helloMsg = new RREP();
			helloMsg.setDestination_IP_Adress(this.id);
			helloMsg.setDestination_Sequence_Number(this.sequenceNumber);
			helloMsg.setHop_Count(0);
			helloMsg.setTimeToLive(1);
			
			this.sendMsg(helloMsg);
		}
	}

	private void updateLifetimeOfRoutes(long executionTime){
		for(RouteTableEntry routeTableEntry: routingTable){
			if(routeTableEntry.isValid()){
				routeTableEntry.decrementRouteLifetime(executionTime);
			}
		}
	}
	
	@Override
	public void processRecivedMessage() {
		Message recivedMsg = inputBuffer.removeFirst();
		if (recivedMsg instanceof RREQ) {
			reciveRREQ((RREQ) recivedMsg);
		} else {
			if (recivedMsg instanceof RREP) {
				reciveRREP((RREP) recivedMsg);
			}
			else{
				if(recivedMsg instanceof RREP_ACK){
					reciveRREP_ACK((RREP_ACK)recivedMsg);
				}
				else{
					if(recivedMsg instanceof RERR){
						reciveRERR((RERR)recivedMsg);
					}
					else{
						if(recivedMsg instanceof PayloadMessage){
							recivePayloadMessage((PayloadMessage) recivedMsg);
						}						
					}
				}
			}
		}
	}

	private void reciveRREQ(RREQ msg){
		//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": Recive RREQ from Node " + msg.getSenderID() + ". DestinationNode: " + msg.getDestination_IP_Addresse());
		//Update route table entry for rreq transmitter node
		updateRouteTable(msg.getSenderID(), -1, 1, msg.getSenderID(), MAX_ROUTE_LIFETIME);
		
		//search for already received RREQ with same Originator IP Address and RREQ ID
		for(TransmittedRREQ rreq: recivedRREQs){
			if(rreq.getOriginatorIpAdress() == msg.getOriginator_IP_Adress() && rreq.getRreqId() == msg.getRREQ_ID()){
				//RREQ already received and processed
				return;
			}
		}
		//RREQ not received and processed jet
		recivedRREQs.add(new TransmittedRREQ(msg.getOriginator_IP_Adress(), msg.getRREQ_ID()));
		
		//Update routingtable for rreq originator
		if(msg.getOriginator_IP_Adress() != this.id){
			updateRouteTable(msg.getOriginator_IP_Adress(), msg.getOriginator_Sequence_Number(), msg.getHop_Count()+1, msg.getSenderID(), MAX_ROUTE_LIFETIME);
	
			
			//Search for route to destination
			boolean createRREP = false;
			RouteTableEntry routeToDestination = null;
			if(msg.getDestination_IP_Addresse() == this.id){
				//System.out.println("Node " + this.id + ": This node ist destination");
				createRREP = true;
			}
			else{
				//search for route in routing table
				for(RouteTableEntry route: routingTable){
					if(msg.getDestination_IP_Addresse() == route.getDestinationAdress() && msg.getDestination_Sequence_Number() <= route.getDestinationSequenceNumber() && !msg.isDestinationOnly()){
						//System.out.println("Node " + this.id + ": Route for destination found");
						routeToDestination = route;
						createRREP = true;
					}
				}
			}
			if(createRREP){
				//System.out.println("" +simulator.getNetworkLifetime() +": Node " + this.id + ": Create RREP");
				RREP rrep = new RREP();
				rrep.setDestination_IP_Adress(msg.getDestination_IP_Addresse());
				rrep.setOrignator_IP_Adress(msg.getOriginator_IP_Adress());
				
				if(routeToDestination == null){
					//Route Reply Generation by the Destination
					this.sequenceNumber++; 	//Increment sequence Number
					rrep.setDestination_Sequence_Number(this.sequenceNumber);
					rrep.setHop_Count(0);
					//TODO: set route lifeitime
					//rrep.setLifetime(lifetime);
				}
				else{
					rrep.setDestination_Sequence_Number(routeToDestination.getDestinationSequenceNumber());
					rrep.setHop_Count(routeToDestination.getHopCount());
					//TODO: set route lifeitime
					//rrep.setLifetime(lifetime);
				}
				rrep.setDestinationID(getNextHopToDestination(msg.getOriginator_IP_Adress()));
				rrep.setSenderID(this.id);
				rrep.setTimeToLive(msg.getHop_Count() *2);
				rrep.setLifetime(9*60*1000); //Lifetime of Route = 9 minutes
				
				this.addNodeAsPrecursor(rrep.getDestinationID(), rrep.getDestination_IP_Adress());
				
				sendMsg(rrep);
				
			}
			else{
				if(msg.getTimeToLive() > 1){
					//create copy of RREQ
					RREQ rreqCopy = (RREQ) msg.clone();
					rreqCopy.setSenderID(this.id);
					rreqCopy.setHop_Count(msg.getHop_Count()+1);
					rreqCopy.setTimeToLive(msg.getTimeToLive() -1);
					
					
					sendMsg(rreqCopy);
					this.sendBroadcastMessageInCurrentHelloInterval = true;
				}
			}
			
		}		
		
	}
	
	private void addNodeAsPrecursor(int nodeID, int destination){
		LinkedList<Integer> precursorList = null;
		for(RouteTableEntry tableEntry: routingTable){
			if(tableEntry.getDestinationAdress() == destination){
				precursorList = tableEntry.getPrecursorList();

				if(!precursorList.contains(nodeID)){
					tableEntry.addPrecursor(nodeID);
				}
				
				break;
			}
		}
	}
	
	private boolean updateRouteTable(int destination, int sequenceNumber, int hopCount, int nextHop, long maxRouteLifetime){
		boolean routeAlreadyExists = false;
		boolean routingTableUpdated = false;
		for(RouteTableEntry route: routingTable){
			if(route.getDestinationAdress() == destination){
				routeAlreadyExists = true;
				if((route.getDestinationSequenceNumber() < sequenceNumber)|| ((route.getDestinationSequenceNumber() == sequenceNumber) && (route.getHopCount() > hopCount))){			
					route.setDestinationSequenceNumber(sequenceNumber);
					route.setHopCount(hopCount);
					route.setNextHop(nextHop);
					route.setValid(true);
					route.setLifetime(maxRouteLifetime);

					routingTableUpdated = true;
				}
				
				if((route.getDestinationSequenceNumber() <= sequenceNumber) && (route.getLifetime() < maxRouteLifetime)){
					route.setLifetime(maxRouteLifetime);
				}
				
				if(!route.isValid()){
					route.setDestinationSequenceNumber(sequenceNumber);
					route.setHopCount(hopCount);
					route.setNextHop(nextHop);
					route.setValid(true);
					route.setLifetime(maxRouteLifetime);

					routingTableUpdated = true;
				}
				
			}
		}
		
		if(!routeAlreadyExists){
			//create new route 
			RouteTableEntry route = new RouteTableEntry();
			route.setDestinationAdress(destination);
			route.setDestinationSequenceNumber(sequenceNumber);
			route.setHopCount(hopCount);
			route.setNextHop(nextHop);
			route.setValid(true);
			route.setLifetime(maxRouteLifetime);

			routingTableUpdated = true;
			routingTable.add(route);
		}
		
		return routingTableUpdated;
	}
	
	private int getNextHopToDestination(int destination){
		for(RouteTableEntry route: routingTable){
			if(route.getDestinationAdress() == destination && route.isValid()){
				return route.getNextHop();
			}
		}
		return -1;
	}
	
	private void reciveRREP(RREP msg){
		//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": Recive RREP from Node " + msg.getSenderID() + ". DestinationNode: " + msg.getDestination_IP_Adress() + "; HopCount: " + msg.getHop_Count());
		this.numberRecivedRREPdMsg++;
		//Update routing table for previous hop
		updateRouteTable(msg.getSenderID(), -1, 1, msg.getSenderID(), MAX_ROUTE_LIFETIME);
		
		int nextHopCount = msg.getHop_Count() +1;
		
		//Update routing table for desintation node
		if(updateRouteTable(msg.getDestination_IP_Adress(), msg.getDestination_Sequence_Number(), nextHopCount, msg.getSenderID(), msg.getLifetime())){
			//Forward RREP if routing table was updated
			
			//Forward RREP-Message if TTL > 0
			if(msg.getOrignator_IP_Adress() != this.id){
				if(msg.getDestinationID() == this.id){
					if(msg.getTimeToLive() > 1){
						//foward RREP to originator node
						RREP copyRREP = msg.clone();
						copyRREP.setSenderID(this.id);
						copyRREP.setDestinationID(getNextHopToDestination(msg.getOrignator_IP_Adress()));
						copyRREP.setHop_Count(nextHopCount);
						copyRREP.setTimeToLive(msg.getTimeToLive() -1);
						sendMsg(copyRREP);
						//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": forward RREP");
						this.addNodeAsPrecursor(copyRREP.getDestinationID(), copyRREP.getDestination_IP_Adress());
					}
				}
			}
		}
		
		//Search for messages that wait for a route for the destination
		LinkedList<Message> msgForWhichRouteWasFound = new LinkedList<Message>();
		for(Message waitingMsg: waitingForRouteBuffer){
			if(waitingMsg instanceof PayloadMessage){
				if(((PayloadMessage)waitingMsg).getPayloadDestinationAdress() == msg.getDestination_IP_Adress()){
					//simulator.resetTransmissionUnitFromAllNodes();
					
					waitingMsg.setDestinationID(getNextHopToDestination(msg.getDestination_IP_Adress()));
					//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": Send payload message");
					//aktualisiere Sendezeit
					waitingMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
					outputBuffer.add(waitingMsg);
					msgForWhichRouteWasFound.add(waitingMsg);
				}
			}
		}
		//Delete Msg from waiting list
		for(Message deleteMsg: msgForWhichRouteWasFound){
			waitingForRouteBuffer.remove(deleteMsg);
		}
	}
	
	private void reciveRREP_ACK(RREP_ACK msg){
		
	}
	
	private void reciveRERR(RERR msg){
		
	}
	
	private void recivePayloadMessage(PayloadMessage msg){
		//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": recive payloadMessage from node " + msg.getPayloadSourceAdress() + " to node " + msg.getPayloadDestinationAdress());
		if(msg.getDestinationID() == this.id){
			if(msg.getPayloadDestinationAdress() != this.id){
				//forward message to next hop
				//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": forward payloadMessage from node " + msg.getPayloadSourceAdress() + " to node " + msg.getPayloadDestinationAdress());
				this.sendMsg(msg);
			}
			else{
				long transmissionTime = simulator.getNetworkLifetime() - msg.getStartTransmissionTime();
				//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": message from node " + msg.getPayloadSourceAdress() + " recived. Transmissiontime: " + transmissionTime);
				numberRecivedPayloadMsg++;
				msg.setEndTransmissionTime(simulator.getNetworkLifetime());
				this.lastRecivedPayloadMessage = msg;
			}
		}
	}
	

	@Override
	public void sendMsg(Message msg) {
		if(msg.getDestinationID() == -1){
			//Broadcast message send this to all neighbors
			super.sendMsg(msg);
		}
		else{
			
			if(msg instanceof RREP){
				outputBuffer.add(msg);
			}
			
			if(msg instanceof RREQ){
				outputBuffer.add(msg);
			}
			
			if(msg instanceof PayloadMessage){
				//Search valid route to destination
				RouteTableEntry routeTableEntry = null;
				
				for (RouteTableEntry tempEntry : routingTable) {
					if (tempEntry.getDestinationAdress() == ((PayloadMessage)msg).getPayloadDestinationAdress()) {
						routeTableEntry = tempEntry;
						break;
					}
				}
				
				if(routeTableEntry != null && routeTableEntry.isValid()){
					//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": Send payload message");
					msg.setDestinationID(getNextHopToDestination(((PayloadMessage)msg).getPayloadDestinationAdress()));
					outputBuffer.add(msg);
				}
				else{
					startRouteDiscoveryProcess(((PayloadMessage)msg).getPayloadDestinationAdress());
					waitingForRouteBuffer.add(msg);
				}
				
			}
		}
		
		
	}
	
	private void startRouteDiscoveryProcess(int destinationID) {
		//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": Start route discovery process");
		
		RREQ rreq = new RREQ(this.id, this.sequenceNumber, destinationID, 0, this.rreqID);
		
		//Search routeTableEntry for destination
		RouteTableEntry routeTableEntry = null;
		for (RouteTableEntry tempEntry : routingTable) {
			if (tempEntry.getDestinationAdress() == destinationID) {
				routeTableEntry = tempEntry;
				break;
			}
		}
		
		if(routeTableEntry == null){
			//Set unknown sequence number flag
			rreq.setU(1);
		}
		else{
			//Set last known sequence number
			rreq.setDestination_Sequence_Number(routeTableEntry.getDestinationSequenceNumber());
		}
		
		rreq.setDestinationOnly(false);
		rreq.setTimeToLive(1000);
		rreq.setSenderID(this.id);
		
		
		//Add RREQ to trransmittedRREQ list
		this.transmittedRREQs.add(new TransmittedRREQ(this.id, this.rreqID));
		
		//Inkrement RREQ_ID
		this.rreqID++;
		
		
		
		//send RREQ
		outputBuffer.add(rreq);
		
	}

	@Override
	public void startSendingProcess(PayloadMessage tmpMsg) {
		
		//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": start transmission process, msg destination: " + tmpMsg.getPayloadDestinationAdress() );
		
		tmpMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
		tmpMsg.setSenderID(this.id);
		tmpMsg.setPayloadSourceAdress(this.id);
		
		this.sendMsg(tmpMsg);
		
	}
	
	public void reciveMsg(Message msg){
		//currentlyTransmittingAMessage = true;
		if(incommingMsg == null){
			incommingMsg = msg;
		}
		else{
			//collision
			//System.out.println("Collision detected at Node " + this.id);
			graph.addCollision();
			if((msg instanceof RREP) && msg.getDestinationID() == this.id){
				//System.out.println("Collision-Error: RREP was not transfered to destination");
				//System.out.println("Hard collision error: RREP message is collided with another message -> for simulation give RREP priority");
				incommingMsg = msg;
				
			}
			if((msg instanceof PayloadMessage) && (msg.getDestinationID() == this.id)){
				//System.out.println("Collision-Error: Payload was not transfered to destination");
				//System.out.println("Hard collision error: Payload message is collided with another message -> for simulation give payloadMessage priority");
				incommingMsg = msg;
				
			}
		}
	}

	public int getNumberRecivedRREPdMsg() {
		return numberRecivedRREPdMsg;
	}

}
