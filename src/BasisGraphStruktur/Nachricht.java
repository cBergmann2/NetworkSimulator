package BasisGraphStruktur;

public class Nachricht {
	
	public static final int UEBERTRAGUNGSZEIT_PRO_BIT = 4475; //Übertragungszeit in µsec
	
	private int zielknotenID;
	private int startknotenID;
	private int datenmenge;
	private int uebertragungszeit;

	public Nachricht(int zielknotenID){
		this.zielknotenID = zielknotenID;
		this.startknotenID = -1;
		this.datenmenge = 0;
		this.uebertragungszeit = 0;
	}
	
	public Nachricht(int zielknotenID, int datenmenge){
		this.zielknotenID = zielknotenID;
		this.datenmenge = datenmenge;
		this.startknotenID = -1;
		this.uebertragungszeit = 0;
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
}
