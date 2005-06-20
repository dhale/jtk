/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A line segment for picking.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.17
 */
public class PickSegment implements Cloneable {

  /**
   * Constructs a pick segment with the specified near and far endpoints.
   * @param n the near endpoint.
   * @param f the far endpoint.
   */
  public PickSegment(Point3 n, Point3 f) {
    _n = n.clone();
    _f = f.clone();
  }

  /**
   * Clones this pick segment.
   */
  public PickSegment clone() {
    return new PickSegment(_n,_f);
  }

  /**
   * Gets the near endpoint of this pick segment.
   * @return the near endpoint.
   */
  public Point3 getNearPoint() {
    return _n.clone();
  }

  /**
   * Gets the far endpoint of this pick segment.
   * @return the far endpoint.
   */
  public Point3 getFarPoint() {
    return _f.clone();
  }

  /**
   * Transforms this pick segment, given the specified transform matrix.
   * @param m the transform matrix.
   */
  public void transform(Matrix44 m) {
    _n = m.times(_n);
    _f = m.times(_f);
  }

  private Point3 _n;
  private Point3 _f;
}
