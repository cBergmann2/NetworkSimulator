package DSDV;

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

public class DsdvEvaluationUnit extends EvaluationUnit{

	//private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	private static final int networkWidth[] = {2, 3, 4, 5, 6, 7, 8, 9, 10};
	
	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;
	
	@Override
	public void evaluateSpeedAnalysis() {
		System.out.println("Start DSDV spped anylsis.");
		DsdvSimulator simulator = new DsdvSimulator();

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

			
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0 + " SinkNode: 1");
			transmissionTime_min[0][i] = numberOfNodes[i];
			transmissionTime_min[1][i] = simulator.speedAnalysis(networkWidth[i], 0, 1)/1000;
			transmissionTime_min_collisions[0][i] = numberOfNodes[i];
			transmissionTime_min_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_min[0][i] = numberOfNodes[i];
			transmissionTime_msg_min[1][i] = simulator.getMsgTransmissionTime()/1000; 
			System.out.println("Min Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");

			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0 + " SinkNode: " + (networkWidth[i]-1));
			transmissionTime_med[0][i] = numberOfNodes[i];
			transmissionTime_med[1][i] = simulator.speedAnalysis(networkWidth[i], 0, networkWidth[i]-1)/1000;
			transmissionTime_med_collisions[0][i] = numberOfNodes[i];
			transmissionTime_med_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_med[0][i] = numberOfNodes[i];
			transmissionTime_msg_med[1][i] = simulator.getMsgTransmissionTime()/1000; 
			System.out.println("Med Simulation for " + Math.pow(networkWidth[i], 2) + " nodes completed.");
	
			
			System.out.println("Max Simulation for " + Math.pow(networkWidth[i], 2) + " nodes. SourceNode: " + 0 + " SinkNode: " + (int)(Math.pow(networkWidth[i], 2) - 1));
			transmissionTime_max[0][i] = numberOfNodes[i];
			transmissionTime_max[1][i] = simulator.speedAnalysis(networkWidth[i], 0,
					(int) Math.pow(networkWidth[i], 2) - 1) /1000;
			transmissionTime_max_collisions[0][i] = numberOfNodes[i];
			transmissionTime_max_collisions[1][i] = simulator.getCollisions();
			transmissionTime_msg_max[0][i] = numberOfNodes[i];
			transmissionTime_msg_max[1][i] = simulator.getMsgTransmissionTime()/1000; 
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
			ChartUtilities.saveChartAsPNG(new File("Output/DSDV/DSDV_Uebertragungszeit_Nachricht.png"), chart2,
					CHART_WIDTH, CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void evaluateCostAnalysis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evaluateNetworkLivetimeStaticSendBehavior() {
		// TODO Auto-generated method stub
		
	}

}
