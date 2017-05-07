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
						zweiSprungNachbar.addVerbindungsknoten(nachricht.getKnotenID());
						knotenInListeEnthalten = true;
						break;
					}
				}
				if(!knotenInListeEnthalten){
					NachbarKnoten zweiSprungNachbar = new NachbarKnoten(zweiSprungNachbarID);
					zweiSprungNachbar.addVerbindungsknoten(nachricht.getKnotenID());
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
		
		HELLO_Nachricht nachricht = new HELLO_Nachricht(this.getID(), anzahlVerbundenerKnoten, nachbarn);
		
		for(int i=0; i<anzahlVerbundenerKnoten; i++){
			((OLSR_Knoten)verbundeneKnoten[i]).empfangeHelloNachricht(nachricht);
		}
		
		anzahlSendeoperationenHelloNachrichten++;
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
	
}//end OLSR_Knoten