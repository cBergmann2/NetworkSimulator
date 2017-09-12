package AODV;

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
import Flooding.FloodingSimulator;
import OLSR.OlsrSimulator;
import Simulator.EvaluationUnit;

public class AodvEvaluationUnit extends EvaluationUnit {

	private static final int MAX_NETWORK_WIDTH = 32;

	// private static final int networkWidth[] = {3, 5, 10, 15, 22, 27, 32};
	// private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10,
	// 11, 12, 13, 14};
	private static final int networkWidth[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	//private static final int networkWidth[] = {9};

	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		System.out.println("Start AODVM spped anylsis.");
		AodvSimulator simulator = new AodvSimulator();

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
			transmissionTime_max[0][i] = numberOfNodes[i];
			transmissionTime_max[1][i] = simulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			transmissionTime_max_collisions[0][i] = numberOfNodes[i];
			transmissionTime_max_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_max[0][i] = numberOfNodes[i];
			transmissionTime_msg_max[1][i] = simulator.getMsgTransmissionTime() / 1000.0;
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = simulator.speedAnalysis(networkWidth[i], 0, 1) / 1000.0;
			transmissionTime_min_collisions[0][i] = numberOfNodes[i];
			transmissionTime_min_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_min[0][i] = numberOfNodes[i];
			transmissionTime_msg_min[1][i] = simulator.getMsgTransmissionTime() / 1000.0;
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = simulator.speedAnalysis(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2) / 1000.0;
			transmissionTime_med_collisions[0][i] = numberOfNodes[i];
			transmissionTime_med_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_med[0][i] = numberOfNodes[i];
			transmissionTime_msg_med[1][i] = simulator.getMsgTransmissionTime() / 1000.0;
			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

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
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Uebertragungszeit.png"), chart, CHART_WIDTH,
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
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Uebertragungszeit_Kollisionen.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Msg Transmission time
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", transmissionTime_msg_max);
		dataset2.addSeries("mittlere Distanz", transmissionTime_msg_med);
		dataset2.addSeries("minimale Distanz", transmissionTime_msg_min);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Übertragungszeit [s]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Uebertragungszeit_Nachricht.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void evaluateCostAnalysis() {
		AodvSimulator aodvSimulator = new AodvSimulator();
		long networkLifetime = 0L;

		double numberOfNodes[] = new double[networkWidth.length];
		double maxDistance[][] = new double[2][networkWidth.length];
		double maxDistance_collisions[][] = new double[2][networkWidth.length];
		double maxDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];
		double maxDistance_onlyPayloadMsg[][] = new double[2][networkWidth.length];

		double minDistance[][] = new double[2][networkWidth.length];
		double minDistance_collisions[][] = new double[2][networkWidth.length];
		double minDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];
		double minDistance_onlyPayloadMsg[][] = new double[2][networkWidth.length];

