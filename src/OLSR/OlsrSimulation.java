package OLSR;

import MatlabChart.MatlabChart;

public class OlsrSimulation {

	public void simulation(){
		int anzahlExperimente = 10;
		
		int[] netzwerkBreite = new int[anzahlExperimente];
		double[] anzahlGesendeterHelloNachrichten = new double[anzahlExperimente];
		double[] anzahlEmpfangenerHelloNachrichten = new double[anzahlExperimente];
		
		for(int i=0; i<netzwerkBreite.length; i++){
			netzwerkBreite[i] = i+2;
		}
		
		
		for(int i=0; i<netzwerkBreite.length; i++){
			OLSR_Graph graph = new OLSR_Graph(netzwerkBreite[i]);
			
			//Graph initialisieren
			graph.helloNachrichtenSenden();
			graph.mprSetsBestimmen();
			graph.helloNachrichtenSenden();
			graph.tcNachrichtenSenden();
			
			
			//experimente[i][1] = graph.getAnzahlGesendeterHelloNachrichten();
			anzahlGesendeterHelloNachrichten[i] = graph.getAnzahlGesendeterHelloNachrichten();
			//experimente[i][2] = graph.getAnzahlEmpfangenerHelloNachrichten();
			anzahlEmpfangenerHelloNachrichten[i] = graph.getAnzahlEmpfangenerHelloNachrichten();
			//experimente[i][3] = graph.getAnzahlGesendeterTcNachrichten();
			//experimente[i][4] = graph.getAnzahlEmpfangenerTcNachrichten();
		}
		
		MatlabChart fig = new MatlabChart();
		double[] netzwerkBreiteDouble = new double[netzwerkBreite.length];
		for(int i=0; i<netzwerkBreite.length; i++){
			netzwerkBreiteDouble[i] = (double)netzwerkBreite[i];
		}
		fig.plot(netzwerkBreiteDouble, anzahlGesendeterHelloNachrichten, "-r", (float) 2.0, "Gesendete HELLO Nachrichten");
		fig.plot(netzwerkBreiteDouble, anzahlEmpfangenerHelloNachrichten, "-b", (float) 2.0, "Empfangene HELLO Nachrichten");
		fig.RenderPlot();
		fig.grid("on", "on");
		fig.saveas("Output/OLSR/HELLO_Nachrichten.jpeg",640,480);
	}
	
	
	
}
