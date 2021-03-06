package AODV;

import java.util.LinkedList;

import Simulator.CollisionMessage;
import Simulator.Message;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;

/**
 * Specializes the NetworkNode class for AODV routing scheme
 * 
 * @author Christoph Bergmann
 */

public class AodvNetworkNode extends NetworkNode {

	private static final long HELLO_INTERVAL = 1*30*1000; // HELLO_INTERVAL in
															// ms
	private static final long ALLOWED_HELLO_LOSS = 2;
	public static final long MAX_ROUTE_LIFETIME = 9 * 60 * 1000;
	private static final long NEXT_ROUTE_UPDATE_INTERVAL = 30000; // NEXT_ROUTE_UPDATE_INTERVAL
																	// in ms

	private LinkedList<RouteTableEntry> routingTable;
	private LinkedList<Message> waitingForRouteBuffer;
	private LinkedList<TransmittedRREQ> transmittedRREQs;
	private LinkedList<TransmittedRREQ> recivedRREQs;
	private LinkedList<RERR> receivedRERRs;
	private int sequenceNumber;
	private int rreqID;
	private int numberRecivedRREPdMsg;
	private int numberTransmittedRREQMsg;
	private int numberTransmittedRREPMsg;

	private boolean nodeIsUsedForActiveRoute;
	private long timeOfLastPayloadMessage;

	private int nextPayloadMsgDestination;

	private long helloInvervalCounter;
	private long nextRouteUpdateTime;
	private boolean sendBroadcastMessageInCurrentHelloInterval;

	/**
	 * Creates network node with given id
	 * @param id	The ID from the new network node
	 */
	public AodvNetworkNode(int id) {
		super(id);
		this.routingTable = new LinkedList<RouteTableEntry>();
		this.waitingForRouteBuffer = new LinkedList<Message>();
		this.transmittedRREQs = new LinkedList<TransmittedRREQ>();
		this.recivedRREQs = new LinkedList<TransmittedRREQ>();
		this.receivedRERRs = new LinkedList<RERR>();
		this.sequenceNumber = 1;
		this.rreqID = 1;
		this.numberRecivedRREPdMsg = 0;

		this.helloInvervalCounter = 0L;
		this.sendBroadcastMessageInCurrentHelloInterval = false;

		numberTransmittedRREQMsg = 0;
		numberTransmittedRREPMsg = 0;
		numberTransmittedPayloadMsg = 0;

		this.nextPayloadMsgDestination = this.id;

		nodeIsUsedForActiveRoute = false;
		timeOfLastPayloadMessage = -1;
	}

	/**
	 * Performs time dependent tasks. E.g. generating HELLO-Messages
	 */
	@Override
	protected void performeTimeDependentTasks(long executionTime) {

		// Hello-Messages
		this.helloInvervalCounter += executionTime;
		if (helloInvervalCounter >= HELLO_INTERVAL) {
			if (!sendBroadcastMessageInCurrentHelloInterval) {
				// Generate and send hello message
				if(nodeIsUsedForActiveRoute){
					generateHelloMessage();
				}
			}
			// reset helloInvervalCounter and msg send flag
			this.helloInvervalCounter = 0L;
			this.sendBroadcastMessageInCurrentHelloInterval = false;
		}

		// Check if node is on activeRoute
		if (timeOfLastPayloadMessage != -1 && timeOfLastPayloadMessage > simulator.getNetworkLifetime() - 600000) {
			nodeIsUsedForActiveRoute = true;
		} else {
			nodeIsUsedForActiveRoute = false;
		}

		// Check if all routes are valid
		validateRouteLifetime(executionTime);

	}

	/**
	 * Generates a HELLO-Message if node has at least one precursor
	 */
	private void generateHelloMessage() {
		boolean nodeHasPrecursor = false;

		
		// Search for precursor
		for (RouteTableEntry routeTableEntry : routingTable) {
			if ((routeTableEntry.getPrecursorList().size() > 0) && routeTableEntry.isValid()) {
				nodeHasPrecursor = true;
				break;
			}
		}

		if (nodeHasPrecursor) {
			// Node has at least one precursor -> generate and send hello
			// message
			RREP helloMsg = new RREP();
			helloMsg.setDestination_IP_Adress(-1);
			helloMsg.setDestination_Sequence_Number(this.sequenceNumber);
			helloMsg.setHop_Count(0);
			helloMsg.setTimeToLive(1);

			this.sendMsg(helloMsg);
			this.numberTransmittedRREPMsg++;
		}
	}

