package Simulator;
import AODV.AodvEvaluationUnit;
import Flooding.FloodingEvaluationUnit;

public class Main {

	
	public static void main(String args[]){

		
		FloodingEvaluationUnit floodingEvaluationUnit = new FloodingEvaluationUnit();
		//floodingEvaluationUnit.evaluateSpeedAnalysis();
		//floodingEvaluationUnit.evaluateKostAnalysis();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStochasticSendBehavior();
		
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior();
		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		//aodvEvaluationUnit.evaluateSpeedAnalysis();
		aodvEvaluationUnit.evaluateCostAnalysis();
		
	}
}
