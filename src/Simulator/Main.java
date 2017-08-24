package Simulator;
import AODVM.AodvmEvaluationUnit;
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
		floodingEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(8);
		//floodingEvaluationUnit.evaluateNetworkPartitioningAnalysis(8);

		
		AodvEvaluationUnit aodvEvaluationUnit = new AodvEvaluationUnit();
		//aodvEvaluationUnit.evaluateSpeedAnalysis();
		//aodvEvaluationUnit.evaluateCostAnalysis();
		//aodvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);

		
		AodvmEvaluationUnit aodvmEvaluationUnit = new AodvmEvaluationUnit();
		//aodvmEvaluationUnit.evaluateSpeedAnalysis();
		//aodvmEvaluationUnit.evaluateCostAnalysis();
		//aodvmEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//aodvmEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		
		DsdvEvaluationUnit dsdvEvaluationUnit = new DsdvEvaluationUnit();
		//dsdvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//dsdvEvaluationUnit.evaluateSpeedAnalysis();
		//dsdvEvaluationUnit.evaluateCostAnalysis();
		//dsdvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//dsdvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
		
		
		EadvEvaluationUnit eadvEvaluationUnit = new EadvEvaluationUnit();
		//eadvEvaluationUnit.evaluateSpeedAnalysis();
		//eadvEvaluationUnit.evaluateSpeedAnalysisWhenNetworkStarts();
		//eadvEvaluationUnit.evaluateCostAnalysis();
		//eadvEvaluationUnit.evaluateNetworkLivetimeStaticSendBehaviorOneDestination(80);
		//eadvEvaluationUnit.evaluateNetworkPartitioningAnalysis(80);
	}
}
