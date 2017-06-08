package DSDV;

import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;

public class DsdvNetworkNode extends NetworkNode{

	public DsdvNetworkNode(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	protected void performeTimeDependentTasks(){
		
		//Send routing updates to neighbors
	}

	@Override
	public void processRecivedMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSendingProcess(PayloadMessage tmpMsg) {
		// TODO Auto-generated method stub
		
	}

}
