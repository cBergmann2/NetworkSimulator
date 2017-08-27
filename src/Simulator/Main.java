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
		//floodingEvaluationUnit.evaluateNetworkPartitioningAnalysisOneDestination(8);		
		//floodingEvaluationUnit.evaluateNetworkPartioningAnaylsisRandomSorceAndDest(80, 10);
		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		//aodvEvaluationUnit.evaluateSpeedAnalysis();
		//aodvEvaluationUnit.evaluateCostAnalysis();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(8);

		
		AodvmEvaluationUnit aodvmEvaluationUnit = new AodvmEvaluationUnit();
		//aodvmEvaluationUnit.evaluateSpeedAnalysis();
		//aodvmEvaluationUnit.evaluateCostAnalysis();
		//aodvmEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvmEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//aodvmEvaluationUnit.evaluateNetworkPartitioningAnalysis(8);
		
		DsdvEvaluationUnit dsdvEvaluationUnit = new DsdvEvaluationUnit();
		//dsdvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//dsdvEvaluationUnit.evaluateSpeedAnalysis();
		//dsdvEvaluationUnit.evaluateCostAnalysis();
		//dsdvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//dsdvEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//dsdvEvaluationUnit.evaluateNetworkPartitioningAnalysis(8);
		
		OlsrEvaluationUnit olsrEvaluationUnit = new OlsrEvaluationUnit();
		//olsrEvaluationUnit.evaluateSpeedAnalysis();
		//olsrEvaluationUnit.evaluateCostAnalysis();
		//olsrEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//olsrEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//olsrEvaluationUnit.evaluateNetworkPartitioningAnalysisOneDestination(80);
		olsrEvaluationUnit.evaluateNetworkPartioningAnaylsisRandomSorceAndDest(80, 10);
		
		EadvEvaluationUnit eadvEvaluationUnit = new EadvEvaluationUnit();
		//eadvEvaluationUnit.evaluateSpeedAnalysis();
		//eadvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//eadvEvaluationUnit.evaluateCostAnalysis();
		//eadvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//eadvEvaluationUnit.evaluateNetworkPartitioningAnalysis(8);
	}
}

