/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Real array processing.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class Rap {

  public float[] copy(int n1, float[] rx) {
    return copy(n1,rx,new float[n1]);
  }
  public float[] copy(int n1, float[] rx, float[] ry) {
    for (int i1=0; i1<n1; ++i1)
      ry[i1] = rx[i1];
    return ry;
  }

  public float[] zero(int n1) {
    return new float[n1];
  }
  public float[] zero(int n1, float[] rx) {
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = 0.0f;
    return rx;
  }

  public float[] fill(float ra, int n1) {
    return fill(ra,n1,new float[n1]);
  }
  public float[] fill(float ra, int n1, float[] rx) {
    for (int i1=0; i1<n1; ++i1)
      rx[i1] = ra;
    return rx;
  }

  public float[] add(int n1, float[] rx, float[] ry) {
    return add(n1,rx,ry,new float[2*n1]);
  }
  public float[] add(int n1, float[] rx, float[] ry, float[] rz) {
    for (int i1=0; i1<n1; ++i1)
      rz[i1] = rx[i1]+ry[i1];
    return rz;
  }
}
