package OLSR;

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
import Simulator.EvaluationUnit;

public class OlsrEvaluationUnit extends EvaluationUnit {

	// private static final int networkWidth[] = {3, 5, 10, 15, 22, 27, 32};
	// private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10,
	// 11, 12, 13, 14};
	private static final int networkWidth[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		System.out.println("Start AODVM spped anylsis.");
		OlsrSimulator simulator = new OlsrSimulator();

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
			ChartUtilities.saveChartAsPNG(new File("Output/OLSR/OLSR_Uebertragungszeit.png"), chart, CHART_WIDTH,
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
			ChartUtilities.saveChartAsPNG(new File("Output/OLSR/OLSR_Uebertragungszeit_Kollisionen.png"), chart2,
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
			ChartUtilities.saveChartAsPNG(new File("Output/OLSR/OLSR_Uebertragungszeit_Nachricht.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	public void evaluateCostAnalysis() {
		OlsrSimulator simulator = new OlsrSimulator();
		long networkLifetime = 0L;

		double numberOfNodes[] = new double[networkWidth.length];
		double maxDistance[][] = new double[2][networkWidth.length];
		double maxDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];
		double maxDistance_onlyPayloadMsg[][] = new double[2][networkWidth.length];
		double energyDependendOnRouteDistance[][] = new double[2][networkWidth.length];

		double minDistance[][] = new double[2][networkWidth.length];
		double minDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];
		double minDistance_onlyPayloadMsg[][] = new double[2][networkWidth.length];

		double medDistance[][] = new double[2][networkWidth.length];
		double medDistance_onlyTransmissionEnergy[][] = new double[2][networkWidth.length];
		double medDistance_onlyPayloadMsg[][] = new double[2][networkWidth.length];

		for (int i = 0; i < networkWidth.length; i++) {

			numberOfNodes[i] = Math.pow(networkWidth[i], 2);
			maxDistance[0][i] = numberOfNodes[i];
			maxDistance[1][i] = simulator.energyCostAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);

			maxDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			maxDistance_onlyTransmissionEnergy[1][i] = simulator.getConsumedEnergyInReciveMode()
					+ simulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = simulator.getNetworkLifetime();
			maxDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			maxDistance_onlyPayloadMsg[1][i] = simulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1);
			energyDependendOnRouteDistance[0][i] = simulator.getRouteDistance();
			energyDependendOnRouteDistance[1][i] = maxDistance_onlyPayloadMsg[1][i];

			System.out.println("Max Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + maxDistance[1][i]);

			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = simulator.energyCostAnalysis(networkWidth[i], 0, 1);
			minDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			minDistance_onlyTransmissionEnergy[1][i] = simulator.getConsumedEnergyInReciveMode()
					+ simulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = simulator.getNetworkLifetime();
			minDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			minDistance_onlyPayloadMsg[1][i] = simulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0, 1);

			System.out.println("Min Simulation for " + networkWidth[i] * networkWidth[i]
					+ " nodes completed. Ausführungszeit des Netzwerks: " + networkLifetime + " ms"
					+ " umgesetzte Energie: " + minDistance[1][i]);

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = simulator.energyCostAnalysis(networkWidth[i], 0,
					(networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			medDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			medDistance_onlyTransmissionEnergy[1][i] = simulator.getConsumedEnergyInReciveMode()
					+ simulator.getConsumedEnergyInTransmissionMode();
			networkLifetime = simulator.getNetworkLifetime();
			medDistance_onlyPayloadMsg[0][i] = numberOfNodes[i];
			medDistance_onlyPayloadMsg[1][i] = simulator.energyCostAnalysisWithoutRDP(networkWidth[i], 0,
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
			ChartUtilities.saveChartAsPNG(new File("Output/OLSR/OLSR_Umgesetzte_Energie.png"), chart, CHART_WIDTH,
					CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// consumed transmission energy
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
			ChartUtilities.saveChartAsPNG(new File("Output/OLSR/OLSR_Umgesetzte_Energie_nur_Uebertragungsenergie.png"),
					chart3, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Consumed Energy without route discovery process
		dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", maxDistance_onlyPayloadMsg);
		dataset.addSeries("mittlere Distanz", medDistance_onlyPayloadMsg);
		dataset.addSeries("minimale Distanz", minDistance_onlyPayloadMsg);

		// XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		xAxis = new NumberAxis("Anzahl Knoten im Netzwerk");
		yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);
		plot.getRenderer().setSeriesPaint(1, Color.BLACK);
		plot.getRenderer().setSeriesPaint(2, Color.BLACK);

		chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/OLSR/OLSR_Umgesetzte_Energie_ohne_RouteDiscovery.png"),
					chart, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Consumed Energy dependend on Route distance
		dataset = new DefaultXYDataset();
		dataset.addSeries("Distanz", energyDependendOnRouteDistance);

		// XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		xAxis = new NumberAxis("Distanz");
		yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		plot = new XYPlot(dataset, xAxis, yAxis, line);
		plot.getRenderer().setSeriesPaint(0, Color.BLACK);

		chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(
					new File("Output/AODVM/AODVM_Umgesetzte_Energie_in_Abhängigkeit_von_Distanz.png"), chart,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	@Override
	public void evaluateNetworkLivetimeStaticSendBehavior() {
		// TODO Auto-generated method stub

	}

	@Override
	public void evaluateCostAnalysis() {
		// TODO Auto-generated method stub
		
	}
}