/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Complex;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Complex array processing.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.21
 */
public class Cap {

  public float[] copy(int n1, float[] cx) {
    return copy(n1,cx,new float[2*n1]);
  }
  public float[] copy(int n1, float[] cx, float[] cy) {
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cy[ir] = cx[ir];
      cy[ii] = cx[ii];
    }
    return cy;
  }

  public float[] zero(int n1) {
    return new float[2*n1];
  }
  public float[] zero(int n1, float[] cx) {
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cx[ir] = 0.0f;
      cx[ii] = 0.0f;
    }
    return cx;
  }

  public float[] fill(Complex ca, int n1) {
    return fill(ca,n1,new float[2*n1]);
  }
  public float[] fill(Complex ca, int n1, float[] cx) {
    float ar = ca.r;
    float ai = ca.i;
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cx[ir] = ar;
      cx[ii] = ai;
    }
    return cx;
  }

  public float[] add(int n1, float[] cx, float[] cy) {
    return add(n1,cx,cy,new float[2*n1]);
  }
  public float[] add(int n1, float[] cx, float[] cy, float[] cz) {
    for (int ir=0,ii=1,nn=2*n1; ir<nn; ir+=2,ii+=2) {
      cz[ir] = cx[ir]+cy[ir];
      cz[ii] = cx[ii]+cy[ii];
    }
    return cz;
  }
}
