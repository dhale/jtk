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
 * The projection is a simple scale and translation, such that specified
 * world coordinates (v0,v1) correspond to specified normalized coordinates 
 * (u0,u1).
 * <p> 
 * By definition, u0 is closest to normalized coordinate u=0, and u1 is 
 * closest to normalized coordinate u=1. These coordinates must satisfy
 * the constraints 0.0 &lt;= u0 &lt; u1 &lt;= 1.0.
 * <p>
 * The corresponding world coordinates (v0,v1) must not be equal (so that
 * the mapping to (u0,u1) is well-defined), but are otherwise unconstrained.
 * Note, in particular, that v0 may be greater than v1.
 * <p>
 * Typically, the coordinates (v0,v1) represent bounds in world coordinate 
 * space. Then, the gaps in normalized coordinate space [0,u0) and (u1,1] 
 * represent margins, extra space needed for graphic rendering. The amount 
 * of extra space required varies, depending on the graphics. Accounting for 
 * this varying amount of extra space is a complex but important aspect of 
 * aligning the coordinate systems of two or more graphics.
 * <p>
 * Alignment is accomplished by simply rendering all graphics using
 * the same projector. We obtain this shared projector by merging the 
 * preferred projectors of each graphic. A preferred projector is one
 * that a graphic might use if it were the only one being rendered.
 * <p>
 * We assume that each graphic has a preferred projector that indicates 
 * (1) its world coordinate span and (2) the margins [0,u0) and (u1,1]
 * that it would need if it were the only graphic rendered. We then merge
 * two projectors into one so that the merged projector (1) contains the 
 * union of the two world coordinate spans and (2) has adequate normalized 
 * coordinate margins. 
 * <p>
 * When rendering via such a merged projector, each graphic may use only 
 * a subset of the merged world coordinate span. The ratio of the span
 * v1-v0 in the preferred projector for a graphic to that in the merged
 * projector is its <em>scale factor</em>, a number with magnitude less
 * than or equal to one. We assume that the preferred margins of a graphic 
 * scale by this same factor, and we use those scaled preferred margins to 
 * compute the margins in the merged projector.
 * <p>
 * A projector has a sign, which is the sign of v1-v0. This sign is never
 * ambiguous, because v1 never equals v0. When merging a projector B into
 * into a projector A, we preserve the sign of projector A.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.01.01
 */
public class Projector implements Cloneable {

  /**
   * Constructs a projector with specified v and u values. The
   * parameters u0 and u1 determine the margins of the projector.
   * The world coordinate v0 corresponds to normalized coordinate u0;
   * the world coordinate v1 corresponds to normalized coordinate u1.
   * @param v0 the v coordinate that corresponds to u coordinate u0;
   *  v0 != v1 is required.
   * @param v1 the v coordinate that corresponds to u coordinate u1;
   *  v0 != v1 is required.
   * @param u0 the u coordinate closest to normalized coordinate 0;
   *  0.0 &lt;= u0 &lt; u1 is required.
   * @param u1 the u coordinate closest to normalized coordinate 1;
   *  u0 &lt; u1 &lt;= 1.0 is required.
   */
  public Projector(double v0, double v1, double u0, double u1) {
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
    return new Projector(_v0,_v1,_u0,_u1);
  }

  /**
   * Returns normalized coordinate u corresponding to world coordinate v.
   * @param v world coordinate v.
   * @return normalized coordinate u.
   */
  public double u(double v) {
    return _vshift+_vscale*v;
  }

  /**
   * Returns world coordinate v corresponding to normalized coordinate u.
   * @param u normalized coordinate u.
   * @return world coordinate v.
   */
  public double v(double u) {
    return _ushift+_uscale*u;
  }

  /**
   * Returns the u-coordinate bound closest to u=0.
   * @return the u-coordinate bound.
   */
  public double u0() {
    return _u0;
  }

  /**
   * Returns the u-coordinate bound closest to u=1.
   * @return the u-coordinate bound.
   */
  public double u1() {
    return _u1;
  }

  /**
   * Returns the v-coordinate bound closest to u=0.
   * @return the v-coordinate bound.
   */
  public double v0() {
    return _v0;
  }

  /**
   * Returns the v-coordinate bound closest to u=1.
   * @return the v-coordinate bound.
   */
  public double v1() {
    return _v1;
  }

  /**
   * Merges the specified projector into this projector. 
   * <p>
   *  to be If the sign of
   * v1-v0 for the specified projector is different from that for this 
   * projector, the sign for this projector is preserved.
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

    // Merged world coordinate bounds. Preserve sign of projector A.
    double vmin = min(min(v0a,v1a),min(v0b,v1b));
    double vmax = max(max(v0a,v1a),max(v0b,v1b));
    _v0 = (v0a<v1a)?vmin:vmax;
    _v1 = (v0a<v1a)?vmax:vmin;

    // Scale factors for projectors A and B. These scale factors are never 
    // greater than one. They represent the fractions of the merged span 
    // v1-v0 that are consumed by projectors A and B. Note that the scale
    // factor sa for projector A is always positive, because we preserve
    // sign of projector A. If the sign of scale factor sb for projector
    // B is negative, then we are flipping projector B in the merge.
    double sa = (v1a-v0a)/(_v1-_v0); 
    double sb = (v1b-v0b)/(_v1-_v0);

    // Scaled normalized coordinate bounds for A and B. We assume that the 
    // margins u0 and 1-u1 for projectors A and B scale just like their spans
    // v1-v0. If we are flipping projector B, then we must flip its margins.
    u0a = sa*u0a;
    u1a = 1.0-sa*(1.0-u1a);
    u0b = (sb>0.0)?sb*u0b:-sb*(1.0-u1b);
    u1b = (sb>0.0)?1.0-sb*(1.0-u1b):1.0+sb*u0b;

    // Merged normalized coordinate bounds.
    _u0 = max(u0a,u0b);
    _u1 = min(u1a,u1b);
    assert _u0<_u1 : "_u0<_u1";

    // Recompute shifts and scales.
    computeShiftsAndScales();
  }

  /**
   * Gets the scale factor for the specified projector relative to this one.
   * The scale factor is the ratio of the span v1-v0 of the specified 
   * projector to the span v1-v0 for this projector.
   * @return the scale factor.
   */
  public double getScaleFactor(Projector p) {
    return (p._v1-p._v0)/(_v1-_v0);
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
