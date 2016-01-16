package edu.mines.jtk.mosaic;

/**
 * Abstract interface used to define methods required for changing axis scaling.
 */
public interface AxisScalable {

	public void setHScale(Scale s);
	
	public void setVScale(Scale s);
	
	public Scale getHScale();
	
	public Scale getVScale();
	
}