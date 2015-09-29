/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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
package edu.mines.jtk.sgl;

import java.awt.event.MouseEvent;

/**
 * A constrained mouse. Classes that extend this abstract base class are
 * used to convert mouse events, with mouse pixel coordinates, to points 
 * in a local coordinate system. The constraint is necessary, because 
 * mouse pixel coordinates are two-dimensional, whereas local coordinates 
 * are three-dimensional.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.05
 */
public abstract class MouseConstrained {

  /**
   * Constructs a constrained mouse.
   * @param localToPixel the transform from local to pixel coordinates.
   */
  public MouseConstrained(Matrix44 localToPixel) {
    //_localToPixel = new Matrix44(localToPixel);
    _pixelToLocal = localToPixel.inverse();
  }

  /**
   * Gets the point in local coordinates corresponding to the specified event.
   * @param event the mouse event.
   * @return the point, in local coordinates.
   */
  public abstract Point3 getPoint(MouseEvent event);

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Gets a line segment in local coordinates for the specified event.
   * The endpoints A and B of the line segment lie on the near and far
   * clipping planes, respectively.
   * @param event the mouse event.
   * @return the line segment.
   */
  protected Segment getMouseSegment(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();
    Point3 near = new Point3(x,y,0);
    Point3 far = new Point3(x,y,1);
    near = _pixelToLocal.times(near);
    far = _pixelToLocal.times(far);
    return new Segment(near,far);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  //private Matrix44 _localToPixel;
  private Matrix44 _pixelToLocal;
}
