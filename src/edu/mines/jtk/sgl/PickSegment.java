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
public class PickSegment extends LineSegment {

  /**
   * Constructs a pick segment with the specified near and far endpoints.
   * @param n the near endpoint A.
   * @param f the far endpoint B.
   */
  public PickSegment(Point3 n, Point3 f) {
    super(n,f);
  }

  /**
   * Clones this pick segment.
   * @return the clone.
   */
  public PickSegment clone() throws CloneNotSupportedException {
    return (PickSegment)super.clone();
  }

  /**
   * Gets the near endpoint A of this pick segment.
   * @return the near endpoint.
   */
  public Point3 getNearPoint() {
    return getA();
  }

  /**
   * Gets the far endpoint B of this pick segment.
   * @return the far endpoint.
   */
  public Point3 getFarPoint() {
    return getB();
  }
}
