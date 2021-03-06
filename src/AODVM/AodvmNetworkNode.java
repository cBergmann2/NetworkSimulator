package AODVM;

import java.util.LinkedList;

import Simulator.CollisionMessage;
import Simulator.Message;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;

/**
 * Specializes the NetworkNode class for AODVM routing scheme
 * 
 * @author Christoph Bergmann
 */
public class AodvmNetworkNode extends NetworkNode{
	
	private static final long HELLO_INTERVAL = 5*60000;	//HELLO_INTERVAL in ms
	public static final long MAX_ROUTE_LIFETIME = 9*60*1000;
	private static final long NEXT_ROUTE_UPDATE_INTERVAL = 30000;	//NEXT_ROUTE_UPDATE_INTERVAL in ms
	private static final long DATA_COLLECTION_TIME = 10*1000;		//10 sec
	private static final long DATA_COLLECTION_EXPIRATION_TIME = 5*60*1000; //5 min
	
	private LinkedList<RouteTableEntry> routingTable;
	private LinkedList<Message> waitingForRouteBuffer;
	private LinkedList<TransmittedRREQ> transmittedRREQs;
	private LinkedList<TransmittedRREQ> recivedRREQs;
	private int sequenceNumber;
	private int rreqID;
	private int numberRecivedRREPdMsg;
	private int numberTransmittedRREQMsg;
	private int numberTransmittedRREPMsg;

	private int nextPayloadMsgDestination;
	
	private long helloInvervalCounter;
	private long nextRouteUpdateTime;
	private boolean sendBroadcastMessageInCurrentHelloInterval;
	
	private LinkedList<DataCollectionTimer> dataCollectionTimer;

	/**
	 * Creates network node with given id
	 * @param id	The ID from the new network node
	 */
	public AodvmNetworkNode(int id) {
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
		
		numberTransmittedRREQMsg = 0;
		numberTransmittedRREPMsg = 0;
		numberTransmittedPayloadMsg = 0;
		
		dataCollectionTimer = new LinkedList<DataCollectionTimer>();
		
		this.nextPayloadMsgDestination = this.id;
	}

	/**
	 * Performs time dependent tasks. E.g. send RREP-Message after dataCollectionTimer is expired
	 */
	protected void performeTimeDependentTasks(long executionTime) {
	
		validateRouteLifetime(executionTime);
		
		
		if(simulator.getNetworkLifetime() % 1000 == 0){
			//do this one time per second
			
			LinkedList<DataCollectionTimer> toDeleteTimer = new LinkedList<>();
			
			
			for(DataCollectionTimer timer : dataCollectionTimer){
				if(timer.isRrepSend() && timer.getExparationTime() + DATA_COLLECTION_EXPIRATION_TIME <= simulator.getNetworkLifetime()){
					toDeleteTimer.add(timer);
				}

				if(timer.getExparationTime() <= simulator.getNetworkLifetime() && !timer.isRrepSend()){
					//Generate and send RREP

					RREP rrep = new RREP();
					
					RREQ msg = timer.getRreqWithMaxAlpha();
					
					rrep.setDestination_IP_Adress(msg.getDestination_IP_Addresse());
					rrep.setOrignator_IP_Adress(msg.getOriginator_IP_Adress());
					

					this.sequenceNumber++; 	//Increment sequence Number
					rrep.setDestination_Sequence_Number(this.sequenceNumber);
					rrep.setHop_Count(msg.getHop_Count());
					rrep.setMin_RE(msg.getMin_RE());

					rrep.setDestinationID(msg.getSenderID());
					rrep.setSenderID(this.id);
					rrep.setTimeToLive(msg.getHop_Count() *2);
					rrep.setLifetime(MAX_ROUTE_LIFETIME); 
					
					this.addNodeAsPrecursor(rrep.getDestinationID(), rrep.getDestination_IP_Adress());
					
					sendMsg(rrep);
					timer.setRrepSend(true);
				}
			}
			
			for(DataCollectionTimer timer : toDeleteTimer){
				dataCollectionTimer.remove(timer);
			}
		}
		
	}

