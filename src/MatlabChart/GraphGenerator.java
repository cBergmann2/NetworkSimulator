package MatlabChart;

import org.jfree.data.xy.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;

import AODV.AodvSimulator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;

public class GraphGenerator {

	private final int MAX_NETWORK_WIDTH = 4;
	
	public void speedAnalysis(){
		
		
		//AODV
		AodvSimulator aodvSimulator = new AodvSimulator();
		
		
		double numberOfNodes[] = new double[MAX_NETWORK_WIDTH-1];
		double transmissionTime_max[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_min[][] = new double[2][MAX_NETWORK_WIDTH-1];
		double transmissionTime_med[][] = new double[2][MAX_NETWORK_WIDTH-1];
		for(int i=2; i<MAX_NETWORK_WIDTH+1; i++){
			numberOfNodes[i-2] = Math.pow(i, 2);
			transmissionTime_max[0][i-2] = numberOfNodes[i-2];
			transmissionTime_max[1][i-2] = aodvSimulator.speedAnalysis(i, 0, (int)Math.pow(i, 2)-1);
			System.out.println("Max Simulation for " + i*i + " nodes completed." );

			transmissionTime_min[0][i-2] = numberOfNodes[i-2];
			transmissionTime_min[1][i-2] = aodvSimulator.speedAnalysis(i, 0, 1);
			System.out.println("Min Simulation for " + i*i + " nodes completed." );
			
			transmissionTime_med[0][i-2] = numberOfNodes[i-2];
			transmissionTime_med[1][i-2] = aodvSimulator.speedAnalysis(i, 0, (i/2)*i+i/2);
			System.out.println("Med Simulation for " + i*i + " nodes completed." );

		}
		
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("maximale Distanz", transmissionTime_max);
		dataset.addSeries("mittlere Distanz", transmissionTime_med);
		dataset.addSeries("minimale Distanz", transmissionTime_min);
		
		
		//XYDotRenderer dot = new XYDotRenderer();
		XYLineAndShapeRenderer line = new XYLineAndShapeRenderer();
		
		NumberAxis xAxis = new NumberAxis("Anzahl Knoten");
		NumberAxis yAxis = new NumberAxis("Übertragungszeit [ms]");
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, line);
		
		JFreeChart chart = new JFreeChart(plot);
		
		//LegendTitle legend = chart.getLegend();
		//legend.setPosition(RectangleEdge.RIGHT);
		
		/*
		ApplicationFrame frame = new ApplicationFrame("Ausgabe");
		ChartPanel chartPanel = new ChartPanel(chart);
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
		
		 */
		
		chart.getPlot().setBackgroundPaint( Color.WHITE );
		chart.setBackgroundPaint(Color.WHITE);
		
		this.createImg("Output/AODV/AODV_Uebertragungszeit_max_Entfernung.png", chart);

	}
	
	private void createImg(String uri, JFreeChart chart){
		try {
			ChartUtilities.saveChartAsPNG(new File(uri),chart,480,360);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
