/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.*;
import static java.lang.Math.*;

/**
 * Cross-correlation of two arrays with overlapping Gaussian windows.
 * Given two input arrays f and g and a specified lag, this filter computes 
 * an output array r of local cross-correlation coefficients. The output 
 * coefficients are local in that each sample in the array r contains the 
 * cross-correlation of the arrays f and g multiplied by a Gaussian window
 * that is centered at that sample. In other words, for each sample in the
 * input arrays f and g, this filter computes an output local cross-correlation
 * coefficient r.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.22
 */
public class CorrelationFilter {

  /**
   * Construct a correlation filter with specified width.
   * @param sigma the correlation window width; must not be less than 1.
   */
  public CorrelationFilter(double sigma) {
    Check.argument(sigma>=1.0,"sigma>=1.0");
    _rgf = new RecursiveGaussianFilter(sigma*sqrt(2.0)/2.0);
    _sigma = sigma;
  }

  /**
   * Applies this correlation filter for the specified lag.
   * @param lag the lag.
   * @param f the 1st input array; can be the same as g.
   * @param g the 2nd input array; can be the same as f.
   * @param r the output array; cannot be the same as f or g.
   */
  public void apply(int lag, float[] f, float[] g, float[] r) {
    Check.argument(f!=r,"f!=r");
    Check.argument(g!=r,"g!=r");

    int n1 = f.length;
    int s1 = (lag>=0)?1:-1;
    int l1 = abs(lag);
    int l1f = s1*((l1+0)/2);
    int l1g = s1*((l1+1)/2);
    int l1m = s1*(l1%2);

    int i1min = max(l1f,-l1g);
    int i1max = n1-max(-l1f,l1g);
    float[] t = r;
    for (int i1=i1min; i1<i1max; ++i1) {
      t[i1] = f[i1-l1f]*g[i1+l1g];
    }

    float scale1 = (float)exp((-0.25*l1*l1)/(_sigma*_sigma));
    float scales = (float)(1.0/(sqrt(PI)*2*_sigma));
    float scale = 0.5f*scales*scale1;
    i1min = abs(l1g);
    i1max = n1-i1min;
    float[] h = new float[n1];
    for (int i1=i1min; i1<i1max; ++i1) {
      h[i1] = scale*(t[i1]+t[i1-l1m]);
    }

    _rgf.apply0(h,r);
  }

  public void apply(
    int lag1, int lag2, float[][] f, float[][] g, float[][] r) 
  {
    Check.argument(f!=r,"f!=r");
    Check.argument(g!=r,"g!=r");

    int n1 = f[0].length;
    int s1 = (lag1>=0)?1:-1;
    int l1 = abs(lag1);
    int l1f = s1*((l1+0)/2);
    int l1g = s1*((l1+1)/2);
    int l1m = s1*(l1%2);

    int n2 = f.length;
    int s2 = (lag2>=0)?1:-1;
    int l2 = abs(lag2);
    int l2f = s2*((l2+0)/2);
    int l2g = s2*((l2+1)/2);
    int l2m = s2*(l2%2);

    int i1min = max(l1f,-l1g);
    int i1max = n1-max(-l1f,l1g);
    int i2min = max(l2f,-l2g);
    int i2max = n2-max(-l2f,l2g);
    float[][] t = r;
    for (int i2=i2min; i2<i2max; ++i2) {
      for (int i1=i1min; i1<i1max; ++i1) {
        t[i2][i1] = f[i2-l2f][i1-l1f]*g[i2+l2g][i1+l1g];
      }
    }

    float scale1 = (float)exp((-0.25*l1*l1)/(_sigma*_sigma));
    float scale2 = (float)exp((-0.25*l2*l2)/(_sigma*_sigma));
    float scales = (float)(1.0/(PI*4*_sigma*_sigma));
    float scale = 0.25f*scales*scale1*scale2;
    i1min = abs(l1g);
    i1max = n1-i1min;
    i2min = abs(l2g);
    i2max = n2-i2min;
    float[][] h = new float[n2][n1];
    for (int i2=i2min; i2<i2max; ++i2) {
      for (int i1=i1min; i1<i1max; ++i1) {
        h[i2][i1] = scale*(t[i2    ][i1    ] +
                           t[i2    ][i1-l1m] + 
                           t[i2-l2m][i1    ] + 
                           t[i2-l2m][i1-l1m]);
      }
    }

    _rgf.apply00(h,r);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  RecursiveGaussianFilter _rgf;
  double _sigma;
}
