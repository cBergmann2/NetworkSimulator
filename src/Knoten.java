

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:15
 */
public class Knoten {

	protected int ID;

	int anzahlVerbundenerKnoten;
	protected Knoten[] verbundeneKnoten;

	private Knoten(){
		anzahlVerbundenerKnoten = 0;
		verbundeneKnoten = new Knoten[8];			//NachbarschaftsVerweis initialisieren
		for(int i= 0; i<8; i++){
			verbundeneKnoten[i] = null;
		}
	}
	
	public Knoten(int id){
		this();
		this.ID = id;
	}

	public void finalize() throws Throwable {

	}

	public boolean nachbarKnotenHinzufuegen(Knoten knoten){
		if(this.anzahlVerbundenerKnoten >= 8){	//bereits alle mögliche Nachbarn hinzugefügt 
			return false;
		}
		
		if(knoten == null){						//Kein Knoten übergebnen
			return false;
		}
		
		int i=0;
		while(verbundeneKnoten[i] != null){		//Freie position suchen
			i++;
		}
		verbundeneKnoten[i] = knoten;			//Nachbarknoten in Feld einfügen
		this.anzahlVerbundenerKnoten++;
		return true;	
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public String toString(){
		String returnString = "KnotenID:" + this.ID + "; Nachbarn: ";
		for(int i= 0; i<this.anzahlVerbundenerKnoten; i++){
			returnString = returnString + verbundeneKnoten[i].getID() + " ";
		}
		return returnString;
	}
	
	
}//end Knoten