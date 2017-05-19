package AODV;

import java.util.LinkedList;

import BasisGraphStruktur.Knoten;
import BasisGraphStruktur.Nachricht;

public class AODV_Knoten extends Knoten{
	
	//Routing Tabelle
	private LinkedList<RoutingTableEntry> routingTable;
	private LinkedList<ReversePathRoutingTableEntry> reversePathRoutingTable;
	private LinkedList<RREP> recivedRREPs;
	private int sequenceNumber;
	private int broadcastID;
	int routeRequestExpirationTime;
	private LinkedList<Nachricht> sendeBuffer;


	public AODV_Knoten(int id, int routeRequestExpirationTime) {
		super(id);
		this.routingTable = new LinkedList<RoutingTableEntry>();
		this.reversePathRoutingTable = new LinkedList<ReversePathRoutingTableEntry>();
		this.routeRequestExpirationTime = routeRequestExpirationTime;
		this.recivedRREPs = new LinkedList<RREP>();
		this.sendeBuffer = new LinkedList<Nachricht>();
	}

	@Override
	public void nachrichtSenden(Nachricht nachricht) {
		//Zielknoten in Routingtabelle suchen
		RoutingTableEntry entry = null;
		for(RoutingTableEntry tempEntry: routingTable){
			if(tempEntry.getZielknotenID() == nachricht.getZielknotenID()){
				entry = tempEntry;
				break;
			}
		}
		
		if(entry == null || entry.getTableEntryExpirationTime() > this.getCurrentTime()){
			//Keine Route zum Ziel bekannt
			
			//Nachricht in SendeBuffer speichern
			this.sendeBuffer.add(nachricht);
			
			//Route Discovery Prozess starten
			this.startRouteDiscoveryProcess(nachricht.getZielknotenID(), entry);
		}
		else{
			//�bertragungszeit zur Nachricht hinzuf�gen
			nachricht.addUebertragungszeit(nachricht.getDatenmenge()*nachricht.UEBERTRAGUNGSZEIT_PRO_BIT);
			
			//Nachricht an Next-Hop Nachbarn senden
			for(Knoten knoten: verbundeneKnoten){
				if(knoten != null){
					if(knoten.getID() == entry.getNextHop()){
						knoten.nachrichtEmpfangen(nachricht);
					
					}
				}
			}
		}
	}

	@Override
	public void nachrichtEmpfangen(Nachricht nachricht) {
		if(nachricht.getZielknotenID() != this.ID){
			//nachricht weiterleiten
			this.nachrichtSenden(nachricht);
		}
		else{
			System.out.println("Knoten " +  this.ID + ": Nachricht von Knoten " + nachricht.getStartknotenID() + " empfangen nach :"+ nachricht.getUebertragungszeit() +" �sec." );
		}
	}
	
	public void startRouteDiscoveryProcess(int zielknoten, RoutingTableEntry routingTableEntry){
		int destinationSequenceNumber = 0;
		if(routingTableEntry != null){
			destinationSequenceNumber = routingTableEntry.getDest_sequence_number() +1;
		}
		
		//RREQ-Nachricht generieren
		RREQ rreq = new RREQ(this.ID,this.sequenceNumber, this.broadcastID, zielknoten, destinationSequenceNumber);
		rreq.addUebertragungszeit(RREQ.RREQ_UEBERTRAGUNGSZEIT + 10000 + this.ID * 50000);
		
		for(Knoten knoten: this.verbundeneKnoten){
			if(knoten != null){
				((AODV_Knoten)knoten).receiveRREQ(rreq, this.ID);
			}
		}
		
	}
	
