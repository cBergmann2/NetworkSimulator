package BasisGraphStruktur;

/**
 * @author Christoph Bergmann
 * @version 1.0
 * @created 05-Mai-2017 11:06:00
 */
public abstract class Graph {

	/**
	 * Beschreibt die Breite, Höhe und Anzahl Knoten des Netzwerks. Das Netzwerk
	 * enthält Breite*Höhe Knoten. Wobei Höhe = Breite gilt.
	 */
	protected int netzwerkBreite;

	protected Knoten[] knoten; // Speichert alle Knoten des Netzwerks


	
	/*
	public Graph(int netzwerkBreite) {
		this.netzwerkBreite = netzwerkBreite; // Netzwerkbreite festlegen

		knoten = new Knoten[netzwerkBreite * netzwerkBreite]; // Array zum Speichern der Knoten des Netzwerks erstellen
		
		for (int i = 0; i < knoten.length; i++) {
			knoten[i] = new Knoten(i); // Knoten des Netzwerks erstellen
		}

		this.netzwerkInitialisieren();
	}
	*/

	public void finalize() throws Throwable {

	}

	public int getAnzahlSendeoperationenImNetzwerk() {
		int anzSendeoperationen = 0;
		for(int i=0; i<knoten.length; i++){
			anzSendeoperationen += knoten[i].getAnzahlSendeoperationen();
		}
		
		return anzSendeoperationen;
	}

	public int getEmpfangsoperationenImNetzwerk() {
		int anzEmpfangsoperationen = 0;
		for(int i=0; i<knoten.length; i++){
			anzEmpfangsoperationen += knoten[i].getAnzahlEmpfangsoperationen();
		}
		return anzEmpfangsoperationen;
	}

	public void netzwerkInitialisieren() {

		for (int i = 0; i < knoten.length; i++) { // Knoten seine Nachbarn
													// zuweisen
			int knotenID = knoten[i].getID();
			if (netzwerkBreite >= 2) {
				if (knotenID == 0 || knotenID == this.netzwerkBreite - 1
						|| knotenID == netzwerkBreite * (netzwerkBreite - 1)
						|| knotenID == this.netzwerkBreite * this.netzwerkBreite - 1) {

					if (knotenID == 0) { // Eckknoten links oben
						knoten[i].nachbarKnotenHinzufuegen(knoten[i + 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[netzwerkBreite]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[netzwerkBreite + 1]);
					}

					if (knotenID == this.netzwerkBreite - 1) { // Eckknoten oben
																// rechts
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[2 * netzwerkBreite - 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[2 * netzwerkBreite - 2]);
					}

					if (knotenID == netzwerkBreite * (netzwerkBreite - 1)) { // Eckknoten
																				// unten
																				// links
						knoten[i].nachbarKnotenHinzufuegen(knoten[i + 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite + 1]);
					}

					if (knotenID == netzwerkBreite * netzwerkBreite - 1) { // Eckknoten
																			// unten
																			// rechts
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite - 1]);
					}
				} else {
					if (knotenID < netzwerkBreite) { // Randknoten oben
						knoten[i].nachbarKnotenHinzufuegen(knoten[i - 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i + 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite - 1]);
						knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite + 1]);

					} else {
						if (knotenID > netzwerkBreite * (netzwerkBreite - 1)) { // Randknoten
																				// unten
							knoten[i].nachbarKnotenHinzufuegen(knoten[i - 1]);
							knoten[i].nachbarKnotenHinzufuegen(knoten[i + 1]);
							knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite]);
							knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite - 1]);
							knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite + 1]);
						} else {
							if (knotenID % netzwerkBreite == 0) { // Randknoten
																	// links
								knoten[i].nachbarKnotenHinzufuegen(knoten[i + 1]);
								knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite]);
								knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite + 1]);
								knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite]);
								knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite + 1]);
							} else {
								if ((knotenID + 1) % netzwerkBreite == 0) { // Randknoten
																			// rechts
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite - 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite - 1]);
								} else { // Knoten in der Mitte des Feldes
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i + 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite - 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i - netzwerkBreite + 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite - 1]);
									knoten[i].nachbarKnotenHinzufuegen(knoten[i + netzwerkBreite + 1]);
								}
							}
						}
					}
				}
			}
		}
	}

	public String toString() {
		String returnString = "";

		for (int i = 0; i < knoten.length; i++) {
			returnString = returnString + knoten[i].toString() + "\n";
		}

		return returnString;
	}

}// end Graph