package Simulator;
//import AODV.AodvEvaluationUnit;
import AODV_RFC.AodvEvaluationUnit;
import Flooding.FloodingEvaluationUnit;

public class Main {

	
	public static void main(String args[]){

		
		FloodingEvaluationUnit floodingEvaluationUnit = new FloodingEvaluationUnit();
		//floodingEvaluationUnit.evaluateSpeedAnalysis();
		//floodingEvaluationUnit.evaluateKostAnalysis();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStochasticSendBehavior();
		
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10);
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1000);
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(100);
		
		//floodingEvaluationUnit.evaluateNetworkPartitioningAnalysis(1000);
		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		aodvEvaluationUnit.evaluateSpeedAnalysis();
		//aodvEvaluationUnit.evaluateCostAnalysis();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(100);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1000);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10000);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(1);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(10);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(100);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(1000);
		
	}
}
