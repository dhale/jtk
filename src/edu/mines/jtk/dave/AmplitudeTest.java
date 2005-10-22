/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.gui.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;
import static edu.mines.jtk.mosaic.Mosaic.*;

/**
 * Statitistical test of different measures of relative amplitude.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.22
 */
public class AmplitudeTest {

  private static float[] makeSignal() {
    float flo = 0.021f;
    float fhi = 0.400f;
    int m = 25;
    KaiserWindow kw = KaiserWindow.fromWidthAndLength(flo,2*m);
    int n = 2*m+1;
    float[] s = new float[n];
    for (int i=0; i<n; ++i) {
      float t = (float)(i-m);
      float w = (float)kw.evaluate(t);
      float slo = flo*sinc(flo*t);
      float shi = fhi*sinc(fhi*t);
      s[i] = w*(shi-slo);
    }
    return s;
  }

  private static float sinc(float x) {
    return (x==0.0f)?1.0f:sin(FLT_PI*x)/(FLT_PI*x);
  }

  private static float amp1(float[] t) {
    int n = t.length;
    float s = 0.0f;
    for (int i=0; i<n; ++i)
      s += abs(t[i]);
    return s/(float)n;
  }

  private static float amp2(float[] t) {
    int n = t.length;
    float s = 0.0f;
    for (int i=0; i<n; ++i)
      s += t[i]*t[i];
    return sqrt(s/(float)n);
  }

  private static float ampm(float[] t) {
    int n = t.length;
    int[] imax = new int[1];
    Array.max(t,imax);
    _si.setInput(n,1.0,0.0,t);
    double xmax = _si.findMax(imax[0]);
    return _si.interpolate(xmax);
  }
  private static SincInterpolator _si = new SincInterpolator();

  private static void runTest() {
    float[] s = makeSignal();
    int nt = s.length;
    double dt = 1.0;
    double ft = -(nt/2)*dt;
    Sampling st = new Sampling(nt,dt,ft);
    float[] t1 = new float[nt];
    float[] t2 = new float[nt];
    Random random = new Random();
    float smax = Array.max(s);
    float sigmaNoise = 0.2f*smax;
    int n = 1000000;
    float[] r1 = new float[n];
    float[] r2 = new float[n];
    float[] rm = new float[n];
    for (int i=0; i<n; ++i) {
      for (int it=0; it<nt; ++it) {
        float n1 = sigmaNoise*(float)random.nextGaussian();
        float n2 = sigmaNoise*(float)random.nextGaussian();
        t1[it] = 1.0f*s[it]+n1;
        t2[it] = 2.0f*s[it]+n2;
      }
      if (i==0) {
        SequencePlot sp = new SequencePlot("s",st,s,
                                           "t1",st,t1,
                                           "t2",st,t2);
      }
      r1[i] = amp1(t2)/amp1(t1);
      r2[i] = amp2(t2)/amp2(t1);
      rm[i] = ampm(t2)/ampm(t1);
    }
    System.out.println("abs: mean="+mean(r1)+" dev="+dev(r1));
    System.out.println("rms: mean="+mean(r2)+" dev="+dev(r2));
    System.out.println("max: mean="+mean(rm)+" dev="+dev(rm));
    Histogram h1 = new Histogram(r1,100);
    Histogram h2 = new Histogram(r2,100);
    Histogram hm = new Histogram(rm,100);
    SequencePlot sp = new SequencePlot(
      "abs",h1.getBinSampling(),h1.getDensities(),
      "rms",h2.getBinSampling(),h2.getDensities(),
      "max",hm.getBinSampling(),hm.getDensities());
    sp.setAxisBottomLabel("amplitude ratio");
                     
  }

  private static float mean(float[] t) {
    int n = t.length;
    double s = 0.0;
    for (int i=0; i<n; ++i) {
      s += t[i];
    }
    s /= n;
    return (float)s;
  }

  private static float dev(float[] t) {
    int n = t.length;
    double s = 0.0;
    double ss = 0.0;
    for (int i=0; i<n; ++i) {
      s += t[i];
      ss += t[i]*t[i];
    }
    s /= n;
    ss /= n;
    return (float)sqrt(ss-s*s);
  }

  public static void main(String[] args) {
    runTest();
  }
}
