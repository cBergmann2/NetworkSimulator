package AODV;

import java.util.LinkedList;

import BasisGraphStruktur.Knoten;
import BasisGraphStruktur.Nachricht;
import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;

public class AodvNetworkNode extends NetworkNode {

	private LinkedList<Message> messagesToBeSent;
	private LinkedList<RoutingTableEntry> routingTable;
	private LinkedList<RREQ> recivedRREQs;
	private LinkedList<RREP> recivedRREPs;
	private LinkedList<ReversePathRoutingTableEntry> reversePathRoutingTable;
	private int sequenceNumber;
	private int broadcastID;

	public AodvNetworkNode(int id) {
		super(id);
		messagesToBeSent = new LinkedList<Message>();
		routingTable = new LinkedList<RoutingTableEntry>();
		recivedRREQs = new LinkedList<RREQ>();
		recivedRREPs = new LinkedList<RREP>();
		reversePathRoutingTable = new LinkedList<ReversePathRoutingTableEntry>();
	}

	public void processRecivedMessage() {
		Message recivedMsg = inputBuffer.removeFirst();
		if (recivedMsg instanceof RREQ) {
			reciveRREQ((RREQ) recivedMsg);
		} else {
			if (recivedMsg instanceof RREP) {
				reciveRREP((RREP) recivedMsg);
			}
		}

	}

	private void reciveRREQ(RREQ rreq) {

		if (rreq.getSource_addr() != this.id) {
			boolean firstRREQ = true;

			// search RREQ to the same RREQ process
			for (ReversePathRoutingTableEntry tableEntry : reversePathRoutingTable) {
				if (tableEntry.getSource_addr() == rreq.getSource_addr()
						&& tableEntry.getBroadcast_id() == rreq.getBroadcast_id()) {
					firstRREQ = false;

				}
			}

			if (firstRREQ) {

				System.out.println("Node: " + this.id + " recive RREQ from node: " + rreq.getSenderID());

				// Save rreq message
				this.reversePathRoutingTable.add(new ReversePathRoutingTableEntry(rreq.getSource_addr(),
						rreq.getSource_sequence_number(), rreq.getBroadcast_id(), rreq.getDest_addr(),
						ReversePathRoutingTableEntry.ENTRY_EXPIRATION_TIME, rreq.getSenderID()));

				if (rreq.getDest_addr() == this.id) {
					// RREQ destination is this node
					RREP rrep;
					if (rreq.getDest_sequenc_number() > this.sequenceNumber) {
						rrep = new RREP(this.id, rreq.getSenderID(), rreq.getSource_addr(), rreq.getDest_addr(),
								rreq.getDest_sequenc_number(), Integer.MAX_VALUE, rreq.getHop_cnt());
					} else {
						rrep = new RREP(this.id, rreq.getSenderID(), rreq.getSource_addr(), rreq.getDest_addr(),
								this.sequenceNumber, Integer.MAX_VALUE, rreq.getHop_cnt());
					}

					this.outputBuffer.add(rrep);
				} else {

					// search destination in routing table
					RoutingTableEntry entry = null;
					for (RoutingTableEntry tempEntry : routingTable) {
						if (tempEntry.getZielknotenID() == rreq.getDest_addr()) {
							entry = tempEntry;
							break;
						}
					}

					if (entry == null || entry.isExpired()
							|| entry.getDest_sequence_number() < rreq.getDest_sequenc_number()) {
						// Keine Route zum Ziel bekannt -> RREQ-Nachricht an
						// Nachbarn weiterleiten

						// RREQ an alle Nachbarn weiterleiten
						RREQ copyOfRREQ = rreq.clone();
						copyOfRREQ.incrementHopCount();
						copyOfRREQ.setSenderID(this.id);
						copyOfRREQ.setDestinationID(0);

						// RREQ-Kopie an Nachbarn senden
						this.outputBuffer.add(copyOfRREQ);

					} else {
						// Route für Zielknoten bekannt -> RREP an Startknoten
						// senden
						RREP rrep = new RREP(this.id, rreq.getSenderID(), rreq.getSource_addr(), rreq.getDest_addr(),
								entry.getDest_sequence_number(), entry.getLifetime(), entry.getNumberOfHops());

						this.outputBuffer.add(rrep);
					}
				}
			}
		}
	}

