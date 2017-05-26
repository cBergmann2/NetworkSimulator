package Simulator;
import AODV.AodvSimulator;
import Flooding.FloodingEvaluationUnit;
import MatlabChart.GraphGenerator;
import SimulationNetwork.Simulator;

public class Main {

	
	public static void main(String args[]){

		
		FloodingEvaluationUnit floodingEvaluationUnit = new FloodingEvaluationUnit();
		floodingEvaluationUnit.evaluateSpeedAnalysis();
		
	}
}
