/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.logging.Logger;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mesh.TetMesh;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Nearest neighbor gridding of scattered samples of 3D functions f(x1,x2,x3).
 * Each gridded value is the value of the nearest known (scattered) sample.
 * This gridder can also compute distances to those nearest known samples.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.22
 */
public class NearestGridder3 implements Gridder3 {

  /**
   * Constructs a nearest neighbor gridder with specified known samples.
   * The specified arrays are copied; not referenced.
   * @param f array of known sample values f(x1,x2,x3).
   * @param x1 array of known sample x1 coordinates.
   * @param x2 array of known sample x2 coordinates.
   * @param x3 array of known sample x3 coordinates.
   */
  public NearestGridder3(float[] f, float[] x1, float[] x2, float[] x3) {
    setScattered(f,x1,x2,x3);
  }

  /**
   * Computes nearest neighbor distances and values.
   * @param s1 sampling for coordinate x1.
   * @param s2 sampling for coordinate x2.
   * @param s3 sampling for coordinate x3.
   * @param d array of distances to nearest known samples.
   * @param g array of nearest known sample values.
   */
  public void computeDistancesAndValues(
    Sampling s1, Sampling s2, Sampling s3, float[][][] d, float[][][] g) 
  {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    for (int i3=0; i3<n3; ++i3) {
      log.fine("computeDistancesAndValues: i3="+i3);
      float x3 = (float)s3.getValue(i3);
      for (int i2=0; i2<n2; ++i2) {
        log.finer("computeDistancesAndValues: i2="+i2);
        float x2 = (float)s2.getValue(i2);
        for (int i1=0; i1<n1; ++i1) {
          float x1 = (float)s1.getValue(i1);
          TetMesh.Node node = _mesh.findNodeNearest(x1,x2,x3);
          if (g!=null)
            g[i3][i2][i1] = _f[node.index];
          if (d!=null) {
            float d1 = x1-node.x();
            float d2 = x2-node.y();
            float d3 = x3-node.z();
            d[i3][i2][i1] = sqrt(d1*d1+d2*d2+d3*d3);
          }
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // interface Gridder2

  public void setScattered(float[] f, float[] x1, float[] x2, float[] x3) {
    _f = copy(f);
    _mesh = new TetMesh();
    int n = f.length;
    for (int i=0; i<n; ++i) {
      TetMesh.Node node = new TetMesh.Node(x1[i],x2[i],x3[i]);
      node.index = i;
      boolean added = _mesh.addNode(node);
      Check.argument(added,"samples have unique coordinates (x1,x2,x3)");
    }
  }

  public float[][][] grid(Sampling s1, Sampling s2, Sampling s3) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s3.getCount();
    float[][][] g = new float[n3][n2][n1];
    computeDistancesAndValues(s1,s2,s3,null,g);
    return g;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Logger log = 
    Logger.getLogger(NearestGridder3.class.getName());

  private float[] _f;
  private TetMesh _mesh;
}
