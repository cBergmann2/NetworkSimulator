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

import AODV.AodvSimulator;
import Simulator.EvaluationUnit;

public class FloodingEvaluationUnit extends EvaluationUnit{
	
	private static final int MAX_NETWORK_WIDTH = 32;
	private static final int CHART_HIGHT = 300;
	private static final int CHART_WIDTH = 280;

	@Override
	public void evaluateSpeedAnalysis() {
		FloodingSimulator floodingSimulator = new FloodingSimulator();
		
		
		double numberOfNodes[] = new double[MAX_NETWORK_WIDTH-1];
		double transmissionTime_max[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_max_collisions[][] = new double[2][MAX_NETWORK_WIDTH-1];

		double min[][] = new double[2][MAX_NETWORK_WIDTH-1];
		
		double transmissionTime_min[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_min_collisions[][] = new double[2][MAX_NETWORK_WIDTH-1];
		
		double transmissionTime_med[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_med_collisions[][] = new double[2][MAX_NETWORK_WIDTH-1];
		
		
		for(int i=2; i<MAX_NETWORK_WIDTH+1; i++){
			numberOfNodes[i-2] = Math.pow(i, 2);
			transmissionTime_max[0][i-2] = numberOfNodes[i-2];
			transmissionTime_max[1][i-2] = floodingSimulator.speedAnalysis(i, (int)Math.pow(i, 2)-i, (int)Math.pow(i, 2)-1);
			transmissionTime_max_collisions[0][i-2] = numberOfNodes[i-2];
			transmissionTime_max_collisions[1][i-2] = floodingSimulator.getCollisions();
			System.out.println("Max Simulation for " + i*i + " nodes completed." );

			transmissionTime_min[0][i-2] = numberOfNodes[i-2];
			transmissionTime_min[1][i-2] = floodingSimulator.speedAnalysis(i, 0, 1);
			transmissionTime_min_collisions[0][i-2] = numberOfNodes[i-2];
			transmissionTime_min_collisions[1][i-2] = floodingSimulator.getCollisions();
			System.out.println("Min Simulation for " + i*i + " nodes completed." );
			
			transmissionTime_med[0][i-2] = numberOfNodes[i-2];
			transmissionTime_med[1][i-2] = floodingSimulator.speedAnalysis(i, 0, (i/2)*i+i/2);
			transmissionTime_med_collisions[0][i-2] = numberOfNodes[i-2];
			transmissionTime_med_collisions[1][i-2] = floodingSimulator.getCollisions();
			System.out.println("Med Simulation for " + i*i + " nodes completed." );

		}
		
		//Transmission Time
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", transmissionTime_max);
		dataset.addSeries("mittlere Distanz", transmissionTime_med);
		dataset.addSeries("minimale Distanz", transmissionTime_min);
		
		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
		
		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [ms]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		
		JFreeChart chart = new JFreeChart(plot);
		
		chart.getPlot().setBackgroundPaint( Color.WHITE );
		chart.setBackgroundPaint(Color.WHITE);
		

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Uebertragungszeit.png"),chart,480,360);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Collisions
		DefaultXYDataset dataset2 = new DefaultXYDataset();
		dataset2.addSeries("maximale Distanz", transmissionTime_max_collisions);
		dataset2.addSeries("mittlere Distanz", transmissionTime_med_collisions);
		dataset2.addSeries("minimale Distanz", transmissionTime_min_collisions);
		
		//XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
		
		NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis2 = new NumberAxis("Kollisionen");
		XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
		
		JFreeChart chart2 = new JFreeChart(plot2);
		
		chart2.getPlot().setBackgroundPaint( Color.WHITE );
		chart2.setBackgroundPaint(Color.WHITE);
		

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Uebertragungszeit_Kollisionen.png"),chart2,480,360);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void evaluateKostAnalysis() {
		FloodingSimulator floodingSimulator = new FloodingSimulator();
		
		
		double numberOfNodes[] = new double[MAX_NETWORK_WIDTH-1];
		double maxDistance[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_max_collisions[][] = new double[2][MAX_NETWORK_WIDTH-1];
		
		double minDistance[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_min_collisions[][] = new double[2][MAX_NETWORK_WIDTH-1];
		
		double medDistance[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_med_collisions[][] = new double[2][MAX_NETWORK_WIDTH-1];
		
		
		for(int i=2; i<MAX_NETWORK_WIDTH+1; i++){
			numberOfNodes[i-2] = Math.pow(i, 2);
			maxDistance[0][i-2] = numberOfNodes[i-2];
			maxDistance[1][i-2] = floodingSimulator.energyCostAnalysis(i, (int)Math.pow(i, 2)-i, (int)Math.pow(i, 2)-1);
			transmissionTime_max_collisions[0][i-2] = numberOfNodes[i-2];
			transmissionTime_max_collisions[1][i-2] = floodingSimulator.getCollisions();
			System.out.println("Max Simulation for " + i*i + " nodes completed. Ausführungszeit des Netzwerks: " + floodingSimulator.getNetworkLifetime() + " ms" );

			minDistance[0][i-2] = numberOfNodes[i-2];
			minDistance[1][i-2] = floodingSimulator.energyCostAnalysis(i, 0, 1);
			transmissionTime_min_collisions[0][i-2] = numberOfNodes[i-2];
			transmissionTime_min_collisions[1][i-2] = floodingSimulator.getCollisions();
			System.out.println("Min Simulation for " + i*i + " nodes completed. Ausführungszeit des Netzwerks: " + floodingSimulator.getNetworkLifetime() + " ms" );

			
			medDistance[0][i-2] = numberOfNodes[i-2];
			medDistance[1][i-2] = floodingSimulator.energyCostAnalysis(i, 0, (i/2)*i+i/2);
			transmissionTime_med_collisions[0][i-2] = numberOfNodes[i-2];
			transmissionTime_med_collisions[1][i-2] = floodingSimulator.getCollisions();
			System.out.println("Med Simulation for " + i*i + " nodes completed. Ausführungszeit des Netzwerks: " + floodingSimulator.getNetworkLifetime() + " ms" );


		}
		
		//Transmission Time
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", maxDistance);
		dataset.addSeries("mittlere Distanz", medDistance);
		dataset.addSeries("minimale Distanz", minDistance);
		
		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
		
		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Umgesetzte Energie [nAs]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		
		JFreeChart chart = new JFreeChart(plot);
		
		chart.getPlot().setBackgroundPaint( Color.WHITE );
		chart.setBackgroundPaint(Color.WHITE);
		

		try {
			ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Umgesetzte_Energie.png"),chart,CHART_WIDTH,CHART_HIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Collisions
				DefaultXYDataset dataset2 = new DefaultXYDataset();
				dataset2.addSeries("maximale Distanz", transmissionTime_max_collisions);
				dataset2.addSeries("mittlere Distanz", transmissionTime_med_collisions);
				dataset2.addSeries("minimale Distanz", transmissionTime_min_collisions);
				
				//XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
				
				NumberAxis xAxis2 = new NumberAxis("Anzahl Knoten");
				NumberAxis yAxis2 = new NumberAxis("Kollisionen");
				XYPlot plot2 = new XYPlot(dataset2, xAxis2, yAxis2, line);
				
				JFreeChart chart2 = new JFreeChart(plot2);
				
				chart2.getPlot().setBackgroundPaint( Color.WHITE );
				chart2.setBackgroundPaint(Color.WHITE);
				

				try {
					ChartUtilities.saveChartAsPNG(new File("Output/Flooding/Flooding_Umgesetzte_Energie_Kollisionen.png"),chart2,CHART_WIDTH,CHART_HIGHT);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}

}