	/**
	 * Check if neighbors on active used routes are alive
	 * If neighbor is down, send RERR message
	 *  
	 * @param executionTime
	 */
	private void validateRouteLifetime(long executionTime){
		if(this.nextRouteUpdateTime <= simulator.getNetworkLifetime()){
			for(RouteTableEntry routeTableEntry: routingTable){
				routeTableEntry.isRouteValid(simulator.getNetworkLifetime());
			}
			
			this.nextRouteUpdateTime = simulator.getNetworkLifetime() + NEXT_ROUTE_UPDATE_INTERVAL;
		}
	}
	
	/**
	 * Determine the message format and call the processing process
	 */
	public void processRecivedMessage() {

		while (inputBuffer.size() > 0) {
			Message recivedMsg = inputBuffer.removeFirst();
			if (recivedMsg instanceof RREQ) {
				reciveRREQ((RREQ) recivedMsg);
			} else {
				if (recivedMsg instanceof RREP) {
					reciveRREP((RREP) recivedMsg);
				} else {
					if (recivedMsg instanceof PayloadMessage) {
						recivePayloadMessage((PayloadMessage) recivedMsg);
					} else {
						if (recivedMsg instanceof CollisionMessage) {
							
						}
					}
				}
			}
		}

	}

	/**
	 * Process an RREQ msg
	 * @param msg
	 */
	private void reciveRREQ(RREQ msg){
		//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": Recive RREQ from Node " + msg.getSenderID() + ". DestinationNode: " + msg.getDestination_IP_Addresse());
		
		//search for already received RREQ with same Originator IP Address and RREQ ID
		for(TransmittedRREQ rreq: recivedRREQs){
			if(rreq.getOriginatorIpAdress() == msg.getOriginator_IP_Adress() && rreq.getRreqId() == msg.getRREQ_ID()){
				//RREQ already received and processed
				return;
			}
		}
		
		//Update routingtable for rreq originator
		if(msg.getOriginator_IP_Adress() != this.id){

			updateRouteTable(msg.getOriginator_IP_Adress(), msg.getOriginator_Sequence_Number(), msg.getHop_Count()+1, msg.getSenderID(), MAX_ROUTE_LIFETIME);
	
			
			if(msg.getDestination_IP_Addresse() == this.id){
				//System.out.println(simulator.getNetworkLifetime() + ": Node " + this.id + ": This node ist destination. Received RREQ from node " + msg.getSenderID());
				
				//Start data collection timer
				this.startDataCollectionTimer(msg.getOriginator_IP_Adress(), msg.getRREQ_ID());
				
				//Add rreq to dataCollectionTimer
				for(DataCollectionTimer dataCollectionTimer : this.dataCollectionTimer){
					if(dataCollectionTimer.getOriginatorID() == msg.getOriginator_IP_Adress()){
						dataCollectionTimer.addReceivedRREQ(msg);
						break;
					}
				}
				
			}
			else{
				
				//RREQ not received and processed jet
				recivedRREQs.add(new TransmittedRREQ(msg.getOriginator_IP_Adress(), msg.getRREQ_ID()));
				
				
				//forward RREQ
				if(msg.getTimeToLive() > 1){
					//create copy of RREQ
					RREQ rreqCopy = (RREQ) msg.clone();
					rreqCopy.setSenderID(this.id);
					rreqCopy.setHop_Count(msg.getHop_Count()+1);
					rreqCopy.setTimeToLive(msg.getTimeToLive() -1);
					rreqCopy.setDestinationID(-1);
					
					double batterieLoad = ((double)availableEnery)*1.0 / ((double)startEnergy)*1.0 * 100.0;
					
					rreqCopy.setMin_RE((int)batterieLoad);
					
					sendMsg(rreqCopy);
					//this.sendBroadcastMessageInCurrentHelloInterval = true;
				}
			}
			
			/*
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
				rrep.setLifetime(MAX_ROUTE_LIFETIME); //Lifetime of Route = 9 minutes
				
				this.addNodeAsPrecursor(rrep.getDestinationID(), rrep.getDestination_IP_Adress());
				
				sendMsg(rrep);
				
			}
			*/
		
		}		
		
	}
	
