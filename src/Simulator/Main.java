package Simulator;
import AODV.AodvEvaluationUnit;
import AODVM.AodvmEvaluationUnit;
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
		//aodvEvaluationUnit.evaluateCostAnalysisRouteDiscoveryProcess();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysisOneDestination(80);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnaylsisRandomSorceAndDest(80,10);

		
		AodvmEvaluationUnit aodvmEvaluationUnit = new AodvmEvaluationUnit();
		//aodvmEvaluationUnit.evaluateSpeedAnalysis();
		//aodvmEvaluationUnit.evaluateCostAnalysis();
		//aodvmEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvmEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//aodvmEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		//aodvmEvaluationUnit.evaluateNetworkPartitioningAnaylsisRandomSorceAndDest(80,10);

		
		DsdvEvaluationUnit dsdvEvaluationUnit = new DsdvEvaluationUnit();
		//dsdvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//dsdvEvaluationUnit.evaluateSpeedAnalysis();
		//dsdvEvaluationUnit.evaluateCostAnalysis();
		//dsdvEvaluationUnit.evaluateNetworkLivetimeWithoutPayloadMessageTransmission();
		//dsdvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//dsdvEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//dsdvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		//dsdvEvaluationUnit.evaluateNetworkPartioningAnaylsisRandomSorceAndDest(80, 10);
		
		OlsrEvaluationUnit olsrEvaluationUnit = new OlsrEvaluationUnit();
		//olsrEvaluationUnit.evaluateSpeedAnalysis();
		//olsrEvaluationUnit.evaluateCostAnalysis();
		//olsrEvaluationUnit.evaluateNetworkLivetimeWithoutPayloadMessageTransmission();
		//olsrEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//olsrEvaluationUnit.evaluateNetworkLivetimeRandomSorceAndDest(80, 10);
		//olsrEvaluationUnit.evaluateNetworkPartitioningAnalysisOneDestination(80);
		//olsrEvaluationUnit.evaluateNetworkPartioningAnaylsisRandomSorceAndDest(8, 10);
		
		EadvEvaluationUnit eadvEvaluationUnit = new EadvEvaluationUnit();
		//eadvEvaluationUnit.evaluateSpeedAnalysis();
		//eadvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//eadvEvaluationUnit.evaluateCostAnalysis();
		//eadvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//eadvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		
		//ComparativeEvaluation.speedAnalysis();
		//ComparativeEvaluation.speedAnalysisWhenNetworkIsInitialized();
		//ComparativeEvaluation.costAnalysis();
		ComparativeEvaluation.costAnalysisRoutingData();
		//ComparativeEvaluation.lifetimeAnalysisOneDestination(80);
		//ComparativeEvaluation.lifetimeAnalysisRandomSorceAndDest(80, 10);
		//ComparativeEvaluation.PartitioningAnalysisOneDestination(80);
		//ComparativeEvaluation.PartitioningAnalysisRandomSourceAndDest(80, 10);
	}
}

