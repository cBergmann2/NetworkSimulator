package Fluten;
import java.util.LinkedList;

import BasisGraphStruktur.Knoten;
import BasisGraphStruktur.Nachricht;

public class FlutenKnoten extends Knoten{
	
	private LinkedList<Nachricht> empfangeneNachrichten;
	private int anzahlEmpfangenerNachrichten;
	private int anzahlGesendeterNachrichten;

	public FlutenKnoten(int id) {
		super(id);
		
		empfangeneNachrichten = new LinkedList<Nachricht>();
		anzahlEmpfangenerNachrichten = 0;
		anzahlGesendeterNachrichten = 0;
	}

	@Override
	public void nachrichtSenden(Nachricht nachricht) {
		anzahlGesendeterNachrichten++;
		for(Knoten knoten: verbundeneKnoten){
			((FlutenKnoten)knoten).nachrichtEmpfangen(nachricht);
		}
		
	}

	@Override
	public void nachrichtEmpfangen(Nachricht nachricht) {
		anzahlEmpfangenerNachrichten++;
		if(!empfangeneNachrichten.contains(nachricht)){
			//Nachricht weiterleiten
			for(Knoten knoten: verbundeneKnoten){
				((FlutenKnoten)knoten).nachrichtEmpfangen(nachricht);
			}
			anzahlGesendeterNachrichten++;
		}
		
	}

}
