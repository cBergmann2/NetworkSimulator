package Fluten;

import BasisGraphStruktur.Graph;

public class FlutenGraph extends Graph{

	public FlutenGraph(int netzwerkBreite) {
		this.netzwerkBreite = netzwerkBreite; // Netzwerkbreite festlegen

		knoten = new FlutenKnoten[netzwerkBreite * netzwerkBreite]; // Array zum Speichern der Knoten des Netzwerks erstellen
		
		for (int i = 0; i < knoten.length; i++) {
			knoten[i] = new FlutenKnoten(i); // Knoten des Netzwerks erstellen
		}

		this.netzwerkInitialisieren();
	}
	
	public FlutenKnoten getKnoten(int knotenID){
		
		if(knotenID < this.knoten.length){
			return (FlutenKnoten)this.knoten[knotenID];
		}
		return null;
	}
	
}
