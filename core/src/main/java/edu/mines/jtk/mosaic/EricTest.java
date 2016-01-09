package edu.mines.jtk.mosaic;


public class EricTest {
	public static void main(String args[]){
		
		// can I make a simplePlot?
		// use this simple plot to observe changes to TileAxis
		int n = 5000;
		float X = 100;
		float dx = X/n;
		float[] x = new float[n];
		float[] logx = new float[n];
		float[] f = new float[n];
		float[] logf = new float[n];
		float[] flogx = new float[n];
		for(int i=0; i<n; ++i){
			x[i] = 0.1f+i*dx;
			logx[i] = (float)Math.log10(x[i]);
			f[i] = f1(x[i]);
			logf[i] = (float)Math.log10(f[i]);
			flogx[i] = f1(Math.log10(x[i]));
		}
		
		PlotPanel plot = new PlotPanel(2,2);
		plot.getMosaic().getTileAxisLeft(1).setLogScale(true);
		plot.getMosaic().getTileAxisBottom(1).setLogScale(true);
		plot.addPoints(0,0,x, f);
		plot.addPoints(1,0,x, logf);
		plot.addPoints(0,1,logx, f);
		plot.addPoints(1,1,logx, logf);
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
		return (float) (Math.exp(x/10)*Math.sin(10/x)) + 10;
	}
}