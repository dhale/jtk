/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.ArrayList;

import edu.mines.jtk.util.Check;

/**
 * Volume of an n-dimensional convex polytope, via Lasserre's algorithm.
 * The n-dimensional (nD) polytope is specified as the intersection of 
 * at least n+1 half-spaces. A 1D polytope is a line segment defined by
 * two intersecting half-lines (or rays); a 2D polytope is a polygon 
 * defined by at least three intersecting half-planes, a 3D polytope is a
 * polyhedron defined by at least four intersecting half-spaces, and so on.
 * <p>
 * Each nD half-space is represented by an inequality of the form 
 * a1*x1 + a2*x2 + ... + an*xn &lt;= b. For example, in 2D, each 
 * half-space is defined by coefficients a1, a2 and b, and at least 
 * three such half-spaces are required to define a bounded polygon.
 * This class computes the area of such a polygon or, more generally,
 * the volume of an nD polytope, using Lasserre's recursive algorithm.
 * <p>
 * This implementation currently assumes that the half-planes for any 
 * redundant half-spaces are not parallel. This assumption is and (the 
 * computed volume) is valid if each specified half-plane represents 
 * part of the boundary of a strictly convex polytope. In particular, 
 * this assumption is valid for any Voronoi polytope.
 * <p>
 * See Lasserre J.B., 1983, An analytical expression and an algorithm
 * for the volume of a convex polyhedron in R^n: Journal of Optimization
 * Theory and Applications, 39, 363--377.
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.09
 */
public class LasserreVolume {

  /**
   * Constructs an initially unbounded (infinite) volume in n dimensions.
   * @param n the number of dimensions.
   */
  public LasserreVolume(int n) {
    _m = 0;
    _n = n;
    _abStack = new ArrayList<AbList>(_n);
    for (int i=1; i<=n; ++i) {
      _ab = new AbList(i);
      _abStack.add(_ab);
    }
  }

  /**
   * Adds a 1D half-space a1*x1 &lt;= b to bound this volume.
   * Other coefficients a2, a3, ..., an, if any, are assumed to be zero.
   * The dimension of this volume must be at least 2.
   * @param a1 the coefficient a1.
   * @param b the right-hand-side.
   */
  public void addHalfSpace(double a1, double b) {
    Check.state(_n>=1,"dimension >= 1");
    double[] ab = _ab.add();
    for (int i=1; i<_n; ++i) 
      ab[i] = 0.0;
    ab[0] = a1;
    ab[_n] = b;
    ++_m;
  }

  /**
   * Adds a 2D half-space a1*x1 + a2*x2 &lt;= b to bound this volume.
   * Other coefficients a3, a4, ..., an, if any, are assumed to be zero.
   * The dimension of this volume must be at least 2.
   * @param a1 the coefficient a1.
   * @param a2 the coefficient a2.
   * @param b the right-hand-side.
   */
  public void addHalfSpace(double a1, double a2, double b) {
    Check.state(_n>=2,"dimension >= 2");
    double[] ab = _ab.add();
    for (int i=2; i<_n; ++i) 
      ab[i] = 0.0;
    ab[0] = a1;
    ab[1] = a2;
    ab[_n] = b;
    ++_m;
  }

  /**
   * Adds a 3D half-space a1*x1 + a2*x2 + a3*x3 &lt;= b to bound this volume.
   * Other coefficients a4, a5, ..., an, if any, are assumed to be zero.
   * The dimension of this volume must be at least 3.
   * @param a1 the coefficient a1.
   * @param a2 the coefficient a2.
   * @param a3 the coefficient a3.
   * @param b the right-hand-side.
   */
  public void addHalfSpace(double a1, double a2, double a3, double b) {
    Check.state(_n>=3,"dimension >= 3");
    double[] ab = _ab.add();
    for (int i=3; i<_n; ++i) 
      ab[i] = 0.0;
    ab[0] = a1;
    ab[1] = a2;
    ab[2] = a3;
    ab[_n] = b;
    ++_m;
  }

  /**
   * Adds an nD half-space a1*x1 + a2*x2 + &hellip; + an*xn &lt;= b.
   * Any missing coefficients are assumed to be zero.
   * @param a array {a1,a2,...,an} of coefficients.
   * @param b the right-hand-side.
   */
  public void addHalfSpace(double[] a, double b) {
    int n = a.length;
    if (n>_n) n = _n;
    double[] ab = _ab.add();
    for (int i=n; i<_n; ++i) 
      ab[i] = 0.0;
    for (int i=0; i<n; ++i)
      ab[i] = a[i];
    ab[_n] = b;
    ++_m;
  }

