/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static edu.mines.jtk.util.M.*;
import edu.mines.jtk.util.*;

/**
 * Converts (projects) world coordinates v to/from normalized coordinates u.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.14
 */
public class Projector implements Cloneable {

  /**
   * Constructs a projector with specified u and v values. The
   * parameters u0 and u1 determine the margins of the projector.
   * The world coordinate v0 corresponds to normalized coordinate u0.
   * @param u0 the u coordinate closest to normalized coordinate 0;
   *  0.0 &lt;= u0 &lt; u1 is required.
   * @param u1 the u coordinate closest to normalized coordinate 1;
   *  u0 &lt; u1 &lt;= 1.0 is required.
   * @param v0 the v coordinate that corresponds to u coordinate u0;
   *  v0 != v1 is required.
   * @param v1 the v coordinate that corresponds to u coordinate u1;
   *  v0 != v1 is required.
   */
  public Projector(double u0, double u1, double v0, double v1) {
    Check.argument(0.0<=u0,"0.0 <= u0");
    Check.argument(u0<u1,"u0 < u1");
    Check.argument(u1<=1.0,"u1 <= 1.0");
    Check.argument(v0!=v1,"v0 != v1");
    _u0 = u0;
    _u1 = u1;
    _v0 = v0;
    _v1 = v1;
    computeShiftsAndScales();
  }

  /**
   * Returns a clone of this projector.
   * @return the clone.
   */
  public Projector clone() {
    return new Projector(_u0,_u1,_v0,_v1);
  }

  /**
   * Converts world coordinate v to normalized coordinate u.
   * @param v world coordinate v.
   * @return normalized coordinate u.
   */
  public double u(double v) {
    return _vshift+_vscale*v;
  }

  /**
   * Converts normalized coordinate u to world coordinate v.
   * @param u normalized coordinate u.
   * @return world coordinate v.
   */
  public double v(double u) {
    return _ushift+_uscale*u;
  }

  /**
   * Merges the specified projector into this projector.
   * @param p the projector.
   */
  public void merge(Projector p) {

    // Ignore null projectors.
    if (p==null)
      return;

    // Parameters for this projector A.
    double u0a = _u0;
    double u1a = _u1;
    double v0a = _v0;
    double v1a = _v1;

    // Parameters for that projector B.
    double u0b = p._u0;
    double u1b = p._u1;
    double v0b = p._v0;
    double v1b = p._v1;

    // Merge normalized coordinate bounds. Ensure u0 < u1.
    _u0 = max(u0a,u0b);
    _u1 = min(u1a,u1b);
    if (_u0>=_u1) {
      double um = 0.5*(_u0+_u1);
      _u0 = um-10.0*DBL_EPSILON;
      _u1 = um+10.0*DBL_EPSILON;
    }

    // Merge world coordinate bounds. (Condition v0 < v1 is invariant.)
    if (v0a<v1a) {
      _v0 = min(v0a,min(v0b,v1b));
      _v1 = max(v1a,max(v0b,v1b));
    } else {
      _v0 = max(v0a,max(v0b,v1b));
      _v1 = min(v1a,min(v0b,v1b));
    }

    // Recompute shifts and scales.
    computeShiftsAndScales();
  }

  public boolean equals(Object obj) {
    if (this==obj)
      return true;
    if (obj==null || this.getClass()!=obj.getClass())
      return false;
    Projector that = (Projector)obj;
    return this._u0==that._u0 && 
           this._u1==that._u1 &&
           this._v0==that._v0 &&
           this._v1==that._v1;
  }

  public int hashCode() {
    long u0bits = Double.doubleToLongBits(_u0);
    long u1bits = Double.doubleToLongBits(_u1);
    long v0bits = Double.doubleToLongBits(_v0);
    long v1bits = Double.doubleToLongBits(_v1);
    return (int)(u0bits^(u0bits>>>32) ^
                 u1bits^(u1bits>>>32) ^
                 v0bits^(v0bits>>>32) ^
                 v1bits^(v1bits>>>32));
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _u0,_u1;
  private double _v0,_v1;
  private double _ushift,_uscale;
  private double _vshift,_vscale;

  private void computeShiftsAndScales() {
    _uscale = (_v1-_v0)/(_u1-_u0);
    _ushift = _v0-_uscale*_u0;
    _vscale = (_u1-_u0)/(_v1-_v0);
    _vshift = _u0-_vscale*_v0;
  }
}
