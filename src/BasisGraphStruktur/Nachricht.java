package BasisGraphStruktur;

import java.util.LinkedList;

public class Nachricht {
	
	public static final int UEBERTRAGUNGSZEIT_PRO_BIT = 4475; //Übertragungszeit in µsec
	
	private int zielknotenID;
	private int startknotenID;
	private int datenmenge;
	private int uebertragungszeit;
	private LinkedList<Integer> zwischenKnoten;

	public Nachricht(int zielknotenID){
		this.zielknotenID = zielknotenID;
		this.startknotenID = -1;
		this.datenmenge = 0;
		this.uebertragungszeit = 0;
		this.zwischenKnoten = new LinkedList<Integer>();
	}
	
	public Nachricht(int zielknotenID, int datenmenge){
		this.zielknotenID = zielknotenID;
		this.datenmenge = datenmenge;
		this.startknotenID = -1;
		this.uebertragungszeit = 0;
		this.zwischenKnoten = new LinkedList<Integer>();
	}
	
	public int getZielknotenID() {
		return zielknotenID;
	}

	public int getStartknotenID() {
		return startknotenID;
	}

	public void setStartknotenID(int startknotenID) {
		this.startknotenID = startknotenID;
	}

	public int getDatenmenge() {
		return datenmenge;
	}

	public void addUebertragungszeit(int uebertragungszeit){
		this.uebertragungszeit += uebertragungszeit;
	}
	
	public int getUebertragungszeit() {
		return uebertragungszeit;
	}

	public LinkedList<Integer> getZwischenKnoten() {
		return zwischenKnoten;
	}

	public void addZwischenKnoten(int zwischenKnoten) {
		this.zwischenKnoten.add(zwischenKnoten);
	}
}
