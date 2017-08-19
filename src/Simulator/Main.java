package Simulator;
//import AODV.AodvEvaluationUnit;
import AODV_RFC.AodvEvaluationUnit;
import DSDV.DsdvEvaluationUnit;
import EADV.EadvEvaluationUnit;
import Flooding.FloodingEvaluationUnit;

public class Main {

	
	public static void main(String args[]){

		
		FloodingEvaluationUnit floodingEvaluationUnit = new FloodingEvaluationUnit();
		//floodingEvaluationUnit.evaluateSpeedAnalysis();
		//floodingEvaluationUnit.evaluateCostAnalysis();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStochasticSendBehavior();
		
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1);
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(100);
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1000);
		floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(1);
		
		//floodingEvaluationUnit.evaluateNetworkPartitioningAnalysis(1);
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(100);
		//floodingEvaluationUnit.evaluateNetworkPartitioningAnalysisOneDestination(1);
		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		//aodvEvaluationUnit.evaluateSpeedAnalysis();
		//aodvEvaluationUnit.evaluateCostAnalysis();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(100);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(1000);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehavior(10000);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorChangingDestination(100);
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(100);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(1);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(10);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(100);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(1000);
		
		
		DsdvEvaluationUnit dsdvEvaluationUnit = new DsdvEvaluationUnit();
		//dsdvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//dsdvEvaluationUnit.evaluateSpeedAnalysis();
		//dsdvEvaluationUnit.evaluateCostAnalysis();
		//dsdvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(100);
		//dsdvEvaluationUnit.evaluateNetworkPartitioningAnalysis(100);
		
		
		EadvEvaluationUnit eadvEvaluationUnit = new EadvEvaluationUnit();
		//eadvEvaluationUnit.evaluateSpeedAnalysis();
		//eadvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//eadvEvaluationUnit.evaluateCostAnalysis();
		//eadvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(100);
		//eadvEvaluationUnit.evaluateNetworkPartitioningAnalysis(1);
	}
}
