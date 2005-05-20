/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A tuple with three components x, y, and z.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.20
 */
public class Tuple3 implements Cloneable {

  /**
   * The component x.
   */
  double x;

  /**
   * The component y.
   */
  double y;

  /**
   * The component z.
   */
  double z;

  /**
   * Constructs a tuple with all components equal to zero.
   */
  public Tuple3() {
  }

  /**
   * Constructs a tuple with specified components.
   * @param x the x component.
   * @param y the y component.
   * @param z the z component.
   */
  public Tuple3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Returns a clone of this tuple.
   * @return the clone.
   */
  public Tuple3 clone() {
    return new Tuple3(x,y,z);
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Tuple3 that = (Tuple3)obj;
    return this.x==that.x && this.y==that.y && this.z==that.z;
  }

  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    long zbits = Double.doubleToLongBits(z);
    return (int)(xbits^(xbits>>>32)^
                 ybits^(ybits>>>32)^
                 zbits^(zbits>>>32));
  }

  public String toString() {
    return "("+x+","+y+","+z+")";
  }
}