	private void reciveRREP(RREP rrep) {

		boolean replaceTableEntry = false;
		RREP tableEntryToReplace = null;
		RoutingTableEntry routingTableEntryToReplace = null;
		boolean newRREP = true;

		if (rrep.getDestinationID() == this.id) {
			
			System.out.println("Node: "+ this.id + "recived RREP from node " + rrep.getSenderID());
			
			// Überprüfen ob bereits RREP zum Route-Discovery-Prozess empfangen
			// wurde
			for (RREP tempRREP : recivedRREPs) {
				if (tempRREP.getSource_addr() == rrep.getSource_addr()
						&& tempRREP.getDest_addr() == rrep.getDest_addr()) {
					newRREP = false;
					// Überprüfen ob aktuellere Route oder schnellere Route
					// vorliegt
					if (rrep.getDest_sequence_number() > tempRREP.getDest_sequence_number()) {
						replaceTableEntry = true;
						tableEntryToReplace = tempRREP;
					} else {
						if ((rrep.getDest_sequence_number() == tempRREP.getDest_sequence_number())
								&& (rrep.getHop_cnt() < tempRREP.getHop_cnt())) {
							replaceTableEntry = true;
							tableEntryToReplace = tempRREP;
						}
					}
				}
			}
			if (replaceTableEntry) {
				// Routingtabelle aktualisieren
				recivedRREPs.remove(tableEntryToReplace);
				recivedRREPs.add(rrep);

				RoutingTableEntry routingTableEntry = new RoutingTableEntry(rrep.getDest_addr(),
						rrep.getDest_sequence_number(), rrep.getSenderID(), rrep.getHop_cnt());
				routingTable.add(routingTableEntry);
			}

			if (newRREP) {
				// neuen RREP speichern
				recivedRREPs.add(rrep);
				RoutingTableEntry routingTableEntry = new RoutingTableEntry(rrep.getDest_addr(),
						rrep.getDest_sequence_number(), rrep.getSenderID(), rrep.getHop_cnt());
				routingTable.add(routingTableEntry);
			}

			if (newRREP || replaceTableEntry) {
				// RREP weiterleiten, sofern aktueller Knoten nicht Zielknoten
				// ist
				if (rrep.getSource_addr() != this.id) {

					// Knoten bestimmen an den RREP weitergeleitet werden soll
					int nextHop = this.id;
					for (ReversePathRoutingTableEntry tableEntry : reversePathRoutingTable) {
						if ((tableEntry.getDest_addr() == rrep.getDest_addr())
								&& (tableEntry.getSource_addr() == rrep.getSource_addr())) {
							nextHop = tableEntry.getNextHop();
							break;
						}
					}
					if (nextHop != this.id) {

						RREP rrepCopy = rrep.clone();
						rrepCopy.setDestinationID(nextHop);
						this.outputBuffer.add(rrepCopy);

					} else {
						// FEHLER
						// TODO: handle error
					}

				}
			}

			if ((newRREP || replaceTableEntry) && (rrep.getSource_addr() == this.id)) {
				// Nachrichten die in Sendebuffer liegen senden

				// Überprüfen ob Nachricht für Ziel im Sendebuffer enthalten ist
				LinkedList<Nachricht> zuSendendeNachrichten = new LinkedList<Nachricht>();
				for (Message msg : messagesToBeSent) {
					if (msg.getDestinationID() == rrep.getDest_addr()) {
						outputBuffer.add(msg);
						System.out.println("Route from node " + this.id + " to destination " + msg.getDestinationID()
								+ " discovered.");
					}
				}
			}
		}
	}

	public void addMessageToSent(Message msg) {
		// Search destination in routing table
		RoutingTableEntry entry = null;
		for (RoutingTableEntry tempEntry : routingTable) {
			if (tempEntry.getZielknotenID() == msg.getDestinationID()) {
				entry = tempEntry;
				break;
			}
		}

		if (entry == null || entry.isExpired()) {
			// No route to destination available

			// safe msg in messagesToBeSent buffer
			messagesToBeSent.add(msg);

			// start route discovery process
			this.startRouteDiscoveryProcess(msg.getDestinationID(), entry);
		} else {
			// calculate message transmission time
			msg.setRemainingTransmissionTime(msg.getDataVolume() * msg.TRANSMISSION_TIME_PER_BIT);
			// nachricht.addZwischenKnoten(this.ID);

			// send message to all neighbors
			this.outputBuffer.add(msg);
		}
	}

	private void startRouteDiscoveryProcess(int destinationID, RoutingTableEntry routingTableEntry) {
		int destinationSequenceNumber = 0;
		if (routingTableEntry != null) {
			destinationSequenceNumber = routingTableEntry.getDest_sequence_number() + 1;
		}

		// RREQ-Nachricht generieren
		RREQ rreq = new RREQ(this.id, 0, this.id, this.sequenceNumber, this.broadcastID, destinationID,
				destinationSequenceNumber);
		this.recivedRREQs.add(rreq);

		this.outputBuffer.add(rreq);
	}

}
