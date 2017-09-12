package Flooding;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import DSDV.DsdvSimulator;
import OLSR.OlsrSimulator;
import Simulator.EvaluationUnit;

public class FloodingEvaluationUnit extends EvaluationUnit {

	// private static final int MAX_NETWORK_WIDTH = 10;
	// private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10,
	// 11, 12, 13, 14};
	private static final int networkWidth[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		FloodingSimulator floodingSimulator = new FloodingSimulator();

		double numberOfNodes[] = new double[networkWidth.length];
		double transmissionTime_max[][] = new double[2][networkWidth.length];
		double transmissionTime_max_collisions[][] = new double[2][networkWidth.length];
		double transmissionTime_vs_distance_max[][] = new double[2][networkWidth.length];

		double min[][] = new double[2][networkWidth.length];

		double transmissionTime_min[][] = new double[2][networkWidth.length];
		double transmissionTime_min_collisions[][] = new double[2][networkWidth.length];

		double transmissionTime_med[][] = new double[2][networkWidth.length];
		double transmissionTime_med_collisions[][] = new double[2][networkWidth.length];

		double sendTime_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			transmissionTime_max[0][i] = numberOfNodes[i];
			transmissionTime_max[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) / 1000.0;
			transmissionTime_max_collisions[0][i] = numberOfNodes[i];
			transmissionTime_max_collisions[1][i] = floodingSimulator.getCollisions();
			transmissionTime_vs_distance_max[0][i] = 2 * (Math.sqrt(numberOfNodes[i]) - 1);
			transmissionTime_vs_distance_max[1][i] = transmissionTime_max[1][i];

			sendTime_IdleMode[0][i] = numberOfNodes[i];
			sendTime_IdleMode[1][i] = floodingSimulator.getAverageTimeInIdleMode();
			sendTime_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_ReciveMode[1][i] = floodingSimulator.getAverageTimeInReciveMode();
			sendTime_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_TransmissionMode[1][i] = floodingSimulator.getAverageTimeInTransmissionMode();
			sendTime_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_WaitingForMediumAccesPermission[1][i] = floodingSimulator
					.getAverageTimeWaitingForMediumAccesPermission();

			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2)
					+ " nodes completed. Transmission time: " + transmissionTime_max[1][i] + " s");

			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0, 1) / 1000.0;
			transmissionTime_min_collisions[0][i] = numberOfNodes[i];
			transmissionTime_min_collisions[1][i] = floodingSimulator.getCollisions();
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2) / 1000.0;
			transmissionTime_med_collisions[0][i] = numberOfNodes[i];
			transmissionTime_med_collisions[1][i] = floodingSimulator.getCollisions();
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
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Uebertragungszeit.png"), chart,
					CHART_WIDTH, CHART_HIGHT);
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

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis2 = new NumberAxis("Kollisionen");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Uebertragungszeit_Kollisionen.png"),
					chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Übertragungszeit vs. Distance
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", transmissionTime_vs_distance_max);

		// XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		xAxis2 = new NumberAxis("Distanz");
		yAxis2 = new NumberAxis("Übertragungszeit[s]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Max_Uebertragungszeit_vs_Distanz.png"),
					chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in transmission mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", sendTime_TransmissionMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		String filename = "Output/Flooding/Flooding_Speedanalyse_prozentualeZeit_Sendemodus.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in idle mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", sendTime_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Idle-Modus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Speedanalyse_prozentualeZeit_IdleModus.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time Waiting For MediumAccesPermission
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", sendTime_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten wartet auf Medium [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Speedanalyse_prozentualeZeit_WaitingForMediumAccesPermission.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in recive mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", sendTime_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Empfangs-Modus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Speedanalyse_prozentualeZeit_ReciveModus.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void evaluateCostAnalysis() {
		FloodingSimulator floodingSimulator = new FloodingSimulator();

		double numberOfNodes[] = new double[networkWidth.length];
		double maxDistance[][] = new double[2][networkWidth.length];
		double maxDistance_collisions[][] = new double[2][networkWidth.length];
		double maxDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];

		double minDistance[][] = new double[2][networkWidth.length];
		double minDistance_collisions[][] = new double[2][networkWidth.length];
		double minDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];

		double medDistance[][] = new double[2][networkWidth.length];
		double medDistance_collisions[][] = new double[2][networkWidth.length];
		double medDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			maxDistance[0][i] = numberOfNodes[i];
			maxDistance[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i],
					(int) Math.pow(networkWidth[i], 2) - networkWidth[i], (int) Math.pow(networkWidth[i], 2) - 1);
			maxDistance_collisions[0][i] = numberOfNodes[i];
			maxDistance_collisions[1][i] = floodingSimulator.getCollisions();
			maxDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			maxDistance_onlyTransmissionEnergy[1][i] = floodingSimulator.getConsumedEnergyInTransmissionMode();
			// maxDistance_onlyTransmissionEnergy[1][i] =
			// floodingSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2)
					+ " nodes completed. Ausführungszeit des Netzwerks: " + floodingSimulator.getNetworkLifetime()
					+ " ms" + " Sende/Empfangsenergie: " + maxDistance_onlyTransmissionEnergy[1][i] + " nAs");

			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i], 0, 1);
			minDistance_collisions[0][i] = numberOfNodes[i];
			minDistance_collisions[1][i] = floodingSimulator.getCollisions();
			minDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			minDistance_onlyTransmissionEnergy[1][i] = floodingSimulator.getConsumedEnergyInTransmissionMode();
			// minDistance_onlyTransmissionEnergy[1][i] =
			// floodingSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2)
					+ " nodes completed. Ausführungszeit des Netzwerks: " + floodingSimulator.getNetworkLifetime()
					+ " ms" + " Sende/Empfangsenergie: " + minDistance_onlyTransmissionEnergy[1][i] + " nAs");

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			medDistance_collisions[0][i] = numberOfNodes[i];
			medDistance_collisions[1][i] = floodingSimulator.getCollisions();
			medDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			medDistance_onlyTransmissionEnergy[1][i] = floodingSimulator.getConsumedEnergyInTransmissionMode();
			// medDistance_onlyTransmissionEnergy[1][i] =
			// floodingSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2)
					+ " nodes completed. Ausführungszeit des Netzwerks: " + floodingSimulator.getNetworkLifetime()
					+ " ms" + " Sende/Empfangsenergie: " + medDistance_onlyTransmissionEnergy[1][i] + " nAs");

		}

		// Consumed energy
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", maxDistance);
		dataset.addSeries("mittlere Distanz", medDistance);
		dataset.addSeries("minimale Distanz", minDistance);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Umgesetzte_Energie.png"), chart,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// consumed transmission energy
		DefaultXYDataset dataset3 = new DefaultXYDataset();
		dataset3.addSeries("maximale Distanz", maxDistance_onlyTransmissionEnergy);
		dataset3.addSeries("mittlere Distanz", medDistance_onlyTransmissionEnergy);
		dataset3.addSeries("minimale Distanz", minDistance_onlyTransmissionEnergy);

		NumberAxis xAxis3 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis3 = new NumberAxis("Umgesetzte Energie [nAs]");
		XYPlot plot3 = new XYPlot(dataset3, xAxis3, yAxis3, line);
		plot3.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot3.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot3.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart3 = new JFreeChart(plot3);

		chart3.getPlot().setBackgroundPaint(Color.WHITE);
		chart3.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(
					new File("Output/Flooding/Flooding_Umgesetzte_Energie_nur_Uebertragungsenergie.png"), chart3,
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

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis2 = new NumberAxis("Kollisionen");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		plot2.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot2.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Umgesetzte_Energie_Kollisionen.png"),
					chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void evaluateNetworkLivetimeStaticSendBehavior() {
		// TODO Auto-generated method stub

	}

	public void evaluateNetworkLivetimeStaticSendBehaviorOneDestination(int payloadSize) {

		System.out.println("\nFlooding Lifetime analysis");

		FloodingSimulator simulator = new FloodingSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];

		double sendTime_60[][] = new double[2][networkWidth.length];

		double sendTime_600[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			System.out.println(
					"OLSR - Lifetimeanalysis, transmission period : 60 s, number of nodes: " + numberOfNodes[i]);
			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			System.out.println(
					"OLSR - Lifetimeanalysis, transmission period : 300 s, number of nodes: " + numberOfNodes[i]);
			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 5 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			System.out.println(
					"OLSR - Lifetimeanalysis, transmission period : 600 s, number of nodes: " + numberOfNodes[i]);
			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 10 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 5 min", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 10 min", sendTime_600);
		// dataset.addSeries("Knoten Sendet alle 20 m", sendTime_1200);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis = new NumberAxis("Netzwerklebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		String filename = "Output/Flooding/Flooding_Lebenszeitanalyse_OneDestination_" + payloadSize + "Bit.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void evaluateNetworkLivetimeRandomSorceAndDest(int payloadSize, int maxPairs) {
		System.out.println("\nFlooding Lifetime analysis random source and destination node");

		FloodingSimulator simulator = new FloodingSimulator();

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
		dataset.addSeries("Knoten Sendet alle 10 s", sendTime_10);
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

		String filename = "Output/Flooding/Flooding_Lebenszeitanalyse_randomSourceAndDest_" + payloadSize + "Bit.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void evaluateNetworkPartitioningAnalysisOneDestination(int payloadSize) {
		System.out.println("Start partitioning analysis");

		FloodingSimulator simulator = new FloodingSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_60[][] = new double[2][networkWidth.length];

		double sendTime_300[][] = new double[2][networkWidth.length];

		double sendTime_600[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.partitioningAnalysisOnePayloadmessageDestination(networkWidth[i], 1 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_300[0][i] = numberOfNodes[i];
			sendTime_300[1][i] = simulator.partitioningAnalysisOnePayloadmessageDestination(networkWidth[i], 5 * 60,
					payloadSize) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.partitioningAnalysisOnePayloadmessageDestination(networkWidth[i], 10 * 60,
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

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Netzwerk Lebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		String filename = "Output/Flooding/Flooding_partitionierungsanalyse_OneDestination_" + payloadSize + "Bit.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void evaluateNetworkPartioningAnaylsisRandomSorceAndDest(int payloadSize, int maxPairs) {
		System.out.println("\nFlooding Lifetime analysis random source and destination node");

		FloodingSimulator simulator = new FloodingSimulator();

		double numberOfNodes[] = new double[networkWidth.length];

		double sendTime_10[][] = new double[2][networkWidth.length];
		double sendTime_60[][] = new double[2][networkWidth.length];
		double sendTime_300[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.partitioningAnalysisRandomSorceAndDest(networkWidth[i], 1 * 30, payloadSize,
					maxPairs) / 1000 / 60;

			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.partitioningAnalysisRandomSorceAndDest(networkWidth[i], 1 * 60, payloadSize,
					maxPairs) / 1000 / 60;

			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_300[0][i] = numberOfNodes[i];
			sendTime_300[1][i] = simulator.partitioningAnalysisRandomSorceAndDest(networkWidth[i], 5 * 60, payloadSize,
					maxPairs) / 1000 / 60;

			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

		}

		// Network Lifetime
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Knoten Sendet alle 10 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 5 min", sendTime_300);
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

		String filename = "Output/Flooding/Flooding_Partitionierungsanalyse_randomSourceAndDest_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
