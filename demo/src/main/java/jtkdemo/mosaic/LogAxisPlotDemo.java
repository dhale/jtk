package jtkdemo.mosaic;

import java.awt.Color;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.mosaic.Projector;
import edu.mines.jtk.mosaic.Scale;
import edu.mines.jtk.util.ArrayMath;

public class LogAxisPlotDemo {
	public static void main(String args[]){
		
		// use this simple plot to observe changes to TileAxis
		int n = 1000;
		float X = 300;

		// a good old linear function
		float[] x = new float[n];
		x = ArrayMath.rampfloat(0.04f, X/n, n);
		float[] f1 = new float[n];
		float[] f2 = new float[n];
		for(int i=0; i<n; ++i){
			f1[i] = ArrayMath.pow(x[i],1);
			f2[i] = ArrayMath.pow(0.5f*x[i],1);
		}

		PlotPanel plot = new PlotPanel();
		PointsView pv1 = plot.addPoints(x, f1);
		PointsView pv2 = plot.addPoints(x, f2);
		pv1.getHorizontalProjector().setScale(Scale.LOG);
		System.out.println(pv1.getHorizontalProjector().getScale());
		plot.setVisible(true);
	    PlotFrame frame = new PlotFrame(plot);
	    frame.setSize(800,800);
	    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
		
	    
	    // Projector log scale test
		// assuming safe input for now
	    Projector p = new Projector(0.1, 100, 0.0, 1.0);
	    p.setScale(Scale.LINEAR);
	    System.out.println("p.u(0) = " + p.u(0.3));
	    System.out.println("p.u(50) = " + p.u(50));
	    System.out.println("p.u(100) = " + p.u(100));
	    p.setScale(Scale.LOG);
	    System.out.println("p.u(0.3) = " + p.u(0.3));
	    System.out.println("p.u(10) = " + p.u(10));
	    System.out.println("p.u(100) = " + p.u(100));
	    System.out.println("p.v(p.u(0.3)) = " + p.v(p.u(0.3)));
	    System.out.println("p.v(p.u(10)) = " + p.v(p.u(10)));
	    System.out.println("p.v(p.u(100)) = " + p.v(p.u(100)));
	    
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
