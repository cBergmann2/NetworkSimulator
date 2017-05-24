package Simulator;
import SimulationNetwork.Simulator;

public class Main {

	
	public static void main(String args[]){
		
		//FlutenSimulation flutenSimulation = new FlutenSimulation();
		//flutenSimulation.simulation();
		
		/*AodvSimulation aodvSimulation = new AodvSimulation();
		aodvSimulation.simulation();*/
		
		Simulator simulator = new Simulator();
		simulator.lifetimeAnalysis(3);
		
	}
}
