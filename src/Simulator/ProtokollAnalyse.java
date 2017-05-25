package Simulator;

import BasisGraphStruktur.Graph;

public abstract class ProtokollAnalyse {
	
	protected Graph graph;
	
	public ProtokollAnalyse(Graph graph){
		this.graph = graph;
	}

	/**
	 * 
	 * @return Anzahl Hops bis Nachricht �bertragen wurde
	 */
	public abstract int geschwindigkeitsanalyse();
	
	public abstract int kostenanalyse();
	
	/**
	 * Bestimmt die Zeit, die vom Start des Netzwerks bis zum Ausfall des ersten Knotens vergeht.
	 * 
	 * 
	 * @param sendewahrscheinlichkeitEinesKnotens	Wahrscheinlichkeit mit der ein Knoten pro Sekunde eine Nachricht sendet. 
	 * 												Der Zielknoten wird zuf�llig aus allen erreichbaren Knoten gew�hlt.
	 * 	
	 * @return anzahlSekunden bis erster Knoten des Netzwerks ausf�llt
	 */
	public abstract int netzwerklebenszeitanalyse(double sendewahrscheinlichkeitEinesKnotens);
		
	public abstract int partitionierungsanalyse(double sendewahrscheinlichkeitEinesKnotens);
	
}