	public void receiveRREQ(RREQ routeRequest, int sendeknoten){
		
		boolean ersteRREQ = true;
		
		
		//Suche RREQ zu gleichem Route-Discovery-Prozess
		for(ReversePathRoutingTableEntry tableEntry: reversePathRoutingTable){
			if(tableEntry.getSource_addr() == routeRequest.getSource_addr() 
					&& tableEntry.getBroadcast_id() == routeRequest.getBroadcast_id()){
				ersteRREQ = false;
			}
		}
		
		if(ersteRREQ){
		
			//RREQ-Nachricht speichern
			this.reversePathRoutingTable.add(new ReversePathRoutingTableEntry(routeRequest.getSource_addr(), routeRequest.getSource_sequence_number(), routeRequest.getBroadcast_id(), routeRequest.getDest_addr(),this.getCurrentTime() + routeRequestExpirationTime, sendeknoten));
			
			if(routeRequest.getDest_addr() == this.ID){
				RREP rrep;
				if(routeRequest.getDest_sequenc_number() > this.sequenceNumber){
					rrep = new RREP(routeRequest.getSource_addr(),routeRequest.getDest_addr(),routeRequest.getDest_sequenc_number(), Integer.MAX_VALUE, routeRequest.getHop_cnt());
					rrep.addUebertragungszeit(routeRequest.getUebertragungszeit());
					rrep.addUebertragungszeit(RREP.RREP_UEBERTRAGUNGSZEIT + 10000 + this.ID * 50000);
				}
				else{
					rrep = new RREP(routeRequest.getSource_addr(),routeRequest.getDest_addr(),this.sequenceNumber, Integer.MAX_VALUE, routeRequest.getHop_cnt());
					rrep.addUebertragungszeit(routeRequest.getUebertragungszeit());
					rrep.addUebertragungszeit(RREP.RREP_UEBERTRAGUNGSZEIT + 10000 + this.ID * 50000);
				}

				for(Knoten knoten: verbundeneKnoten){
					if(knoten != null){
						if(knoten.getID() == sendeknoten){
							((AODV_Knoten)knoten).receiveRREP(rrep, this.ID);
						}
					}
				}
			}
			
			//Zielknoten in Routingtabelle suchen
			RoutingTableEntry entry = null;
			for(RoutingTableEntry tempEntry: routingTable){
				if(tempEntry.getZielknotenID() == routeRequest.getDest_addr()){
					entry = tempEntry;
					break;
				}
			}
			
			if(entry == null || 
					entry.getTableEntryExpirationTime() < this.getCurrentTime() || 
					entry.getDest_sequence_number() < routeRequest.getDest_sequenc_number()){
				//Keine Route zum Ziel bekannt -> RREQ-Nachricht an Nachbarn weiterleiten
				
				
				//RREQ an alle Nachbarn weiterleiten
				for(Knoten knoten: verbundeneKnoten){
					if(knoten != null){
						//Kopie der Nachricht erstellen und Hop-Count erh�hen
						RREQ copyOfRREQ = routeRequest.clone();
						copyOfRREQ.incrementHopCount();
						copyOfRREQ.addUebertragungszeit(RREQ.RREQ_UEBERTRAGUNGSZEIT + 10000 + this.ID * 50000);
						
						//RREQ-Kopie an Nachbarn senden
						((AODV_Knoten)knoten).receiveRREQ(copyOfRREQ, this.ID);
					}
				}
				
			}
			else{
				//Route f�r Zielknoten bekannt -> RREP an Startknoten senden
				RREP rrep = new RREP(routeRequest.getSource_addr(),routeRequest.getDest_addr(),entry.getDest_sequence_number(), entry.getLifetime(), entry.getNumberOfHops());
				rrep.addUebertragungszeit(routeRequest.getUebertragungszeit());
				rrep.addUebertragungszeit(RREP.RREP_UEBERTRAGUNGSZEIT + 10000 + this.ID * 50000);
				
				for(Knoten knoten: verbundeneKnoten){
					if(knoten.getID() == entry.nextHop){
						((AODV_Knoten)knoten).receiveRREP(rrep, this.ID);
					}
				}
			}
		}
	}
	
