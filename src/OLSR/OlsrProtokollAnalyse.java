package OLSR;

import BasisGraphStruktur.Graph;
import Simulator.ProtokollAnalyse;

public class OlsrProtokollAnalyse extends ProtokollAnalyse{

	public OlsrProtokollAnalyse(Graph graph) {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int geschwindigkeitsanalyse() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int kostenanalyse() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int netzwerklebenszeitanalyse(double sendewahrscheinlichkeitEinesKnotens) {
		int zeit = 0; // Zeit in Sekunden
		
		while(graph.alleKnotenFunktionsbereit()){
			//Alle zwei Sekunden HELLO-Nachrichten senden
			if(zeit%2 == 0){
				((OLSR_Graph)graph).helloNachrichtenSenden();
			}
			
			//Alle 5 Sekunden TC-Nachrichten senden
			if(zeit%5 == 0){
				((OLSR_Graph)graph).tcNachrichtenSenden();
			}
			
			//Jede Sekunde zufällig Nachrichten Senden
			
			
			//Zeit inkrementieren
			zeit++;
		}
		
		return zeit;
	}

	@Override
	public int partitionierungsanalyse(double sendewahrscheinlichkeitEinesKnotens) {
		// TODO Auto-generated method stub
		return 0;
	}

}
