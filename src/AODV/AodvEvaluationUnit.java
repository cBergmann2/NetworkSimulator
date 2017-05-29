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

import Simulator.EvaluationUnit;

public class AodvEvaluationUnit extends EvaluationUnit {

	private static final int MAX_NETWORK_WIDTH = 32;
	
	private static final int networkWidth[] = {3, 5, 10, 15, 22, 27, 32};

	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		AodvSimulator aodvSimulator = new AodvSimulator();

		double numberOfNodes[] = new double[MAX_NETWORK_WIDTH - 1];
		double transmissionTime_max[][] = new double[2][MAX_NETWORK_WIDTH - 1];
		double transmissionTime_max_collisions[][] = new double[2][MAX_NETWORK_WIDTH - 1];

		double min[][] = new double[2][MAX_NETWORK_WIDTH - 1];

		double transmissionTime_min[][] = new double[2][MAX_NETWORK_WIDTH - 1];
		double transmissionTime_min_collisions[][] = new double[2][MAX_NETWORK_WIDTH - 1];

		double transmissionTime_med[][] = new double[2][MAX_NETWORK_WIDTH - 1];
		double transmissionTime_med_collisions[][] = new double[2][MAX_NETWORK_WIDTH - 1];

		for (int i = 2; i < MAX_NETWORK_WIDTH + 1; i++) {
			numberOfNodes[i - 2] = Math.pow(i, 2);
			transmissionTime_max[0][i - 2] = numberOfNodes[i - 2];
			transmissionTime_max[1][i - 2] = aodvSimulator.speedAnalysis(i, (int) Math.pow(i, 2) - i,
					(int) Math.pow(i, 2) - 1);
			transmissionTime_max_collisions[0][i - 2] = numberOfNodes[i - 2];
			transmissionTime_max_collisions[1][i - 2] = aodvSimulator.getCollisions();
			System.out.println("Max Simulation for " + i * i + " nodes completed.");

			transmissionTime_min[0][i - 2] = numberOfNodes[i - 2];
			transmissionTime_min[1][i - 2] = aodvSimulator.speedAnalysis(i, 0, 1);
			transmissionTime_min_collisions[0][i - 2] = numberOfNodes[i - 2];
			transmissionTime_min_collisions[1][i - 2] = aodvSimulator.getCollisions();
			System.out.println("Min Simulation for " + i * i + " nodes completed.");

			transmissionTime_med[0][i - 2] = numberOfNodes[i - 2];
			transmissionTime_med[1][i - 2] = aodvSimulator.speedAnalysis(i, 0, (i / 2) * i + i / 2);
			transmissionTime_med_collisions[0][i - 2] = numberOfNodes[i - 2];
			transmissionTime_med_collisions[1][i - 2] = aodvSimulator.getCollisions();
			System.out.println("Med Simulation for " + i * i + " nodes completed.");

		}

		// Transmission Time
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", transmissionTime_max);
		dataset.addSeries("mittlere Distanz", transmissionTime_med);
		dataset.addSeries("minimale Distanz", transmissionTime_min);

		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();

		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [ms]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);

		JFreeChart chart = new JFreeChart(plot);

		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV/AODV_Uebertragungszeit.png"), chart, CHART_WIDTH,
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

		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis2 = new NumberAxis("Kollisionen");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);

		JFreeChart chart2 = new JFreeChart(plot2);

		chart2.getPlot().setBackgroundPaint(Color.WHITE);
		chart2.setBackgroundPaint(Color.WHITE);

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/AODV/AODV_Uebertragungszeit_Kollisionen.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void evaluateCostAnalysis() {
		AodvSimulator aodvSimulator = new AodvSimulator();

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
			maxDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], (int) Math.pow(networkWidth[i], 2) - networkWidth[i],
					(int) Math.pow(i, 2) - 1);
			maxDistance_collisions[0][i] = numberOfNodes[i];
			maxDistance_collisions[1][i] = aodvSimulator.getCollisions();
			maxDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			maxDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Max Simulation for " + networkWidth[i] * networkWidth[i] + " nodes completed. Ausführungszeit des Netzwerks: "
					+ aodvSimulator.getNetworkLifetime() + " ms");

			minDistance[0][i] = numberOfNodes[i];
			minDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], 0, 1);
			minDistance_collisions[0][i] = numberOfNodes[i];
			minDistance_collisions[1][i] = aodvSimulator.getCollisions();
			minDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			minDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Min Simulation for " + networkWidth[i] * networkWidth[i] + " nodes completed. Ausführungszeit des Netzwerks: "
					+ aodvSimulator.getNetworkLifetime() + " ms");

			medDistance[0][i] = numberOfNodes[i];
			medDistance[1][i] = aodvSimulator.energyCostAnalysis(networkWidth[i], 0, (networkWidth[i] / 2) * networkWidth[i] + networkWidth[i] / 2);
			medDistance_collisions[0][i] = numberOfNodes[i];
			medDistance_collisions[1][i] = aodvSimulator.getCollisions();
			medDistance_onlyTransmissionEnergy[0][i] = numberOfNodes[i];
			medDistance_onlyTransmissionEnergy[1][i] = aodvSimulator.getConsumedEnergyInReciveMode()
					+ aodvSimulator.getConsumedEnergyInTransmissionMode();
			System.out.println("Med Simulation for " + networkWidth[i] * networkWidth[i] + " nodes completed. Ausführungszeit des Netzwerks: "
					+ aodvSimulator.getNetworkLifetime() + " ms");

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
			ChartUtilities.saveChartAsPNG(new File("Output/AODV/AODV_Umgesetzte_Energie.png"), chart,
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
					new File("Output/AODV/AODV_Umgesetzte_Energie_nur_Uebertragungsenergie.png"), chart3,
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
			ChartUtilities.saveChartAsPNG(new File("Output/AODV/AODV_Umgesetzte_Energie_Kollisionen.png"),
					chart2, CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
