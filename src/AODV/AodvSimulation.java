package AODV;

public class AodvSimulation {
	
	public void simulation(){
		AODV_Graph graph = new AODV_Graph(10);

		graph.nachrichtSenden(0, 99);
		
	}
	
	
	/**
	 * 
	 * @param netzwerkBreite	Definiert Anzahl an Knoten im Netzwerk
	 * @param startknoten		Knoten von dem aus Nachricht gesendet werden soll
	 * @param zielknoten		Knoten zu dem Nachricht gesndet werden soll
	 * @return					Anzahl an Übertragungen bis Paket bei Ziel angekommen ist
	 */
	/*public int geschwindigkeitsanalyse(int netzwerkBreite, int startknoten, int zielknoten){
		
		if(netzwerkBreite < 2){
			//Netzwerk besteht nur aus einem oder weniger Knoten, eine Analyse ist in diesem Fall nicht möglich
			return -1;
		}
		
		if(startknoten > netzwerkBreite*netzwerkBreite-1 || zielknoten > netzwerkBreite*netzwerkBreite-1){
			//Start- oder Zielknoten liegen ausserhalb des Netzwerks
			return -1;
		}
		
		AODV_Graph graph = new AODV_Graph(netzwerkBreite);
		graph.nachrichtSenden(startknoten, zielknoten);
		
		int anzahlUebertragungen = ((AODV_Knoten)graph.getKnoten(startknoten)).getRoutingTableEntry(zielknoten).getNumberOfHops();
		
		
		
	}*/

}
