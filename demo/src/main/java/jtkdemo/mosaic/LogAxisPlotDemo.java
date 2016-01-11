package jtkdemo.mosaic;

import java.awt.Color;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.Scale;
import edu.mines.jtk.util.ArrayMath;

public class LogAxisPlotDemo {
	public static void main(String args[]){
		
		// use this simple plot to observe changes to TileAxis
		int n = 1000;
		float X = 100;

		// a good old linear function
		float[] x = new float[n];
		x = ArrayMath.rampfloat(0, X/n, n);
		float[] f = new float[n];
		for(int i=0; i<n; ++i){
			f[i] = ArrayMath.pow(x[i],2);
		}

		PlotPanel plot = new PlotPanel();
		plot.addPoints(x, f);	
		plot.setVisible(true);
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
