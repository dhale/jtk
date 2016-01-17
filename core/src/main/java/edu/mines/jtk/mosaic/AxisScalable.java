package edu.mines.jtk.mosaic;

/**
 * Abstract interface used to define methods required for changing axis scaling of TiledViews.
 */
public interface AxisScalable {

	public AxisScalable setHScale(Scale s);
	
	public AxisScalable setVScale(Scale s);
	
	public Scale getHScale();
	
	public Scale getVScale();
	
}