package OLSR;
import java.util.LinkedList;

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:08
 */
public class HELLO_Nachricht extends OlsrNachricht{
	
	private int senderKnotenID;
	private int[] nachbarschaft;	// Feld enthält IDs der Nachbarschaftsknoten
	private LinkedList<Integer> mprSet;

	public HELLO_Nachricht(int knotenID, int anzahlNachbarn, int[] nachbarn, int hopCount){
				
		this.senderKnotenID = knotenID;
		this.nachbarschaft = nachbarn;
		this.hopCount = hopCount;
		this.mprSet = new LinkedList<Integer>();
	}
	
	public int getSenderKnotenID(){
		return senderKnotenID;
	}
	
	public int[] getNachbarschaft(){
		return nachbarschaft;
	}

	public LinkedList<Integer> getMprSet(){
		return this.mprSet;
	}
	
	public void addMpr(int mprID){
		this.mprSet.add(mprID);
	}
	
	public void finalize() throws Throwable {

	}
	
}//end HELLO_Nachricht