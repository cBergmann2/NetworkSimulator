import java.util.Comparator;

public class MoeglicherMprComparator implements Comparator{

	@Override
	public int compare(Object mpr1, Object mpr2) {
		if(((MoeglicherMPR)mpr1).getAnzahlNochNichtAbgedeckteNachbarn() > ((MoeglicherMPR)mpr2).getAnzahlNochNichtAbgedeckteNachbarn()){
			return 1;
		}
		else{
			if(((MoeglicherMPR)mpr1).getAnzahlNochNichtAbgedeckteNachbarn() < ((MoeglicherMPR)mpr2).getAnzahlNochNichtAbgedeckteNachbarn()){
				return -1;
			}
			else{
				//Vergleich knotenIn2HopDistanzZumMprSelector
				if(((MoeglicherMPR)mpr1).getAnzahlKnotenIn2HopDistanzZumMprSelctor() > ((MoeglicherMPR)mpr2).getAnzahlKnotenIn2HopDistanzZumMprSelctor()){
					return 1;
				}
				else{
					if(((MoeglicherMPR)mpr1).getAnzahlKnotenIn2HopDistanzZumMprSelctor() < ((MoeglicherMPR)mpr2).getAnzahlKnotenIn2HopDistanzZumMprSelctor()){
						return -1;
					}
					else{
						return 0;
					}
				}
			}
		}
	}

}
