/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A tuple with four components x, y, z, and w.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.19
 */
public class Tuple4 {

  /**
   * The component x.
   */
  public double x;

  /**
   * The component y.
   */
  public double y;

  /**
   * The component z.
   */
  public double z;

  /**
   * The component w.
   */
  public double w;

  /**
   * Constructs a tuple with all components equal to zero.
   */
  public Tuple4() {
  }

  /**
   * Constructs a tuple with specified components.
   * @param x the x component.
   * @param y the y component.
   * @param z the z component.
   * @param w the w component.
   */
  public Tuple4(double x, double y, double z, double w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  /**
   * Constructs a copy of the specified tuple.
   * @param t the tuple.
   */
  public Tuple4(Tuple4 t) {
    x = t.x;
    y = t.y;
    z = t.z;
    w = t.w;
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Tuple4 that = (Tuple4)obj;
    return this.x==that.x && 
           this.y==that.y && 
           this.z==that.z &&
           this.w==that.w;
  }

  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    long zbits = Double.doubleToLongBits(z);
    long wbits = Double.doubleToLongBits(w);
    return (int)(xbits^(xbits>>>32)^
                 ybits^(ybits>>>32)^
                 zbits^(zbits>>>32)^
                 wbits^(wbits>>>32));
  }

  public String toString() {
    return "("+x+","+y+","+z+","+w+")";
  }
}
