/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import edu.mines.jtk.util.Check;

/**
 * Converts (projects) world coordinates v to/from normalized coordinates u.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.14
 */
public class Projector {

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

  ///////////////////////////////////////////////////////////////////////////
  // private

  private double _u0,_u1;
  private double _v0,_v1;
  private double _ushift,_uscale;
  private double _vshift,_vscale;

  private void computeShiftAndScale() {
    _uscale = (_v1-_v0)/(_u1-_u0);
    _ushift = _v0-_uscale*_u0;
    _vscale = (_u1-_u0)/(_v1-_v0);
    _vshift = _u0-_vscale*_v0;
  }
}
