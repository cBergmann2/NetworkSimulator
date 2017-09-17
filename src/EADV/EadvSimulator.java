package EADV;

import Simulator.IR_Receiver;
import Simulator.NetworkNode;
import Simulator.PayloadMessage;
import Simulator.Simulator;

/**
 * Simulator for EADV routing scheme
 * @author Christoph
 *
 */
public class EadvSimulator extends Simulator {

	private long msgTransmissionTime;
	private long energyCostsForPropagationNetworkStructure;

	/**
	 * Performs the speed analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		EadvNetworkGraph graph = new EadvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		boolean transmissionInNetworkDetected = false;

		this.networkLifetime = 0L;

		for (int id = 0; id < networkNodes.length; id++) {
			((EadvNetworkNode) networkNodes[id]).setSimulator(this);
		}

		// set data sink
		((EadvNetworkNode) networkNodes[destinationNodeId]).setNodeIsDataSink(true);
		// start initial broadcast
		((EadvNetworkNode) networkNodes[destinationNodeId]).sendInitialBroadcast();

		// Propagate network structure
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
				if (networkNodes[id].getOutputBufferSize() > 0) {
					transmissionInNetworkDetected = true;
					break;
				}

				for (IR_Receiver irReceiver : networkNodes[id].getIrReceiver()) {
					if (irReceiver.isRecevingAMessage()) {
						transmissionInNetworkDetected = true;
					}
				}
				
				if(((EadvNetworkNode)networkNodes[id]).getRoutingTableSize() == 0){
					if(id != destinationNodeId){
						transmissionInNetworkDetected = true;
					}
				}
			}
		} while (transmissionInNetworkDetected);

		// network structure known in all nodes
		// start transmitting msg from source to destination

		System.out.println("Send payload message");

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();

		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}

	/**
	 * Performs the speed analysis when the network starts.
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long speedAnalysisWhenNetworkStarts(int networkWidth, int sourceNodeId, int destinationNodeId) {

		EadvNetworkGraph graph = new EadvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();

		for (NetworkNode node : networkNodes) {
			node.setSimulator(this);
		}

		// set data sink
		((EadvNetworkNode) networkNodes[destinationNodeId]).setNodeIsDataSink(true);
		// start initial broadcast
		((EadvNetworkNode) networkNodes[destinationNodeId]).sendInitialBroadcast();

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());

		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();

		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime();

		return msgTransmissionTime;
	}

	/**
	 * Performs the energy cost analysis for the given parameter
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		EadvNetworkGraph graph = new EadvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		this.networkLifetime = 0L;
		boolean transmissionInNetworkDetected = false;

		for (int id = 0; id < networkNodes.length; id++) {
			((EadvNetworkNode) networkNodes[id]).setSimulator(this);
		}

		// set data sink
		((EadvNetworkNode) networkNodes[destinationNodeId]).setNodeIsDataSink(true);
		// start initial broadcast
		((EadvNetworkNode) networkNodes[destinationNodeId]).sendInitialBroadcast();

		// Propagate network structure
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
				if (networkNodes[id].getOutputBufferSize() > 0) {
					transmissionInNetworkDetected = true;
					break;
				}
				
				for (IR_Receiver irReceiver : networkNodes[id].getIrReceiver()) {
					if (irReceiver.isRecevingAMessage()) {
						transmissionInNetworkDetected = true;
					}
				}
				

			}
		} while (transmissionInNetworkDetected);

		// network structure known in all nodes

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

		EadvNetworkGraph graph = new EadvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();

		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].setSimulator(this);
		}

		((EadvNetworkNode) networkNodes[networkWidth / 2]).setNodeIsDataSink(true);
		// start initial broadcast
		((EadvNetworkNode) networkNodes[networkWidth / 2]).sendInitialBroadcast();

		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod,
				payloadSize));
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
		EadvNetworkGraph graph = new EadvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();

		for (int id = 0; id < networkNodes.length; id++) {
			((EadvNetworkNode) networkNodes[id]).setSimulator(this);
		}

		((EadvNetworkNode) networkNodes[networkWidth / 2]).setNodeIsDataSink(true);
		// start initial broadcast
		((EadvNetworkNode) networkNodes[networkWidth / 2]).sendInitialBroadcast();

		return this.partitioningAnalysisOnePayloadmessageDestination(graph, networkWidth, transmissionPeriod,
				payloadSize);
	}

	/**
	 * Get the message transmission time
	 * Note: you have to call the speed analysis first
	 * @return message transmission time
	 */
	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

	/**
	 * Get the energy costs for propagate the network structure in the network
	 * Note: you have to call the cost analysis first
	 * @return
	 */
	public long getEnergyCostsForPropagationNetworkStructure() {
		return energyCostsForPropagationNetworkStructure;
	}
}
