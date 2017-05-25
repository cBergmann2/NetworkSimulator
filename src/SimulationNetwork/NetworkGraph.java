package SimulationNetwork;

public abstract class NetworkGraph {
	
	protected NetworkNode nodes[];
	protected int networkWidth;
	protected long lifetime;
	
	public NetworkGraph(){

	}
	
	public void initializeNetworkStructure(){
		for (int i = 0; i < nodes.length; i++) { //Set neighbors of each node
			int knotenID = nodes[i].getId();
			if (networkWidth >= 2) {
				if (knotenID == 0 || knotenID == this.networkWidth - 1
						|| knotenID == networkWidth * (networkWidth - 1)
						|| knotenID == this.networkWidth * this.networkWidth - 1) {

					if (knotenID == 0) { // Eckknoten links oben
						nodes[i].addNeighbor(nodes[i + 1]);
						nodes[i].addNeighbor(nodes[networkWidth]);
						nodes[i].addNeighbor(nodes[networkWidth + 1]);
					}

					if (knotenID == this.networkWidth - 1) { // Eckknoten oben
						// rechts
						nodes[i].addNeighbor(nodes[i - 1]);
						nodes[i].addNeighbor(nodes[2 * networkWidth - 1]);
						nodes[i].addNeighbor(nodes[2 * networkWidth - 2]);
					}

					if (knotenID == networkWidth * (networkWidth - 1)) { // Eckknoten
						// unten
						// links
						nodes[i].addNeighbor(nodes[i + 1]);
						nodes[i].addNeighbor(nodes[i - networkWidth]);
						nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
					}

					if (knotenID == networkWidth * networkWidth - 1) { // Eckknoten
						// unten
						// rechts
						nodes[i].addNeighbor(nodes[i - 1]);
						nodes[i].addNeighbor(nodes[i - networkWidth]);
						nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
					}
				} else {
					if (knotenID < networkWidth) { // Randknoten oben
						nodes[i].addNeighbor(nodes[i - 1]);
						nodes[i].addNeighbor(nodes[i + 1]);
						nodes[i].addNeighbor(nodes[i + networkWidth]);
						nodes[i].addNeighbor(nodes[i + networkWidth - 1]);
						nodes[i].addNeighbor(nodes[i + networkWidth + 1]);

					} else {
						if (knotenID > networkWidth * (networkWidth - 1)) { // Randknoten
							// unten
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[i - networkWidth]);
							nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
							nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
						} else {
							if (knotenID % networkWidth == 0) { // Randknoten
								// links
								nodes[i].addNeighbor(nodes[i + 1]);
								nodes[i].addNeighbor(nodes[i - networkWidth]);
								nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
								nodes[i].addNeighbor(nodes[i + networkWidth]);
								nodes[i].addNeighbor(nodes[i + networkWidth + 1]);
							} else {
								if ((knotenID + 1) % networkWidth == 0) { // Randknoten
									// rechts
									nodes[i].addNeighbor(nodes[i - 1]);
									nodes[i].addNeighbor(nodes[i - networkWidth]);
									nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
									nodes[i].addNeighbor(nodes[i + networkWidth]);
									nodes[i].addNeighbor(nodes[i + networkWidth - 1]);
								} else { // Knoten in der Mitte des Feldes
									nodes[i].addNeighbor(nodes[i - 1]);
									nodes[i].addNeighbor(nodes[i + 1]);
									nodes[i].addNeighbor(nodes[i - networkWidth]);
									nodes[i].addNeighbor(nodes[i + networkWidth]);
									nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
									nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
									nodes[i].addNeighbor(nodes[i + networkWidth - 1]);
									nodes[i].addNeighbor(nodes[i + networkWidth + 1]);
								}
							}
						}
					}
				}
			}
		}
	}

	public long getLifetime() {
		return lifetime;
	}

	public void setLifetime(long lifetime) {
		this.lifetime = lifetime;
	}

	public NetworkNode[] getNetworkNodes() {
		return nodes;
	}
}