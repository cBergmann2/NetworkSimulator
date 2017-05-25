package BasisGraphStruktur;


/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:15
 */
public abstract class Knoten {

	protected int ID;

	protected int anzahlVerbundenerKnoten;
	protected Knoten[] verbundeneKnoten;
	
	proteced double
	
	protected double energiebedarfProEmpfangemByte;
	protected double zusaetzlicherEnergiebedarfProEmpfangsaktion;
	protected double energiebedarfProGesendetemByte;
	protected double zustaetlicherEnergiebedarfProSendeaktion;
	
	protected int anzahlSendeoperationen;
	protected int anzahlEmpfangsoperationen;
	
	protected boolean knotenFunktionsbereit;
	

	private Knoten(){
		anzahlVerbundenerKnoten = 0;
		verbundeneKnoten = new Knoten[8];			//NachbarschaftsVerweis initialisieren
		for(int i= 0; i<8; i++){
			verbundeneKnoten[i] = null;
		}
		
		knotenFunktionsbereit = true;
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
	
	public abstract void nachrichtSenden(Nachricht nachricht);
	
	/**
	 * Sendet Nachricht mit Wahrscheinlichkeit sendewahrscheinlichkeit an einen zufälligen Knoten im Netzwerk
	 * @param nachricht
	 * @param sendewahrscheinlichkeit
	 */
	public abstract void zufaelligeNachrichtSenden(double sendewahrscheinlichkeit);
	
	public abstract void nachrichtEmpfangen(Nachricht nachricht);
	
	public String toString(){
		String returnString = "KnotenID:" + this.ID + "; Nachbarn: ";
		for(int i= 0; i<this.anzahlVerbundenerKnoten; i++){
			returnString = returnString + verbundeneKnoten[i].getID() + " ";
		}
		return returnString;
	}

	public int getAnzahlVerbundenerKnoten() {
		return anzahlVerbundenerKnoten;
	}

	public Knoten[] getVerbundeneKnoten() {
		return verbundeneKnoten;
	}

	public int getAnzahlSendeoperationen() {
		return anzahlSendeoperationen;
	}

	public int getAnzahlEmpfangsoperationen() {
		return anzahlEmpfangsoperationen;
	}

	public boolean isKnotenFunktionsbereit() {
		return knotenFunktionsbereit;
	}
	
	
}//end Knoten