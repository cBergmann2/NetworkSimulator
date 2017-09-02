package OLSR;

import java.util.LinkedList;

import javax.swing.event.TableColumnModelEvent;

import Simulator.Message;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;

public class OlsrNetworkNode extends NetworkNode {

	LinkedList<Neighbor> aliveNeighbors;
	LinkedList<OlsrMessage> messagesToSend;
	LinkedList<TopologyTableEntry> topologyTable;
	LinkedList<RoutingTableEntry> routingTable;
	LinkedList<PayloadMessage> msgWaintingBuffer;
	LinkedList<TopologyDiscoveryMessage> receivedTopologyDiscoveryMessages;
	int topologyDiscoverySequenceNumber;
	int networkSize;
	boolean networkStructureDiscovered;
	boolean sendControlMessages;


	public OlsrNetworkNode(int id) {
		super(id);
		aliveNeighbors = new LinkedList<Neighbor>();
		messagesToSend = new LinkedList<OlsrMessage>();
		topologyTable = new LinkedList<TopologyTableEntry>();
		routingTable = new LinkedList<RoutingTableEntry>();
		msgWaintingBuffer = new LinkedList<PayloadMessage>();
		receivedTopologyDiscoveryMessages = new LinkedList<TopologyDiscoveryMessage>();
		topologyDiscoverySequenceNumber = 0;
		networkSize = -1;
		networkStructureDiscovered = false;
		sendControlMessages = true;

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void performeTimeDependentTasks(long executionTime) {

		if (sendControlMessages) {
			if (simulator.getNetworkLifetime() % 1000 == 0) {
				// collect all messages and send packet

				if (messagesToSend.size() > 0) {

					BasicPacketFormat msg = new BasicPacketFormat();

					for (OlsrMessage olsrMsg : messagesToSend) {
						msg.addMessage(olsrMsg);
					}

					for (OlsrMessage olsrMsg : msg.getMessages()) {
						messagesToSend.remove(olsrMsg);
					}

					// System.out.println(simulator.getNetworkLifetime() + ":
					// Node " + this.id + " - send broadcast msg.");
					this.sendMsg(msg);
				}

			}

			if (simulator.getNetworkLifetime() % (30*1000) == 0) {
				sendHelloMessage();
				// System.out.println(simulator.getNetworkLifetime() + ": Node "
				// + this.id + " - send Hello msg.");
			}

			if (simulator.getNetworkLifetime() % (9*60*1000) == 0) {
				// Send Topology discovery message
				sendTDMessage();
				// System.out.println(simulator.getNetworkLifetime() + ": Node "
				// + this.id + " - send TD message.");
			}

		}

		if (simulator.getNetworkLifetime() % (1 * 60000) == 0) {
			receivedTopologyDiscoveryMessages.clear();
		}

	}

	private void sendTDMessage() {
		TopologyDiscoveryMessage tdMessage = new TopologyDiscoveryMessage();
		tdMessage.setOriginatorAddress(this.id);
		tdMessage.setTtl(255);
		tdMessage.setAnsn(topologyDiscoverySequenceNumber);

		for (Neighbor neighbor : aliveNeighbors) {
			tdMessage.addAdvertisedNeighbor(neighbor.getId());
		}

		messagesToSend.add(tdMessage);
	}

	private void sendHelloMessage() {
		OlsrHelloMessage helloMessage = new OlsrHelloMessage();

		helloMessage.setOriginatorAddress(this.id);
		helloMessage.setTtl(1);

		for (Neighbor neighbor : aliveNeighbors) {
			helloMessage.addNeighborInterfaceAddress(neighbor.getId());
		}

		messagesToSend.add(helloMessage);
	}

	@Override
	public void processRecivedMessage() {

		Message receivedMsg = inputBuffer.removeFirst();

		if (receivedMsg instanceof BasicPacketFormat) {

			for (OlsrMessage msg : ((BasicPacketFormat) receivedMsg).getMessages()) {
				if (msg instanceof OlsrHelloMessage) {
					processReceivedHelloMessage((OlsrHelloMessage) msg);
				} else {
					if (msg instanceof TopologyDiscoveryMessage) {
						processRececivedTopologyDiscoveryMessage((TopologyDiscoveryMessage) msg);
					}
				}
			}

		} else {
			if (receivedMsg instanceof PayloadMessage) {
				processReceivedPayloadMessage((PayloadMessage) receivedMsg);
			}
		}

	}

	private void processReceivedPayloadMessage(PayloadMessage msg) {
		if (msg.getDestinationID() == this.id) {

			if (msg.getPayloadDestinationAdress() != this.id) {
				// forward message to next hop
				// System.out.println(""+simulator.getNetworkLifetime() +": Node
				// "+ this.id + ": forward payloadMessage from node " +
				// msg.getPayloadSourceAdress() + " to node " +
				// msg.getPayloadDestinationAdress());
				this.sendMsg(msg);
			} else {
				// long transmissionTime = simulator.getNetworkLifetime() -
				// msg.getStartTransmissionTime();
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

	private void processRececivedTopologyDiscoveryMessage(TopologyDiscoveryMessage msg) {

		/*
		 * System.out.print(simulator.getNetworkLifetime() + ": Node " + this.id
		 * + " received ND message. Neighbors: "); for (int neighbor :
		 * msg.getAdvertiesdNeighborList()) { System.out.print(neighbor + " ,");
		 * } System.out.println();
		 */

		for (TopologyDiscoveryMessage receivedMsg : receivedTopologyDiscoveryMessages) {
			if (msg.getOriginatorAddress() == receivedMsg.getOriginatorAddress()
					&& msg.getMessageSequenceNumber() == receivedMsg.getMessageSequenceNumber()) {
				// message has already been processed
				return;
			}
		}
		receivedTopologyDiscoveryMessages.add(msg.getCopy());

		for (TopologyTableEntry tableEntry : topologyTable) {
			if (tableEntry.getDestinationAddress() == msg.getOriginatorAddress()) {
				// Node is already known
				if (tableEntry.isTopologyChanged(msg)) {
					tableEntry.clearNeighborList();
					for (int neighbor : msg.getAdvertiesdNeighborList()) {
						tableEntry.addNeighbor(neighbor);
					}
					recalculateRoutingTable();
				}

				for (OlsrMessage olsrMsg : messagesToSend) {
					if (olsrMsg instanceof TopologyDiscoveryMessage) {
						if (olsrMsg.getOriginatorAddress() == msg.getOriginatorAddress()) {
							// current packet already contains a TD message with
							// this originator
							return;
						}
					}
				}

				messagesToSend.add(msg);
				return;
			}
		}

		// New node
		TopologyTableEntry tableEntry = new TopologyTableEntry();
		tableEntry.setDestinationAddress(msg.getOriginatorAddress());
		for (int neighbor : msg.getAdvertiesdNeighborList()) {
			tableEntry.addNeighbor(neighbor);
		}
		topologyTable.add(tableEntry);
		recalculateRoutingTable();

		messagesToSend.add(msg);
	}

	private void recalculateRoutingTable() {

		int detectedRoutes = 0;

		routingTable.clear();

		// Add direct neighbor nodes
		for (Neighbor neighbor : this.aliveNeighbors) {
			routingTable.add(new RoutingTableEntry(neighbor.getId(), neighbor.getId(), 1));
			findMessageToSend(neighbor.getId());
		}

		// Add two hop neighbors
		for (Neighbor neighbor : this.aliveNeighbors) {

			for (int twoHopNeighbor : neighbor.getReachableTwoHopNeighbors()) {
				if (!routeForNodeIsCalculated(twoHopNeighbor)) {
					routingTable.add(new RoutingTableEntry(twoHopNeighbor, neighbor.getId(), 2));
					findMessageToSend(twoHopNeighbor);
				}
			}
		}

		// Discover topology messages to create routing table
		do {
			detectedRoutes = 0;
			for (TopologyTableEntry tableEntry : topologyTable) {

				if (!routeForNodeIsCalculated(tableEntry.getDestinationAddress())) {

					for (int neighbor : tableEntry.getNeighbors()) {
						if (routeForNodeIsCalculated(neighbor)) {
							RoutingTableEntry routingEntry = getRouteTableEntry(neighbor);

							routingTable.add(new RoutingTableEntry(tableEntry.getDestinationAddress(),
									routingEntry.getNextHop(), routingEntry.getDistance() + 1));

							findMessageToSend(tableEntry.getDestinationAddress());

							detectedRoutes++;

							break;
						}
					}

				}

			}

		} while (detectedRoutes > 0);

		
		if(routingTable.size() == this.networkSize){
			this.networkStructureDiscovered = true;
		}
		 
	}

	private boolean routeForNodeIsCalculated(int nodeAddress) {
		for (RoutingTableEntry tabelEntry : routingTable) {
			if (tabelEntry.getDestinationAddress() == nodeAddress) {
				return true;
			}
		}

		return false;
	}

	public RoutingTableEntry getRouteTableEntry(int destinationAddress) {
		for (RoutingTableEntry tabelEntry : routingTable) {
			if (tabelEntry.getDestinationAddress() == destinationAddress) {
				return tabelEntry;
			}
		}

		return null;
	}

	private void processReceivedHelloMessage(OlsrHelloMessage msg) {
		updateNeighborList(msg);
	}

	private void updateNeighborList(OlsrHelloMessage msg) {
		for (Neighbor neighbor : aliveNeighbors) {
			if (neighbor.getId() == msg.getOriginatorAddress()) {
				// Neighbor already known
				neighbor.setLastHelloMessage(simulator.getNetworkLifetime());
				neighbor.setExpectedTimeNextHelloMessage(simulator.getNetworkLifetime() + msg.getHTime() * 1000);
				return;
			}
		}

		// neighbor not yet known
		Neighbor neighbor = new Neighbor();
		neighbor.setId(msg.getOriginatorAddress());
		neighbor.setLastHelloMessage(simulator.getNetworkLifetime());
		neighbor.setExpectedTimeNextHelloMessage(simulator.getNetworkLifetime() + msg.getHTime() * 1000);
		for (int twoHopNeighbor : msg.getNeighborInterfaceAdresses()) {
			neighbor.addReachableTwoHopNeighbor(twoHopNeighbor);
		}
		aliveNeighbors.add(neighbor);
		recalculateRoutingTable();

	}

	@Override
	public void startSendingProcess(PayloadMessage tmpMsg) {
		tmpMsg.setStartTransmissionTime(simulator.getNetworkLifetime());
		this.sendMsg(tmpMsg);
		this.numberTransmittedPayloadMsg++;
	}

	public void sendMsg(Message msg) {
		if (msg instanceof BasicPacketFormat) {

			msg.setDestinationID(-1);
			this.outputBuffer.add(msg);
		}
		if (msg instanceof PayloadMessage) {
			if (routeForNodeIsCalculated(((PayloadMessage) msg).getPayloadDestinationAdress())) {
				RoutingTableEntry tableEntry = getRouteTableEntry(((PayloadMessage) msg).getPayloadDestinationAdress());
				msg.setDestinationID(tableEntry.getNextHop());

				// System.out.println("Send msg");

				this.outputBuffer.add(msg);
				//this.numberTransmittedPayloadMsg++;
			} else {
				// No Route for destination available
				msgWaintingBuffer.add((PayloadMessage) msg);
			}
		}

	}

	private void findMessageToSend(int destinationAddress) {

		LinkedList<PayloadMessage> msgToSend = new LinkedList<PayloadMessage>();

		for (PayloadMessage msg : this.msgWaintingBuffer) {
			if (msg.getPayloadDestinationAdress() == destinationAddress) {
				msgToSend.add(msg);
			}
		}

		for (PayloadMessage msg : msgToSend) {
			this.msgWaintingBuffer.remove(msg);
			msg.setStartTransmissionTime(simulator.getNetworkLifetime());
			this.sendMsg(msg);
		}

	}

	public void setNetworkSize(int size) {
		this.networkSize = size;
	}

	/**
	 * @return the networkStructureDiscovered
	 */
	public boolean isNetworkStructureDiscovered() {
		return networkStructureDiscovered;
	}

	/**
	 * @return the sendControlMessages
	 */
	public boolean isSendControlMessages() {
		return sendControlMessages;
	}

	/**
	 * @param sendControlMessages the sendControlMessages to set
	 */
	public void setSendControlMessages(boolean sendControlMessages) {
		this.sendControlMessages = sendControlMessages;
	}

}
