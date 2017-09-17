package OLSR;

import DSDV.DsdvNetworkGraph;
import Flooding.FloodingNetworkGraph;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;
import Simulator.Simulator;

/**
 * Simulator for OLSR routing scheme
 * @author Christoph Bergmann
 *
 */
public class OlsrSimulator extends Simulator {

	private long msgTransmissionTime;
	private int routeDistance;
	private long consumedEnergyForControlMsg;

	/**
	 * Performs the speed analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();

		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return this.networkLifetime;
	}

	/**
	 * Performs the energy cost analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		// calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());

		return energyCosts;
	}

	/**
	 * @return the msgTransmissionTime
	 */
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

	/**
	 * Performs the energy cost analysis without any transmission of control messages
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysisWithoutControlmessages(int networkWidth, int sourceNodeId, int destinationNodeId) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);
		boolean networkStructureDiscovered;
		boolean transmissionInNetworkDetected;
		char dataToSend[] = { 'A' };
		networkLifetime = 0;
		long consumedEnergy = 0L;

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
			((OlsrNetworkNode) networkNodes[id]).setNetworkSize(networkNodes.length);
		}

		// Propagate network structure
		do {
			// Performe 1 ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
			networkLifetime += NODE_EXECUTION_TIME;

			networkStructureDiscovered = true;
			for (int id = 0; id < networkNodes.length; id++) {
				if (!((OlsrNetworkNode) networkNodes[id]).isNetworkStructureDiscovered()) {
					networkStructureDiscovered = false;
					break;
				}
			}
		} while (!networkStructureDiscovered);

		// Stop sending of control messages
		for (int id = 0; id < networkNodes.length; id++) {
			((OlsrNetworkNode) networkNodes[id]).setSendControlMessages(false);
		}

		do {
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

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

		// Sum consumed energy for Hello- and TC-Messages
		this.consumedEnergyForControlMsg = 0;
		for (int id = 0; id < networkNodes.length; id++) {
			this.consumedEnergyForControlMsg += networkNodes[id].getConsumedEnergyInTransmissionMode();
		}

		// Reset battery
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}

		PayloadMessage msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		this.routeDistance = ((OlsrNetworkNode) networkNodes[sourceNodeId]).getRouteTableEntry(destinationNodeId)
				.getDistance();

		do {
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;

		} while (networkNodes[destinationNodeId].getNumberOfRecivedPayloadMessages() == 0);

		for (int id = 0; id < networkNodes.length; id++) {
			consumedEnergy += networkNodes[id].getConsumedEnergyInTransmissionMode();
		}

		return consumedEnergy;
	}

	/**
	 * @return the routeDistance
	 */
	public int getRouteDistance() {
		return routeDistance;
	}

	/**
	 * @return the consumedEnergyForControlMsg
	 */
	public long getConsumedEnergyForControlMsg() {
		return consumedEnergyForControlMsg;
	}

	/**
	 * Perform lifetime analysis without any transmission of payload messages
	 * @param networkWidth
	 * @return network lifetime without any transmission of payload messages
	 */
	public double lifetimeAnalysisWithoutPayloadMessageTransmission(int networkWidth) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

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

	/**
	 * Lifetime analysis, every Node sends its messages to one destination
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long lifetimeAnalysisStaticSendBehaviorOneDestination(int networkWidth, int transmissionPeriod,
			int payloadSize) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod,
				payloadSize));
	}

	/**
	 * Lifetime analysis, every Node sends its messages to one destination
	 * @param networkWidth
	 * @param transmissionPeriod
	 * @param payloadSize
	 * @return
	 */
	public long lifetimeAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod, int payloadSize,
			int maxPairs) {

		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

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
	public long partitioningAnalysisOnePayloadmessageDestination(int networkWidth, int transmissionPeriod,
			int payloadSize) {
		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

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
	public long partitioningAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod, int payloadSize,
			int maxPairs) {
		OlsrNetworkGraph graph = new OlsrNetworkGraph(networkWidth);

		return this.partitioningAnalysisRandomSorceAndDest(graph, networkWidth, transmissionPeriod, payloadSize,
				maxPairs);
	}

}
