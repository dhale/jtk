package edu.mines.jtk.mosaic;

public class EricTest {
	public static void main(String args[]){
		
		// can I print?
		System.out.println("Hello?");
		
		// can I make a simplePlot?
		// use this simple plot to observe changes to TileAxis
		int n = 100;
		float[] x = new float[n];
		float[] logx = new float[n];
		float[] f = new float[n];
		for(int i=0; i<n; ++i){
			x[i] = i;
			logx[i] = (float)(-1.5 + i*3.5/n);
			f[i] = (float)Math.log10(((float) Math.sin(2*Math.PI*0.01*i)+2)*(float)Math.exp(x[i]/10));
		}
		SimplePlot plot = new SimplePlot();
		plot.getPlotPanel().getMosaic().getTileAxisBottom(0).setLogScale(true);
		plot.getPlotPanel().getMosaic().getTileAxisLeft(0).setLogScale(true);
		plot.addPoints(logx, f);
		
		
		
	}
}