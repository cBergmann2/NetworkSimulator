package AODV_RFC;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import Flooding.FloodingSimulator;
import Simulator.EvaluationUnit;

public class AodvEvaluationUnit extends EvaluationUnit {

	private static final int MAX_NETWORK_WIDTH = 32;
	
	//private static final int networkWidth[] = {3, 5, 10, 15, 22, 27, 32};
	private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	//private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10};
	
	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;
	
	@Override
	public void evaluateSpeedAnalysis() {
		System.out.println("Start AODV_RFC spped anylsis.");
		AodvSimulator aodvSimulator = new AodvSimulator();

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
			transmissionTime_max[1][i] = aodvSimulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) /1000.0;
			transmissionTime_max_collisions[0][i] = numberOfNodes[i];
			transmissionTime_max_collisions[1][i] = aodvSimulator.getCollisions();
			transmissionTime_msg_max[0][i] = numberOfNodes[i];
			transmissionTime_msg_max[1][i] = aodvSimulator.getMsgTransmissionTime()/1000.0; 
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			
			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = aodvSimulator.speedAnalysis(networkWidth[i], 0, 1)/1000.0;
			transmissionTime_min_collisions[0][i] = numberOfNodes[i];
			transmissionTime_min_collisions[1][i] = aodvSimulator.getCollisions();
			transmissionTime_msg_min[0][i] = numberOfNodes[i];
			transmissionTime_msg_min[1][i] = aodvSimulator.getMsgTransmissionTime()/1000.0; 
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = aodvSimulator.speedAnalysis(networkWidth[i], 0, (networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2)/1000.0;
			transmissionTime_med_collisions[0][i] = numberOfNodes[i];
			transmissionTime_med_collisions[1][i] = aodvSimulator.getCollisions();
			transmissionTime_msg_med[0][i] = numberOfNodes[i];
			transmissionTime_msg_med[1][i] = aodvSimulator.getMsgTransmissionTime()/1000.0; 
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
			maxDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], (int) Math.pow(networkWidth[i], 2) - networkWidth[i],
					(int) Math.pow(networkWidth[i], 2) - 1);
		
			
			maxDistance_collisions[0][i] = numberOfNodes[i];
			maxDistance_collisions[1][i] = aodvSimulator.getCollisions();
			maxDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			maxDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = aodvSimulator.getNetworkLifetime();
			maxDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			maxDistance_onlyPayloadMsg[1][i] = aodvSimulator.energyCostAnalysisWithoutRDP(networkWidth[i], (int) Math.pow(networkWidth[i], 2) - networkWidth[i], (int) Math.pow(networkWidth[i], 2) - 1);
			
			System.out.println("Max Simulation for " + networkWidth[i] * networkWidth[i] + " nodes completed. Ausführungszeit des Netzwerks: "
					+ networkLifetime + " ms"
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
	
			System.out.println("Min Simulation for " + networkWidth[i] * networkWidth[i] + " nodes completed. Ausführungszeit des Netzwerks: "
					+ networkLifetime + " ms"
							+ " umgesetzte Energie: " + minDistance[1][i]);

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], 0, (networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			medDistance_collisions[0][i] = numberOfNodes[i];
			medDistance_collisions[1][i] = aodvSimulator.getCollisions();
			medDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			medDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = aodvSimulator.getNetworkLifetime();
			medDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			medDistance_onlyPayloadMsg[1][i] = aodvSimulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0, (networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
	
			System.out.println("Med Simulation for " + networkWidth[i] * networkWidth[i] + " nodes completed. Ausführungszeit des Netzwerks: "
					+ networkLifetime + " ms"
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

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Umgesetzte_Energie.png"), chart,
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

		NumberAxis xAxis3 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis3 = new NumberAxis("Kollisionen");
		XYPlot plot3 = new XYPlot(dataset3, xAxis3, yAxis3, line);

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

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV_RFC/AODV_Umgesetzte_Energie_Kollisionen.png"),
					chart2, CHART_WIDTH, CHART_HIGHT);
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

	public void evaluateNetworkLivetimeStaticSendBehavior(int payloadSize) {
		System.out.println("\nAODV Lifetime analysis");
		
		AodvSimulator simulator = new AodvSimulator();
		
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
		
		/*
		double sendTime_1200[][] = new double[2][networkWidth.length];
		double sendTime_1200_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentageRREQMsg[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentageRREPMsg[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentagePayloadMsg[][] = new double[2][networkWidth.length];
		 */
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			
			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 1*60, payloadSize) / 1000 / 60;
			sendTime_10_IdleMode[0][i] = numberOfNodes[i];
			sendTime_10_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_10_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_10_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_10_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_10_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_10_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_10_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_10_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_10_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_10_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			
			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 5*60, payloadSize) / 1000 / 60;
			sendTime_60_IdleMode[0][i] = numberOfNodes[i];
			sendTime_60_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_60_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_60_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_60_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_60_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_60_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_60_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_60_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_60_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_60_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 10*60, payloadSize) / 1000 / 60;
			sendTime_600_IdleMode[0][i] = numberOfNodes[i];
			sendTime_600_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_600_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_600_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_600_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_600_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_600_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_600_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_600_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_600_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_600_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			
			/*
			sendTime_1200[0][i] = numberOfNodes[i];
			sendTime_1200[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 20*60, payloadSize) / 1000 / 60;
			sendTime_1200_IdleMode[0][i] = numberOfNodes[i];
			sendTime_1200_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_1200_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_1200_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_1200_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_1200_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_1200_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_1200_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_1200_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_1200_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_1200_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"20m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			*/
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

		String filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_" + payloadSize + "Byte.png";
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_Sendemodus_" + payloadSize
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Idle-Modus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_IdleModus_" + payloadSize
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten wartet auf Medium [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_WaitingForMediumAccesPermission_"
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Empfangsmodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_ReciveModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Comparison of data packages
		
		double sendTime_10_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		double sendTime_60_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		double sendTime_600_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		for(int i=0; i<networkWidth.length; i++){
			sendTime_10_PercentageProtocolMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageProtocolMsg[0][i] = sendTime_10_PercentageRREPMsg[1][i] + sendTime_10_PercentageRREQMsg[1][i];
			sendTime_60_PercentageProtocolMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageProtocolMsg[0][i] = sendTime_60_PercentageRREPMsg[1][i] + sendTime_60_PercentageRREQMsg[1][i];
			sendTime_600_PercentageProtocolMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageProtocolMsg[0][i] = sendTime_600_PercentageRREPMsg[1][i] + sendTime_600_PercentageRREQMsg[1][i];
		}
		
		dataset2 = new DefaultXYDataset();
		//dataset2.addSeries("Knoten Sendet alle 10 s (Routinginformationen)", sendTime_10_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 10 s (Nachrichten)", sendTime_10_PercentagePayloadMsg);
		//dataset2.addSeries("Knoten Sendet alle 60 s (Routinginformationen)", sendTime_60_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 60 s (Nachrichten)", sendTime_60_PercentagePayloadMsg);
		//dataset2.addSeries("Knoten Sendet alle 600 s (Routinginformationen)", sendTime_600_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 10 min (Nachrichten)", sendTime_600_PercentagePayloadMsg);
		
		//dataset2.addSeries("Knoten Sendet alle 20 min (Nachrichten)", sendTime_1200_PercentagePayloadMsg);
		
		
		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Gesendete Nachrichten [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeNachrichtenVerteilung" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void evaluateNetworkLivetimeStaticSendBehaviorOneDestination(int payloadSize) {
		System.out.println("\nAODV Lifetime analysis");
		
		AodvSimulator simulator = new AodvSimulator();
		
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
		
		/*
		double sendTime_1200[][] = new double[2][networkWidth.length];
		double sendTime_1200_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentageRREQMsg[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentageRREPMsg[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentagePayloadMsg[][] = new double[2][networkWidth.length];
		 */
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			
			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 1*60, payloadSize) / 1000 / 60;
			/*
			sendTime_10_IdleMode[0][i] = numberOfNodes[i];
			sendTime_10_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_10_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_10_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_10_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_10_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_10_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_10_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_10_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_10_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_10_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			*/
			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 5*60, payloadSize) / 1000 / 60;
			/*
			sendTime_60_IdleMode[0][i] = numberOfNodes[i];
			sendTime_60_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_60_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_60_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_60_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_60_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_60_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_60_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_60_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_60_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_60_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			*/
			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorOneDestination(networkWidth[i], 10*60, payloadSize) / 1000 / 60;
			/*
			sendTime_600_IdleMode[0][i] = numberOfNodes[i];
			sendTime_600_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_600_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_600_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_600_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_600_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_600_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_600_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_600_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_600_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_600_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			*/
			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			
			/*
			sendTime_1200[0][i] = numberOfNodes[i];
			sendTime_1200[1][i] = simulator.lifetimeAnalysisStaticSendBehavior(networkWidth[i], 20*60, payloadSize) / 1000 / 60;
			sendTime_1200_IdleMode[0][i] = numberOfNodes[i];
			sendTime_1200_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_1200_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_1200_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_1200_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_1200_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_1200_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_1200_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_1200_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_1200_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_1200_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"20m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			*/
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

		String filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_OneDestination_" + payloadSize + "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		// Average time in transmission mode
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 10 s", sendTime_10_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_60_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 10 m", sendTime_600_TransmissionMode);
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_Sendemodus_" + payloadSize
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Idle-Modus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_IdleModus_" + payloadSize
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten wartet auf Medium [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_WaitingForMediumAccesPermission_"
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Empfangsmodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_ReciveModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Comparison of data packages
		
		double sendTime_10_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		double sendTime_60_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		double sendTime_600_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		for(int i=0; i<networkWidth.length; i++){
			sendTime_10_PercentageProtocolMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageProtocolMsg[0][i] = sendTime_10_PercentageRREPMsg[1][i] + sendTime_10_PercentageRREQMsg[1][i];
			sendTime_60_PercentageProtocolMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageProtocolMsg[0][i] = sendTime_60_PercentageRREPMsg[1][i] + sendTime_60_PercentageRREQMsg[1][i];
			sendTime_600_PercentageProtocolMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageProtocolMsg[0][i] = sendTime_600_PercentageRREPMsg[1][i] + sendTime_600_PercentageRREQMsg[1][i];
		}
		
		dataset2 = new DefaultXYDataset();
		//dataset2.addSeries("Knoten Sendet alle 10 s (Routinginformationen)", sendTime_10_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 10 s (Nachrichten)", sendTime_10_PercentagePayloadMsg);
		//dataset2.addSeries("Knoten Sendet alle 60 s (Routinginformationen)", sendTime_60_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 60 s (Nachrichten)", sendTime_60_PercentagePayloadMsg);
		//dataset2.addSeries("Knoten Sendet alle 600 s (Routinginformationen)", sendTime_600_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 10 min (Nachrichten)", sendTime_600_PercentagePayloadMsg);
		
		//dataset2.addSeries("Knoten Sendet alle 20 min (Nachrichten)", sendTime_1200_PercentagePayloadMsg);
		
		
		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Gesendete Nachrichten [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeNachrichtenVerteilung" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}
	
	public void evaluateNetworkLivetimeStaticSendBehaviorChangingDestination(int payloadSize) {
		System.out.println("\nAODV Lifetime analysis");
		
		AodvSimulator simulator = new AodvSimulator();
		
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
		
		/*
		double sendTime_1200[][] = new double[2][networkWidth.length];
		double sendTime_1200_IdleMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_ReciveMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_TransmissionMode[][] = new double[2][networkWidth.length];
		double sendTime_1200_WaitingForMediumAccesPermission[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentageRREQMsg[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentageRREPMsg[][] = new double[2][networkWidth.length];
		double sendTime_1200_PercentagePayloadMsg[][] = new double[2][networkWidth.length];
		*/
		
		for (int i = 0; i < networkWidth.length; i++) {
			numberOfNodes[i] = Math.pow(networkWidth[i], 2);

			
			sendTime_10[0][i] = numberOfNodes[i];
			sendTime_10[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorChangingDestination(networkWidth[i], 1*60, payloadSize) / 1000 / 60;
			sendTime_10_IdleMode[0][i] = numberOfNodes[i];
			sendTime_10_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_10_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_10_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_10_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_10_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_10_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_10_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_10_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_10_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_10_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_10_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			
			System.out.println(
					"10s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_60[0][i] = numberOfNodes[i];
			sendTime_60[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorChangingDestination(networkWidth[i], 5*60, payloadSize) / 1000 / 60;
			sendTime_60_IdleMode[0][i] = numberOfNodes[i];
			sendTime_60_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_60_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_60_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_60_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_60_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_60_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_60_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_60_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_60_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_60_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_60_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"60s Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");

			sendTime_600[0][i] = numberOfNodes[i];
			sendTime_600[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorChangingDestination(networkWidth[i], 10*60, payloadSize) / 1000 / 60;
			sendTime_600_IdleMode[0][i] = numberOfNodes[i];
			sendTime_600_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_600_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_600_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_600_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_600_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_600_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_600_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_600_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_600_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_600_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_600_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"10m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			/*
			sendTime_1200[0][i] = numberOfNodes[i];
			sendTime_1200[1][i] = simulator.lifetimeAnalysisStaticSendBehaviorChangingDestination(networkWidth[i], 20*60, payloadSize) / 1000 / 60;
			sendTime_1200_IdleMode[0][i] = numberOfNodes[i];
			sendTime_1200_IdleMode[1][i] = simulator.getAverageTimeInIdleMode();
			sendTime_1200_ReciveMode[0][i] = numberOfNodes[i];
			sendTime_1200_ReciveMode[1][i] = simulator.getAverageTimeInReciveMode();
			sendTime_1200_TransmissionMode[0][i] = numberOfNodes[i];
			sendTime_1200_TransmissionMode[1][i] = simulator.getAverageTimeInTransmissionMode();
			sendTime_1200_WaitingForMediumAccesPermission[0][i] = numberOfNodes[i];
			sendTime_1200_WaitingForMediumAccesPermission[1][i] = simulator
					.getAverageTimeWaitingForMediumAccesPermission();
			sendTime_1200_PercentageRREQMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentageRREQMsg[1][i] = simulator.getPercentageTransmittedRREQMsg();
			sendTime_1200_PercentageRREPMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentageRREPMsg[1][i] = simulator.getPercentageTransmittedRREPMsg();
			sendTime_1200_PercentagePayloadMsg[0][i] = numberOfNodes[i];
			sendTime_1200_PercentagePayloadMsg[1][i] = simulator.getPercentageTransmittedPayloadMsg();
			System.out.println(
					"20m Simulation for " + numberOfNodes[i] + " nodes completed. Ausführungszeit des Netzwerks: "
							+ simulator.getNetworkLifetime() / 1000 / 60 + " min");
			*/
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

		String filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_WechselndeZielknoten_" + payloadSize + "Byte.png";
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_Sendemodus_WechselndeZielknoten_" + payloadSize
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_IdleMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Idle-Modus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_IdleModus_WechselndeZielknoten_" + payloadSize
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_WaitingForMediumAccesPermission);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten wartet auf Medium [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_WaitingForMediumAccesPermission_WechselndeZielknoten_"
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
		//dataset2.addSeries("Knoten Sendet alle 20 m", sendTime_1200_ReciveMode);

		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Knoten im Empfangsmodus [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeZeit_ReciveModus_WechselndeZielknoten_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Comparison of data packages
		
		double sendTime_10_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		double sendTime_60_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		double sendTime_600_PercentageProtocolMsg[][] = new double[2][networkWidth.length];
		for(int i=0; i<networkWidth.length; i++){
			sendTime_10_PercentageProtocolMsg[0][i] += numberOfNodes[i];
			sendTime_10_PercentageProtocolMsg[1][i] += sendTime_10_PercentageRREPMsg[1][i] + sendTime_10_PercentageRREQMsg[1][i];
			sendTime_60_PercentageProtocolMsg[0][i] += numberOfNodes[i];
			sendTime_60_PercentageProtocolMsg[1][i] += sendTime_60_PercentageRREPMsg[1][i] + sendTime_60_PercentageRREQMsg[1][i];
			sendTime_600_PercentageProtocolMsg[0][i] += numberOfNodes[i];
			sendTime_600_PercentageProtocolMsg[1][i] += sendTime_600_PercentageRREPMsg[1][i] + sendTime_600_PercentageRREQMsg[1][i];
		}
		
		dataset2 = new DefaultXYDataset();
		//dataset2.addSeries("Knoten Sendet alle 10 s (Routinginformationen)", sendTime_10_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 10 s (Nachrichten)", sendTime_10_PercentagePayloadMsg);
		//dataset2.addSeries("Knoten Sendet alle 60 s (Routinginformationen)", sendTime_60_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 60 s (Nachrichten)", sendTime_60_PercentagePayloadMsg);
		//dataset2.addSeries("Knoten Sendet alle 600 s (Routinginformationen)", sendTime_600_PercentageProtocolMsg);
		dataset2.addSeries("Knoten Sendet alle 10 min (Nachrichten)", sendTime_600_PercentagePayloadMsg);
		
		//dataset2.addSeries("Knoten Sendet alle 20 min (Nachrichten)", sendTime_1200_PercentagePayloadMsg);
		
		
		xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis2 = new NumberAxis("Gesendete Nachrichten [%]");
		plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_Lebenszeitanalyse_prozentualeNachrichtenVerteilung_WechselndeZielknoten_" + payloadSize
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
		this.evaluateNetworkLivetimeStaticSendBehavior(10);
		
	}
	
	public void evaluateNetworkPartitioningAnalysis(int payloadSize) {
		System.out.println("Start partitioning analysis");

		AodvSimulator simulator = new AodvSimulator();

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
			sendTime_10[1][i] = simulator.partitioningAnalysis(networkWidth[i], 60, payloadSize) / 1000 / 60;
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
			sendTime_60[1][i] = simulator.partitioningAnalysis(networkWidth[i], 5*60, payloadSize) / 1000 / 60;
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
			sendTime_600[1][i] = simulator.partitioningAnalysis(networkWidth[i], 10*60, payloadSize) / 1000 / 60;
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

		// Average time in transmission mode
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("Knoten Sendet alle 60 s", sendTime_10_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 5 min", sendTime_60_TransmissionMode);
		dataset2.addSeries("Knoten Sendet alle 10 min", sendTime_600_TransmissionMode);

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten im Netzwerk");
		NumberAxis yAxis2 = new NumberAxis("Knoten im Sendemodus [%]");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_RFC_partitionierungsanalyse_prozentualeZeit_Sendemodus_" + payloadSize
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

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_RFC_partitionierungsanalyse_prozentualeZeit_IdleModus_" + payloadSize
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

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_RFC_partitionierungsanalyse_prozentualeZeit_WaitingForMediumAccesPermission_"
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

		chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		filename = "Output/AODV_RFC/AODV_RFC_partitionierungsanalyse_prozentualeZeit_ReciveModus_" + payloadSize
				+ "Byte.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(filename), chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