	/**
	 * Check if neighbors on active used routes are alive
	 * If neighbor is down, send RERR message
	 *  
	 * @param executionTime
	 */
	private void validateRouteLifetime(long executionTime) {
		if (this.nextRouteUpdateTime <= simulator.getNetworkLifetime()) {
			for (RouteTableEntry routeTableEntry : routingTable) {
				routeTableEntry.isRouteValid(simulator.getNetworkLifetime());

				if (routeTableEntry.getHopCount() == 1) {
					// Check if node has received at least one message in last
					// ALLOWED_HELLO_LOSS interval from neighbor node
					if (routeTableEntry.getTimeOfLastMessage() < (simulator.getNetworkLifetime()
							- ALLOWED_HELLO_LOSS * HELLO_INTERVAL)) {
						// Neighbor is down
						routeTableEntry.setValid(false);

						if (nodeIsUsedForActiveRoute) {
							RERR rerr = new RERR();
							rerr.addUnreachableDestinationIpAdresses(routeTableEntry.getDestinationAdress());
							rerr.addUnreachableDestinationSequenceNumbers(
									routeTableEntry.getDestinationSequenceNumber() + 1);
							rerr.setDestinationID(-1);
							sendMsg(rerr);
						}
					}
				}
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
					if (recivedMsg instanceof RREP_ACK) {
						reciveRREP_ACK((RREP_ACK) recivedMsg);
					} else {
						if (recivedMsg instanceof RERR) {
							reciveRERR((RERR) recivedMsg);
						} else {
							if (recivedMsg instanceof PayloadMessage) {
								recivePayloadMessage((PayloadMessage) recivedMsg);
							} else {
								System.out.println("Error - Received message with unkown type");
								System.exit(-1);
							}
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
	private void reciveRREQ(RREQ msg) {
		// System.out.println(""+simulator.getNetworkLifetime() +": Node "+
		// this.id + ": Recive RREQ from Node " + msg.getSenderID() + ".
		// DestinationNode: " + msg.getDestination_IP_Addresse());
		// Update route table entry for rreq transmitter node
		updateRouteTable(msg.getSenderID(), -1, 1, msg.getSenderID(), MAX_ROUTE_LIFETIME);

		// search for already received RREQ with same Originator IP Address and
		// RREQ ID
		for (TransmittedRREQ rreq : recivedRREQs) {
			if (rreq.getOriginatorIpAdress() == msg.getOriginator_IP_Adress() && rreq.getRreqId() == msg.getRREQ_ID()) {
				// RREQ already received and processed
				return;
			}
		}
		// RREQ not received and processed jet
		recivedRREQs.add(new TransmittedRREQ(msg.getOriginator_IP_Adress(), msg.getRREQ_ID()));

		// Update routingtable for rreq originator
		if (msg.getOriginator_IP_Adress() != this.id) {
			updateRouteTable(msg.getOriginator_IP_Adress(), msg.getOriginator_Sequence_Number(), msg.getHop_Count() + 1,
					msg.getSenderID(), MAX_ROUTE_LIFETIME);

			// Search for route to destination
			boolean createRREP = false;
			RouteTableEntry routeToDestination = null;
			if (msg.getDestination_IP_Addresse() == this.id) {
				// System.out.println("Node " + this.id + ": This node ist
				// destination");
				createRREP = true;
			} else {
				// search for route in routing table
				for (RouteTableEntry route : routingTable) {
					if (msg.getDestination_IP_Addresse() == route.getDestinationAdress()
							&& msg.getDestination_Sequence_Number() <= route.getDestinationSequenceNumber()
							&& !msg.isDestinationOnly()) {
						if (route.isValid()) {
							// System.out.println("Node " + this.id + ": Route
							// for destination found");
							routeToDestination = route;
							createRREP = true;
						}
					}
				}
			}
			if (createRREP) {
				// System.out.println("" +simulator.getNetworkLifetime() +":
				// Node " + this.id + ": Create RREP");
				RREP rrep = new RREP();
				rrep.setDestination_IP_Adress(msg.getDestination_IP_Addresse());
				rrep.setOrignator_IP_Adress(msg.getOriginator_IP_Adress());

				if (routeToDestination == null) {
					// Route Reply Generation by the Destination
					this.sequenceNumber += 2; // Increment sequence Number
					rrep.setDestination_Sequence_Number(this.sequenceNumber);
					rrep.setHop_Count(0);
					// TODO: set route lifeitime
					// rrep.setLifetime(lifetime);
				} else {
					rrep.setDestination_Sequence_Number(routeToDestination.getDestinationSequenceNumber());
					rrep.setHop_Count(routeToDestination.getHopCount());
					// TODO: set route lifeitime
					// rrep.setLifetime(lifetime);
				}
				rrep.setDestinationID(getNextHopToDestination(msg.getOriginator_IP_Adress()));
				rrep.setSenderID(this.id);
				rrep.setTimeToLive(msg.getHop_Count() + 1);
				rrep.setLifetime(MAX_ROUTE_LIFETIME); // Lifetime of Route = 9
														// minutes

				this.addNodeAsPrecursor(rrep.getDestinationID(), rrep.getDestination_IP_Adress());

				sendMsg(rrep);

			} else {

				if (msg.getTimeToLive() > 1) {
					// create copy of RREQ
					RREQ rreqCopy = (RREQ) msg.clone();
					rreqCopy.setSenderID(this.id);
					rreqCopy.setHop_Count(msg.getHop_Count() + 1);
					rreqCopy.setTimeToLive(msg.getTimeToLive() - 1);
					rreqCopy.setDestinationID(-1);

					sendMsg(rreqCopy);
					this.sendBroadcastMessageInCurrentHelloInterval = true;
				}
			}

		}

	}

	/** 
	 * Add a node as a precursor
	 * @param nodeID
	 * @param destination
	 */
	private void addNodeAsPrecursor(int nodeID, int destination) {
		LinkedList<Integer> precursorList = null;
		for (RouteTableEntry tableEntry : routingTable) {
			if (tableEntry.getDestinationAdress() == destination) {
				precursorList = tableEntry.getPrecursorList();

				if (!precursorList.contains(nodeID)) {
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
	private void updateRouteLifetime(int destination) {
		for (RouteTableEntry route : routingTable) {
			if (route.getDestinationAdress() == destination) {
				if (route.isValid()) {
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
	private boolean updateRouteTable(int destination, int sequenceNumber, int hopCount, int nextHop,
			long maxRouteLifetime) {
		boolean routeAlreadyExists = false;
		boolean routingTableUpdated = false;
		for (RouteTableEntry route : routingTable) {
			if (route.getDestinationAdress() == destination) {
				routeAlreadyExists = true;
				if ((route.getDestinationSequenceNumber() < sequenceNumber)
						|| ((route.getDestinationSequenceNumber() == sequenceNumber)
								&& (route.getHopCount() > hopCount))) {
					route.setDestinationSequenceNumber(sequenceNumber);
					route.setHopCount(hopCount);
					route.setNextHop(nextHop);
					route.setValid(true);
					route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);

					routingTableUpdated = true;
				}

				if ((route.getDestinationSequenceNumber() <= sequenceNumber)
						&& (route.getExpirationTime() > simulator.getNetworkLifetime())) {
					route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);
				}

				if (!route.isValid()) {
					route.setDestinationSequenceNumber(sequenceNumber);
					route.setHopCount(hopCount);
					route.setNextHop(nextHop);
					route.setValid(true);
					route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);

					routingTableUpdated = true;
				}

				if (route.getHopCount() == 1) {
					route.setTimeOfLastMessage(simulator.getNetworkLifetime());
				}

			}
		}

		if (!routeAlreadyExists) {
			// create new route
			RouteTableEntry route = new RouteTableEntry();
			route.setDestinationAdress(destination);
			route.setDestinationSequenceNumber(sequenceNumber);
			route.setHopCount(hopCount);
			route.setNextHop(nextHop);
			route.setValid(true);
			route.setExpirationTime(simulator.getNetworkLifetime() + maxRouteLifetime);

			routingTableUpdated = true;

			if (route.getHopCount() == 1) {
				route.setTimeOfLastMessage(simulator.getNetworkLifetime());
			}

			routingTable.add(route);
		}

		return routingTableUpdated;
	}

	/**
	 * Search next hop to given destination in routing table
	 * @param destination
	 * @return
	 */
	private int getNextHopToDestination(int destination) {
		for (RouteTableEntry route : routingTable) {
			if (route.getDestinationAdress() == destination && route.isValid()) {
				return route.getNextHop();
			}
		}
		return -1;
	}

	/**
	 * Process an RREP msg
	 * @param msg
	 */
	private void reciveRREP(RREP msg) {
		// System.out.println(""+simulator.getNetworkLifetime() +": Node "+
		// this.id + ": Recive RREP from Node " + msg.getSenderID() + ".
		// DestinationNode: " + msg.getDestination_IP_Adress() + "; HopCount: "
		// + msg.getHop_Count());
		this.numberRecivedRREPdMsg++;
		// Update routing table for previous hop
		updateRouteTable(msg.getSenderID(), -1, 1, msg.getSenderID(), MAX_ROUTE_LIFETIME);

		int nextHopCount = msg.getHop_Count() + 1;

		// Update routing table for destination node
		if (updateRouteTable(msg.getDestination_IP_Adress(), msg.getDestination_Sequence_Number(), nextHopCount,
				msg.getSenderID(), msg.getLifetime())) {
			// Forward RREP if routing table was updated

			// Forward RREP-Message if TTL > 0
			if (msg.getOrignator_IP_Adress() != this.id) {
				if (msg.getDestinationID() == this.id) {
					if (msg.getTimeToLive() > 1) {
						// foward RREP to originator node
						RREP copyRREP = msg.clone();
						copyRREP.setSenderID(this.id);
						copyRREP.setDestinationID(getNextHopToDestination(msg.getOrignator_IP_Adress()));
						copyRREP.setHop_Count(nextHopCount);
						copyRREP.setTimeToLive(msg.getTimeToLive() - 1);
						sendMsg(copyRREP);
						// System.out.println(""+simulator.getNetworkLifetime()
						// +": Node "+ this.id + ": forward RREP");
						this.addNodeAsPrecursor(copyRREP.getDestinationID(), copyRREP.getDestination_IP_Adress());
					}
				}
			}
		}

		// Search for messages that wait for a route for the destination
		LinkedList<Message> msgForWhichRouteWasFound = new LinkedList<Message>();
		for (Message waitingMsg : waitingForRouteBuffer) {
			if (waitingMsg instanceof PayloadMessage) {
				if (((PayloadMessage) waitingMsg).getPayloadDestinationAdress() == msg.getDestination_IP_Adress()) {
					// simulator.resetTransmissionUnitFromAllNodes();

					waitingMsg.setDestinationID(getNextHopToDestination(msg.getDestination_IP_Adress()));
					// System.out.println(""+simulator.getNetworkLifetime() +":
					// Node " + this.id + ": Send payload message");
					// aktualisiere Sendezeit
					waitingMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
					outputBuffer.add(waitingMsg);
					//this.numberTransmittedPayloadMsg++;
					msgForWhichRouteWasFound.add(waitingMsg);
				}
			}
		}
		// Delete Msg from waiting list
		for (Message deleteMsg : msgForWhichRouteWasFound) {
			waitingForRouteBuffer.remove(deleteMsg);
		}
	}

	/**
	 * Process an RREP_ACK msg
	 * @param msg
	 */
	private void reciveRREP_ACK(RREP_ACK msg) {

	}

	/**
	 * Process an RERR msg
	 * @param msg
	 */
	private void reciveRERR(RERR msg) {

		// check if RERR was already received
		for (RERR rerr : receivedRERRs) {
			if ((rerr.getUnreachableDestinationIpAdresses().getFirst() == msg.getUnreachableDestinationIpAdresses()
					.getFirst())
					&& (rerr.getUnreachableDestinationSequenceNumbers().getFirst() == msg
							.getUnreachableDestinationSequenceNumbers().getFirst())) {
				// RERR already received
				return;
			}
		}

		receivedRERRs.add(msg);
		for (int unreachableNodeAddress : msg.getUnreachableDestinationIpAdresses()) {

			for (RouteTableEntry routeTableEntry : routingTable) {
				if (routeTableEntry.getDestinationAdress() == unreachableNodeAddress) {
					routeTableEntry.setValid(false);
					routeTableEntry.setDestinationAdress(routeTableEntry.getDestinationSequenceNumber() + 1);
				}
			}

		}

		// forward message
		sendMsg(msg);

	}

	/**
	 * Process an PayloadMessage msg
	 * @param msg
	 */
	private void recivePayloadMessage(PayloadMessage msg) {
		// System.out.println(""+simulator.getNetworkLifetime() +": Node "+
		// this.id + ": recive payloadMessage from node " +
		// msg.getPayloadSourceAdress() + " to node " +
		// msg.getPayloadDestinationAdress());

		if (msg.getDestinationID() == this.id) {
			updateRouteLifetime(msg.getPayloadSourceAdress());
			timeOfLastPayloadMessage = simulator.getNetworkLifetime();

			if (msg.getPayloadDestinationAdress() != this.id) {
				// forward message to next hop
				// System.out.println(""+simulator.getNetworkLifetime() +": Node
				// "+ this.id + ": forward payloadMessage from node " +
				// msg.getPayloadSourceAdress() + " to node " +
				// msg.getPayloadDestinationAdress());
				this.sendMsg(msg);
			} else {
				long transmissionTime = simulator.getNetworkLifetime() - msg.getStartTransmissionTime();
				// System.out.println(""+simulator.getNetworkLifetime() +": Node
				// " + this.id + ": message from node " +
				// msg.getPayloadSourceAdress() + " recived. Transmissiontime: "
				// + transmissionTime);
				msg.setEndTransmissionTime(simulator.getNetworkLifetime());
				this.lastRecivedPayloadMessage = msg;
				numberRecivedPayloadMsg++;
			}
		}
	}

	/**
	 * send the given Message
	 */
	public void sendMsg(Message msg) {

		if (msg instanceof RREP) {
			// System.out.println(""+simulator.getNetworkLifetime() +": Node "+
			// this.id + ": send RREP. DestinationNode: " +
			// msg.getDestinationID());
			outputBuffer.add(msg);
			this.numberTransmittedRREPMsg++;
		}

		if (msg instanceof RREQ) {
			// System.out.println(""+simulator.getNetworkLifetime() +": Node "+
			// this.id + ": send RREQ. DestinationNode: " +
			// ((RREQ)msg).getDestination_IP_Addresse());
			outputBuffer.add(msg);
			this.numberTransmittedRREQMsg++;
		}

		if (msg instanceof PayloadMessage) {
			// Search valid route to destination
			RouteTableEntry routeTableEntry = null;

			for (RouteTableEntry tempEntry : routingTable) {
				if (tempEntry.getDestinationAdress() == ((PayloadMessage) msg).getPayloadDestinationAdress()) {
					routeTableEntry = tempEntry;
					break;
				}
			}

			// check if next hop is alive
			/*
			 * if(routeTableEntry != null){ //check if next hop selection is
			 * alive if(!graph.getNetworkNodes()[routeTableEntry.getNextHop()].
			 * isNodeAlive()){ routeTableEntry.setValid(false); } }
			 */

			if (routeTableEntry != null && routeTableEntry.isValid()) {
				// System.out.println(""+simulator.getNetworkLifetime() +": Node
				// " + this.id + ": Send payload message");
				msg.setDestinationID(getNextHopToDestination(((PayloadMessage) msg).getPayloadDestinationAdress()));
				routeTableEntry.setExpirationTime(simulator.getNetworkLifetime() + MAX_ROUTE_LIFETIME);
				outputBuffer.add(msg);
				//this.numberTransmittedPayloadMsg++;
				// System.out.println(""+simulator.getNetworkLifetime() +": Node
				// "+ this.id + ": send PayloadMsg. DestinationNode: " +
				// msg.getDestinationID());
			} else {
				if (routeTableEntry != null && !routeTableEntry.isValid() && ((PayloadMessage)msg).getPayloadSourceAdress() != this.id) {
					// Route is no longer valid
					RERR rerr = new RERR();

					rerr.addUnreachableDestinationIpAdresses(((PayloadMessage) msg).getPayloadDestinationAdress());
					rerr.addUnreachableDestinationSequenceNumbers(routeTableEntry.getDestinationSequenceNumber() + 1);
					rerr.setDestinationID(-1);

					sendMsg(rerr);

				} else {
					// check if Route Discovery process is already started
					boolean routeDiscoveryProcessStarted = false;
					for (Message tmpMsg : waitingForRouteBuffer) {
						if (tmpMsg instanceof PayloadMessage) {
							if (((PayloadMessage) tmpMsg).getPayloadDestinationAdress() == ((PayloadMessage) msg)
									.getPayloadDestinationAdress()) {
								routeDiscoveryProcessStarted = true;
								break;
							}
						}
					}

					if (routeDiscoveryProcessStarted) {
						waitingForRouteBuffer.add(msg);
					} else {
						startRouteDiscoveryProcess(((PayloadMessage) msg).getPayloadDestinationAdress());
						waitingForRouteBuffer.add(msg);
					}
				}
			}

		}
	}

	/**
	 * Start a route discovery process for the given destination
	 * @param destinationID
	 */
	private void startRouteDiscoveryProcess(int destinationID) {
		// System.out.println(""+simulator.getNetworkLifetime() +": Node " +
		// this.id + ": Start route discovery process");

		RREQ rreq = new RREQ(this.id, this.sequenceNumber, destinationID, 0, this.rreqID);

		// Search routeTableEntry for destination
		RouteTableEntry routeTableEntry = null;
		for (RouteTableEntry tempEntry : routingTable) {
			if (tempEntry.getDestinationAdress() == destinationID) {
				routeTableEntry = tempEntry;
				break;
			}
		}

		if (routeTableEntry == null) {
			// Set unknown sequence number flag
			rreq.setU(1);
		} else {
			// Set last known sequence number
			rreq.setDestination_Sequence_Number(routeTableEntry.getDestinationSequenceNumber());
		}

		rreq.setDestinationOnly(false);
		rreq.setTimeToLive(1000);
		rreq.setSenderID(this.id);
		rreq.setDestinationID(-1);

		// Add RREQ to trransmittedRREQ list
		this.transmittedRREQs.add(new TransmittedRREQ(this.id, this.rreqID));

		// Inkrement RREQ_ID
		this.rreqID++;

		// send RREQ
		this.sendMsg(rreq);
		// outputBuffer.add(rreq);
		// this.numberTransmittedRREQMsg++;

	}

	/**
	 * Send a message from this node
	 */
	public void startSendingProcess(PayloadMessage tmpMsg) {

		// System.out.println(""+simulator.getNetworkLifetime() +": Node " +
		// this.id + ": start transmission process, msg destination: " +
		// tmpMsg.getPayloadDestinationAdress() );

		tmpMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
		tmpMsg.setSenderID(this.id);
		tmpMsg.setPayloadSourceAdress(this.id);

		this.sendMsg(tmpMsg);

	}

	
	public void reciveMsg(Message msg) {
		// currentlyTransmittingAMessage = true;
		if (incommingMsg == null) {
			incommingMsg = msg;
		} else {
			// collision
			// System.out.println("Collision detected at Node " + this.id);
			graph.addCollision();
			if ((msg instanceof RREP) && msg.getDestinationID() == this.id) {
				// System.out.println("Collision-Error: RREP was not transfered
				// to destination");
				// System.out.println("Hard collision error: RREP message is
				// collided with another message -> for simulation give RREP
				// priority");
				incommingMsg = msg;

			}
			if ((msg instanceof PayloadMessage) && (msg.getDestinationID() == this.id)) {
				// System.out.println("Collision-Error: Payload was not
				// transfered to destination");
				// System.out.println("Hard collision error: Payload message is
				// collided with another message -> for simulation give
				// payloadMessage priority");
				incommingMsg = msg;

			}
		}
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
	 * 
	 * @param seconds
	 * @param nodeExecutionTime
	 * @param networkSize
	 */
	public void generateTransmissionEveryTSecondsChangingDestination(int seconds, int nodeExecutionTime,
			int networkSize, int payloadSize) {
		if (this.elapsedTimeSinceLastGenerationOfTransmission / 1000 >= seconds) {
			this.elapsedTimeSinceLastGenerationOfTransmission = 0L; // Reset
																	// timer

			// find random destination
			// int randomDestination = (int) (Math.random() * networkSize);

			// calculate destination node
			int destination = this.nextPayloadMsgDestination++;
			if (destination == this.id) {
				destination++;
				this.nextPayloadMsgDestination++;
			}

			// set message payload as simulation time
			long networkLivetime = simulator.getNetworkLifetime();
			char dataToSend[] = { (char) ((networkLivetime >> 8) & 0xFF), (char) ((networkLivetime >> 16) & 0xFF),
					(char) ((networkLivetime >> 24) & 0xFF), (char) ((networkLivetime >> 32) & 0xFF),
					(char) ((networkLivetime >> 40) & 0xFF), (char) ((networkLivetime >> 48) & 0xFF),
					(char) ((networkLivetime >> 56) & 0xFF), (char) ((networkLivetime >> 64) & 0xFF) };

			PayloadMessage tmpMsg = new PayloadMessage(id, destination, dataToSend);
			tmpMsg.setPayloadHash(networkLivetime);
			tmpMsg.setPayloadSize(payloadSize);

			this.startSendingProcess(tmpMsg);

		}

		this.elapsedTimeSinceLastGenerationOfTransmission += nodeExecutionTime;
	}

}
