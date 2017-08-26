package SimulationNetwork;

import java.util.LinkedList;

import Flooding.FloodingNetworkGraph;

public abstract class Simulator {

	protected static int NODE_EXECUTION_TIME = 2;

	protected long networkLifetime;

	protected long consumedEnergyInTransmissionMode;
	protected long consumedEnergyInReciveMode;
	protected long consumedEnergyInIdleMode;

	protected int collisions;

	private int numberOfInactiveNodes = 0;

	protected double averageTimeInIdleMode;
	protected double averageTimeInReciveMode;
	protected double averageTimeInTransmissionMode;
	protected double averageTimeWaitingForMediumAccesPermission;

	protected NetworkGraph graph;

	/**
	 * Calculates time between start and end of transmission process
	 * 
	 * @param distance
	 *            Distance between source and destination
	 * @return Time between start and end of transmission process
	 */
	public long speedAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId) {

		networkLifetime = 0;
		this.graph = graph;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		// char dataToSend[] = { 'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L',
		// 'D' };
		char dataToSend[] = { 'A' };

		PayloadMessage msg = new PayloadMessage(0, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe network node

			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

		} while (networkNodes[destinationNodeId].getNumberOfRecivedPayloadMessages() == 0);

		long transmissionTime = networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getEndTransmissionTime()
				- networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getStartTransmissionTime();

		System.out.println(networkLifetime + ": destination has msg received. Transmission time: " + transmissionTime);

		calculateAverageNodeTimes(graph.getNetworkNodes());

		return networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getTransmissionTime();
	}

	/**
	 * Calculates energy costs for all nodes in the network to deliver a payload
	 * message.
	 * 
	 * @param graph
	 * @param networkWidth
	 * @param sourceNodeID
	 * @param destinationNodeID
	 * @return consumed energy in nAs;
	 */
	public long energyCostAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId) {
		long consumedEnergy = 0L;
		boolean transmissionInNetworkDetected;

		consumedEnergyInIdleMode = 0L;
		consumedEnergyInReciveMode = 0L;
		consumedEnergyInTransmissionMode = 0L;

		networkLifetime = 0;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		char dataToSend[] = { 'A' };

		PayloadMessage msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

			// Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
				if (id != destinationNodeId) {
					if (networkNodes[id].getOutgoingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					if (networkNodes[id].getIncomingMessage() != null) {
						transmissionInNetworkDetected = true;
						break;
					}
					if (networkNodes[id].getOutputBufferSize() > 0) {
						transmissionInNetworkDetected = true;
						break;
					}
				}
			}

		} while (transmissionInNetworkDetected
				|| networkNodes[destinationNodeId].getNumberOfRecivedPayloadMessages() == 0);

		for (int id = 0; id < networkNodes.length; id++) {
			consumedEnergyInIdleMode += networkNodes[id].getConsumedEnergyInIdleMode();
			// System.out.println("Node " + id + ": consumedEnergy idle: " +
			// networkNodes[id].getConsumedEnergyInIdleMode() + ", time in
			// idleMode " + (networkNodes[id].getIdleTime()
			// +networkNodes[id].getWaitingTimeForMediumAccesPermission()) + ",
			// time in recive mode "+ networkNodes[id].getReciveTime() + ", time
			// in transmit mode " + networkNodes[id].getTransmissionTime() );
			consumedEnergyInReciveMode += networkNodes[id].getConsumedEnergyInReciveMode();
			consumedEnergyInTransmissionMode += networkNodes[id].getConsumedEnergyInTransmissionMode();
			// System.out.println("Node " + id + ": energy consumption in
			// transmission mode = " +
			// networkNodes[id].getConsumedEnergyInTransmissionMode());
		}

		calculateAverageNodeTimes(graph.getNetworkNodes());

		// System.out.println("Consumed energy idle mode: " +
		// consumedEnergyInIdleMode + ", time in idle mode: " +
		// (this.averageTimeInIdleMode +
		// this.averageTimeWaitingForMediumAccesPermission));
		// System.out.println("Consumed energy recive mode: " +
		// consumedEnergyInReciveMode + ", time in recive mode: " +
		// this.averageTimeInTransmissionMode);
		// System.out.println("Consumed energy transmit mode: " +
		// consumedEnergyInTransmissionMode + ", time in transmit mode: " +
		// this.averageTimeInReciveMode);

		return (consumedEnergyInTransmissionMode);
	}

	/**
	 * Run network with given parameter to determine duration until first node
	 * is out of power
	 * 
	 * @param networkWidth
	 *            sqrt(Number of Nodes)
	 * @param sendProbability
	 *            Probability to send Data, when node is in idle mode
	 * @return duration until first node is out of power
	 */
	public long lifetimeAnalysisStochasticBehavior(NetworkGraph graph, int networkWidth, double sendProbability) {
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		do {

			for (int id = 0; id < networkNodes.length; id++) {
				// Generate random transmission of data
				networkNodes[id].generateRandomTransmissionLoad(sendProbability, networkNodes.length);
			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (allNodesAlive(networkNodes));

		System.out.println("Network Lifetime:" + networkLifetime / 1000 / 60 / 60 / 24 + " Tage bzw "
				+ networkLifetime / 1000 + " Sekunden.");

		return networkLifetime;
	}

	/**
	 * Run network with given parameter to determine duration until first node
	 * is out of power
	 * 
	 * @param networkWidth
	 *            sqrt(Number of Nodes)
	 * @param sendProbability
	 *            Probability to send Data, when node is in idle mode
	 * @return duration until first node is out of power
	 */
	public long lifetimeAnalysisStaticBehavior(NetworkGraph graph, int networkWidth, int transmissionPeriod,
			int payloadSize) {
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		do {

			for (int id = 0; id < networkNodes.length; id++) {
				// Generate static transmission of data
				networkNodes[id].generateTransmissionEveryTSeconds(transmissionPeriod, NODE_EXECUTION_TIME,
						networkNodes.length, payloadSize);
			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (allNodesAlive(networkNodes));

		calculateAverageNodeTimes(graph.getNetworkNodes());

		int recivedPayloadMsg = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			recivedPayloadMsg += networkNodes[id].getNumberOfRecivedPayloadMessages();
		}

		System.out.println("Network Lifetime:" + networkLifetime / 1000 / 60 / 60 / 24 + " Tage bzw "
				+ networkLifetime / 1000 + " Sekunden. Recived PayloadMsg: " + recivedPayloadMsg);

		return networkLifetime;
	}

	/**
	 * Run network with given parameter to determine duration until first node
	 * is out of power. All Nodes will send their data to one destination
	 * 
	 * @param networkWidth
	 *            sqrt(Number of Nodes)
	 * @param sendProbability
	 *            Probability to send Data, when node is in idle mode
	 * @return duration until first node is out of power
	 */
	public long lifetimeAnalysisStaticBehaviorOneDestination(NetworkGraph graph, int networkWidth,
			int transmissionPeriod, int payloadSize) {
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;

		int destinationNode = networkWidth / 2;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
			networkNodes[id].setDestinationNode(destinationNode);
		}

		networkNodes[destinationNode].setBatteryPowered(false);

		do {

			for (int id = 0; id < networkNodes.length; id++) {
				// Generate static transmission of data
				networkNodes[id].generateTransmissionEveryTSeconds(transmissionPeriod, NODE_EXECUTION_TIME,
						networkNodes.length, payloadSize);
			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (allNodesAlive(networkNodes));

		calculateAverageNodeTimes(graph.getNetworkNodes());

		int recivedPayloadMsg = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			recivedPayloadMsg += networkNodes[id].getNumberOfRecivedPayloadMessages();
		}

		System.out.println("Network Lifetime:" + networkLifetime / 1000 / 60 / 60 / 24 + " Tage bzw "
				+ networkLifetime / 1000 + " Sekunden. Recived PayloadMsg: " + recivedPayloadMsg);

		return networkLifetime;
	}

	public long lifetimeAnalysisRandomSorceAndDest(NetworkGraph graph, int networkWidth, int transmissionPeriod,
			int payloadSize, int maxPairs) {
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;

		LinkedList<Integer> availableNodes = new LinkedList<Integer>();
		LinkedList<CommunicationPair> communicationPairs = new LinkedList<CommunicationPair>();

		char dataToSend[] = { 'A' };

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
			availableNodes.add(id);
		}

		do {

			// check for complete transmissions
			LinkedList<CommunicationPair> deletablePairs = new LinkedList<CommunicationPair>();
			for (CommunicationPair pair : communicationPairs) {
				// if(networkNodes[pair.getDestination()].getLastRecivedPayloadMessage()
				// != null){
				// if(networkNodes[pair.getDestination()].getLastRecivedPayloadMessage().getPayloadSourceAdress()
				// == pair.getSource()){
				if (pair.getStartTransmissionTime() + (transmissionPeriod * 1000) < networkLifetime) {
					pair.incrementTransmittedMessages();
					if (pair.getNumberTransmittedMessages() < 3) {
						PayloadMessage msg = new PayloadMessage(pair.getSource(), pair.getDestination(), dataToSend);
						msg.setPayloadHash(networkLifetime);
						networkNodes[pair.getSource()].startSendingProcess(msg);
					} else {
						availableNodes.add(pair.getSource());
						availableNodes.add(pair.getDestination());
						deletablePairs.add(pair);
					}
				}
				// }
				// }
			}
			for (CommunicationPair pair : deletablePairs) {
				communicationPairs.remove(pair);
			}

			// Generate Transmission
			while (communicationPairs.size() < maxPairs && availableNodes.size() >= 2) {

				// Select source randomly
				int sourceNo = (int) (Math.random() * availableNodes.size());
				int source = availableNodes.remove(sourceNo);

				// Select destination randomly
				int destNo = (int) (Math.random() * availableNodes.size());
				int dest = availableNodes.remove(destNo);

				if (source < networkNodes.length && dest < networkNodes.length) {

					communicationPairs.add(new CommunicationPair(source, dest, networkLifetime));

					// Generate and send payload message
					PayloadMessage msg = new PayloadMessage(source, dest, dataToSend);
					msg.setPayloadHash(networkLifetime);
					networkNodes[source].startSendingProcess(msg);
				}

			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (allNodesAlive(networkNodes));

		int numberReceivedPayloadMsg = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			numberReceivedPayloadMsg += networkNodes[id].getNumberOfRecivedPayloadMessages();
		}

		System.out.println("Received Payloadmsg: " + numberReceivedPayloadMsg);

		return networkLifetime;

	}

	/**
	 * Run network with given parameter to determine duration until the network
	 * is partitioned
	 * 
	 * @param networkWidth
	 * @param sendProbability
	 * @return duration until network is partitioned
	 */
	public long partitioningAnalysis(NetworkGraph graph, int networkWidth, int transmissionPeriod, int payloadSize) {
		networkLifetime = 0;
		int simulatedMinutes = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;
		numberOfInactiveNodes = 0;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		do {

			for (int id = 0; id < networkNodes.length; id++) {
				// Generate static transmission of data
				networkNodes[id].generateTransmissionEveryTSeconds(transmissionPeriod, NODE_EXECUTION_TIME,
						networkNodes.length, payloadSize);
			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (60000) == 0) {
				simulatedMinutes++;
				// System.out.println("Simulated minutes: " + simulatedMinutes);
			}

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (!isNetworkPartitioned(graph));

		calculateAverageNodeTimes(graph.getNetworkNodes());

		System.out.println("Network Lifetime:" + networkLifetime / 1000 / 60 / 60 / 24 + " Tage bzw "
				+ networkLifetime / 1000 + " Sekunden.");

		return networkLifetime;
	}

	/**
	 * Run network with given parameter to determine duration until the network
	 * is partitioned
	 * 
	 * @param networkWidth
	 * @param sendProbability
	 * @return duration until network is partitioned
	 */
	public long partitioningAnalysisOnePayloadmessageDestination(NetworkGraph graph, int networkWidth,
			int transmissionPeriod, int payloadSize) {
		networkLifetime = 0;
		int simulatedMinutes = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;
		this.numberOfInactiveNodes = 0;

		int destinationNode = networkWidth / 2;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
			networkNodes[id].setDestinationNode(destinationNode);
		}

		networkNodes[destinationNode].setBatteryPowered(false);

		do {

			for (int id = 0; id < networkNodes.length; id++) {
				// Generate static transmission of data
				networkNodes[id].generateTransmissionEveryTSeconds(transmissionPeriod, NODE_EXECUTION_TIME,
						networkNodes.length, payloadSize);
			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (60000) == 0) {
				simulatedMinutes++;
				// System.out.println("Simulated minutes: " + simulatedMinutes);
			}

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (!isNetworkPartitioned(graph));

		calculateAverageNodeTimes(graph.getNetworkNodes());

		System.out.println("Network Lifetime:" + networkLifetime / 1000 / 60 / 60 / 24 + " Tage bzw "
				+ networkLifetime / 1000 + " Sekunden.");

		return networkLifetime;
	}

	public long partitioningAnalysisRandomSorceAndDest(FloodingNetworkGraph graph, int networkWidth,
			int transmissionPeriod, int payloadSize, int maxPairs) {

		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;

		LinkedList<Integer> availableNodes = new LinkedList<Integer>();
		LinkedList<CommunicationPair> communicationPairs = new LinkedList<CommunicationPair>();

		char dataToSend[] = { 'A' };

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
			availableNodes.add(id);
		}

		do {

			// check for complete transmissions
			LinkedList<CommunicationPair> deletablePairs = new LinkedList<CommunicationPair>();
			for (CommunicationPair pair : communicationPairs) {

				if(!networkNodes[pair.getSource()].isNodeAlive() || !networkNodes[pair.getDestination()].isNodeAlive()){
					if(networkNodes[pair.getSource()].isNodeAlive()){
						availableNodes.add(pair.getSource());
					}
					
					if(networkNodes[pair.getDestination()].isNodeAlive()){
						availableNodes.add(pair.getDestination());
					}
					
					deletablePairs.add(pair);
				}
				else{
					if (pair.getStartTransmissionTime() + (transmissionPeriod * 1000) < networkLifetime) {
						pair.incrementTransmittedMessages();
						if (pair.getNumberTransmittedMessages() < 3) {
							PayloadMessage msg = new PayloadMessage(pair.getSource(), pair.getDestination(), dataToSend);
							msg.setPayloadHash(networkLifetime);
							networkNodes[pair.getSource()].startSendingProcess(msg);
						} else {
							availableNodes.add(pair.getSource());
							availableNodes.add(pair.getDestination());
							deletablePairs.add(pair);
						}
					}
				}
				
				
			}
			for (CommunicationPair pair : deletablePairs) {
				communicationPairs.remove(pair);
			}

			// Generate Transmission
			while (communicationPairs.size() < maxPairs && availableNodes.size() >= 2) {

				// Select source randomly
				int sourceNo = (int) (Math.random() * availableNodes.size());
				int source = availableNodes.remove(sourceNo);

				// Select destination randomly
				int destNo = (int) (Math.random() * availableNodes.size());
				int dest = availableNodes.remove(destNo);

				if (source < networkNodes.length && dest < networkNodes.length) {

					communicationPairs.add(new CommunicationPair(source, dest, networkLifetime));

					// Generate and send payload message
					PayloadMessage msg = new PayloadMessage(source, dest, dataToSend);
					msg.setPayloadHash(networkLifetime);
					networkNodes[source].startSendingProcess(msg);
				}

			}

			// performe network nodes
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
			networkLifetime += NODE_EXECUTION_TIME;

			if (networkLifetime % (3600000) == 0) {
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}

			if (networkLifetime % (86400000) == 0) {
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}

		} while (!isNetworkPartitioned(graph));

		int numberReceivedPayloadMsg = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			numberReceivedPayloadMsg += networkNodes[id].getNumberOfRecivedPayloadMessages();
		}

		System.out.println("Received Payloadmsg: " + numberReceivedPayloadMsg);

		return networkLifetime;
	}

	private boolean isNetworkPartitioned(NetworkGraph graph) {
		LinkedList<Integer> inactiveNodes = getInactiveNodes(graph.getNetworkNodes());
		int numberOfInactiveNodes = inactiveNodes.size();
		if ((numberOfInactiveNodes >= 2) && (numberOfInactiveNodes > this.numberOfInactiveNodes)) {
			System.out
					.print("Partitioning analysis: " + numberOfInactiveNodes + " nodes are inactive. Inactive Nodes: ");
			for (int node : inactiveNodes) {
				System.out.print(" " + node);
			}
			System.out.println();

			NetworkNode networkNodes[] = graph.getNetworkNodes();

			// Check if only one or none node is active
			if ((numberOfInactiveNodes == (graph.getNetworkNodes().length - 1))
					|| (numberOfInactiveNodes == (graph.getNetworkNodes().length))) {
				return true;
			}

			//
			for (NetworkNode node : networkNodes) {
				if (node.isNodeAlive()) {
					if (!checkIfNodeCanReachAllAliveNodes(node.getId(), graph)) {
						// System.out.println("Node " + node.getId() + " can't
						// reach all other alive nodes");
						return true;
					}
				}
			}
		}
		this.numberOfInactiveNodes = numberOfInactiveNodes;
		return false;
	}

	private boolean checkIfNodeCanReachAllAliveNodes(int selectedNode, NetworkGraph graph) {

		NetworkNode networkNodes[] = graph.getNetworkNodes();

		// Create list of alive nodes
		LinkedList<AliveNode> aliveNodeList = new LinkedList<AliveNode>();
		for (NetworkNode node : networkNodes) {
			if (node.isNodeAlive()) {
				aliveNodeList.add(new AliveNode(node.getId()));
			}
		}

		while (selectedNode != -1) {

			// Set all alive nodes from current node as reachable
			for (NetworkNode node : networkNodes[selectedNode].getConnectedNodes()) {
				if (node.isNodeAlive()) {
					for (AliveNode aliveNode : aliveNodeList) {
						if (aliveNode.getNodeID() == node.getId()) {
							aliveNode.setReachable(true);
							break;
						}
					}
				}
			}

			// set current node as reachable and processed
			for (AliveNode node : aliveNodeList) {
				if (node.getNodeID() == selectedNode) {
					node.setReachable(true);
					node.setProcessed(true);
					break;
				}
			}

			// search next reachable and not processed node in aliveNodeList
			selectedNode = -1;
			for (AliveNode node : aliveNodeList) {
				if (node.isReachable() && !node.isProcessed()) {
					selectedNode = node.getNodeID();
					break;
				}
			}
		}

		// check if all alive nodes are reachable
		for (AliveNode node : aliveNodeList) {
			if (!node.isReachable()) {
				return false;
			}
		}

		return true;
	}

	protected LinkedList<Integer> getInactiveNodes(NetworkNode networkNodes[]) {
		LinkedList<Integer> inactiveNodes = new LinkedList<Integer>();
		for (NetworkNode node : networkNodes) {
			if (!node.isNodeAlive()) {
				inactiveNodes.add(node.getId());
			}
		}
		return inactiveNodes;
	}

	protected int getNumberOfInactiveNodes(NetworkNode networkNodes[]) {
		int inactiveNodes = 0;

		for (NetworkNode node : networkNodes) {
			if (!node.isNodeAlive()) {
				inactiveNodes++;
			}
		}
		return inactiveNodes;
	}

	protected boolean allNodesAlive(NetworkNode networkNodes[]) {
		for (NetworkNode node : networkNodes) {
			if (!node.isNodeAlive()) {

				System.out.println("Node " + node.getId() + " is down: " + "Number transmitted msg : "
						+ node.getNumberTransmittedPayloadMsg() + " "
						+ +((double) node.getIdleTime() / (double) networkLifetime) + "% idle time"
						+ ((double) node.getReciveTime() / (double) networkLifetime) + "% recive time"
						+ ((double) node.getTransmissionTime() / (double) networkLifetime) + "% transmit time"
						+ ((double) node.getWaitingTimeForMediumAccesPermission() / (double) networkLifetime)
						+ "% waiting for medium access time");
				return false;
			}
		}
		return true;
	}

	protected void calculateAverageNodeTimes(NetworkNode networkNodes[]) {

		this.averageTimeInIdleMode = 0.0;
		this.averageTimeInReciveMode = 0.0;
		this.averageTimeInTransmissionMode = 0.0;
		this.averageTimeWaitingForMediumAccesPermission = 0.0;

		for (NetworkNode node : networkNodes) {
			this.averageTimeInIdleMode += node.getIdleTime() / (double) networkLifetime;
			this.averageTimeInReciveMode += node.getReciveTime() / (double) networkLifetime;
			this.averageTimeInTransmissionMode += node.getTransmissionTime() / (double) networkLifetime;
			this.averageTimeWaitingForMediumAccesPermission += node.getWaitingTimeForMediumAccesPermission()
					/ (double) networkLifetime;
		}
		this.averageTimeInIdleMode /= networkNodes.length;
		this.averageTimeInReciveMode /= networkNodes.length;
		this.averageTimeInTransmissionMode /= networkNodes.length;
		this.averageTimeWaitingForMediumAccesPermission /= networkNodes.length;
	}

	public long getNetworkLifetime() {
		return networkLifetime;
	}

	public long getConsumedEnergyInTransmissionMode() {
		return consumedEnergyInTransmissionMode;
	}

	public long getConsumedEnergyInReciveMode() {
		return consumedEnergyInReciveMode;
	}

	public long getConsumedEnergyInIdleMode() {
		return consumedEnergyInIdleMode;
	}

	public int getCollisions() {
		return collisions;
	}

	protected void setCollisions(int collisions) {
		this.collisions = collisions;
	}

	public double getAverageTimeInIdleMode() {
		return averageTimeInIdleMode;
	}

	public double getAverageTimeInReciveMode() {
		return averageTimeInReciveMode;
	}

	public double getAverageTimeInTransmissionMode() {
		return averageTimeInTransmissionMode;
	}

	public double getAverageTimeWaitingForMediumAccesPermission() {
		return averageTimeWaitingForMediumAccesPermission;
	}

	public void resetTransmissionUnitFromAllNodes() {
		for (NetworkNode node : this.graph.getNetworkNodes()) {
			node.resetTransmissionUnit();
		}
	}

	public int getNumberTransmittedPayloadMsg() {
		int countPayloadMsg = 0;

		for (NetworkNode node : graph.getNetworkNodes()) {
			countPayloadMsg += node.getNumberTransmittedPayloadMsg();
		}

		return countPayloadMsg;
	}
}
