
public class Main {

	
	public static void main(String args[]){
		int anzahlExperimente = 30;
		
		int[] netzwerkBreite = new int[anzahlExperimente];
		double[] anzahlGesendeterHelloNachrichten = new double[anzahlExperimente];
		double[] anzahlEmpfangenerHelloNachrichten = new double[anzahlExperimente];
		
		for(int i=0; i<netzwerkBreite.length; i++){
			netzwerkBreite[i] = i+2;
		}
		
		
		for(int i=0; i<netzwerkBreite.length; i++){
			OLSR_Graph graph = new OLSR_Graph(netzwerkBreite[i]);
			
			//Graph initialisieren
			graph.HelloNachrichtenSenden();
			graph.mprSetsBestimmen();
			graph.HelloNachrichtenSenden();
			graph.TcNachrichtenSenden();
			
			/*
			System.out.println(graph.toString());
			System.out.println();
			
			System.out.println("Anzahl gesendeter HELLO-Nachrichten: " + graph.getAnzahlGesendeterHelloNachrichten());
			System.out.println("Anzahl empfangener HELLO-Nachrichten: " + graph.getAnzahlEmpfangenerHelloNachrichten());
			
			System.out.println("Anzahl gesendeter TC-Nachrichten: " + graph.getAnzahlGesendeterTcNachrichten());
			System.out.println("Anzahl empfangener TC-Nachrichten: " + graph.getAnzahlEmpfangenerTcNachrichten());
			*/
			
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
		fig.saveas("MyPlot.jpeg",640,480);
	}
}
