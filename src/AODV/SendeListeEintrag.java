package AODV;

public class SendeListeEintrag {
	private int knotenID;
	private Object nachricht;
	
	public SendeListeEintrag(int knotenID, Object nachricht){
		this.knotenID = knotenID;
		this.nachricht = nachricht;
	}

	public int getKnotenID() {
		return knotenID;
	}

	public Object getNachricht() {
		return nachricht;
	}
}
