package EADV;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import Simulator.EvaluationUnit;

public class EadvEvaluationUnit extends EvaluationUnit {

	private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10};
	//private static final int networkWidth[] = {14};

	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		System.out.println("Start EADV spped anylsis.");
		EadvSimulator simulator = new EadvSimulator();

		double numberOfNodes[] = new double[networkWidth.length];
		double transmissionTime_max[][] = new double[2][networkWidth.length];
		double transmissionTime_max_collisions[][] = new double[2][networkWidth.length];
		double transmissionTime_msg_max[][] = new double[2][networkWidth.length];

		double transmissionTime_min[][] = new double[2][networkWidth.length];
		double transmissionTime_min_collisions[][] = new double[2][networkWidth.length];
		double transmissionTime_msg_min[][] = new double[2][networkWidth.length];

		double transmissionTime_med[][] = new double[2][networkWidth.length];
		double transmissionTime_med_collisions[][] = new double[2][networkWidth.length];
		double transmissionTime_msg_med[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			System.out.println(
					"Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0 + " SinkNode: 1");
			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = simulator.speedAnalysis(networkWidth[i], 0, 1) / 1000.0;
			transmissionTime_min_collisions[0][i] = numberOfNodes[i];
			transmissionTime_min_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_min[0][i] = numberOfNodes[i];
			transmissionTime_msg_min[1][i] = simulator.getMsgTransmissionTime() / 1000.0;
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0
					+ " SinkNode: " + (networkWidth[i] - 1));
			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = simulator.speedAnalysis(networkWidth[i], 0, networkWidth[i] - 1) / 1000.0;
			transmissionTime_med_collisions[0][i] = numberOfNodes[i];
			transmissionTime_med_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_med[0][i] = numberOfNodes[i];
			transmissionTime_msg_med[1][i] = simulator.getMsgTransmissionTime() / 1000.0;
			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0
					+ " SinkNode: " + (int) (Math.pow(networkWidth[i], 2) - 1));
			transmissionTime_max[0][i] = numberOfNodes[i];
			transmissionTime_max[1][i] = simulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			transmissionTime_max_collisions[0][i] = numberOfNodes[i];
			transmissionTime_max_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_max[0][i] = numberOfNodes[i];
			transmissionTime_msg_max[1][i] = simulator.getMsgTransmissionTime() / 1000.0;
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");
		}

		// Transmission Time
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", transmissionTime_max);
		dataset.addSeries("mittlere Distanz", transmissionTime_med);
		dataset.addSeries("minimale Distanz", transmissionTime_min);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [s]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		/*
		try {
			ChartUtilities.saveChartAsPNG(new File("Output/DSDV/DSDV_Uebertragungszeit.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Collisions
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", transmissionTime_max_collisions);
		dataset2.addSeries("mittlere Distanz", transmissionTime_med_collisions);
		dataset2.addSeries("minimale Distanz", transmissionTime_min_collisions);

		// XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Kollisionen");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/DSDV/DSDV_Uebertragungszeit_Kollisionen.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		// Msg Transmission time
		dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", transmissionTime_msg_max);
		dataset.addSeries("mittlere Distanz", transmissionTime_msg_med);
		dataset.addSeries("minimale Distanz", transmissionTime_msg_min);

		xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis = new NumberAxis("Übertragungszeit [s]");
		plot = new XYPlot(dataset, xAxis, yAxis, line);

		chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/EADV/EADV_Uebertragungszeit_Nachricht.png"), chart,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void evaluateSpeedAnalysisWhenNetworkStarts() {
		
		System.out.println("Start DSDV speed anylsis when network starts.");
		EadvSimulator simulator = new EadvSimulator();

		double numberOfNodes[] = new double[networkWidth.length];
		double transmissionTime_max[][] = new double[2][networkWidth.length];

		double transmissionTime_min[][] = new double[2][networkWidth.length];

		double transmissionTime_med[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			System.out.println(
					"Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0 + " SinkNode: 1");
			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = simulator.speedAnalysisWhenNetworkStarts(networkWidth[i], 0, 1) / 1000;
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0
					+ " SinkNode: " + (networkWidth[i] - 1));
			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = simulator.speedAnalysisWhenNetworkStarts(networkWidth[i], 0, networkWidth[i] - 1) / 1000;
			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0
					+ " SinkNode: " + (int) (Math.pow(networkWidth[i], 2) - 1));
			transmissionTime_max[0][i] = numberOfNodes[i];
			transmissionTime_max[1][i] = simulator.speedAnalysisWhenNetworkStarts(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000;
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");
		}

		// Transmission Time
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", transmissionTime_max);
		dataset.addSeries("mittlere Distanz", transmissionTime_med);
		dataset.addSeries("minimale Distanz", transmissionTime_min);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [s]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/EADV/EADV_UebertragungszeitWennNetzwerkstartet.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
	}

	@Override
	public void evaluateCostAnalysis() {
		
		EadvSimulator simulator = new EadvSimulator();
		long networkLifetime = 0L;

		double numberOfNodes[] = new double[networkWidth.length];
		double maxDistance[][] = new double[2][networkWidth.length];
		double minDistance[][] = new double[2][networkWidth.length];
		double medDistance[][] = new double[2][networkWidth.length];
		double energyCostsNetworkStructurePropagation[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {

			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			System.out.println("Start min Simulation for " + numberOfNodes[i] + " nodes.");
			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = simulator.energyCostAnalysis(networkWidth[i], 0, 1);
			networkLifetime = simulator.getNetworkLifetime();
			System.out.println("Min Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + minDistance[1][i]);
			energyCostsNetworkStructurePropagation[0][i] = numberOfNodes[i];
			energyCostsNetworkStructurePropagation[1][i] = simulator.getEnergyCostsForPropagationNetworkStructure();

			System.out.println("Start med Simulation for " + numberOfNodes[i] + " nodes.");
			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = simulator.energyCostAnalysis(networkWidth[i], 0, networkWidth[i] - 1);
			networkLifetime = simulator.getNetworkLifetime();
			System.out.println("Med Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + medDistance[1][i]);

			System.out.println("Start max Simulation for " + numberOfNodes[i] + " nodes.");
			maxDistance[0][i] = numberOfNodes[i];
			maxDistance[1][i] = simulator.energyCostAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			networkLifetime = simulator.getNetworkLifetime();
			System.out.println("Max Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + maxDistance[1][i]);
		}

		// Consumed energy
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", maxDistance);
		dataset.addSeries("mittlere Distanz", medDistance);
		dataset.addSeries("minimale Distanz", minDistance);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/DSDV/DSDV_Umgesetzte_Energie_Nachrichtenübertragung.png"),
					chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Consumed energy network structure propagation
		dataset = new DefaultXYDataset();
		dataset.addSeries("Netzwerkgröße", energyCostsNetworkStructurePropagation);

		line = new XYLineAndShapeRenderer();

		xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		plot = new XYPlot(dataset, xAxis, yAxis, line);

		chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);
		chart.removeLegend();

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/DSDV/DSDV_Umgesetzte_Energie_NetzwerkStrukturPropagiereung.png"),
					chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	
	public void evaluateNetworkLivetimeStaticSendBehaviorOneDestination(int payloadSize) {
		
		System.out.println("\nAODV Lifetime analysis");
		
		EadvSimulator simulator = new EadvSimulator();
		
		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];
		double sendTime_10_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_10_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_10_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_10_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];
		double sendTime_10_PercentageRREQMsg[][] = new double[2][networkWidth.length];
		double sendTime_10_PercentageRREPMsg[][] = new double[2][networkWidth.length];
		double sendTime_10_PercentagePayloadMsg[][] = new double[2][networkWidth.length];
		

		double sendTime_60[][] = new double[2][networkWidth.length];
		double sendTime_60_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_60_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_60_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_60_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];
		double sendTime_60_PercentageRREQMsg[][] = new double[2][networkWidth.length];
		double sendTime_60_PercentageRREPMsg[][] = new double[2][networkWidth.length];
		double sendTime_60_PercentagePayloadMsg[][] = new double[2][networkWidth.length];

		double sendTime_600[][] = new double[2][networkWidth.length];
		double sendTime_600_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_600_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_600_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_600_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];
		double sendTime_600_PercentageRREQMsg[][] = new double[2][networkWidth.length];
		double sendTime_600_PercentageRREPMsg[][] = new double[2][networkWidth.length];
		double sendTime_600_PercentagePayloadMsg[][] = new double[2][networkWidth.length];
		

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			System.out.println("DSDV - Lifetimeanalysis, transmission period : 60 s, number of nodes: " + numberOfNodes[i]);
			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1*60, payloadSize) / 1000 / 60;
			
			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			System.out.println("DSDV - Lifetimeanalysis, transmission period : 300 s, number of nodes: " + numberOfNodes[i]);
			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 5*60, payloadSize) / 1000 / 60;
			
			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			System.out.println("DSDV - Lifetimeanalysis, transmission period : 600 s, number of nodes: " + numberOfNodes[i]);
			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 10*60, payloadSize) / 1000 / 60;
			
			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			
			
		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 5 m", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 10 m", sendTime_600);
		//dataset.addSeries("Knoten Sendet alle 20 m", sendTime_1200);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Netzwerk Lebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		String filename = "Output/EADV/EADV_Lebenszeitanalyse_OneDestination_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	public void evaluateNetworkPartitioningAnalysis(int payloadSize) {
		
		System.out.println("Start partitioning analysis");

		EadvSimulator simulator = new EadvSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];
		double sendTime_10_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_10_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_10_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_10_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];

		double sendTime_60[][] = new double[2][networkWidth.length];
		double sendTime_60_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_60_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_60_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_60_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];

		double sendTime_600[][] = new double[2][networkWidth.length];
		double sendTime_600_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_600_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_600_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_600_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.partitioningAnalysis(networkWidth[i], 60, payloadSize) / 1000.0 / 60.0;
			sendTime_10_IdleMode[0][i] = numberOfNodes[i];
			sendTime_10_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_10_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_10_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_10_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_10_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_10_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_10_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.partitioningAnalysis(networkWidth[i], 5*60, payloadSize) / 1000.0 / 60.0;
			sendTime_60_IdleMode[0][i] = numberOfNodes[i];
			sendTime_60_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_60_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_60_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_60_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_60_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_60_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_60_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.partitioningAnalysis(networkWidth[i], 10*60, payloadSize) / 1000.0 / 60.0;
			sendTime_600_IdleMode[0][i] = numberOfNodes[i];
			sendTime_600_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_600_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_600_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_600_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_600_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_600_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_600_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 5 min", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 10 min", sendTime_600);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Netzwerk Lebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		String filename = "Output/EADV/EADV_partitionierungsanalyse_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in transmission mode
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_10_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 5 min", sendTime_60_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 10 min", sendTime_600_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/EADV/EADV_partitionierungsanalyse_prozentualeZeit_Sendemodus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in idle mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_10_IdleMode);
		dataset2.addSeries("Knoten Sendet alle 5 min", sendTime_60_IdleMode);
		dataset2.addSeries("Knoten Sendet alle 10 min", sendTime_600_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Idle-Modus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/EADV/EADV_partitionierungsanalyse_prozentualeZeit_IdleModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time Waiting For MediumAccesPermission
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_10_WaitingForMediumAccesPermission);
		dataset2.addSeries("Knoten Sendet alle 5 min", sendTime_60_WaitingForMediumAccesPermission);
		dataset2.addSeries("Knoten Sendet alle 10 min", sendTime_600_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten wartet auf Medium [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);
		
		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/EADV/EADV_partitionierungsanalyse_prozentualeZeit_WaitingForMediumAccesPermission_"
				+ payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in recive mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_10_ReciveMode);
		dataset2.addSeries("Knoten Sendet alle 5 min", sendTime_60_ReciveMode);
		dataset2.addSeries("Knoten Sendet alle 10 min", sendTime_600_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Empfangsmodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);
		
		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/EADV/EADV_partitionierungsanalyse_prozentualeZeit_ReciveModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
	}
	
	@Override
	public void evaluateNetworkLivetimeStaticSendBehavior() {
		// TODO Auto-generated method stub

	}

}
