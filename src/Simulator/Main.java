package Simulator;
import AODV.AodvSimulator;
import MatlabChart.GraphGenerator;
import SimulationNetwork.Simulator;

public class Main {

	
	public static void main(String args[]){
		
		//FlutenSimulation flutenSimulation = new FlutenSimulation();
		//flutenSimulation.simulation();
		
		//AodvSimulator aodvSimulation = new AodvSimulator();
		//aodvSimulation.lifetimeAnalysis(5, 0.001);
		
		/*
		Simulator simulator = new Simulator();
		simulator.lifetimeAnalysis(4, 0.99);*/
		
		
		GraphGenerator graphGenerator = new GraphGenerator();
		graphGenerator.speedAnalysis();
		
	}
}
