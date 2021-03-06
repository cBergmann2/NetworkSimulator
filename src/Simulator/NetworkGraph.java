package Simulator;

/**
 * Network Graph strucure
 * 
 * @author Christoph Bergmann
 *
 */
public abstract class NetworkGraph {

	protected NetworkNode nodes[];
	protected int networkWidth;
	protected long lifetime;

	protected int collisions;

	protected boolean fourNeighborhood = true;

	public NetworkGraph() {

	}

	/**
	 * Connects the nodes based on the adjusted network structure
	 */
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

	/**
	 * Returns the correct network lifetime if it is updated by the simulator
	 * @return lifetime of the network
	 */
	public long getLifetime() {
		return lifetime;
	}

	/**
	 * Set the lifetime of the network
	 * This method should be updated every simulation step
	 * @param lifetime
	 */
	public void setLifetime(long lifetime) {
		this.lifetime = lifetime;
	}

	/**
	 * Get all nodes from this network
	 * @return NetworkNode[] array of all nodes
	 */
	public NetworkNode[] getNetworkNodes() {
		return nodes;
	}

	/**
	 * 
	 * @return number of collisions
	 */
	public int getCollisions() {
		return collisions;
	}

	/**
	 * Add a detected collision
	 */
	public void addCollision() {
		this.collisions++;
	}

	/**
	 * 
	 * @return true if the network uses a four neighborhood structure
	 */
	public boolean isFourNeighborhood() {
		return fourNeighborhood;
	}

	/**
	 * Set the netowk structure
	 * @param fourNeighborhood
	 */
	public void setFourNeighborhood(boolean fourNeighborhood) {
		this.fourNeighborhood = fourNeighborhood;
	}
}
