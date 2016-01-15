package jtkdemo.mosaic;

import java.awt.Color;

import edu.mines.jtk.mosaic.PlotFrame;
import edu.mines.jtk.mosaic.PlotPanel;
import edu.mines.jtk.mosaic.PointsView;
import edu.mines.jtk.mosaic.Projector;
import edu.mines.jtk.mosaic.Scale;
import edu.mines.jtk.util.ArrayMath;
import static edu.mines.jtk.util.MathPlus.*;

public class LogAxisPlotDemo {
	public static void main(String args[]){
		
		// CURRENT ISSUES
		// when zoomed plot does not pan correctly (maybe will be better when data is transformed?)
		// still need to transform data (view dependent? Start with 1d interface for PointsView)
		//			- This might be done in the computeXY method in PointsView?
		//			- Which leads to Transcaler.combineWith()...
		
		// use this simple plot to observe changes to TileAxis
		int n = 1000;
		float X = 300;

		// a good old linear function
		float[] x = new float[n];
		x = ArrayMath.rampfloat(0.04f, X/n, n);
		float[] f1 = new float[n];
		float[] f2 = new float[n];
		for(int i=0; i<n; ++i){
			f1[i] = pow(1.5f*x[i],1);
			f2[i] = (float)exp(0.02*x[i]);
		}

		PlotPanel plot = new PlotPanel(2,2);
		
		// plain old linear plots
		PointsView pv1 = plot.addPoints(0,1,x, f1);
		PointsView pv2 = plot.addPoints(0,1,x, f2);
		pv1.setLineColor(Color.BLUE);
		pv2.setLineColor(Color.RED);
		
		// log-x plots
		PointsView pv3 = plot.addPoints(0,0,x, f1);
		PointsView pv4 = plot.addPoints(0,0,x, f2);
		pv3.getHorizontalProjector().setScale(Scale.LOG);
		pv3.setLineColor(Color.BLUE);
		pv4.setLineColor(Color.RED);

		// log-y plots
		PointsView pv5 = plot.addPoints(1,1,x, f1);
		PointsView pv6 = plot.addPoints(1,1,x, f2);
		pv5.getVerticalProjector().setScale(Scale.LOG);
		pv5.setLineColor(Color.BLUE);
		pv6.setLineColor(Color.RED);
		
		// log-log plots
		PointsView pv7 = plot.addPoints(1,0,x, f1);
		PointsView pv8 = plot.addPoints(1,0,x, f2);
		pv8.getVerticalProjector().setScale(Scale.LOG);
		pv8.getHorizontalProjector().setScale(Scale.LOG);
		pv7.setLineColor(Color.BLUE);
		pv8.setLineColor(Color.RED);		
		
		System.out.println(pv1.getHorizontalProjector().getScale());
		
		plot.setVisible(true);
	    PlotFrame frame = new PlotFrame(plot);
	    frame.setSize(800,800);
	    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
/*	    
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
	    */
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
