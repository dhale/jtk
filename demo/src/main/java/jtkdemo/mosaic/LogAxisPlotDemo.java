package jtkdemo.mosaic;

import java.awt.Color;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;

public class LogAxisPlotDemo {
	public static void main(String args[]){
		
		// use this simple plot to observe changes to TileAxis
		int n = 1000;
		float X = 100;

		// a good old linear function
		float[] x = linSpace(0.001, X, n);
		float[] f1 = new float[n];
		float[] f2 = new float[n];
		for(int i=0; i<n; ++i){
			f1[i] = f1(x[i]);
			f2[i] = f2(x[i]);
		}

		// log scale functions
		float[] logx = logSpace(-1.5, Math.log10(X), n);
		float[] expx = linSpace(-1.5, Math.log10(X), n);
		float[] logf1 = new float[n];
		float[] logf2 = new float[n];
		float[] f1logx = new float[n];
		float[] f2logx = new float[n];
		float[] logf1logx = new float[n];
		float[] logf2logx = new float[n];
		for(int i=0; i<n; ++i){
			logf1[i] = (float)Math.log10(f1[i]);
			logf2[i] = (float)Math.log10(f2[i]);
			f1logx[i] = f1(logx[i]);
			logf1logx[i] = (float)Math.log10(f1logx[i]);
			f2logx[i] = f2(logx[i]);
			logf2logx[i] = (float)Math.log10(f2logx[i]);
		}
		
		PlotPanel plot = new PlotPanel(2,2);
		plot.getMosaic().getTileAxisLeft(1).setLogScale(true);
		plot.getMosaic().getTileAxisBottom(1).setLogScale(true);
		
		plot.addPoints(0,0,x, f1).setLineColor(Color.BLUE);
		plot.addPoints(0,0,x, f2).setLineColor(Color.RED);
		
		plot.addPoints(1,0,x, logf1).setLineColor(Color.BLUE);
		plot.addPoints(1,0,x, logf2).setLineColor(Color.RED);
		
		
		plot.addPoints(0,1,expx, f1logx).setLineColor(Color.BLUE);
		plot.addPoints(0,1,expx, f2logx).setLineColor(Color.RED);
		
		plot.addPoints(1,1,expx, logf1logx).setLineColor(Color.BLUE);
		plot.addPoints(1,1,expx, logf2logx).setLineColor(Color.RED);
		
		plot.setVisible(true);
		plot.setHLabel(0, "x - linear scale");
		plot.setHLabel(1, "x - log scale");
		plot.setVLabel(0, "y - linear scale");
		plot.setVLabel(1, "y - log scale");
	    PlotFrame frame = new PlotFrame(plot);
	    frame.setSize(800,800);
	    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
		
	}
	
	public static float f1(double x){
		return (float) (4*x*Math.sin(10/x)) + 10.0f;
	}
	
	public static float f2(double x){
		return (float)Math.exp(x/20);
	}
	
	
	// generate an array of log-spaced data, i.e between 10^a and 10^b with even steps in the exponent
	public static float[] logSpace(double a, double b, int n){
		float[] y = new float[n];
		double de = (b-a)/n;
		for(int i=0; i<n; i++)
			y[i] = (float)Math.pow(10, a+i*de);
		return y;
	}
	
	// generate an array of evenly spaced data
	public static float[] linSpace(double a, double b, int n){
		float[] y = new float[n];
		double de = (b-a)/n;
		for(int i=0; i<n; i++)
			y[i] = (float)(a + i*de);
		return y;
	}

}
