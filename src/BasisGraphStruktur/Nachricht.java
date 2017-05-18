package BasisGraphStruktur;

public class Nachricht {
	private int zielknotenID;
	private int startknotenID;

	public Nachricht(int zielknotenID){
		this.zielknotenID = zielknotenID;
		this.startknotenID = -1;
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
}
