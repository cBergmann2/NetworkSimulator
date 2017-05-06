

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:08
 */
public class HELLO_Nachricht {
	
	private int knotenID;
	private int[] nachbarschaft;	// Feld enthält IDs der Nachbarschaftsknoten

	public HELLO_Nachricht(int knotenID, int anzahlNachbarn, int[] nachbarn){
				
		this.knotenID = knotenID;
		this.nachbarschaft = nachbarn;

	}
	
	public int getKnotenID(){
		return knotenID;
	}
	
	public int[] getNachbarschaft(){
		return nachbarschaft;
	}

	public void finalize() throws Throwable {

	}
}//end HELLO_Nachricht