	public void receiveRREP(RREP routeReply, int senderId){
		boolean replaceTableEntry = false;
		RREP tableEntryToReplace = null;
		RoutingTableEntry routingTableEntryToReplace = null;
		boolean newRREP = true;
		
		//�berpr�fen ob bereits RREP zum Route-Discovery-Prozess empfangen wurde
		for(RREP rrep: recivedRREPs){
			if(rrep.getSource_addr() == routeReply.getSource_addr() && rrep.getDest_addr() == routeReply.getDest_addr()){
				newRREP = false;
				//�berpr�fen ob aktuellere Route oder schnellere Route vorliegt
				if(routeReply.getDest_sequence_number() > rrep.getDest_sequence_number()){
					replaceTableEntry = true;
					tableEntryToReplace = rrep;
				}
				else{
					if((routeReply.getDest_sequence_number() == rrep.getDest_sequence_number()) && (routeReply.getHop_cnt() < rrep.getHop_cnt())){
						replaceTableEntry = true;
						tableEntryToReplace = rrep;
					}
				}
			}
		}
		if(replaceTableEntry){
			//Routingtabelle aktualisieren
			recivedRREPs.remove(tableEntryToReplace);
			recivedRREPs.add(routeReply);
			
			RoutingTableEntry routingTableEntry = new RoutingTableEntry(routeReply.getDest_addr(), routeReply.getDest_sequence_number(), senderId, routeReply.getHop_cnt());
			routingTable.add(routingTableEntry);
		}
		
		if(newRREP){
			//neuen RREP speichern
			recivedRREPs.add(routeReply);
			RoutingTableEntry routingTableEntry = new RoutingTableEntry(routeReply.getDest_addr(), routeReply.getDest_sequence_number(), senderId, routeReply.getHop_cnt());
			routingTable.add(routingTableEntry);
		}
		
		
		if(newRREP || replaceTableEntry){
			//RREP weiterleiten, sofern aktueller Knoten nicht Zielknoten ist
			if(routeReply.getDest_addr() != this.ID){
				
				//Knoten bestimmen an den RREP weitergeleitet werden soll
				int nextHop = this.ID;
				for(ReversePathRoutingTableEntry tableEntry: reversePathRoutingTable){
					if((tableEntry.getDest_addr() == routeReply.getDest_addr()) && (tableEntry.getSource_addr() == routeReply.getSource_addr())){
						nextHop = tableEntry.getNextHop();
						break;
					}
				}
				if(nextHop != this.ID){
					for(Knoten knoten: verbundeneKnoten){
						if(knoten != null){
							if(knoten.getID() == nextHop){
								routeReply.addUebertragungszeit(RREP.RREP_UEBERTRAGUNGSZEIT + 10000 + this.ID * 50000);
								((AODV_Knoten)knoten).receiveRREP(routeReply, this.ID);
							}
						}
					}
				}
				else{
					//FEHLER
					//TODO: handle error
				}
				
			}
		}
		
		if((newRREP || replaceTableEntry) && (routeReply.getSource_addr() == this.ID)){
			//Nachrichten die in Sendebuffer liegen senden
			
			boolean keineNachrichtZuSenden = false;
			
			//while(!keineNachrichtZuSenden){
				Nachricht nachricht = null;
				keineNachrichtZuSenden = true;
				
				//�berpr�fen ob Nachricht f�r Ziel im Sendebuffer enthalten ist
				for(Nachricht tempNachricht: sendeBuffer){
					if(tempNachricht.getZielknotenID() == routeReply.getDest_addr()){
						tempNachricht.addUebertragungszeit(routeReply.getUebertragungszeit());
						this.nachrichtSenden(tempNachricht);
						keineNachrichtZuSenden = false;
					}
				}
				
				//Nachricht aus Sendebuffer entfernen
				sendeBuffer.remove(nachricht);
			//}
		}
	}
	
	public int getCurrentTime(){
		//TODO: get current time from simulation system
		return Integer.MAX_VALUE;
	}
	
	public RoutingTableEntry getRoutingTableEntry(int zielknotenID){
		for(RoutingTableEntry tableEntry: routingTable){
			if(tableEntry.getZielknotenID() == zielknotenID){
				return tableEntry;
			}
		}
		return null;
	}

}
