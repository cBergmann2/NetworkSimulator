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
		//aodvEvaluationUnit.evaluateCostAnalysis();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1);
		aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10);
		aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(100);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1000);
		aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10000);
		
	}
}
