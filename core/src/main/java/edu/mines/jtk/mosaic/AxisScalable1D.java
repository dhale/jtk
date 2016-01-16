package edu.mines.jtk.mosaic;

public class AxisScalable1D implements AxisScalable {

	Scale scale;
	
	public AxisScalable1D(Scale s) {
		scale  = s;
	}
	
	public AxisScalable1D() {
		scale  = Scale.LINEAR;
	}	
	
	@Override
	public TiledView setHScale(Scale s) {
		// TODO Auto-generated method stub
		System.out.println("AxisScalable1D.setHScale()");
		return null;
	}

	@Override
	public TiledView setVScale(Scale s) {
		// TODO Auto-generated method stub
		System.out.println("AxisScalable1D.setVScale()");
		return null;
	}

	@Override
	public Scale getHScale() {
		// TODO Auto-generated method stub
		System.out.println("AxisScalable1D.getHScale()");
		return null;
	}

	@Override
	public Scale getVScale() {
		// TODO Auto-generated method stub
		System.out.println("AxisScalable1D.getVScale()");
		return null;
	}

}