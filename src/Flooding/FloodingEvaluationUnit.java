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

import Simulator.EvaluationUnit;

public class FloodingEvaluationUnit extends EvaluationUnit {

	private static final int MAX_NETWORK_WIDTH = 10;
	int networkWidth[] = {2, 5, 10, 24, 32};
	//int networkWidth[] = { 3, 5, 10 };

	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		FloodingSimulator floodingSimulator = new FloodingSimulator();

		double numberOfNodes[] = new double[networkWidth.length];
		double transmissionTime_max[][] = new double[2][networkWidth.length];
		double transmissionTime_max_collisions[][] = new double[2][networkWidth.length];

		double min[][] = new double[2][networkWidth.length];

		double transmissionTime_min[][] = new double[2][networkWidth.length];
		double transmissionTime_min_collisions[][] = new double[2][networkWidth.length];

		double transmissionTime_med[][] = new double[2][networkWidth.length];
		double transmissionTime_med_collisions[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			transmissionTime_max[0][i] = numberOfNodes[i];
			transmissionTime_max[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], (int) Math.pow(networkWidth[i], 2) - networkWidth[i],
					(int) Math.pow(networkWidth[i], 2) - 1) /1000;
			transmissionTime_max_collisions[0][i] = numberOfNodes[i];
			transmissionTime_max_collisions[1][i] = floodingSimulator.getCollisions();
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0, 1) /1000;
			transmissionTime_min_collisions[0][i] = numberOfNodes[i];
			transmissionTime_min_collisions[1][i] = floodingSimulator.getCollisions();
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = floodingSimulator.speedAnalysis(networkWidth[i], 0, (networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2) /1000;
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

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [s]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Uebertragungszeit.png"), chart, CHART_WIDTH, CHART_HIGHT);
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
			maxDistance[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i], (int) Math.pow(networkWidth[i], 2) - networkWidth[i],
					(int) Math.pow(networkWidth[i], 2) - 1);
			maxDistance_collisions[0][i] = numberOfNodes[i];
			maxDistance_collisions[1][i] = floodingSimulator.getCollisions();
			maxDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			maxDistance_onlyTransmissionEnergy[1][i] = floodingSimulator.getConsumedEnergyInReciveMode()
					+ floodingSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed. Ausführungszeit des Netzwerks: "
					+ floodingSimulator.getNetworkLifetime() + " ms");

			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i], 0, 1);
			minDistance_collisions[0][i] = numberOfNodes[i];
			minDistance_collisions[1][i] = floodingSimulator.getCollisions();
			minDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			minDistance_onlyTransmissionEnergy[1][i] = floodingSimulator.getConsumedEnergyInReciveMode()
					+ floodingSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed. Ausführungszeit des Netzwerks: "
					+ floodingSimulator.getNetworkLifetime() + " ms");

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = floodingSimulator.energyCostAnalysis(networkWidth[i], 0, (networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			medDistance_collisions[0][i] = numberOfNodes[i];
			medDistance_collisions[1][i] = floodingSimulator.getCollisions();
			medDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			medDistance_onlyTransmissionEnergy[1][i] = floodingSimulator.getConsumedEnergyInReciveMode()
					+ floodingSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed. Ausführungszeit des Netzwerks: "
					+ floodingSimulator.getNetworkLifetime() + " ms");

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

		// consumed ransmission energy
		DefaultXYDataset dataset3 = new DefaultXYDataset();
		dataset3.addSeries("maximale Distanz", maxDistance_onlyTransmissionEnergy);
		dataset3.addSeries("mittlere Distanz", medDistance_onlyTransmissionEnergy);
		dataset3.addSeries("minimale Distanz", minDistance_onlyTransmissionEnergy);

		NumberAxis xAxis3 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis3 = new NumberAxis("Kollisionen");
		XYPlot plot3 = new XYPlot(dataset3, xAxis3, yAxis3, line);

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

	public void evaluateNetworkLivetimeStochasticSendBehavior() {
		FloodingSimulator floodingSimulator = new FloodingSimulator();

		// int networkWidth[] = {3, 5, 10, 15, 22, 27, 32};
		int networkWidth[] = { 3, 5, 10, 15 };

		double numberOfNodes[] = new double[networkWidth.length];

		double sendProbability_0001[][] = new double[2][networkWidth.length];

		double sendProbability_001[][] = new double[2][networkWidth.length];
		// double maxDistance_collisions[][] = new
		// double[2][MAX_NETWORK_WIDTH-1];
		// double maxDistance_onlyTransmissionEnergy[][] = new
		// double[2][MAX_NETWORK_WIDTH-1];

		double sendProbability_005[][] = new double[2][networkWidth.length];

		double sendProbability_01[][] = new double[2][networkWidth.length];
		// double minDistance_collisions[][] = new
		// double[2][MAX_NETWORK_WIDTH-1];
		// double minDistance_onlyTransmissionEnergy[][] = new
		// double[2][MAX_NETWORK_WIDTH-1];

		// double sendProbability_10[][] = new double[2][networkWidth.length];
		// double medDistance_collisions[][] = new
		// double[2][MAX_NETWORK_WIDTH-1];
		// double medDistance_onlyTransmissionEnergy[][] = new
		// double[2][MAX_NETWORK_WIDTH-1];

		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			sendProbability_0001[0][i] = numberOfNodes[i];

			sendProbability_0001[1][i] = floodingSimulator.lifetimeAnalysisStochasitcSendBehavior(networkWidth[i],
					0.0001) / 1000 / 60;
			System.out.println(
					"0,01% Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ floodingSimulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendProbability_001[0][i] = numberOfNodes[i];
			sendProbability_001[1][i] = floodingSimulator.lifetimeAnalysisStochasitcSendBehavior(networkWidth[i], 0.001)
					/ 1000 / 60;
			System.out.println(
					"0,1% Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ floodingSimulator.getNetworkLifetime() / 1000 / 60 + " min");

			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			sendProbability_005[0][i] = numberOfNodes[i];
			sendProbability_005[1][i] = floodingSimulator.lifetimeAnalysisStochasitcSendBehavior(networkWidth[i], 0.005)
					/ 1000 / 60;
			System.out.println(
					"0,5% Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ floodingSimulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendProbability_01[0][i] = numberOfNodes[i];
			sendProbability_01[1][i] = floodingSimulator.lifetimeAnalysisStochasitcSendBehavior(networkWidth[i], 0.01)
					/ 1000 / 60;
			System.out.println(
					"5% Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ floodingSimulator.getNetworkLifetime() / 1000 / 60 + " min");

			// sendProbability_10[0][i] = numberOfNodes[i];
			// sendProbability_10[1][i] =
			// floodingSimulator.lifetimeAnalysis(networkWidth[i], 0.1) /1000 /
			// 60;
			// System.out.println("10% Simulation for " + numberOfNodes[i] + "
			// nodes completed. Ausführungszeit des Netzwerks: " +
			// floodingSimulator.getNetworkLifetime() /1000 / 60 + " min" );

		}

		// Consumed energy
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Sendewahrscheinlichkeit 0,01%", sendProbability_0001);
		dataset.addSeries("Sendewahrscheinlichkeit 0,1%", sendProbability_001);
		dataset.addSeries("Sendewahrscheinlichkeit 0,5%", sendProbability_005);
		dataset.addSeries("Sendewahrscheinlichkeit 1%", sendProbability_01);
		// dataset.addSeries("Sendewahrscheinlichkeit 10%", sendProbability_10);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Netzwerk Lebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Lebenszeit.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void evaluateNetworkLivetimeStaticSendBehavior(int payloadSize) {
		FloodingSimulator simulator = new FloodingSimulator();

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
			sendTime_10[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 10, payloadSize) / 1000 / 60;
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
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 60, payloadSize) / 1000 / 60;
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
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 600, payloadSize) / 1000 / 60;
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
		dataset.addSeries("Knoten Sendet alle 10 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 10 m", sendTime_600);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Netzwerk Lebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		String filename = "Output/Flooding/Flooding_Lebenszeitanalyse_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in transmission mode
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Lebenszeitanalyse_prozentualeZeit_Sendemodus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in idle mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_IdleMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_IdleMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Lebenszeitanalyse_prozentualeZeit_IdleModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time Waiting For MediumAccesPermission
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_WaitingForMediumAccesPermission);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_WaitingForMediumAccesPermission);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Lebenszeitanalyse_prozentualeZeit_WaitingForMediumAccesPermission_"
				+ payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in recive mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_ReciveMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_ReciveMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_Lebenszeitanalyse_prozentualeZeit_ReciveModus_" + payloadSize
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

	public void evaluateNetworkPartitioningAnalysis(int payloadSize) {
		System.out.println("Start partitioning analysis");

		FloodingSimulator simulator = new FloodingSimulator();

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
			sendTime_10[1][i] = simulator.partitioningAnalysis(networkWidth[i], 10, payloadSize) / 1000 / 60;
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
			sendTime_60[1][i] = simulator.partitioningAnalysis(networkWidth[i], 60, payloadSize) / 1000 / 60;
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
			sendTime_600[1][i] = simulator.partitioningAnalysis(networkWidth[i], 600, payloadSize) / 1000 / 60;
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
		dataset.addSeries("Knoten Sendet alle 10 s", sendTime_10);
		dataset.addSeries("Knoten Sendet alle 60 s", sendTime_60);
		dataset.addSeries("Knoten Sendet alle 10 m", sendTime_600);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Netzwerk Lebenszeit [Minuten]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		String filename = "Output/Flooding/Flooding_partitionierungsanalyse_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in transmission mode
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_partitionierungsanalyse_prozentualeZeit_Sendemodus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in idle mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_IdleMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_IdleMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_partitionierungsanalyse_prozentualeZeit_IdleModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time Waiting For MediumAccesPermission
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_WaitingForMediumAccesPermission);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_WaitingForMediumAccesPermission);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_partitionierungsanalyse_prozentualeZeit_WaitingForMediumAccesPermission_"
				+ payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Average time in recive mode
		dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_ReciveMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_ReciveMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten");
		yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/Flooding/Flooding_partitionierungsanalyse_prozentualeZeit_ReciveModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