	/**
	 * Starts the data collection timer when received a rreq for this node for the first time
	 * Data collection timer has to be deleted after they have been expired
	 * @param originator_Adress		Address from node that has started the route discovery process
	 * @param rreq_ID				ID of the RREQ message
	 */
	private void startDataCollectionTimer(int originator_Adress, int rreq_ID) {
		for(DataCollectionTimer dataCollectionTimer : this.dataCollectionTimer){
			if(dataCollectionTimer.getOriginatorID() == originator_Adress){
				//data collection timer already installed
				return;
			}
		}
		
		//install data collection timer
		DataCollectionTimer timer = new DataCollectionTimer();
		timer.setOriginatorID(originator_Adress);
		timer.setExprationTime(this.simulator.getNetworkLifetime() + DATA_COLLECTION_TIME);
		this.dataCollectionTimer.add(timer);
		
	}

	/**
	 * Adds a node as precursor
	 * @param nodeID	
	 * @param destination
	 */
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
	
	/**
	 * Set route lifetime for the given destination to maximum value
	 * @param destination
	 */
	private void updateRouteLifetime(int destination){
		for(RouteTableEntry route: routingTable){
			if(route.getDestinationAdress() == destination){
				if(route.isValid()){
					route.setExpirationTime(simulator.getNetworkLifetime() + MAX_ROUTE_LIFETIME);
				}
			}
		}
	}
	