  /**
   * Removes all half-spaces for this volume, making it infinite.
   */
  public void clear() {
    _ab.clear();
    _m = 0;
  }

  /**
   * Gets this volume.
   * @return the volume; may be infinite.
   */
  public double getVolume() {
    return volume(_m,_n);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // A list of coefficients in the system of half-spaces Ax <= b.
  // Each row of the system is represented by a double[] array of length n+1,
  // where n is the number of columns in the matrix A. In each such array of
  // n+1 doubles, elements with indices [0:n-1] are the coefficients of A, 
  // and the last element with index [n] is the right-hand-side coefficient b.
  private static class AbList extends ArrayList<double[]> {
    AbList(int n) {
      super(n+1);
      _m = 0;
      _n = n;
    }
    public double[] add() { // the caller fills in the returned array
      if (_m==size()) // construct a new double[] only when necessary
        add(new double[_n+1]);
      ++_m;
      return get(_m-1);
    }
    public void clear() { // override so that size() does not change
      _m = 0;
    }
    private int _m; // number of double[] added since last clear
    private int _n; // number of columns in matrix A
    void dump() { // for debugging
      System.out.println("m="+_m+" n="+_n);
      for (double[] ab:this) {
        for (double abi:ab)
          System.out.print(abi+" ");
        System.out.println();
      }
    }
  }

  private int _m; // number of half-spaces added since last clear
  private int _n; // dimension of polytope
  private AbList _ab; // coefficients a and b in specified system Ax <= b
  private ArrayList<AbList> _abStack; // stack of n lists for recursion;
                                      // _ab is the last one in the stack;
                                      // 1st one in stack is for 1D ax<=b

  private double volume(int m, int n) {

    // Special case, when volume cannot possibly be bounded.
    if (m<=n)
      return Double.POSITIVE_INFINITY;

    // List of coefficients in A and b for dimension n.
    AbList abList = _abStack.get(n-1);

    // For dimension 1, volume is simply length.
    if (n==1) {
      double xlower = Double.NEGATIVE_INFINITY; // lower bound
      double xupper = Double.POSITIVE_INFINITY; // upper bound
      for (int irow=0; irow<m; ++irow) {
        double[] arow = abList.get(irow);
        double aval = arow[0];
        double bval = arow[1];
        if (aval<0.0) {
          double x = bval/aval;
          if (x>xlower) xlower = x;
        } else if (aval>0.0) {
          double x = bval/aval;
          if (x<xupper) xupper = x;
        }
      }
      double len = xupper-xlower;
      if (len<0.0) len = 0.0;
      return len;
    }
    
    // Else, for dimension 2 or higher, ...
    else {
      double sum = 0.0;

      // For each row of the system, ...
      for (int irow=0; irow<m; ++irow) {
        double[] arow = abList.get(irow);
        double brow = arow[n]; // b is last coefficient in each row

        // If b is zero, then can skip this row; see scaling by b below.
        if (brow==0.0)
          continue;

        // Find the pivot, the element in this row with largest magnitude.
        int jpiv = 0;
        double amax = 0.0;
        for (int j=0; j<n; ++j) {
          double a = arow[j];
          if (a<0.0) a = -a;
          if (a>amax) {
            jpiv = j; // index of pivot
            amax = a; // absolute value of pivot
          }
        }

        // If zero pivot, skip this row. (What about pivots near zero?)
        if (amax==0.0)
          continue;

        // Build reduced system by eliminating the pivot row and column.
        AbList abNext = _abStack.get(n-2);
        abNext.clear();
        double spiv = 1.0/arow[jpiv];
        for (int i=0; i<m; ++i) {
          if (i==irow) continue;
          double[] ai = abList.get(i);
          double[] ak = abNext.add();
          for (int j=0,l=0; j<=n; ++j) {
            if (j==jpiv) continue;
            ak[l++] = ai[j]-spiv*ai[jpiv]*arow[j]; // reduced A and b
          }
        }

        // Recursively compute the volume of the reduced system.
        double vol = volume(m-1,n-1);

        // If infinite, the polytope is unbounded.
        if (vol==Double.POSITIVE_INFINITY)
          return vol;

        // Accumulate scaled volumes. (Note: arow[n] = b for this row.)
        sum += brow/amax*vol;
      }
      return sum/n;
    }
  }
}
