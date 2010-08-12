/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Demonstrate roundoff error for typical computations with uniform sampling.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.08
 */
public class SamplingBench {

  public static void main(String[] args) {
    float f = 0.0f;
    float d = 1.0f/3.0f;
    int n = 10000;
    double errorMaximum = 0.0;
    float x = f;
    for (int i=0; i<n; ++i,x+=d) {
      double xd = f+i*d;
      errorMaximum = max(errorMaximum,abs(xd-x));
    }
    double errorEstimate = FLT_EPSILON*n*(abs(f)+abs(f+(n-1)*d))/2.0;
    System.out.println("error estimate ="+errorEstimate/d);
    System.out.println("error maximum  ="+errorMaximum/d);
  }
}
