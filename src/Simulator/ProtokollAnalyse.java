package Simulator;

import BasisGraphStruktur.Graph;

public abstract class ProtokollAnalyse {
	
	protected Graph graph;
	
	public ProtokollAnalyse(Graph graph){
		this.graph = graph;
	}

	/**
	 * 
	 * @return Anzahl Hops bis Nachricht übertragen wurde
	 */
	public abstract int geschwindigkeitsanalyse();
	
	public abstract int kostenanalyse();
	
	/**
	 * Bestimmt die Zeit, die vom Start des Netzwerks bis zum Ausfall des ersten Knotens vergeht.
	 * 
	 * 
	 * @param sendewahrscheinlichkeitEinesKnotens	Wahrscheinlichkeit mit der ein Knoten pro Sekunde eine Nachricht sendet. 
	 * 												Der Zielknoten wird zufällig aus allen erreichbaren Knoten gewählt.
	 * 	
	 * @return anzahlSekunden bis erster Knoten des Netzwerks ausfällt
	 */
	public abstract int netzwerklebenszeitanalyse(double sendewahrscheinlichkeitEinesKnotens);
		
	public abstract int partitionierungsanalyse(double sendewahrscheinlichkeitEinesKnotens);
	
}
