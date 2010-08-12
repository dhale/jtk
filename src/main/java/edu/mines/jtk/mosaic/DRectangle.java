/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.max;
import static java.lang.Math.min;

import edu.mines.jtk.util.Check;

/**
 * A double-precision rectangle. The rectangle is represented by a corner
 * point (x,y) and size (width,height). The corner point (x,y) is that
 * point inside the rectangle with minimum x and y coordinates. All points
 * <em>contained</em> by the rectangle have x coordinates in the range 
 * [x,x+width] and y coordinates in the range [y,y+height].
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.11
 */
public class DRectangle {

  /**
   * The minimum x-coordinate for this rectangle.
   */
  public double x;

  /**
   * The minimum y-coordinate for this rectangle.
   */
  public double y;

  /**
   * The width of this rectangle. The maximum x-coordinate is x+width.
   */
  public double width;

  /**
   * The height of this rectangle. The maximum y-coordinate is y+height.
   */
  public double height;

  /**
   * Constructs a rectangle.
   * @param x the minimum x coordinate.
   * @param y the minimum y coordinate.
   * @param width the width; must not be negative.
   * @param height the height; must not be negative.
   */
  public DRectangle(double x, double y, double width, double height) {
    Check.argument(width>=0.0,"width is non-negative");
    Check.argument(height>=0.0,"height is non-negative");
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Constructs a copy of the specified rectangle.
   * @param r the rectangle.
   */
  public DRectangle(DRectangle r) {
    this(r.x,r.y,r.width,r.height);
  }

  /**
   * Returns the union of this rectangle and a specified rectangle.
   * @param rect a rectangle.
   * @return the union.
   */
  public DRectangle union(DRectangle rect) {
    double xn = min(this.x,rect.x);
    double yn = min(this.y,rect.y);
    double wn = max(this.x+this.width,rect.x+rect.width)-xn;
    double hn = max(this.y+this.height,rect.y+rect.height)-yn;
    return new DRectangle(xn,yn,wn,hn);
  }

  /**
   * Returns the intersection of this rectangle and a specified rectangle.
   * @param rect a rectangle.
   * @return the intersection.
   */
  public DRectangle intersection(DRectangle rect) {
    double xn = max(this.x,rect.x);
    double yn = max(this.y,rect.y);
    double wn = max(min(this.x+this.width,rect.x+rect.width)-xn,0.0);
    double hn = max(min(this.y+this.height,rect.y+rect.height)-yn,0.0);
    return new DRectangle(xn,yn,wn,hn);
  }

  /**
   * Determines whether this rectangle is empty.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return width<=0.0 || height<=0.0;
  }

  /**
   * Determines whether this rectangle contains the specified point.
   * @param point the point.
   * @return true, if this rectangle contains the point; false, otherwise.
   */
  public boolean contains(DPoint point) {
    return contains(point.x,point.y);
  }

  /**
   * Determines whether this rectangle contains the point (x,y).
   * @param x the x-coordinate of the point.
   * @param y the y-coordinate of the point.
   * @return true, if this rectangle contains the point; false, otherwise.
   */
  public boolean contains(double x, double y) {
    return this.x<=x && 
           this.y<=y && 
           (x-this.x)<=this.width && 
           (y-this.y)<=this.height;
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    DRectangle that = (DRectangle)obj;
    return this.x==that.x && 
           this.y==that.y && 
           this.width==that.width &&
           this.height==that.height;
  }

  public int hashCode() {
    long xbits = Double.doubleToLongBits(x);
    long ybits = Double.doubleToLongBits(y);
    long wbits = Double.doubleToLongBits(width);
    long hbits = Double.doubleToLongBits(height);
    return (int)(xbits^(xbits>>>32) ^
                 ybits^(ybits>>>32) ^
                 wbits^(wbits>>>32) ^
                 hbits^(hbits>>>32));
  }

  public String toString() {
    return "("+x+","+y+","+width+","+height+")";
  }
}
