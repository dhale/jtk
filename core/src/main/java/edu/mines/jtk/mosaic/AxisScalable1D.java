package edu.mines.jtk.mosaic;

public class AxisScalable1D implements AxisScalable {

	private AxisScalable _as;
	private Scale _hscale, _vscale;
	
	public AxisScalable1D(AxisScalable as, Scale hscale, Scale vscale) {
		_as = as;
		_hscale = hscale;
		_vscale = vscale;
	}
	
	@Override
	public TiledView setHScale(Scale s) {
		  if(s != _hscale){
			  Projector hp = ((TiledView)_as).getHorizontalProjector();
			  Tile tile = ((TiledView)_as).getTile();
			  if(hp != null)
				  hp.setScale(s);
			  if( tile != null)
				  tile.setHScale(s);
	  	  }
		_hscale = s;  
		System.out.println("AxisScalable1D.setHScale()");
		return null;
	}

	@Override
	public TiledView setVScale(Scale s) {
		  if(s != _vscale){
			  Projector vp = ((TiledView)_as).getVerticalProjector();
			  Tile tile = ((TiledView)_as).getTile();
			  if(vp != null)
				  vp.setScale(s);
			  if( tile != null)
				  tile.setVScale(s);
	  	  }
		  _vscale = s; 
		System.out.println("AxisScalable1D.setVScale()");
		return null;
	}

	@Override
	public Scale getHScale() {
		return _hscale;
	}

	@Override
	public Scale getVScale() {
		return _vscale;
	}

}