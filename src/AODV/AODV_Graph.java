package AODV;

import java.util.LinkedList;

import BasisGraphStruktur.Graph;
import BasisGraphStruktur.Nachricht;
import OLSR.OLSR_Knoten;

public class AODV_Graph extends Graph{
	
	int routeRequestExpirationTime;
	protected LinkedList<SendeListeEintrag> sendeListe;
	
	public AODV_Graph(int netzwerkBreite){
		
		this.netzwerkBreite = netzwerkBreite; // Netzwerkbreite festlegen
		
		routeRequestExpirationTime = 10;
		sendeListe = new LinkedList<SendeListeEintrag>();

		knoten = new AODV_Knoten[netzwerkBreite * netzwerkBreite]; // Array zum Speichern der Knoten des Netzwerks erstellen
		
		for (int i = 0; i < knoten.length; i++) {
			knoten[i] = new AODV_Knoten(i, routeRequestExpirationTime, this); // Knoten des Netzwerks erstellen
		}

		this.netzwerkInitialisieren();
	}
	
	public void requestToSend(int knotenID, Object nachricht){
		System.out.println("Permission to send requested by Node "+ knotenID);
		if(sendeListe.isEmpty()){
			SendeListeEintrag eintrag = new SendeListeEintrag(knotenID, nachricht);
			sendeListe.add(eintrag);
			System.out.println("Permission to send granted to Node " + knotenID);
			((AODV_Knoten)knoten[knotenID]).permissionToSend(nachricht);
		}
		else{
			SendeListeEintrag eintrag = new SendeListeEintrag(knotenID, nachricht);
			sendeListe.add(eintrag);
		}
	}
	
	public void transmissionCompleted(int knotenID){
		System.out.println("Transmission completed by Node "+ knotenID);
		sendeListe.removeFirst();
		if(!sendeListe.isEmpty()){
			SendeListeEintrag eintrag = sendeListe.getFirst();
			//sendeListe.removeFirst();
			System.out.println("Permission to send granted to Node " + eintrag.getKnotenID());
			((AODV_Knoten)knoten[eintrag.getKnotenID()]).permissionToSend(eintrag.getNachricht());
		}
	}
	
	public void nachrichtSenden(int startknoten, int zielknoten){
		Nachricht nachricht = new Nachricht(zielknoten);
		nachricht.setStartknotenID(startknoten);
		
		knoten[startknoten].nachrichtSenden(nachricht);
	}
	
	public double getEnergiekostenRREQs(){
		double energiekosten = 0;
		
		for(int i=0; i<knoten.length; i++){
			energiekosten += ((AODV_Knoten)knoten[i]).getEnergiekostenRREQs();
		}
		
		
		return energiekosten;
	}
}
