package Fluten;
import java.util.LinkedList;

import BasisGraphStruktur.Knoten;
import BasisGraphStruktur.Nachricht;

public class FlutenKnoten extends Knoten{
	
	private LinkedList<Nachricht> empfangeneNachrichten;
	//private int anzahlEmpfangenerNachrichten;
	//private int anzahlGesendeterNachrichten;

	public FlutenKnoten(int id) {
		super(id);
		
		empfangeneNachrichten = new LinkedList<Nachricht>();
		anzahlSendeoperationen = 0;
		anzahlEmpfangsoperationen = 0;
	}

	@Override
	public void nachrichtSenden(Nachricht nachricht) {
		anzahlSendeoperationen++;
		for(Knoten knoten: verbundeneKnoten){
			if(knoten != null){
				((FlutenKnoten)knoten).nachrichtEmpfangen(nachricht);
			}
		}
		
	}

	@Override
	public void nachrichtEmpfangen(Nachricht nachricht) {
		anzahlEmpfangsoperationen++;
		if(!empfangeneNachrichten.contains(nachricht)){
			empfangeneNachrichten.add(nachricht);
			//Nachricht weiterleiten
			for(Knoten knoten: verbundeneKnoten){
				if(knoten != null){
					((FlutenKnoten)knoten).nachrichtEmpfangen(nachricht);
				}
			}
			anzahlSendeoperationen++;
		}
		
	}

}
