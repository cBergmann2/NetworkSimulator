
public class Main {

	
	public static void main(String args[]){
		OLSR_Graph graph = new OLSR_Graph(10);
		
		//Graph initialisieren
		graph.HelloNachrichtenSenden();
		graph.mprSetsBestimmen();
		graph.HelloNachrichtenSenden();
		graph.TcNachrichtenSenden();
		
		
		System.out.println(graph.toString());
		System.out.println();
		
		System.out.println("Anzahl gesendeter HELLO-Nachrichten: " + graph.getAnzahlGesendeterHelloNachrichten());
		System.out.println("Anzahl empfangener HELLO-Nachrichten: " + graph.getAnzahlEmpfangenerHelloNachrichten());
		
		System.out.println("Anzahl gesendeter TC-Nachrichten: " + graph.getAnzahlGesendeterTcNachrichten());
		System.out.println("Anzahl empfangener TC-Nachrichten: " + graph.getAnzahlEmpfangenerTcNachrichten());

	}
}
