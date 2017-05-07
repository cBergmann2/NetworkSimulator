import java.util.LinkedList;
import java.util.List;

public class NachbarKnoten {
	
	int ID;
	List<Integer> erreichbarVonKnoten;
	
	
	public NachbarKnoten(int knotenID){
		this.ID = knotenID;
		this.erreichbarVonKnoten = new LinkedList<Integer>();
	}
	
	public void addVerbindungsknoten(int knotenID){
		erreichbarVonKnoten.add(knotenID);
	}
	
	public List<Integer> getErreichbarVonKnoten(){
		return erreichbarVonKnoten;
	}
	
	public int getID(){
		return this.ID;
	}

}
