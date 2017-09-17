package DSDV;

import Simulator.NetworkNode;
import Simulator.PayloadMessage;
import Simulator.Simulator;

public class DsdvSimulator extends Simulator {

	private long msgTransmissionTime;
	private long energyCostsForPropagationNetworkStructure;
	private long energyCostsForPeriodicUpdate;

	/**
	 * Performs the speed analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		boolean transmissionInNetworkDetected = false;

		this.networkLifetime = 0L;

		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setSimulator(this);
		}

		// Propagate network structure
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
		} while (((DsdvNetworkNode) networkNodes[0]).getForwardTable().size() < networkNodes.length);

		// network structure known in all nodes
		// suspend propagation of network structure
		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setPropagateNetworkStructure(false);
		}

		// wait until all transmission processes are completed
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
					if (networkNodes[id].getOutputBufferSize() > 0) {
						transmissionInNetworkDetected = true;
						break;
					}
				}
			}
		} while (transmissionInNetworkDetected);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();

		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}

	/**
	 * Performs the speed analysis when the network starts for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long speedAnalysisWhenNetworkStarts(int networkWidth, int sourceNodeId, int destinationNodeId) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();

		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}
	
	/**
	 * Performs the energy cost analysis for adding a node to the network
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysisAddNewNode(int networkWidth){
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		this.networkLifetime = 0L;
		boolean transmissionInNetworkDetected = false;
		boolean informationInNetworkCommunicated = false;
		
		System.out.println("Energy cost analysis for event based update message");

		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setSimulator(this);
		}

		// Propagate network structure
		do {
			// Performe 1 ms every iteration
			for (int id = 1; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			// Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
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

		} while (transmissionInNetworkDetected);
		
		//Reset batterys from all nodes
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}
		
		for (int id = 1; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setPropagateNetworkStructure(false);
		}
		
		//Add node to network
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			// Check if there is any transmission process in the network
			informationInNetworkCommunicated = true;
			for (int id = 1; id < networkNodes.length; id++) {
				if(((DsdvNetworkNode)networkNodes[id]).getForwardTable().size() < networkNodes.length){
					informationInNetworkCommunicated = false;
				}
			}
			
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
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

		} while (!informationInNetworkCommunicated || transmissionInNetworkDetected);
		
		long energyCosts = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			energyCosts +=  networkNodes[id].getConsumedEnergyInTransmissionMode();
			System.out.println("Node " + id + ": energy consumption in transmission mode = " + networkNodes[id].getConsumedEnergyInTransmissionMode());
		}
		
		
		return energyCosts;
		
	}

	/**
	 * Performs the energy cost analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		this.networkLifetime = 0L;
		boolean transmissionInNetworkDetected = false;

		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setSimulator(this);
		}

		// Propagate network structure
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
		} while (((DsdvNetworkNode) networkNodes[0]).getForwardTable().size() < networkNodes.length);

		// network structure known in all nodes
		// suspend propagation of network structure
		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setPropagateNetworkStructure(false);
		}

		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

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

		} while (transmissionInNetworkDetected);
		
		// Sum energy costs for propagation of network structure
		long energyCostsPropagation = 0L;
		for (int id = 0; id < networkNodes.length; id++) {
			//energyCostsPropagation += networkNodes[id].getConsumedEnergyInIdleMode();
			//energyCostsPropagation += networkNodes[id].getConsumedEnergyInReciveMode();
			energyCostsPropagation += networkNodes[id].getConsumedEnergyInTransmissionMode();
		}
		this.energyCostsForPropagationNetworkStructure = energyCostsPropagation;

		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}
		
		//Calculate energy costs of periodic update messages
		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setPropagateNetworkStructure(true);
			networkNodes[id].resetBattery();
			((DsdvNetworkNode) networkNodes[id]).forceTheNodeToSendPeriodicUpdateMessage();	
		}
		
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

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

		} while (transmissionInNetworkDetected);
		
		energyCostsForPeriodicUpdate = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			energyCostsForPeriodicUpdate +=  networkNodes[id].getConsumedEnergyInTransmissionMode();
		}
			
		
		// reset network nodes
		// suspend propagation of network structure
		for (int id = 0; id < networkNodes.length; id++) {
			((DsdvNetworkNode) networkNodes[id]).setPropagateNetworkStructure(false);
		}

		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			// Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
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

		} while (transmissionInNetworkDetected);
		
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}

		//Calclulate energy costs for one message transmission
		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		return energyCosts;
	}

	/**
	 * Lifetime analysis, every Node sends its messages to one destination
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long lifetimeAnalysisStaticSendBehaviorOneDestination(int networkWidth, int transmissionPeriod,
			int payloadSize) {

		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod,
				payloadSize));
	}
	
	/**
	 * Lifetime analysis, message source and destination is changing after every message transfer
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @param maxPairs
	 * @return
	 */
	public long lifetimeAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod,
			int payloadSize, int maxPairs) {
		
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		return this.lifetimeAnalysisRandomSorceAndDest(graph, networkWidth, transmissionPeriod, payloadSize, maxPairs);
	}

	/**
	 * Partitioning analysis,  every Node sends its messages to one destination
	 * 
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long partitioningAnalysis(int networkWidth, int transmissionPeriod, int payloadSize) {
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		return this.partitioningAnalysisOnePayloadmessageDestination(graph, networkWidth, transmissionPeriod,
				payloadSize);
	}
	
	/**
	 * Partitioning analysis, message source and destination is changing after every message transfer
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @param maxPairs
	 * @return
	 */
	public long partitioningAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod, int payloadSize, int maxPairs) {
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);

		return this.partitioningAnalysisRandomSorceAndDest(graph, networkWidth, transmissionPeriod, payloadSize, maxPairs);
	}

	/**
	 * Get the message transmission time
	 * The message transmission time is determined in speed analysis 
	 * @return message transmission time
	 */
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

	/**
	 * Get the Energy costs for propagate the network structure
	 * You have to run first the cost analysis to get a correct value
	 * @return energy costs for propagate the network structure
	 */
	public long getEnergyCostsForPropagationNetworkStructure() {
		return energyCostsForPropagationNetworkStructure;
	}

	/**
	 * Get the Energy costs for propagate periodic updates
	 * @return
	 */
	public long getEnergyCostsForPeriodicUpdate() {
		return energyCostsForPeriodicUpdate;
	}

	/**
	 * Perform lifetime analysis without any transmission of payload messages
	 * @param networkWidth
	 * @return network lifetime without any transmission of payload messages
	 */
	public double lifetimeAnalysisWithoutPayloadMessageTransmission(int networkWidth) {
		
		DsdvNetworkGraph graph = new DsdvNetworkGraph(networkWidth);
		
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}
		
		do {
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


		return networkLifetime;
	}

}
