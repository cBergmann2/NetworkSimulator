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
	
	protected double energiebedarfProEmpfangemBit;
	protected double zusaetzlicherEnergiebedarfProEmpfangsaktion;
	protected double energiebedarfProGesendetemBit;
	protected double zustaetlicherEnergiebedarfProSendeaktion;
	protected double energiekostenStartsignal;
	protected double energiekostenStoppsignal;
	
	protected double energiekosten; //in A Sekunden
	
	protected int anzahlSendeoperationen;
	protected int anzahlEmpfangsoperationen;

	private Knoten(){
		anzahlVerbundenerKnoten = 0;
		verbundeneKnoten = new Knoten[8];			//NachbarschaftsVerweis initialisieren
		for(int i= 0; i<8; i++){
			verbundeneKnoten[i] = null;
		}
		
		energiekosten = 0.0;
	}
	
	public Knoten(int id){
		this();
		this.ID = id;
		this.energiekosten = 0.0;
		this.energiebedarfProGesendetemBit = 0.000077858;		
		this.energiekostenStartsignal = 0.000150705;
		this.energiekostenStoppsignal = 0.00020094;
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
	
	public double getEneriekosten(){
		return this.energiekosten;
	}
	
	
}//end Knoten