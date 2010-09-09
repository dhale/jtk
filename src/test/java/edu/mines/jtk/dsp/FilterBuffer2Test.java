/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.FilterBuffer2}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.21
 */
public class FilterBuffer2Test extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(FilterBuffer2Test.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testLaplacian() {
    FilterBuffer2.Extrapolation[] exs = {
      FilterBuffer2.Extrapolation.ZERO_VALUE,
      FilterBuffer2.Extrapolation.ZERO_SLOPE,
    };
    for (FilterBuffer2.Extrapolation ex:exs) {
      float[][] xi = impulsesInput();
      float[][] xc = constantInput();
      float[][] xr = randomInput();
      float[][][] xs = {xi,xc,xr};
      for (int itest=0; itest<3; ++itest) {
        float[][] x1 = xs[itest];
        float[][] x2 = copy(x1);
        float[][] y1 = x1;
        float[][] y2 = x2;
        computeLaplacian1(ex,x1,y1);
        computeLaplacian2(ex,x2,y2);
        assertEquals(y1,y2);
      }
    }
  }

  private static float[][] impulsesInput() {
    int n1 = 5;
    int n2 = 5;
    float[][] x = zerofloat(n1,n2); 
    x[n2/2][n1/2] = 1.0f;
    x[0   ][   0] = 1.0f;
    x[0   ][n1-1] = 1.0f;
    x[n2-1][   0] = 1.0f;
    x[n2-1][n1-1] = 1.0f;
    return x;
  }

  private static float[][] constantInput() {
    int n1 = 5;
    int n2 = 5;
    return fillfloat(1.0f,n1,n2); 
  }

  private static float[][] randomInput() {
    int n1 = 5;
    int n2 = 5;
    return randfloat(n1,n2); 
  }

  private static void computeLaplacian1(
    FilterBuffer2.Extrapolation ex, float[][] x, float[][] y) 
  {
    int n1 = x[0].length;
    int n2 = x.length;
    FilterBuffer2 fbx = new FilterBuffer2(1,1,1,1,x);
    fbx.setExtrapolation(ex);
    for (int i2=0; i2<n2; ++i2) {
      float[] xm = fbx.get(i2-1);
      float[] x0 = fbx.get(i2  );
      float[] xp = fbx.get(i2+1);
      float[] y0 = y[i2];
      for (int i1=0,j1=1; i1<n1; ++i1,++j1)
        y0[i1] = xm[j1]+xp[j1]+x0[j1-1]+x0[j1+1]-4.0f*x0[j1];
    }
  }

  private static void computeLaplacian2(
    FilterBuffer2.Extrapolation ex, float[][] x, float[][] y) 
  {
    int n1 = x[0].length;
    int n2 = x.length;
    FilterBuffer2 fbx = new FilterBuffer2(1,1,1,1,x);
    FilterBuffer2 fby = new FilterBuffer2(1,1,1,1,y);
    fbx.setExtrapolation(ex);
    fby.setExtrapolation(ex);
    fbx.setMode(FilterBuffer2.Mode.INPUT);
    fby.setMode(FilterBuffer2.Mode.OUTPUT);
    for (int i2=0; i2<=n2; ++i2) {
      float[] xm = fbx.get(i2-1);
      float[] x0 = fbx.get(i2  );
      float[] ym = fby.get(i2-1);
      float[] y0 = fby.get(i2  );
      for (int i1=0,j1=1; i1<=n1; ++i1,++j1) {
        float d1 = x0[j1]-x0[j1-1];
        float d2 = x0[j1]-xm[j1  ];
        y0[j1  ] -= d1+d2;
        y0[j1-1] += d1;
        ym[j1  ] += d2;
      }
    }
    fby.flush();
  }

  private static void exampleBilaplacian() {
    int n1 = 5;
    int n2 = 5;
    float[][] x = zerofloat(n1,n2); x[n2/2][n1/2] = 1.0f;
    //float[][] x = fillfloat(1.0f,n1,n2);
    dump(x);
    float[][] y = x;
    FilterBuffer2 fbx = new FilterBuffer2(1,1,1,1,x);
    FilterBuffer2 fby = new FilterBuffer2(1,1,1,1,y);
    fbx.setExtrapolation(FilterBuffer2.Extrapolation.ZERO_SLOPE);
    //fby.setExtrapolation(FilterBuffer2.Extrapolation.ZERO_SLOPE);
    fbx.setMode(FilterBuffer2.Mode.INPUT);
    fby.setMode(FilterBuffer2.Mode.OUTPUT);
    for (int i2=0; i2<n2; ++i2) {
      float[] x2m = fbx.get(i2-1); // x[i2-1]
      float[] x20 = fbx.get(i2  ); // x[i2  ]
      float[] x2p = fbx.get(i2+1); // x[i2+1]
      float[] y2m = fby.get(i2-1); // y[i2-1]
      float[] y20 = fby.get(i2  ); // y[i2  ]
      float[] y2p = fby.get(i2+1); // y[i2+1]
      for (int i1=0,j1=1; i1<n1; ++i1,++j1) {
        float t = 0.0f;
        t += x2m[j1  ];
        t += x2p[j1  ];
        t += x20[j1-1];
        t += x20[j1+1];
        t -= x20[j1  ]*4.0f;
        y20[j1  ] -= t*4.0f;
        y20[j1+1] += t;
        y20[j1-1] += t;
        y2p[j1  ] += t;
        y2m[j1  ] += t;
      }
    }
    fby.flush();
    dump(y);
  }

  private static void trace(String s) {
    System.out.println(s);
  }

  private static final float TOLERANCE = 1000.0f*FLT_EPSILON;
  private static void assertEquals(float[] a, float[] b) {
    int n = a.length;
    for (int i=0; i<n; ++i) {
      assertEquals(a[i],b[i],TOLERANCE);
    }
  }
  private static void assertEquals(float[][] a, float[][] b) {
    int n = a.length;
    for (int i=0; i<n; ++i) {
      assertEquals(a[i],b[i]);
    }
  }
}
