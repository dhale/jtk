/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mesh.TriMesh;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Nearest neighbor gridding of scattered samples of 2D functions f(x1,x2).
 * Each gridded value is the value of the nearest known (scattered) sample.
 * This gridder can also compute distances to those nearest known samples.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.22
 */
public class NearestGridder2 implements Gridder2 {

  /**
   * Constructs a nearest neighbor gridder with specified known samples.
   * The specified arrays are copied; not referenced.
   * @param f array of known sample values f(x1,x2).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   */
  public NearestGridder2(float[] f, float[] x1, float[] x2) {
    setScattered(f,x1,x2);
  }

  /**
   * Computes nearest neighbor distances and values.
   * @param s1 sampling for coordinate x1.
   * @param s2 sampling for coordinate x2.
   * @param d array of distances to nearest known samples.
   * @param g array of nearest known sample values.
   */
  public void computeDistancesAndValues(
    Sampling s1, Sampling s2, float[][] d, float[][] g) 
  {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        TriMesh.Node node = _mesh.findNodeNearest(x1,x2);
        float d1 = x1-node.x();
        float d2 = x2-node.y();
        if (g!=null)
          g[i2][i1] = _f[node.index];
        if (d!=null) 
          d[i2][i1] = sqrt(d1*d1+d2*d2);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // interface Gridder2

  public void setScattered(float[] f, float[] x1, float[] x2) {
    _f = copy(f);
    _mesh = new TriMesh();
    int n = f.length;
    for (int i=0; i<n; ++i) {
      TriMesh.Node node = new TriMesh.Node(x1[i],x2[i]);
      node.index = i;
      boolean added = _mesh.addNode(node);
      Check.argument(added,"samples have unique coordinates (x1,x2)");
    }
  }

  public float[][] grid(Sampling s1, Sampling s2) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    float[][] g = new float[n2][n1];
    computeDistancesAndValues(s1,s2,null,g);
    return g;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float[] _f;
  private TriMesh _mesh;
}
