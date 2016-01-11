package edu.mines.jtk.mosaic;

/**
 * Abstract interface used to define methods required for changing axis scaling.
 */
public interface AxisScalable {

	public TiledView setHScale(Scale s);
	
	public TiledView setVScale(Scale s);
	
	public Scale getScale();
	
}
