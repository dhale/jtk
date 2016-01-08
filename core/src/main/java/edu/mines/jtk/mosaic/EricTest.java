package edu.mines.jtk.mosaic;

public class EricTest {
	public static void main(String args[]){
		
		// can I print?
		System.out.println("Hello?");
		
		// can I make a simplePlot?
		// use this simple plot to observe changes to TileAxis
		int n = 100;
		float[] x = new float[n];
		float[] f = new float[n];
		for(int i=0; i<n; ++i){
			x[i] = i;
			f[i] = (float) Math.sin(2*Math.PI*0.01*i);
		}
		SimplePlot plot = new SimplePlot();
		plot.addPoints(x, f);
		
		
		
	}
}
