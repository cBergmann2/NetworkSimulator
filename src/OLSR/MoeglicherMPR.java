package OLSR;
import java.util.LinkedList;

public class MoeglicherMPR implements Comparable{
	
	private int knotenID;
	private LinkedList<Integer> nachbarn;
	private LinkedList<Integer> nochNichtAbgedeckteNachbarn;
	private LinkedList<Integer> knotenIn2HopDistanzZumMprSelector;
	private LinkedList<Integer> nachbarnOhneMprSelctorUndDessenNachbarn;
	
	public MoeglicherMPR(int knotenID){
		this.knotenID = knotenID;
		nachbarn = new LinkedList<Integer>();
		nochNichtAbgedeckteNachbarn = new LinkedList<Integer>();
		knotenIn2HopDistanzZumMprSelector = new LinkedList<Integer>();
		nachbarnOhneMprSelctorUndDessenNachbarn = new LinkedList<Integer>();
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
	
	public LinkedList<Integer> berechneNochNichtAbgedeckteNachbarn(int mprSelctorID, LinkedList<NachbarKnoten> nichtAbgedeckteNachbarn){
		for(NachbarKnoten nachbar: nichtAbgedeckteNachbarn){
			if(nachbarn.contains(nachbar.getID())){
				nochNichtAbgedeckteNachbarn.add(nachbar.getID());
			}
		}	
		return nochNichtAbgedeckteNachbarn;
	}
	
	public LinkedList<Integer> berechneKnotenIn2HopDistanzZumMprSelector(int mprSelctorID, LinkedList<NachbarKnoten> zweiHopNachbarnVomMprSelector){
		for(NachbarKnoten nachbar: zweiHopNachbarnVomMprSelector){
			if(nachbarn.contains(nachbar.getID())){
				knotenIn2HopDistanzZumMprSelector.add(nachbar.getID());
			}
		}	
		return knotenIn2HopDistanzZumMprSelector;
	}

	public int getAnzahlNochNichtAbgedeckteNachbarn(){
		return nochNichtAbgedeckteNachbarn.size();
	}
	
	public int getAnzahlKnotenIn2HopDistanzZumMprSelctor(){
		return knotenIn2HopDistanzZumMprSelector.size();
	}
	
	@Override
	public int compareTo(Object mpr) {
		if(((MoeglicherMPR)mpr).getAnzahlNochNichtAbgedeckteNachbarn() > this.getAnzahlNochNichtAbgedeckteNachbarn()){
			return 1;
		}
		else{
			if(((MoeglicherMPR)mpr).getAnzahlNochNichtAbgedeckteNachbarn() < this.getAnzahlNochNichtAbgedeckteNachbarn()){
				return -1;
			}
			else{
				//Vergleich knotenIn2HopDistanzZumMprSelector
				return 0;
			}
		}
	}

	
}
