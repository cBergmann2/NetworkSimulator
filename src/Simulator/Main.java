package Simulator;
import AODVM.AodvmEvaluationUnit;
//import AODV.AodvEvaluationUnit;
import AODV_RFC.AodvEvaluationUnit;
import DSDV.DsdvEvaluationUnit;
import EADV.EadvEvaluationUnit;
import Flooding.FloodingEvaluationUnit;
import OLSR.OlsrEvaluationUnit;

public class Main {

	
	public static void main(String args[]){

		
		FloodingEvaluationUnit floodingEvaluationUnit = new FloodingEvaluationUnit();
		//floodingEvaluationUnit.evaluateSpeedAnalysis();
		//floodingEvaluationUnit.evaluateCostAnalysis();
		//floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(8);
		//floodingEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//floodingEvaluationUnit.evaluateNetworkPartitioningAnalysis(8);
		//floodingEvaluationUnit.evaluateNetworkLivetimeStochasticSendBehavior();
		floodingEvaluationUnit.evaluateNetworkPartioningAnaylsisRandomSorceAndDest(80, 10);
		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		//aodvEvaluationUnit.evaluateSpeedAnalysis();
		//aodvEvaluationUnit.evaluateCostAnalysis();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);

		
		AodvmEvaluationUnit aodvmEvaluationUnit = new AodvmEvaluationUnit();
		//aodvmEvaluationUnit.evaluateSpeedAnalysis();
		//aodvmEvaluationUnit.evaluateCostAnalysis();
		//aodvmEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvmEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//aodvmEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		
		DsdvEvaluationUnit dsdvEvaluationUnit = new DsdvEvaluationUnit();
		//dsdvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//dsdvEvaluationUnit.evaluateSpeedAnalysis();
		//dsdvEvaluationUnit.evaluateCostAnalysis();
		//dsdvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		dsdvEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//dsdvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		
		OlsrEvaluationUnit olsrEvaluationUnit = new OlsrEvaluationUnit();
		//olsrEvaluationUnit.evaluateSpeedAnalysis();
		//olsrEvaluationUnit.evaluateCostAnalysis();
		//olsrEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(8);
		olsrEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		
		
		EadvEvaluationUnit eadvEvaluationUnit = new EadvEvaluationUnit();
		//eadvEvaluationUnit.evaluateSpeedAnalysis();
		//eadvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//eadvEvaluationUnit.evaluateCostAnalysis();
		//eadvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//eadvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
	}
}

