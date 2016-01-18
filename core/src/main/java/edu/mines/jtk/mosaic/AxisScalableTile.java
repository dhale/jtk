package edu.mines.jtk.mosaic;

import java.util.Iterator;

public class AxisScalableTile implements AxisScalable {

  private Tile _tile;
  private Scale _hscale, _vscale;

  public AxisScalableTile(Tile tile, Scale hscale, Scale vscale) {
    _tile = tile;
    _hscale = hscale;
    _vscale = vscale;
  }

  @Override
  public AxisScalable setHScale(Scale s) {
    if (_hscale != s) {
      _hscale = s;

      // set all TiledViews in this tile to new scale
      Iterator<TiledView> itr = _tile.getTiledViews();
      while (itr.hasNext()) {
        TiledView tv = itr.next();
        if (tv instanceof AxisScalable)
          ((AxisScalable) tv).setHScale(_hscale);
      }

      // set other tiles in this row to new scale
      Mosaic mos = _tile.getMosaic();
      for (int jrow = 0; jrow < mos.countRows(); ++jrow) {
        Tile t = mos.getTile(jrow, _tile.getColumnIndex());
        t.setHScale(_hscale);
      }

    }

    // update axistics for this tile
    if (_tile.getTileAxisTop() != null) {
      _tile.getTileAxisTop().updateAxisTics();
      _tile.getTileAxisTop().repaint();
    }
    if (_tile.getTileAxisBottom() != null) {
      _tile.getTileAxisBottom().updateAxisTics();
      _tile.getTileAxisBottom().repaint();
    }
    _tile.repaint();
    return _tile;
  }

  @Override
  public AxisScalable setVScale(Scale s) {
    if (_vscale != s) {
      _vscale = s;
      // set all TiledViews in this tile to new scale
      Iterator<TiledView> itr = _tile.getTiledViews();
      while (itr.hasNext()) {
        TiledView tv = itr.next();
        if (tv instanceof AxisScalable)
          ((AxisScalable) tv).setVScale(_vscale);
      }

      // set other tiles in this row to new scale
      Mosaic mos = _tile.getMosaic();
      for (int jcol = 0; jcol < mos.countColumns(); ++jcol) {
        Tile t = mos.getTile(_tile.getRowIndex(), jcol);
        t.setVScale(_vscale);
      }

    }

    // update axistics for this tile
    if (_tile.getTileAxisLeft() != null) {
      _tile.getTileAxisLeft().updateAxisTics();
      _tile.getTileAxisLeft().repaint();
    }
    if (_tile.getTileAxisRight() != null) {
      _tile.getTileAxisRight().updateAxisTics();
      _tile.getTileAxisRight().repaint();
    }
    _tile.repaint();
    return _tile;
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
