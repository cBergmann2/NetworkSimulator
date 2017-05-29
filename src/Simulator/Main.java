package Simulator;
import AODV.AodvEvaluationUnit;
import AODV.AodvSimulator_old;
import Flooding.FloodingEvaluationUnit;
import MatlabChart.GraphGenerator;
import SimulationNetwork.Simulator;

public class Main {

	
	public static void main(String args[]){

		
		FloodingEvaluationUnit floodingEvaluationUnit = new FloodingEvaluationUnit();
		//floodingEvaluationUnit.evaluateSpeedAnalysis();
		//floodingEvaluationUnit.evaluateKostAnalysis();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStochasticSendBehavior();
		
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior();
		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		aodvEvaluationUnit.evaluateSpeedAnalysis();
		
	}
}
