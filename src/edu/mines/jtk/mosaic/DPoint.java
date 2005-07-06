/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

/**
 * A double-precision point (x,y).
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class DPoint {

  /**
   * The x-coordinate of this point.
   */
  public double x;

  /**
   * The y-coordinate of this point.
   */
  public double y;

  /**
   * Constructs a point.
   * @param x the x-coordinate.
   * @param y the y-coordinate.
   */
  public DPoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a copy of the specified point.
   * @param p the point.
   */
  public DPoint(DPoint p) {
    this(p.x,p.y);
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    DPoint that = (DPoint)obj;
    return this.x==that.x && this.y==that.y;
  }

  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    return (int)(xbits^(xbits>>>32) ^
                 ybits^(ybits>>>32));
  }

  public String toString() {
    return "("+x+","+y+")";
  }
}
