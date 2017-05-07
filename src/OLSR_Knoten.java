import java.util.LinkedList;

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:28
 */
public class OLSR_Knoten extends Knoten {

	private int anzahlEmpfangsoperationenHelloNachrichten;
	private int anzahlEmpfangsoperationenTcNachrichten;
	private int anzahlSendeoperationenHelloNachrichten;
	private int anzahlSendeoperationenTcNachrichten;
	
	private int[][] zweiSrpuengeNachbarschaft;
	
	private LinkedList<NachbarKnoten> zweiSprungNachbarn;
	private LinkedList<MoeglicherMPR> mprSet;
	private LinkedList<Integer> mprSelectorSet;
	private LinkedList<TC_Nachricht> empfangeneTcNachrichte;
	
	//private int[] mprSet;

	public OLSR_Knoten(int id){
		super(id);
		
		zweiSrpuengeNachbarschaft = new int[25][9];
		for(int i=0;i<25; i++){
			for(int j=0;j<9; j++){
				zweiSrpuengeNachbarschaft[i][j] = -1;
			}
		}
		
		zweiSprungNachbarn = new LinkedList<NachbarKnoten>();
		
		/*mprSet = new int[8];
		for(int i=0; i<mprSet.length;i++){
			mprSet[i] = -1;
		}
		*/
		mprSet = new LinkedList<MoeglicherMPR>();
		mprSelectorSet = new LinkedList<Integer>();
		empfangeneTcNachrichte = new LinkedList<TC_Nachricht>();
	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	public void bestimmeMprSet(){
		if(zweiSprungNachbarn.size() > 0){
			LinkedList<MoeglicherMPR> moeglicheMPRs = new LinkedList<MoeglicherMPR>();
			for(int i=0; i<anzahlVerbundenerKnoten; i++){
				MoeglicherMPR moeglicherMPR = new MoeglicherMPR(verbundeneKnoten[i].getID());
				for(int j=0; j<verbundeneKnoten[i].anzahlVerbundenerKnoten;j++){
					moeglicherMPR.addNachbar(verbundeneKnoten[i].verbundeneKnoten[j].getID());
				}
				moeglicheMPRs.add(moeglicherMPR);
			}
			
			LinkedList<NachbarKnoten> zweiSprungNachbarnTemp = new LinkedList<NachbarKnoten>();
			for(NachbarKnoten zweiSprungNachbar: zweiSprungNachbarn){
				zweiSprungNachbarnTemp.add(zweiSprungNachbar);
			}
			
			//1. Knoten in 2-Sprung-Nachbarschaft finden, die nur über einen direkten Nachbarn erreicht werden können
			LinkedList<NachbarKnoten> zuLoeschendeZweiSprungNachbarn = new LinkedList<NachbarKnoten>();
			for(NachbarKnoten zweiSprungNachbar: zweiSprungNachbarnTemp){
				if(zweiSprungNachbar.getErreichbarVonKnoten().size() == 1){
					
					MoeglicherMPR zuLoeschenderMpr = null;
					
					for(MoeglicherMPR mpr: moeglicheMPRs){
						if(mpr.getKnotenID() == zweiSprungNachbar.getErreichbarVonKnoten().get(0).intValue()){
							zuLoeschenderMpr = mpr;
							this.mprSet.add(mpr);
							
							//Abgedeckte Knoten des MPR aus zweiSprungNachbarnTemp entfernen
							for(Integer nachbar: mpr.getNachbarn()){
								for(NachbarKnoten zweiSprungNachbarTemp: zweiSprungNachbarnTemp){
									if(zweiSprungNachbarTemp.getID() == nachbar){
										zuLoeschendeZweiSprungNachbarn.add(zweiSprungNachbarTemp);
									}
								}
							}
							
						}
					}
					
					moeglicheMPRs.remove(zuLoeschenderMpr);
					
				}
			}
			for(NachbarKnoten zweiSprungNachbarTemp: zuLoeschendeZweiSprungNachbarn){
				zweiSprungNachbarnTemp.remove(zweiSprungNachbarTemp);
			}
			zuLoeschendeZweiSprungNachbarn.clear();
	
			
			//2. Abdeckung der restlichen Knoten bestimmen
			while(zweiSprungNachbarnTemp.size() > 0){
				for(MoeglicherMPR moeglicherMpr: moeglicheMPRs){
					moeglicherMpr.berechneNochNichtAbgedeckteNachbarn(this.ID, zweiSprungNachbarnTemp);
					moeglicherMpr.berechneKnotenIn2HopDistanzZumMprSelector(this.ID, zweiSprungNachbarn);
				}
				
				MoeglicherMprComparator mprComparator = new MoeglicherMprComparator();
				moeglicheMPRs.sort(mprComparator);
				
				this.mprSet.add(moeglicheMPRs.getLast());
				MoeglicherMPR mpr = moeglicheMPRs.removeLast();
				//Abgedeckte Knoten des MPR aus zweiSprungNachbarnTemp entfernen
				for(Integer nachbar: mpr.getNachbarn()){
					for(NachbarKnoten zweiSprungNachbarTemp: zweiSprungNachbarnTemp){
						if(zweiSprungNachbarTemp.getID() == nachbar){
							zuLoeschendeZweiSprungNachbarn.add(zweiSprungNachbarTemp);
						}
					}
				}
				for(NachbarKnoten zweiSprungNachbarTemp: zuLoeschendeZweiSprungNachbarn){
					zweiSprungNachbarnTemp.remove(zweiSprungNachbarTemp);
				}
				zuLoeschendeZweiSprungNachbarn.clear();
				
			}
		}
		
		
	}

	public void empfangeHelloNachricht(HELLO_Nachricht nachricht){
		anzahlEmpfangsoperationenHelloNachrichten++;
		
		for(int mpr: nachricht.getMprSet()){
			if(mpr == this.ID){
				this.mprSelectorSet.add(nachricht.getSenderKnotenID());
			}
		}
		
		for(int i=0; i<nachricht.getNachbarschaft().length; i++){
			int zweiSprungNachbarID = nachricht.getNachbarschaft()[i];

			boolean isDirekterNachbar = false;
			if(zweiSprungNachbarID == this.ID){
				isDirekterNachbar = true;
			}
			for(int j=0; j<this.anzahlVerbundenerKnoten; j++){
				if(verbundeneKnoten[j].getID() == zweiSprungNachbarID){
					isDirekterNachbar = true;
					break;
				}
			}
			
			if(!isDirekterNachbar){
				
/*				//Zwei-Sprung Nachbarschaftstabelle aufbauen
				int j=0;
				while((zweiSrpuengeNachbarschaft[j][0] != -1) || (zweiSrpuengeNachbarschaft[j][0] == zweiSprungNachbarID)){
					if(zweiSrpuengeNachbarschaft[j][0] == zweiSprungNachbarID){
						int k = 1;
						while(zweiSrpuengeNachbarschaft[j][k] != -1){
							k++;
						}
						zweiSrpuengeNachbarschaft[j][k] = nachricht.getKnotenID();
						break;
					}
					else{
						j++;
					}
				}
				if(zweiSrpuengeNachbarschaft[j][0] == -1){
					zweiSrpuengeNachbarschaft[j][0] = zweiSprungNachbarID;
					zweiSrpuengeNachbarschaft[j][1] = nachricht.getKnotenID();
				}*/
				
				boolean knotenInListeEnthalten = false;
				for(NachbarKnoten zweiSprungNachbar: zweiSprungNachbarn){
					if(zweiSprungNachbar.getID() == zweiSprungNachbarID){
						zweiSprungNachbar.addVerbindungsknoten(nachricht.getSenderKnotenID());
						knotenInListeEnthalten = true;
						break;
					}
				}
				if(!knotenInListeEnthalten){
					NachbarKnoten zweiSprungNachbar = new NachbarKnoten(zweiSprungNachbarID);
					zweiSprungNachbar.addVerbindungsknoten(nachricht.getSenderKnotenID());
					zweiSprungNachbarn.add(zweiSprungNachbar);
				}
			}
		}
		
	}

	public void sendeHelloNachricht(){
		
		int nachbarn[] = new int[anzahlVerbundenerKnoten];
		for(int i=0; i<anzahlVerbundenerKnoten; i++){
			nachbarn[i] = verbundeneKnoten[i].getID();
		}
		
		HELLO_Nachricht nachricht = new HELLO_Nachricht(this.getID(), anzahlVerbundenerKnoten, nachbarn, 1);
		
		for(MoeglicherMPR mpr: mprSet){
			nachricht.addMpr(mpr.getKnotenID());
		}
		
		
		//Hello-Nachricht senden
		for(int i=0; i<anzahlVerbundenerKnoten; i++){
			((OLSR_Knoten)verbundeneKnoten[i]).empfangeHelloNachricht(nachricht);
		}
		
		anzahlSendeoperationenHelloNachrichten++;
	}
	
	public void empfangeTcNachricht(TC_Nachricht nachricht){
		this.anzahlEmpfangsoperationenTcNachrichten++;
		if(!empfangeneTcNachrichte.contains(nachricht)){
			empfangeneTcNachrichte.add(nachricht);
			this.tcNachrichtWeiterleiten(nachricht);
		}
	}
	
	public void tcNachrichtWeiterleiten(TC_Nachricht nachricht){
		if(this.mprSelectorSet.contains(nachricht.getSenderID())){
			
			nachricht.setSenderID(this.ID);							//Sender der Nachricht ändern
			
			for(int i=0; i<anzahlVerbundenerKnoten; i++){			//Nachricht an Nachbarn senden
				((OLSR_Knoten)verbundeneKnoten[i]).empfangeTcNachricht(nachricht);
			}

			this.anzahlSendeoperationenTcNachrichten++;
		}
	}
	
	public void sendeTcNachricht(){
		
		TC_Nachricht tcNachricht = new TC_Nachricht(this.ID, 255);	//TC-Nachricht erstellen
		
		tcNachricht.setSenderID(this.ID);								//Sender der Nachricht ändern
		
		for(MoeglicherMPR mpr: mprSet){								//mprSet hinzufügen
			tcNachricht.addAdvertisedNeighbor(mpr.getKnotenID());
		}
		
		
		for(int i=0; i<anzahlVerbundenerKnoten; i++){				//Nachricht an Nachbarn senden
			((OLSR_Knoten)verbundeneKnoten[i]).empfangeTcNachricht(tcNachricht);
		}
		
		this.anzahlSendeoperationenTcNachrichten++;
	}
	
	public String toString(){
		String returnString = "KnotenID:" + this.ID + "; Nachbarn: ";
		for(int i= 0; i<this.anzahlVerbundenerKnoten; i++){
			returnString = returnString + verbundeneKnoten[i].getID() + " ";
		}
		returnString = returnString + " MPR-Set: ";
		for(MoeglicherMPR mpr: mprSet){
			returnString = returnString + mpr.getKnotenID() + " ";
		}
		return returnString;
	}
	
	public int getAnzahlEmpfangsoperationenHelloNachrichten() {
		return anzahlEmpfangsoperationenHelloNachrichten;
	}

	public void setAnzahlEmpfangsoperationenHelloNachrichten(int anzahlEmpfangsoperationenHelloNachrichten) {
		this.anzahlEmpfangsoperationenHelloNachrichten = anzahlEmpfangsoperationenHelloNachrichten;
	}


	public int getAnzahlEmpfangsoperationenTcNachrichten() {
		return anzahlEmpfangsoperationenTcNachrichten;
	}

	
	
	public void setAnzahlEmpfangsoperationenTcNachrichten(int anzahlEmpfangsoperationenTcNachrichten) {
		this.anzahlEmpfangsoperationenTcNachrichten = anzahlEmpfangsoperationenTcNachrichten;
	}

	
	public int getAnzahlSendeoperationenHelloNachrichten() {
		return anzahlSendeoperationenHelloNachrichten;
	}

	
	public void setAnzahlSendeoperationenHelloNachrichten(int anzahlSendeoperationenHelloNachrichten) {
		this.anzahlSendeoperationenHelloNachrichten = anzahlSendeoperationenHelloNachrichten;
	}

	public int getAnzahlSendeoperationenTcNachrichten() {
		return anzahlSendeoperationenTcNachrichten;
	}

	public void setAnzahlSendeoperationenTcNachrichten(int anzahlSendeoperationenTcNachrichten) {
		this.anzahlSendeoperationenTcNachrichten = anzahlSendeoperationenTcNachrichten;
	}

	@Override
	public void nachrichtSenden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nachrichtEmpfangen() {
		// TODO Auto-generated method stub
		
	}

	
}//end OLSR_Knoten