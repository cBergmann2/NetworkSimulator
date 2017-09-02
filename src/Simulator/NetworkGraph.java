package Simulator;

public abstract class NetworkGraph {

	protected NetworkNode nodes[];
	protected int networkWidth;
	protected long lifetime;

	protected int collisions;

	protected boolean fourNeighborhood = true;

	public NetworkGraph() {

	}

	public void initializeNetworkStructure() {

		if (fourNeighborhood) {
			for (int i = 0; i < nodes.length; i++) { // Set neighbors of each
														// node
				int knotenID = nodes[i].getId();
				if (networkWidth >= 2) {
					if (knotenID == 0 || knotenID == this.networkWidth - 1
							|| knotenID == networkWidth * (networkWidth - 1)
							|| knotenID == this.networkWidth * this.networkWidth - 1) {

						if (knotenID == 0) { // Eckknoten links oben
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[networkWidth]);
							nodes[i].addDestinationIrReceiver(nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
							nodes[i].addDestinationIrReceiver(nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
						}

						if (knotenID == this.networkWidth - 1) { // Eckknoten
																	// oben
							// rechts
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[2 * networkWidth - 1]);
							nodes[i].addDestinationIrReceiver(nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
							nodes[i].addDestinationIrReceiver(nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
						}

						if (knotenID == networkWidth * (networkWidth - 1)) { // Eckknoten
							// unten
							// links
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[i - networkWidth]);
							nodes[i].addDestinationIrReceiver(nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
							nodes[i].addDestinationIrReceiver(nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
						}

						if (knotenID == networkWidth * networkWidth - 1) { // Eckknoten
							// unten
							// rechts
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[i - networkWidth]);
							nodes[i].addDestinationIrReceiver(nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
							nodes[i].addDestinationIrReceiver(nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
						}
					} else {
						if (knotenID < networkWidth) { // Randknoten oben
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[i + networkWidth]);
							nodes[i].addDestinationIrReceiver(nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
							nodes[i].addDestinationIrReceiver(nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
							nodes[i].addDestinationIrReceiver(nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));

						} else {
							if (knotenID > networkWidth * (networkWidth - 1)) { // Randknoten
								// unten
								nodes[i].addNeighbor(nodes[i - 1]);
								nodes[i].addNeighbor(nodes[i + 1]);
								nodes[i].addNeighbor(nodes[i - networkWidth]);
								nodes[i].addDestinationIrReceiver(
										nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
								nodes[i].addDestinationIrReceiver(
										nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
								nodes[i].addDestinationIrReceiver(
										nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
							} else {
								if (knotenID % networkWidth == 0) { // Randknoten
									// links
									nodes[i].addNeighbor(nodes[i + 1]);
									nodes[i].addNeighbor(nodes[i - networkWidth]);
									nodes[i].addNeighbor(nodes[i + networkWidth]);
									nodes[i].addDestinationIrReceiver(
											nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
									nodes[i].addDestinationIrReceiver(
											nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
									nodes[i].addDestinationIrReceiver(
											nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
								} else {
									if ((knotenID + 1) % networkWidth == 0) { // Randknoten
										// rechts
										nodes[i].addNeighbor(nodes[i - 1]);
										nodes[i].addNeighbor(nodes[i - networkWidth]);
										nodes[i].addNeighbor(nodes[i + networkWidth]);
										nodes[i].addDestinationIrReceiver(
												nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
									} else { // Knoten in der Mitte des Feldes
										nodes[i].addNeighbor(nodes[i - 1]);
										nodes[i].addNeighbor(nodes[i + 1]);
										nodes[i].addNeighbor(nodes[i - networkWidth]);
										nodes[i].addNeighbor(nodes[i + networkWidth]);
										nodes[i].addDestinationIrReceiver(
												nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
										nodes[i].addDestinationIrReceiver(
												nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));

									}
								}
							}
						}
					}
				}
			}
		} else {
			//8er neighborhood
			for (int i = 0; i < nodes.length; i++) { // Set neighbors of each node
				int knotenID = nodes[i].getId();
				if (networkWidth >= 2) {
					if (knotenID == 0 || knotenID == this.networkWidth - 1
							|| knotenID == networkWidth * (networkWidth - 1)
							|| knotenID == this.networkWidth * this.networkWidth - 1) {

						if (knotenID == 0) { // Eckknoten links oben
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[networkWidth]);
							nodes[i].addNeighbor(nodes[networkWidth + 1]);
							nodes[i].addDestinationIrReceiver(nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
							nodes[i].addDestinationIrReceiver(
									nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
							nodes[i].addDestinationIrReceiver(nodes[networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_WEST));
						}

						if (knotenID == this.networkWidth - 1) { // Eckknoten
							// oben
							// rechts
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[2 * networkWidth - 1]);
							nodes[i].addNeighbor(nodes[2 * networkWidth - 2]);
							nodes[i].addDestinationIrReceiver(
									nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
							nodes[i].addDestinationIrReceiver(nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
							nodes[i].addDestinationIrReceiver(nodes[2 * networkWidth - 2].getIrReceiver(IR_Receiver.RECEIVER_NORTH_EAST));
						}

						if (knotenID == networkWidth * (networkWidth - 1)) { // Eckknoten
							// unten
							// links
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[i - networkWidth]);
							nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
							nodes[i].addDestinationIrReceiver(
									nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
							nodes[i].addDestinationIrReceiver(nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
							nodes[i].addDestinationIrReceiver(nodes[i - networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_WEST));
						}

						if (knotenID == networkWidth * networkWidth - 1) { // Eckknoten
							// unten
							// rechts
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[i - networkWidth]);
							nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
							nodes[i].addDestinationIrReceiver(
									nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
							nodes[i].addDestinationIrReceiver(nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
							nodes[i].addDestinationIrReceiver(nodes[i - networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_EAST));
						}
					} else {
						if (knotenID < networkWidth) { // Randknoten oben
							nodes[i].addNeighbor(nodes[i - 1]);
							nodes[i].addNeighbor(nodes[i + 1]);
							nodes[i].addNeighbor(nodes[i + networkWidth]);
							nodes[i].addNeighbor(nodes[i + networkWidth - 1]);
							nodes[i].addNeighbor(nodes[i + networkWidth + 1]);
							nodes[i].addDestinationIrReceiver(nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
							nodes[i].addDestinationIrReceiver(
									nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
							nodes[i].addDestinationIrReceiver(nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
							nodes[i].addDestinationIrReceiver(nodes[i + networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_EAST));
							nodes[i].addDestinationIrReceiver(nodes[i + networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_WEST));

						} else {
							if (knotenID > networkWidth * (networkWidth - 1)) { // Randknoten
								// unten
								nodes[i].addNeighbor(nodes[i - 1]);
								nodes[i].addNeighbor(nodes[i + 1]);
								nodes[i].addNeighbor(nodes[i - networkWidth]);
								nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
								nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
								nodes[i].addDestinationIrReceiver(
										nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
								nodes[i].addDestinationIrReceiver(
										nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
								nodes[i].addDestinationIrReceiver(
										nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
								nodes[i].addDestinationIrReceiver(nodes[i - networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_EAST));
								nodes[i].addDestinationIrReceiver(nodes[i - networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_WEST));

							} else {
								if (knotenID % networkWidth == 0) { // Randknoten
									// links
									nodes[i].addNeighbor(nodes[i + 1]);
									nodes[i].addNeighbor(nodes[i - networkWidth]);
									nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
									nodes[i].addNeighbor(nodes[i + networkWidth]);
									nodes[i].addNeighbor(nodes[i + networkWidth + 1]);
									nodes[i].addDestinationIrReceiver(
											nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
									nodes[i].addDestinationIrReceiver(
											nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
									nodes[i].addDestinationIrReceiver(
											nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
									nodes[i].addDestinationIrReceiver(nodes[i - networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_EAST));
									nodes[i].addDestinationIrReceiver(nodes[i + networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_EAST));
								} else {
									if ((knotenID + 1) % networkWidth == 0) { // Randknoten
										// rechts
										nodes[i].addNeighbor(nodes[i - 1]);
										nodes[i].addNeighbor(nodes[i - networkWidth]);
										nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
										nodes[i].addNeighbor(nodes[i + networkWidth]);
										nodes[i].addNeighbor(nodes[i + networkWidth - 1]);
										nodes[i].addDestinationIrReceiver(
												nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
										nodes[i].addDestinationIrReceiver(nodes[i - networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_WEST));
										nodes[i].addDestinationIrReceiver(nodes[i + networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_WEST));
									} else { // Knoten in der Mitte des Feldes
										nodes[i].addNeighbor(nodes[i - 1]);
										nodes[i].addNeighbor(nodes[i + 1]);
										nodes[i].addNeighbor(nodes[i - networkWidth]);
										nodes[i].addNeighbor(nodes[i + networkWidth]);
										nodes[i].addNeighbor(nodes[i - networkWidth - 1]);
										nodes[i].addNeighbor(nodes[i - networkWidth + 1]);
										nodes[i].addNeighbor(nodes[i + networkWidth - 1]);
										nodes[i].addNeighbor(nodes[i + networkWidth + 1]);
										nodes[i].addDestinationIrReceiver(
												nodes[i - networkWidth].getIrReceiver(IR_Receiver.RECEIVER_SOUTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i + 1].getIrReceiver(IR_Receiver.RECEIVER_WEST));
										nodes[i].addDestinationIrReceiver(
												nodes[i + networkWidth].getIrReceiver(IR_Receiver.RECEIVER_NORTH));
										nodes[i].addDestinationIrReceiver(
												nodes[i - 1].getIrReceiver(IR_Receiver.RECEIVER_EAST));
										nodes[i].addDestinationIrReceiver(nodes[i - networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_WEST));
										nodes[i].addDestinationIrReceiver(nodes[i + networkWidth - 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_WEST));
										nodes[i].addDestinationIrReceiver(nodes[i - networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_SOUTH_EAST));
										nodes[i].addDestinationIrReceiver(nodes[i + networkWidth + 1].getIrReceiver(IR_Receiver.RECEIVER_NORTH_EAST));
									}
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

	public int getCollisions() {
		return collisions;
	}

	public void addCollision() {
		this.collisions++;
	}

	public boolean isFourNeighborhood() {
		return fourNeighborhood;
	}

	public void setFourNeighborhood(boolean fourNeighborhood) {
		this.fourNeighborhood = fourNeighborhood;
	}
}