	/**
	 * Update the route table with the given parameter
	 * @param destination
	 * @param sequenceNumber
	 * @param hopCount
	 * @param nextHop
	 * @param maxRouteLifetime
	 * @return
	 */
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
					route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);

					routingTableUpdated = true;
				}
				
				if((route.getDestinationSequenceNumber() <= sequenceNumber) && (route.getExpirationTime() > simulator.getNetworkLifetime())){
					route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);
				}
				
				if(!route.isValid()){
					route.setDestinationSequenceNumber(sequenceNumber);
					route.setHopCount(hopCount);
					route.setNextHop(nextHop);
					route.setValid(true);
					route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);

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
			route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);

			routingTableUpdated = true;
			routingTable.add(route);
		}
		
		return routingTableUpdated;
	}
	
	/**
	 * Search next hop to given destination in routing table
	 * @param destination
	 * @return
	 */
	private int getNextHopToDestination(int destination){
		for(RouteTableEntry route: routingTable){
			if(route.getDestinationAdress() == destination && route.isValid()){
				return route.getNextHop();
			}
		}
		return -1;
	}
	
	
	public RouteTableEntry getRouteTableEntry(int destination){
		for(RouteTableEntry route: routingTable){
			if(route.getDestinationAdress() == destination && route.isValid()){
				return route;
			}
		}
		return null;
	}
	
	/**
	 * Process an RREP msg
	 * @param msg
	 */
	private void reciveRREP(RREP msg){
		
		if(msg.getDestinationID() == this.id){
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
						//this.numberTransmittedPayloadMsg++;
						msgForWhichRouteWasFound.add(waitingMsg);
					}
				}
			}
			//Delete Msg from waiting list
			for(Message deleteMsg: msgForWhichRouteWasFound){
				waitingForRouteBuffer.remove(deleteMsg);
			}
		}
	}

	
	/**
	 * Process an PayloadMessage msg
	 * @param msg
	 */
	private void recivePayloadMessage(PayloadMessage msg){
		//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": recive payloadMessage from node " + msg.getSenderID() + " to node " + msg.getPayloadDestinationAdress());
		
		
		if(msg.getDestinationID() == this.id){
			updateRouteLifetime(msg.getPayloadSourceAdress());

			if(msg.getPayloadDestinationAdress() != this.id){
				//forward message to next hop
				//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": forward payloadMessage from node " + msg.getPayloadSourceAdress() + " to node " + msg.getPayloadDestinationAdress());
				this.sendMsg(msg);
			}
			else{
				long transmissionTime = simulator.getNetworkLifetime() - msg.getStartTransmissionTime();
				//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": message from node " + msg.getPayloadSourceAdress() + " recived. Transmissiontime: " + transmissionTime);
				msg.setEndTransmissionTime(simulator.getNetworkLifetime());
				this.lastRecivedPayloadMessage = msg;
				numberRecivedPayloadMsg++;
			}
		}
	}
	
	/**
	 * send the given Message
	 */
	@Override
	public void sendMsg(Message msg) {
		
			if(msg instanceof RREP){
				//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": send RREP. DestinationNode: " + msg.getDestinationID());
				outputBuffer.add(msg);
				this.numberTransmittedRREPMsg++;
			}
			
			if(msg instanceof RREQ){
				//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": send RREQ. DestinationNode: " + ((RREQ)msg).getDestination_IP_Addresse());
				outputBuffer.add(msg);
				this.numberTransmittedRREQMsg++;
			}
			
			if(msg instanceof PayloadMessage){
				//Search valid route to destination
				RouteTableEntry routeTableEntry = null;
				msg.setSenderID(this.id);
				
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
					//this.numberTransmittedPayloadMsg++;
					//System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": send PayloadMsg. DestinationNode: " + msg.getDestinationID());
				}else{
					//check if Route Discovery process is already started
					boolean routeDiscoveryProcessStarted = false;
					for(Message tmpMsg: waitingForRouteBuffer){
						if(tmpMsg instanceof PayloadMessage){
							if(((PayloadMessage)tmpMsg).getPayloadDestinationAdress() == ((PayloadMessage)msg).getPayloadDestinationAdress()){
								routeDiscoveryProcessStarted = true;
								break;
							}
						}
					}
					
					if(routeDiscoveryProcessStarted){
						waitingForRouteBuffer.add(msg);
					}
					else{
						startRouteDiscoveryProcess(((PayloadMessage)msg).getPayloadDestinationAdress());
						waitingForRouteBuffer.add(msg);
					}
				}
				
			}
	}
	
	/**
	 * Start a route discovery process for the given destination
	 * @param destinationID
	 */
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
		rreq.setDestinationID(-1);
		
		
		//Add RREQ to trransmittedRREQ list
		this.transmittedRREQs.add(new TransmittedRREQ(this.id, this.rreqID));
		
		//Inkrement RREQ_ID
		this.rreqID++;
		
		
		
		//send RREQ
		this.sendMsg(rreq);
		//outputBuffer.add(rreq);
		//this.numberTransmittedRREQMsg++;
		
	}

	/**
	 * Send a message from this node
	 */
	public void startSendingProcess(PayloadMessage tmpMsg) {
		
		//System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": start transmission process, msg destination: " + tmpMsg.getPayloadDestinationAdress() );
		
		tmpMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
		tmpMsg.setSenderID(this.id);
		tmpMsg.setPayloadSourceAdress(this.id);
		
		this.sendMsg(tmpMsg);
		
	}

	/**
	 * 
	 * @return number of received RREP messages
	 */
	public int getNumberRecivedRREPdMsg() {
		return numberRecivedRREPdMsg;
	}

	/**
	 * 
	 * @return number of transmitted RREQ messages
	 */
	public int getNumberTransmittedRREQMsg() {
		return numberTransmittedRREQMsg;
	}

	/**
	 * 
	 * @return number of transmitted RREP messages
	 */
	public int getNumberTransmittedRREPMsg() {
		return numberTransmittedRREPMsg;
	}

	/**
	 * Generates a transmission every t seconds
	 * @param seconds
	 * @param nodeExecutionTime
	 * @param networkSize
	 */
	public void generateTransmissionEveryTSecondsChangingDestination(int seconds, int nodeExecutionTime, int networkSize, int payloadSize){
		if(this.elapsedTimeSinceLastGenerationOfTransmission/1000 >= seconds){
			this.elapsedTimeSinceLastGenerationOfTransmission = 0L; //Reset timer

			// find random destination
			//int randomDestination = (int) (Math.random() * networkSize);

			//calculate destination node
			int destination = this.nextPayloadMsgDestination++;
			if(destination == this.id){
				destination++;
				this.nextPayloadMsgDestination++;
			}
			
			// set message payload as simulation time 
			long networkLivetime = simulator.getNetworkLifetime();
			char dataToSend[] = { (char)((networkLivetime >> 8) & 0xFF), (char)((networkLivetime >> 16) & 0xFF), (char)((networkLivetime >> 24) & 0xFF), (char)((networkLivetime >> 32) & 0xFF), (char)((networkLivetime >> 40) & 0xFF), (char)((networkLivetime >> 48) & 0xFF), (char)((networkLivetime >> 56) & 0xFF), (char)((networkLivetime >> 64) & 0xFF) };
			
			PayloadMessage tmpMsg = new PayloadMessage(id, destination, dataToSend);
			tmpMsg.setPayloadHash(networkLivetime);
			tmpMsg.setPayloadSize(payloadSize);
			
			this.startSendingProcess(tmpMsg);
			
		}
		
		this.elapsedTimeSinceLastGenerationOfTransmission += nodeExecutionTime;
	}
	

}
