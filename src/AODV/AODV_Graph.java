package AODV;

import BasisGraphStruktur.Graph;
import BasisGraphStruktur.Nachricht;
import OLSR.OLSR_Knoten;

public class AODV_Graph extends Graph{
	
	int routeRequestExpirationTime;
	
	public AODV_Graph(int netzwerkBreite){
		
		this.netzwerkBreite = netzwerkBreite; // Netzwerkbreite festlegen
		
		routeRequestExpirationTime = 10;

		knoten = new AODV_Knoten[netzwerkBreite * netzwerkBreite]; // Array zum Speichern der Knoten des Netzwerks erstellen
		
		for (int i = 0; i < knoten.length; i++) {
			knoten[i] = new AODV_Knoten(i, routeRequestExpirationTime); // Knoten des Netzwerks erstellen
		}

		this.netzwerkInitialisieren();
	}
	
	public void nachrichtSenden(int startknoten, int zielknoten){
		Nachricht nachricht = new Nachricht(zielknoten);
		nachricht.setStartknotenID(startknoten);
		
		knoten[startknoten].nachrichtSenden(nachricht);
	}
}
