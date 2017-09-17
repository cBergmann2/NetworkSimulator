package Simulator;

/**
 * Abstract class evaluation unit
 * This class should be specialized for every routing scheme
 * @author Christoph Bergmann
 *
 */
public abstract class EvaluationUnit {
	
	public abstract void evaluateSpeedAnalysis();
	
	public abstract void evaluateCostAnalysis();
	
	public abstract void evaluateNetworkLivetimeStaticSendBehavior();

}