		double medDistance[][] = new double[2][networkWidth.length];
		double medDistance_collisions[][] = new double[2][networkWidth.length];
		double medDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];
		double medDistance_onlyPayloadMsg[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {

			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			maxDistance[0][i] = numberOfNodes[i];
			maxDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i],
					0, (int) Math.pow(networkWidth[i], 2) - 1);

			maxDistance_collisions[0][i] = numberOfNodes[i];
			maxDistance_collisions[1][i] = aodvSimulator.getCollisions();
			maxDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			maxDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = aodvSimulator.getNetworkLifetime();
			maxDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			maxDistance_onlyPayloadMsg[1][i] = aodvSimulator.energyCostAnalysisWithoutRDP(networkWidth[i],
					0, (int) Math.pow(networkWidth[i], 2) - 1);

			System.out.println("Max Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + maxDistance[1][i]);

			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], 0, 1);
			minDistance_collisions[0][i] = numberOfNodes[i];
			minDistance_collisions[1][i] = aodvSimulator.getCollisions();
			minDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			minDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = aodvSimulator.getNetworkLifetime();
			minDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			minDistance_onlyPayloadMsg[1][i] = aodvSimulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0, 1);

			System.out.println("Min Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + minDistance[1][i]);

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			medDistance_collisions[0][i] = numberOfNodes[i];
			medDistance_collisions[1][i] = aodvSimulator.getCollisions();
			medDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			medDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = aodvSimulator.getNetworkLifetime();
			medDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			medDistance_onlyPayloadMsg[1][i] = aodvSimulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);

			System.out.println("Med Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + medDistance[1][i]);

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
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Umgesetzte_Energie.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// consumed ransmission energy
		DefaultXYDataset dataset3 = new DefaultXYDataset();
		dataset3.addSeries("maximale Distanz", maxDistance_onlyTransmissionEnergy);
		dataset3.addSeries("mittlere Distanz", medDistance_onlyTransmissionEnergy);
		dataset3.addSeries("minimale Distanz", minDistance_onlyTransmissionEnergy);

		NumberAxis xAxis3 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis3 = new NumberAxis("Kollisionen");
		XYPlot plot3 = new XYPlot(dataset3, xAxis3, yAxis3, line);
		plot3.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot3.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot3.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart3 = new JFreeChart(plot3);

		chart3.getPlot().setBackgroundPaint(Color.WHITE);
		chart3.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(
					new File("Output/AODV_RFC/AODV_Umgesetzte_Energie_nur_Uebertragungsenergie.png"), chart3,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Collisions
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", maxDistance_collisions);
		dataset2.addSeries("mittlere Distanz", medDistance_collisions);
		dataset2.addSeries("minimale Distanz", minDistance_collisions);

		// XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Kollisionen");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Umgesetzte_Energie_Kollisionen.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Consumed Energy without route discovery process
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", maxDistance_onlyPayloadMsg);
		dataset2.addSeries("mittlere Distanz", medDistance_onlyPayloadMsg);
		dataset2.addSeries("minimale Distanz", minDistance_onlyPayloadMsg);

		// XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Umgesetzte Energie [nAs]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Umgesetzte_Energie_ohne_RouteDiscovery.png"),
					chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void evaluateCostAnalysisRouteDiscoveryProcess(){
		AodvSimulator aodvSimulator = new AodvSimulator();
		long networkLifetime = 0L;

		double numberOfNodes[] = new double[networkWidth.length];
		double maxDistance[][] = new double[2][networkWidth.length];

		double minDistance[][] = new double[2][networkWidth.length];

		double medDistance[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {

			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			maxDistance[0][i] = numberOfNodes[i];
			maxDistance[1][i] = aodvSimulator.energyCostAnalysisRouteDiscoveryProcess(networkWidth[i],
					0, (int) Math.pow(networkWidth[i], 2) - 1);

			System.out.println("Max Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + maxDistance[1][i]);

			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = aodvSimulator.energyCostAnalysisRouteDiscoveryProcess(networkWidth[i], 0, 1);
			
			System.out.println("Min Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + minDistance[1][i]);

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = aodvSimulator.energyCostAnalysisRouteDiscoveryProcess(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			
			System.out.println("Med Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + medDistance[1][i]);

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
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Umgesetzte_Energie_RouteDiscoveryProzess.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void evaluateNetworkLivetimeStaticSendBehaviorOneDestination(int payloadSize) {
		System.out.println("\nAODV Lifetime analysis");

		AodvSimulator simulator = new AodvSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_60[][] = new double[2][networkWidth.length];

		double sendTime_300[][] = new double[2][networkWidth.length];

		double sendTime_600[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;
			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_300[0][i] = numberOfNodes[i];
			sendTime_300[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 5 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 10 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 5 min", sendTime_300);
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

		String filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_OneDestination_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void evaluateNetworkLivetimeStaticSendBehavior() {

	}

	public void evaluateNetworkLivetimeRandomSorceAndDest(int payloadSize, int maxPairs) {
		System.out.println("\nFlooding Lifetime analysis random source and destination node");

		AodvSimulator simulator = new AodvSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];
		double sendTime_60[][] = new double[2][networkWidth.length];
		double sendTime_600[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.lifetimeAnalysisRandomSorceAndDest(networkWidth[i], 1 * 10, payloadSize,
					maxPairs) / 1000 / 60;

			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisRandomSorceAndDest(networkWidth[i], 1 * 60, payloadSize,
					maxPairs) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisRandomSorceAndDest(networkWidth[i], 5 * 60, payloadSize,
					maxPairs) / 1000 / 60;

			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 30 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 5 min", sendTime_600);
		// dataset.addSeries("Knoten Sendet alle 20 m", sendTime_1200);

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

		String filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_randomSourceAndDest_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void evaluateNetworkPartitioningAnalysisOneDestination(int payloadSize) {
		System.out.println("Start partitioning analysis");

		AodvSimulator simulator = new AodvSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];

		double sendTime_60[][] = new double[2][networkWidth.length];

		double sendTime_600[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.partitioningAnalysis(networkWidth[i], 60, payloadSize) / 1000 / 60;

			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.partitioningAnalysis(networkWidth[i], 5 * 60, payloadSize) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.partitioningAnalysis(networkWidth[i], 10 * 60, payloadSize) / 1000 / 60;

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

		String filename = "Output/AODV_RFC/AODV_RFC_partitionierungsanalyse_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void evaluateNetworkPartitioningAnaylsisRandomSorceAndDest(int payloadSize, int maxPairs){
		System.out.println("\nFlooding Lifetime analysis random source and destination node");
		
		AodvSimulator simulator = new AodvSimulator();
		
		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];
		double sendTime_60[][] = new double[2][networkWidth.length];
		double sendTime_300[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			
			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.partitioningAnalysisRandomSorceAndDest(networkWidth[i], 1*30, payloadSize, maxPairs) / 1000 / 60;

			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.partitioningAnalysisRandomSorceAndDest(networkWidth[i], 1*60, payloadSize, maxPairs) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_300[0][i] = numberOfNodes[i];
			sendTime_300[1][i] = simulator.partitioningAnalysisRandomSorceAndDest(networkWidth[i], 5*60, payloadSize, maxPairs) / 1000 / 60;

			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
		
		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 10 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 5 min", sendTime_300);
		//dataset.addSeries("Knoten Sendet alle 20 m", sendTime_1200);

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

		String filename = "Output/AODV_RFC/AODV_RFC_Partitionierungsanalyse_randomSourceAndDest_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
