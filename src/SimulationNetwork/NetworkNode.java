package SimulationNetwork;

import java.util.LinkedList;

public class NetworkNode {
	protected int id;
	protected LinkedList<NetworkNode> connectedNodes;
	protected long elapsedTimeSinceLastReception;
	protected Message incommingMsg;
	protected Message outgoingMsg;
	protected boolean nodeAlive;
	protected LinkedList<Message> inputBuffer;
	protected LinkedList<Message> outputBuffer;
	protected long availableEnery;				//Available energy in nAs
	
	
	public NetworkNode(int id){
		this.id = id;
		connectedNodes = new LinkedList<NetworkNode>();
		incommingMsg = null;
		outgoingMsg = null;
		nodeAlive = true;
		inputBuffer = new LinkedList<Message>();
		outputBuffer = new LinkedList<Message>();
		availableEnery = 4600L*3600L*1000L*1000L;	
	}
	
	
	public void performAction(){
		if(nodeAlive){
			if(incommingMsg != null){
				//Currently resiving a message
				if(incommingMsg.getRemainingTime() <= 0){
					//Transmission complete
					inputBuffer.add(incommingMsg);
					incommingMsg = null;
					// TODO: do action
					// TODO: update consumed energy
				}
				else{
					//Transmission is still in progress
					incommingMsg.decreaseRemainingTime(1);
					// TODO: update consumed energy
				}
			}
			else{
				//No reciving process at this time
				if(outgoingMsg != null){
					//Currently transmitting a message
					if(outgoingMsg.remainingTime <= 0){
						//Transmission complete
						outgoingMsg = null;
						// TODO: shedule next action
						// TODO: update consumed energy
					}
					else{
						//Transmission is still in progress
						outgoingMsg.decreaseRemainingTime(1);
						// TODO: update consumed energy
					}
				}
				else{
					
					availableEnery -= 10000;
				}
			}
		}
		
		if(availableEnery <= 0){
			nodeAlive = false;
		}
	}
	
	public void reciveMsg(Message msg){
		if(incommingMsg == null){
			incommingMsg = msg;
		}
		else{
			//collison
			System.out.println("Collision detected at Node " + this.id);
		}
	}
	
	public void sendMsg(Message msg){
		
		for(NetworkNode node: connectedNodes){
			node.reciveMsg(msg);
		}
	}

	public void addNeighbor(NetworkNode neighbor){
		connectedNodes.add(neighbor);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public boolean isNodeAlive() {
		return nodeAlive;
	}

}
