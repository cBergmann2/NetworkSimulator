import java.util.LinkedList;

public class MoeglicherMPR {
	
	private int knotenID;
	private LinkedList<Integer> nachbarn;
	
	public MoeglicherMPR(int knotenID){
		this.knotenID = knotenID;
		nachbarn = new LinkedList<Integer>();
	}
	
	public void addNachbar(int nachbar){
		this.nachbarn.add(nachbar);
	}
	
	public LinkedList<Integer> getNachbarn(){
		return nachbarn;
	}
	
	public int getKnotenID(){
		return knotenID;
	}

}
