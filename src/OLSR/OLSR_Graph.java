package OLSR;
import BasisGraphStruktur.Graph;

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:22
 */
public class OLSR_Graph extends Graph {

	public OLSR_Knoten m_OLSR_Knoten;

	private OLSR_Graph() {
	}
	
	public OLSR_Graph(int netzwerkBreite){
		this();
		this.netzwerkBreite = netzwerkBreite; // Netzwerkbreite festlegen

		knoten = new OLSR_Knoten[netzwerkBreite * netzwerkBreite]; // Array zum Speichern der Knoten des Netzwerks erstellen
		
		for (int i = 0; i < knoten.length; i++) {
			knoten[i] = new OLSR_Knoten(i); // Knoten des Netzwerks erstellen
		}

		this.netzwerkInitialisieren();
	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	public void helloNachrichtenSenden(){
		for(int i=0; i<knoten.length; i++){
			((OLSR_Knoten)knoten[i]).sendeHelloNachricht();
		}
	}

	public void tcNachrichtenSenden(){
		for(int i=0; i<knoten.length; i++){
			((OLSR_Knoten)knoten[i]).sendeTcNachricht();
		}
	}

	public void mprSetsBestimmen(){
		for(int i=0; i<knoten.length; i++){
			((OLSR_Knoten)knoten[i]).bestimmeMprSet();
		}
	}
	
	public int getAnzahlGesendeterHelloNachrichten(){
		int anzahl = 0;
		for(int i=0; i<this.knoten.length; i++){
			anzahl += ((OLSR_Knoten)knoten[i]).getAnzahlSendeoperationenHelloNachrichten();
		}
		return anzahl;
	}
	
	public int getAnzahlEmpfangenerHelloNachrichten(){
		int anzahl = 0;
		for(int i=0; i<this.knoten.length; i++){
			anzahl += ((OLSR_Knoten)knoten[i]).getAnzahlEmpfangsoperationenHelloNachrichten();
		}
		return anzahl;
	}
	
	public int getAnzahlGesendeterTcNachrichten(){
		int anzahl = 0;
		for(int i=0; i<this.knoten.length; i++){
			anzahl += ((OLSR_Knoten)knoten[i]).getAnzahlSendeoperationenTcNachrichten();
		}
		return anzahl;
	}
	
	public int getAnzahlEmpfangenerTcNachrichten(){
		int anzahl = 0;
		for(int i=0; i<this.knoten.length; i++){
			anzahl += ((OLSR_Knoten)knoten[i]).getAnzahlEmpfangsoperationenTcNachrichten();
		}
		return anzahl;
	}
	
}//end OLSR_Graph