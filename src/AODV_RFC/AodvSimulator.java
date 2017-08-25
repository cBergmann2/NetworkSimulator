package AODV_RFC;

import AODVM.AodvmNetworkGraph;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class AodvSimulator extends Simulator{
	
	private double percentageTransmittedRREQMsg;
	private double percentageTransmittedRREPMsg;
	private double percentageTransmittedPayloadMsg;
	
	private long msgTransmissionTime; 

	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		long time = this.speedAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);

		setCollisions(graph.getCollisions());
		
		PayloadMessage transmittedMsg = graph.getNetworkNodes()[destinationNodeId].getLastRecivedPayloadMessage();
		
		this.msgTransmissionTime = transmittedMsg.getEndTransmissionTime() - transmittedMsg.getStartTransmissionTime();

		return time;
	}
	
	public long energyCostAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {

		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		long energyCosts = this.energyCostAnalysis(graph, networkWidth, sourceNodeId, destinationNodeId);
		
		setCollisions(graph.getCollisions());
		
		calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return energyCosts;
	}
	
	/**
	 * Energy cost analysis without route discovery process
	 * @param networkWidth
	 * @param sourceNodeId
	 * @param destinationNodeId
	 * @return
	 */
	public long energyCostAnalysisWithoutRDP(int networkWidth, int sourceNodeId, int destinationNodeId) {
		
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
		this.graph = graph;
		
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

		char dataToSend[] = { 'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D' };

		PayloadMessage msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);

		do {
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;
		}while(((AodvNetworkNode)networkNodes[sourceNodeId]).getNumberRecivedRREPdMsg() == 0);
		
		//Reset transmission units and battery of all nodes
		this.resetTransmissionUnitFromAllNodes();
		for (int id = 0; id < networkNodes.length; id++) {
			networkNodes[id].resetBattery();
		}
		
		msg = new PayloadMessage(sourceNodeId, (destinationNodeId), dataToSend);
		networkNodes[sourceNodeId].startSendingProcess(msg);
		
		
		do{
			// Performe NODE_EXECUTION_TIME ms every iteration
			for (int id = 0; id < networkNodes.length; id++) {
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}

			networkLifetime += NODE_EXECUTION_TIME;
			
			//Check if there is any transmission process in the network
			transmissionInNetworkDetected = false;
			for (int id = 0; id < networkNodes.length; id++) {
				if(id != destinationNodeId){
					if(networkNodes[id].getOutgoingMessage() != null){
						transmissionInNetworkDetected = true;
						break;
					}
					if(networkNodes[id].getIncomingMessage() != null){
						transmissionInNetworkDetected = true;
						break;
					}
					if(networkNodes[id].getOutputBufferSize() > 0){
						transmissionInNetworkDetected = true;
						break;
					}
				}
			}

		} while (transmissionInNetworkDetected);

		for (int id = 0; id < networkNodes.length; id++) {
			/*consumedEnergy += NetworkNode.NODE_BATTERY_ENERGY_FOR_ONE_DAY_IN_IDLE_MODE
					- networkNodes[id].getAvailableEnery();
			*/
			
			
			consumedEnergyInIdleMode += networkNodes[id].getConsumedEnergyInIdleMode();
			//System.out.println("Node " + id + ": consumedEnergy idle: " + networkNodes[id].getConsumedEnergyInIdleMode());
			consumedEnergyInReciveMode += networkNodes[id].getConsumedEnergyInReciveMode();
			consumedEnergyInTransmissionMode += networkNodes[id].getConsumedEnergyInTransmissionMode();
			//System.out.println("Node " + id + ": time in transmissionMode: " + networkNodes[id].getTransmissionTime()  + ", consumedEnergy transmission: " + networkNodes[id].getConsumedEnergyInTransmissionMode());
			
		}
		

		return (consumedEnergyInIdleMode + consumedEnergyInReciveMode + consumedEnergyInTransmissionMode);
		
	}
	
	public long lifetimeAnalysisStaticSendBehavior(int networkWidth, int transmissionPeriod,int payloadSize){
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		this.graph = graph;
		
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for(int id=0; id<networkNodes.length; id++){
			networkNodes[id].setSimulator(this);
		}
			
		do{
					
			for(int id=0; id<networkNodes.length; id++){
				//Generate static transmission of data
				((AodvNetworkNode)networkNodes[id]).generateTransmissionEveryTSecondsChangingDestination(transmissionPeriod, NODE_EXECUTION_TIME, networkNodes.length, payloadSize);
			}
			

			// performe network nodes
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].performAction(NODE_EXECUTION_TIME);
			}
			
			networkLifetime += NODE_EXECUTION_TIME;
			
			
			if(networkLifetime % (3600000) == 0){
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}
			
			
			if(networkLifetime % (86400000) == 0){
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}
		

		}while(allNodesAlive(networkNodes));
		
		calculateAverageNodeTimes(graph.getNetworkNodes());
		
		int recivedPayloadMsg = 0;
		for(int id=0; id<networkNodes.length; id++){
			recivedPayloadMsg += networkNodes[id].getNumberOfRecivedPayloadMessages();
		}
		
	
		System.out.println("Network Lifetime:" + networkLifetime/1000/60/60/24 + " Tage bzw "+ networkLifetime/1000 + " Sekunden. Recived PayloadMsg: " + recivedPayloadMsg);
	
		
		int sendPayloadMsg = 0;
		int sendRoutingMsg = 0;
		for(int id=0; id<networkNodes.length; id++){
			sendPayloadMsg += ((AodvNetworkNode)networkNodes[id]).getNumberTransmittedPayloadMsg();
			sendRoutingMsg += ((AodvNetworkNode)networkNodes[id]).getNumberTransmittedRREPMsg() + ((AodvNetworkNode)networkNodes[id]).getNumberTransmittedRREQMsg();
		}
		System.out.println("Number send payload msg: " + sendPayloadMsg + ", number send routing msg: " + sendRoutingMsg);
		
		
		calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return networkLifetime;
	}
	
	public long lifetimeAnalysisStaticSendBehaviorChangingDestination(int networkWidth, int transmissionPeriod,int payloadSIze){
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
		long lifetime = this.lifetimeAnalysisStaticBehavior(graph, networkWidth, transmissionPeriod, payloadSIze); 

		NetworkNode networkNodes[] = graph.getNetworkNodes();
		
		int sendPayloadMsg = 0;
		int sendRoutingMsg = 0;
		for(int id=0; id<networkNodes.length; id++){
			sendPayloadMsg += ((AodvNetworkNode)networkNodes[id]).getNumberTransmittedPayloadMsg();
			sendRoutingMsg += ((AodvNetworkNode)networkNodes[id]).getNumberTransmittedRREPMsg() + ((AodvNetworkNode)networkNodes[id]).getNumberTransmittedRREQMsg();
		}
		System.out.println("Number send payload msg: " + sendPayloadMsg + ", number send routing msg: " + sendRoutingMsg);
		
		
		calculatSpreadOfTransmittedMessages(graph.getNetworkNodes());
		
		return lifetime;
	}

	public long lifetimeAnalysisStaticSendBehaviorOneDestination(int networkWidth, int transmissionPeriod,int payloadSize){
		
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
		return (this.lifetimeAnalysisStaticBehaviorOneDestination(graph, networkWidth, transmissionPeriod, payloadSize));
	}
	
	public long lifetimeAnalysisRandomSorceAndDest(int networkWidth, int transmissionPeriod,
			int payloadSize, int maxPairs) {
		
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);

		return this.lifetimeAnalysisRandomSorceAndDest(graph, networkWidth, transmissionPeriod, payloadSize, maxPairs);
	}
	
	public long partitioningAnalysis(int networkWidth, int transmissionPeriod, int payloadSize) {
		AodvNetworkGraph graph = new AodvNetworkGraph(networkWidth);
		
		
		return this.partitioningAnalysisOnePayloadmessageDestination(graph, networkWidth, transmissionPeriod, payloadSize);
	}

	public long getMsgTransmissionTime() {
		return msgTransmissionTime;
	}

	private void calculatSpreadOfTransmittedMessages(NetworkNode networkNodes[]){
		int numberTransmittedMsg = 0;
		int numberTransmittedRREQMsg = 0;
		int numberTransmittedRREPMsg = 0;
		int numberTransmittedPayloadMsg = 0;
		this.percentageTransmittedRREQMsg = 0.0;
		this.percentageTransmittedRREPMsg = 0.0;
		this.percentageTransmittedPayloadMsg = 0.0;

		for(NetworkNode node: networkNodes){
			numberTransmittedRREQMsg += ((AodvNetworkNode)node).getNumberTransmittedRREQMsg();
			numberTransmittedRREPMsg += ((AodvNetworkNode)node).getNumberTransmittedRREPMsg();
			numberTransmittedPayloadMsg += ((AodvNetworkNode)node).getNumberTransmittedPayloadMsg();
			numberTransmittedMsg += ((AodvNetworkNode)node).getNumberTransmittedRREQMsg() + ((AodvNetworkNode)node).getNumberTransmittedRREPMsg() + ((AodvNetworkNode)node).getNumberTransmittedPayloadMsg();
		}
		
		percentageTransmittedRREQMsg = numberTransmittedRREQMsg / (double)numberTransmittedMsg;
		percentageTransmittedRREPMsg = numberTransmittedRREPMsg / (double)numberTransmittedMsg;
		percentageTransmittedPayloadMsg = numberTransmittedPayloadMsg / (double)numberTransmittedMsg;
	}

	public double getPercentageTransmittedRREQMsg() {
		return percentageTransmittedRREQMsg;
	}

	public double getPercentageTransmittedRREPMsg() {
		return percentageTransmittedRREPMsg;
	}

	public double getPercentageTransmittedPayloadMsg() {
		return percentageTransmittedPayloadMsg;
	}


	
}
