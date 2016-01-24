/****************************************************************************
Copyright 2016, Colorado School of Mines.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
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
  public AxisScalable setHScale(Scale s) {
    if(s != _hscale){
      Projector hp = ((TiledView)_as).getHorizontalProjector();
      Tile tile = ((TiledView)_as).getTile();
      if(hp != null)
        hp.setScale(s);
      if( tile != null)
        tile.setHScale(s);
    }
    _hscale = s;  
    return _as;
  }

  @Override
  public AxisScalable setVScale(Scale s) {
  if(s != _vscale){
    Projector vp = ((TiledView)_as).getVerticalProjector();
    Tile tile = ((TiledView)_as).getTile();
    if(vp != null)
      vp.setScale(s);
    if( tile != null)
      tile.setVScale(s);
  }
  _vscale = s; 
    return _as;
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