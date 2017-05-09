package Fluten;

import BasisGraphStruktur.Graph;
import BasisGraphStruktur.Nachricht;
import MatlabChart.MatlabChart;

public class FlutenSimulation {
	public void simulation(){
		int anzahlExperimente = 10;
		
		int[] netzwerkBreite = new int[anzahlExperimente];
		double[] anzahlGesendeterNachrichten = new double[anzahlExperimente];
		double[] anzahlEmpfangenerNachrichten = new double[anzahlExperimente];
		
		for(int i=0; i<netzwerkBreite.length; i++){
			netzwerkBreite[i] = i+2;
		}
		
		
		for(int i=0; i<anzahlExperimente; i++){
			FlutenGraph graph = new FlutenGraph(netzwerkBreite[i]);
			
			Nachricht nachricht = new Nachricht(3);
			
			graph.getKnoten(0).nachrichtSenden(nachricht);
			
			anzahlGesendeterNachrichten[i] = (double)graph.getAnzahlSendeoperationenImNetzwerk();
			
			
		}
		
		MatlabChart fig = new MatlabChart();
		double[] netzwerkBreiteDouble = new double[netzwerkBreite.length];
		for(int i=0; i<netzwerkBreite.length; i++){
			netzwerkBreiteDouble[i] = (double)netzwerkBreite[i];
		}
		fig.plot(netzwerkBreiteDouble, anzahlGesendeterNachrichten, "-r", (float) 2.0, "Gesendete Nachrichten");
		fig.plot(netzwerkBreiteDouble, anzahlEmpfangenerNachrichten, "-b", (float) 2.0, "Empfangene Nachrichten");
		fig.RenderPlot();
		fig.grid("on", "on");
		fig.saveas("Output/Fluten/GesendeteEmpfangeneNachrichten.jpeg",640,480);
	}
}
