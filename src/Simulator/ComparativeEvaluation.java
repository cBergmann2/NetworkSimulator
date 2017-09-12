package Simulator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import AODVM.AodvmSimulator;
import AODV_RFC.AodvSimulator;
import DSDV.DsdvSimulator;
import EADV.EadvSimulator;
import Flooding.FloodingSimulator;
import OLSR.OlsrSimulator;

public class ComparativeEvaluation {
	
	private static final int CHART_WIDTH = 280;
	private static final int CHART_HIGHT = 300;

	private static final int networkWidth[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	
	public static void speedAnalysis(){
		
		double numberOfNodes[] = new double[networkWidth.length];
		
		FloodingSimulator floodingSimulator = new FloodingSimulator();
		DsdvSimulator dsdvSimulator = new DsdvSimulator();
		OlsrSimulator olsrSimulator = new OlsrSimulator();
		AodvSimulator aodvSimulator = new AodvSimulator();
		AodvmSimulator aodvmSimulator = new AodvmSimulator();
		EadvSimulator eadvSimulator = new EadvSimulator();
		
		double floodingTransmissionTime[][] = new double[2][networkWidth.length];
		double dsdvTransmissionTime[][] = new double[2][networkWidth.length];
		double olsrTransmissionTime[][] = new double[2][networkWidth.length];
		double aodvTransmissionTime[][] = new double[2][networkWidth.length];
		double aodvmTransmissionTime[][] = new double[2][networkWidth.length];
		double eadvTransmissionTime[][] = new double[2][networkWidth.length];
		
		System.out.println("Start speed anylsis");
		
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			
			System.out.println("Flooding - " + numberOfNodes[i] + " Nodes");
			floodingTransmissionTime[0][i] = numberOfNodes[i];
			floodingTransmissionTime[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			
			System.out.println("DSDV - " + numberOfNodes[i] + " Nodes");
			dsdvTransmissionTime[0][i] = numberOfNodes[i];
			dsdvTransmissionTime[1][i] = dsdvSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			
			System.out.println("OLSR - " + numberOfNodes[i] + " Nodes");
			olsrTransmissionTime[0][i] = numberOfNodes[i];
			olsrTransmissionTime[1][i] = olsrSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			
			System.out.println("AODV - " + numberOfNodes[i] + " Nodes");
			aodvTransmissionTime[0][i] = numberOfNodes[i];
			aodvTransmissionTime[1][i] = aodvSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			
			System.out.println("AODVM - " + numberOfNodes[i] + " Nodes");
			aodvmTransmissionTime[0][i] = numberOfNodes[i];
			aodvmTransmissionTime[1][i] = aodvmSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			
			System.out.println("EADV - " + numberOfNodes[i] + " Nodes");
			eadvTransmissionTime[0][i] = numberOfNodes[i];
			eadvTransmissionTime[1][i] = eadvSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
		}
		
		//Create plot
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Flooding", floodingTransmissionTime);
		dataset.addSeries("DSDV", dsdvTransmissionTime);
		dataset.addSeries("OLSR", olsrTransmissionTime);
		dataset.addSeries("AODV", aodvTransmissionTime);
		dataset.addSeries("AODVM", aodvmTransmissionTime);
		dataset.addSeries("EADV", eadvTransmissionTime);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [s]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);
		plot.getRenderer().setSeriesPaint(3, Color.BLACK);
		plot.getRenderer().setSeriesPaint(4, Color.BLACK);
		plot.getRenderer().setSeriesPaint(5, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Vergleich/Uebertragungszeit_Netzwerkstart.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void speedAnalysisWhenNetworkIsInitialized(){
		
		double numberOfNodes[] = new double[networkWidth.length];
		
		FloodingSimulator floodingSimulator = new FloodingSimulator();
		DsdvSimulator dsdvSimulator = new DsdvSimulator();
		OlsrSimulator olsrSimulator = new OlsrSimulator();
		AodvSimulator aodvSimulator = new AodvSimulator();
		AodvmSimulator aodvmSimulator = new AodvmSimulator();
		EadvSimulator eadvSimulator = new EadvSimulator();
		
		double floodingTransmissionTime[][] = new double[2][networkWidth.length];
		double dsdvTransmissionTime[][] = new double[2][networkWidth.length];
		double olsrTransmissionTime[][] = new double[2][networkWidth.length];
		double aodvTransmissionTime[][] = new double[2][networkWidth.length];
		double aodvmTransmissionTime[][] = new double[2][networkWidth.length];
		double eadvTransmissionTime[][] = new double[2][networkWidth.length];
		
		System.out.println("Start speed anylsis");
		
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			
			System.out.println("Flooding - " + numberOfNodes[i] + " Nodes");
			floodingTransmissionTime[0][i] = numberOfNodes[i];
			floodingTransmissionTime[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			
			System.out.println("DSDV - " + numberOfNodes[i] + " Nodes");
			dsdvTransmissionTime[0][i] = numberOfNodes[i];
			
			dsdvSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			dsdvTransmissionTime[1][i] = dsdvSimulator.getMsgTransmissionTime() / 1000.0;
			
			System.out.println("OLSR - " + numberOfNodes[i] + " Nodes");
			olsrTransmissionTime[0][i] = numberOfNodes[i];
			olsrSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			olsrTransmissionTime[1][i] = olsrSimulator.getMsgTransmissionTime() / 1000.0;
			
			System.out.println("AODV - " + numberOfNodes[i] + " Nodes");
			aodvTransmissionTime[0][i] = numberOfNodes[i];
			aodvSimulator.speedAnalysis(networkWidth[i], 0,(int) Math.pow(networkWidth[i], 2) - 1);
			aodvTransmissionTime[1][i] = aodvSimulator.getMsgTransmissionTime() / 1000.0;
			
			System.out.println("AODVM - " + numberOfNodes[i] + " Nodes");
			aodvmTransmissionTime[0][i] = numberOfNodes[i];
			aodvmSimulator.speedAnalysis(networkWidth[i], 0, (int) Math.pow(networkWidth[i], 2) - 1);
			aodvmTransmissionTime[1][i] = aodvmSimulator.getMsgTransmissionTime() / 1000.0;
			
			System.out.println("EADV - " + numberOfNodes[i] + " Nodes");
			eadvTransmissionTime[0][i] = numberOfNodes[i];
			eadvSimulator.speedAnalysis(networkWidth[i], 0, (int) Math.pow(networkWidth[i], 2) - 1);
			eadvTransmissionTime[1][i] = eadvSimulator.getMsgTransmissionTime() / 1000.0; 
		}
		
		//Create plot
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Flooding", floodingTransmissionTime);
		dataset.addSeries("DSDV", dsdvTransmissionTime);
		dataset.addSeries("OLSR", olsrTransmissionTime);
		dataset.addSeries("AODV", aodvTransmissionTime);
		dataset.addSeries("AODVM", aodvmTransmissionTime);
		dataset.addSeries("EADV", eadvTransmissionTime);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [s]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);
		plot.getRenderer().setSeriesPaint(3, Color.BLACK);
		plot.getRenderer().setSeriesPaint(4, Color.BLACK);
		plot.getRenderer().setSeriesPaint(5, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Vergleich/Uebertragungszeit_nach_Netzwerkstart.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void costAnalysis(){
		
		double numberOfNodes[] = new double[networkWidth.length];
		
		FloodingSimulator floodingSimulator = new FloodingSimulator();
		DsdvSimulator dsdvSimulator = new DsdvSimulator();
		OlsrSimulator olsrSimulator = new OlsrSimulator();
		AodvSimulator aodvSimulator = new AodvSimulator();
		AodvmSimulator aodvmSimulator = new AodvmSimulator();
		EadvSimulator eadvSimulator = new EadvSimulator();
		
		double floodingConsumedEnergy[][] = new double[2][networkWidth.length];
		double dsdvConsumedEnergy[][] = new double[2][networkWidth.length];
		double olsrConsumedEnergy[][] = new double[2][networkWidth.length];
		double aodvConsumedEnergy[][] = new double[2][networkWidth.length];
		double aodvmConsumedEnergy[][] = new double[2][networkWidth.length];
		double eadvConsumedEnergy[][] = new double[2][networkWidth.length];
		
		System.out.println("Start cost anylsis");
		
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			
			System.out.println("Flooding - " + numberOfNodes[i] + " Nodes");
			floodingConsumedEnergy[0][i] = numberOfNodes[i];
			floodingConsumedEnergy[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			
			System.out.println("DSDV - " + numberOfNodes[i] + " Nodes");
			dsdvConsumedEnergy[0][i] = numberOfNodes[i];
			dsdvConsumedEnergy[1][i] = dsdvSimulator.energyCostAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			
			System.out.println("OLSR - " + numberOfNodes[i] + " Nodes");
			olsrConsumedEnergy[0][i] = numberOfNodes[i];
			olsrConsumedEnergy[1][i] = olsrSimulator.energyCostAnalysisWithoutControlmessages(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			
			System.out.println("AODV - " + numberOfNodes[i] + " Nodes");
			aodvConsumedEnergy[0][i] = numberOfNodes[i];
			aodvConsumedEnergy[1][i] = aodvSimulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			
			System.out.println("AODVM - " + numberOfNodes[i] + " Nodes");
			aodvmConsumedEnergy[0][i] = numberOfNodes[i];
			aodvmConsumedEnergy[1][i] = aodvmSimulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			
			System.out.println("EADV - " + numberOfNodes[i] + " Nodes");
			eadvConsumedEnergy[0][i] = numberOfNodes[i];
			eadvConsumedEnergy[1][i] = eadvSimulator.energyCostAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
		}
		
		//Create plot
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Flooding", floodingConsumedEnergy);
		dataset.addSeries("DSDV", dsdvConsumedEnergy);
		dataset.addSeries("OLSR", olsrConsumedEnergy);
		dataset.addSeries("AODV", aodvConsumedEnergy);
		dataset.addSeries("AODVM", aodvmConsumedEnergy);
		dataset.addSeries("EADV", eadvConsumedEnergy);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);
		plot.getRenderer().setSeriesPaint(3, Color.BLACK);
		plot.getRenderer().setSeriesPaint(4, Color.BLACK);
		plot.getRenderer().setSeriesPaint(5, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Vergleich/KostenNachrichtenuebertragung.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void lifetimeAnalysisOneDestination(int payloadSize){
		
		double numberOfNodes[] = new double[networkWidth.length];
		
		FloodingSimulator floodingSimulator = new FloodingSimulator();
		DsdvSimulator dsdvSimulator = new DsdvSimulator();
		OlsrSimulator olsrSimulator = new OlsrSimulator();
		AodvSimulator aodvSimulator = new AodvSimulator();
		AodvmSimulator aodvmSimulator = new AodvmSimulator();
		EadvSimulator eadvSimulator = new EadvSimulator();
		
		double floodingLifetime[][] = new double[2][networkWidth.length];
		double dsdvLifetime[][] = new double[2][networkWidth.length];
		double olsrLifetime[][] = new double[2][networkWidth.length];
		double aodvLifetime[][] = new double[2][networkWidth.length];
		double aodvmLifetime[][] = new double[2][networkWidth.length];
		double eadvLifetime[][] = new double[2][networkWidth.length];
		
		System.out.println("Start lifetime anylsis");
		
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			
			System.out.println("Flooding - " + numberOfNodes[i] + " Nodes");
			floodingLifetime[0][i] = numberOfNodes[i];
			floodingLifetime[1][i] = floodingSimulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
			
			System.out.println("DSDV - " + numberOfNodes[i] + " Nodes");
			dsdvLifetime[0][i] = numberOfNodes[i];
			dsdvLifetime[1][i] = dsdvSimulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
			
			System.out.println("OLSR - " + numberOfNodes[i] + " Nodes");
			olsrLifetime[0][i] = numberOfNodes[i];
			olsrLifetime[1][i] = olsrSimulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
			
			System.out.println("AODV - " + numberOfNodes[i] + " Nodes");
			aodvLifetime[0][i] = numberOfNodes[i];
			aodvLifetime[1][i] = aodvSimulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
			
			System.out.println("AODVM - " + numberOfNodes[i] + " Nodes");
			aodvmLifetime[0][i] = numberOfNodes[i];
			aodvmLifetime[1][i] = aodvmSimulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
			
			System.out.println("EADV - " + numberOfNodes[i] + " Nodes");
			eadvLifetime[0][i] = numberOfNodes[i];
			eadvLifetime[1][i] = eadvSimulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
		}
		
		//Create plot
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Flooding", floodingLifetime);
		dataset.addSeries("DSDV", dsdvLifetime);
		dataset.addSeries("OLSR", olsrLifetime);
		dataset.addSeries("AODV", aodvLifetime);
		dataset.addSeries("AODVM", aodvmLifetime);
		dataset.addSeries("EADV", eadvLifetime);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Netzwerklebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);
		plot.getRenderer().setSeriesPaint(3, Color.BLACK);
		plot.getRenderer().setSeriesPaint(4, Color.BLACK);
		plot.getRenderer().setSeriesPaint(5, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Vergleich/Lebenszeitanalye.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
