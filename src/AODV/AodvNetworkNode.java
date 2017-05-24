package AODV;

import java.util.LinkedList;

import BasisGraphStruktur.Knoten;
import BasisGraphStruktur.Nachricht;
import SimulationNetwork.Message;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class AodvNetworkNode extends NetworkNode {

	private LinkedList<PayloadMessage> messagesToBeSent;
	private LinkedList<RoutingTableEntry> routingTable;
	//private LinkedList<RREQ> recivedRREQs;
	private LinkedList<RREP> recivedRREPs;
	private LinkedList<ReversePathRoutingTableEntry> reversePathRoutingTable;
	private int sequenceNumber;
	private int broadcastID;
	private int numberRecivedPayloadMsg;

	public AodvNetworkNode(int id) {
		super(id);
		messagesToBeSent = new LinkedList<PayloadMessage>();
		routingTable = new LinkedList<RoutingTableEntry>();
		//recivedRREQs = new LinkedList<RREQ>();
		recivedRREPs = new LinkedList<RREP>();
		reversePathRoutingTable = new LinkedList<ReversePathRoutingTableEntry>();
		numberRecivedPayloadMsg = 0;
	}

	public void processRecivedMessage() {
		Message recivedMsg = inputBuffer.removeFirst();
		if (recivedMsg instanceof RREQ) {
			reciveRREQ((RREQ) recivedMsg);
		} else {
			if (recivedMsg instanceof RREP) {
				reciveRREP((RREP) recivedMsg);
			}
			else{
				if(recivedMsg instanceof PayloadMessage){
					recivePayloadMessage((PayloadMessage) recivedMsg);
				}
			}
		}
	}

	private void recivePayloadMessage(PayloadMessage msg) {
		if(msg.getDestinationID() == this.id){
			if(msg.getPayloadDestinationAdress() != this.id){
				//forward message to next hop
				System.out.println(""+simulator.getNetworkLifetime() +": Node "+ this.id + ": forward payloadMessage from node " + msg.getPayloadSourceAdress() + " to node " + msg.getPayloadDestinationAdress());
				this.sendPayloadMessage(msg);
			}
			else{
				System.out.println(""+simulator.getNetworkLifetime() +": Node " +  this.id + ": message from node " + msg.getPayloadSourceAdress() + " recived.");
				numberRecivedPayloadMsg++;
			}
		}
	}

	private void reciveRREQ(RREQ rreq) {

		//System.out.println(""+simulator.getNetworkLifetime() +": Node: " + this.id + " recive RREQ from node: " + rreq.getSenderID() + "-> Requested path to node: " + rreq.getDest_addr());
		
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

				System.out.println(""+simulator.getNetworkLifetime() +": Node: " + this.id + " recive RREQ from node: " + rreq.getSenderID() + "-> Requested path to node: " + rreq.getDest_addr());

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
						rrep.setLifetime(30000);
						this.outputBuffer.add(rrep);
						//System.out.println(""+simulator.getNetworkLifetime() +": Node " + this.id + ": Route found for sourcenode " + rrep.getSource_addr() + " to destnode " + rrep.getDest_addr() + " -> send RREP");
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
			
			System.out.println(""+simulator.getNetworkLifetime() +": Node: "+ this.id + " recived RREP from node " + rrep.getSenderID());
			
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
						rrepCopy.setSenderID(this.id);
						rrepCopy.setDestinationID(nextHop);
						rrepCopy.setLifetime(30000);
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
				PayloadMessage toDeleteMsg = null;
				LinkedList<Nachricht> zuSendendeNachrichten = new LinkedList<Nachricht>();
				for (PayloadMessage msg : messagesToBeSent) {
					if (msg.getPayloadDestinationAdress() == rrep.getDest_addr()) {
						
						msg.setDestinationID(rrep.getSenderID());
						msg.setSenderID(this.id);
						//System.out.println("Calculated transmission time: " + Message.calculateTransmissionTime(msg.getPayload().length) + " ns");
						msg.setRemainingTransmissionTime(Message.calculateTransmissionTime(msg.getPayload().length));
						outputBuffer.add(msg);
						System.out.println(""+simulator.getNetworkLifetime() +": Route from node " + this.id + " to destination " + msg.getPayloadDestinationAdress() + " discovered -> Send message");
						
						toDeleteMsg = msg;
					}
				}
				if(toDeleteMsg != null){
					//messagesToBeSent.remove(toDeleteMsg);
				}
			}
		}
	}
	
	public void sendMessage(PayloadMessage msg){
		//System.out.println("Node " + id + ": start transmission process for message to destination node " + msg.getPayloadDestinationAdress());
		sendPayloadMessage(msg);
	}

	private void sendPayloadMessage(PayloadMessage msg) {
		// Search destination in routing table
		RoutingTableEntry entry = null;
		for (RoutingTableEntry tempEntry : routingTable) {
			if (tempEntry.getZielknotenID() == msg.getPayloadDestinationAdress()) {
				entry = tempEntry;
				break;
			}
		}

		System.out.println("Node " + id + ": start transmission process for message to destination node " + msg.getPayloadDestinationAdress() );
		
		if (entry == null || entry.isExpired()) {
			// No route to destination available

			// safe msg in messagesToBeSent buffer
			messagesToBeSent.add(msg);

			//System.out.print(" -> no routing table entry found: start route discovery process\n");
			
			// start route discovery process
			this.startRouteDiscoveryProcess(msg.getPayloadDestinationAdress(), entry);
		} else {
			// calculate message transmission time
			// msg.setRemainingTransmissionTime(msg.getDataVolume() * msg.TRANSMISSION_TIME_PER_BIT);
			// nachricht.addZwischenKnoten(this.ID);

			//System.out.print(" -> routing table entry found: send message\n");
			msg.setDestinationID(entry.getNextHop());
			msg.setSenderID(this.id);
			msg.setRemainingTransmissionTime(Message.calculateTransmissionTime(msg.getPayload().length));
			
			// send message to next hop
			this.outputBuffer.add(msg);
			//System.out.println("Node " + this.id + " send message to node " + msg.getPayloadDestinationAdress());
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
		

		this.outputBuffer.add(rreq);
		
		this.broadcastID++;
	}

	protected void performeTimeDependentTasks(){
		//Clear memory of node
		/*
		
		//ReversePathRoutingTable
		if(reversePathRoutingTable.size() > 0){
			LinkedList<ReversePathRoutingTableEntry> toDeleteReversePathEntrys = new LinkedList<ReversePathRoutingTableEntry>();
			for(ReversePathRoutingTableEntry entry: reversePathRoutingTable){
				entry.decrementExpirationTime(1);
				if(entry.getExpirationTime() <= 0 ){
					toDeleteReversePathEntrys.add(entry);
				}
			}
			
			for(ReversePathRoutingTableEntry entry: toDeleteReversePathEntrys){
				reversePathRoutingTable.remove(entry);
			}
		}
		
		//recivedRREPs
		if(recivedRREPs.size() > 0){
			LinkedList<RREP> toRemoveRREPs = new LinkedList<RREP>();
			for(RREP rrep: recivedRREPs){
				rrep.decrementLifetime(1);
				if(rrep.getLifetime() <= 0){
					toRemoveRREPs.add(rrep);
				}
			}
			
			for(RREP rrep: toRemoveRREPs){
				recivedRREPs.remove(rrep);
			}
		}
		*/
	}
	
	public int getNumberOfRecivedPayloadMessages(){
		return this.numberRecivedPayloadMsg;
	}
}
