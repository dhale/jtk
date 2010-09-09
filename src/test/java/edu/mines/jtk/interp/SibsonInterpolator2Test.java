/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import javax.swing.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.mosaic.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.interp.SibsonInterpolator2}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.13
 */
public class SibsonInterpolator2Test extends TestCase {
  public static void main(String[] args) {
    if (args.length>=1 && args[0].equals("demo")) {
      demo();
      return;
    }
    TestSuite suite = new TestSuite(SibsonInterpolator2Test.class);
    junit.textui.TestRunner.run(suite);
  }

  // Bounds on sample coordinates x1 and x2.
  private static final float XMIN = 0.0f;
  private static final float XMAX = 1.0f;

  // Number of scattered samples to be interpolated.
  private static final int NS = 49;

  // Uniform sampling used in interpolation.
  private static final int NX = 201;
  private static final double DX = (XMAX-XMIN)/(NX-1);
  private static final double FX = XMIN;
  private static final Sampling SX = new Sampling(NX,DX,FX);

  // Implementation methods.
  private static SibsonInterpolator2.Method HL = 
    SibsonInterpolator2.Method.HALE_LIANG;
  private static SibsonInterpolator2.Method BS = 
    SibsonInterpolator2.Method.BRAUN_SAMBRIDGE;
  private static SibsonInterpolator2.Method WS = 
    SibsonInterpolator2.Method.WATSON_SAMBRIDGE;

  public void testSimpleTri() {
    testSimpleTri(HL);
    testSimpleTri(BS);
    testSimpleTri(WS);
  }
  private void testSimpleTri(SibsonInterpolator2.Method m) {
    float[] f  = {-2.0f, 0.0f, 1.0f};
    float[] x1 = {-1.0f, 1.0f, 0.0f};
    float[] x2 = {-1.0f,-1.0f, 1.0f};
    SibsonInterpolator2 si = new SibsonInterpolator2(m,f,x1,x2);
    assertValue(si,-0.5f,-0.5f,-1.0f);
    assertValue(si, 0.5f,-0.5f, 0.0f);
    assertValue(si, 0.0f, 0.5f, 0.5f);
    assertValue(si, 0.0f, 0.0f, 0.0f);
  }

  public void testSimpleSquare() {
    testSimpleSquare(HL);
    testSimpleSquare(BS);
    //testSimpleSquare(WS); // WS fails
  }
  private void testSimpleSquare(SibsonInterpolator2.Method m) {
    float[] f  = {-2.0f, 0.0f, 0.0f, 2.0f};
    float[] x1 = {-1.0f, 1.0f,-1.0f, 1.0f};
    float[] x2 = {-1.0f,-1.0f, 1.0f, 1.0f};
    SibsonInterpolator2 si = new SibsonInterpolator2(m,f,x1,x2);
    si.setNullValue(999.0f);
    assertValue(si, 0.0f, 0.0f, 0.0f);
    assertValue(si,-0.5f,-0.5f,-1.0f);
    assertValue(si, 0.5f,-0.5f, 0.0f);
    assertValue(si,-0.5f, 0.5f, 0.0f);
    assertValue(si, 0.5f, 0.5f, 1.0f);
  }

  public void testLinear() {
    testLinear(HL);
    testLinear(BS);
    //testLinear(WS); // WS too inaccurate
  }
  private void testLinear(SibsonInterpolator2.Method m) {
    TestFunction tf = TestFunction.makeLinear();
    float[][] fx = tf.sampleUniform2(NS,XMIN,XMAX,XMIN,XMAX);
    float[] f = fx[0], x1 = fx[1], x2 = fx[2];
    SibsonInterpolator2 si = new SibsonInterpolator2(m,f,x1,x2);
    si.setNullValue(999.0f);
    int n1 = NX, n2 = NX;
    Sampling s1 = SX, s2 = SX;
    float[][] g = si.interpolate(s1,s2);
    for (int i2=0; i2<n2; ++i2) {
      float x2i = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1i = (float)s1.getValue(i1);
        float fe = tf.f(x1i,x2i);
        assertEquals(fe,g[i2][i1]);
      }
    }
  }

  private static final double TOLERANCE = 1.0e-5;
  private void assertEquals(float e, float a) {
    assertEquals(e,a,TOLERANCE);
  }
  private void assertValue(
    SibsonInterpolator2 si, float x1, float x2, float f) 
  {
    float g = si.interpolate(x1,x2);
    assertEquals(f,g);
  }

  ///////////////////////////////////////////////////////////////////////////
  // demos
  public static void demo() {
    //doSkinnyTriangle();
    //doSimpleTriangle();
    //doSimpleSquare();
    TestFunction tfs = TestFunction.makeSine();
    TestFunction tfl = TestFunction.makeLinear();
    TestFunction tfq = TestFunction.makeSphericalQuadratic();
    TestFunction[] tfa = {tfs,tfl,tfq};
    for (TestFunction tf:tfa) {
      doScattered(tf);
      doUniform(tf);
    }
  }
  private static void doScattered(TestFunction tf) {
    float[][] fx = tf.sampleScattered2(NS,XMIN,XMAX,XMIN,XMAX);
    doMethods(tf,fx);
  }

  private static void doUniform(TestFunction tf) {
    float[][] fx = tf.sampleUniform2(NS,XMIN,XMAX,XMIN,XMAX);
    doMethods(tf,fx);
  }

  private static void doSkinnyTriangle() {
    float xmin = XMIN+0.25f*(XMAX-XMIN);
    float xmax = XMAX-0.25f*(XMAX-XMIN);
    float xmid = 0.5f*(xmin+xmax);
    float xtop = xmid+0.1f*(xmax-xmin);
    float[] x1 = {xmin,xmax,xmid};
    float[] x2 = {xmid,xmid,xtop};
    float[] f =  {1.0f,0.5f,1.0f};
    float[][] fx = {f,x1,x2};
    doMethods(null,fx);
  }

  private static void doSimpleTriangle() {
    float xmin = XMIN+0.35f*(XMAX-XMIN);
    float xmax = XMAX-0.35f*(XMAX-XMIN);
    float xavg = 0.5f*(xmin+xmax);
    float[] x1 = {xmin,xmax,xavg};
    float[] x2 = {xmin,xmin,xmax};
    float[] f =  {xmin+xmin,xmax+xmin,xavg+xmax};
    float[][] fx = {f,x1,x2};
    doMethods(null,fx);
  }

  private static void doSimpleSquare() {
    float xmin = XMIN+0.35f*(XMAX-XMIN);
    float xmax = XMAX-0.35f*(XMAX-XMIN);
    float[] x1 = {xmin,xmax,xmin,xmax};
    float[] x2 = {xmin,xmin,xmax,xmax};
    float[] f =  {xmin+xmin,xmax+xmin,xmin+xmax,xmax+xmax};
    float[][] fx = {f,x1,x2};
    doMethods(null,fx);
  }

  private static void doMethods(TestFunction tf, float[][] fx) {
    float[] f = fx[0], x1 = fx[1], x2 = fx[2];
    float[][] fk = null;
    float fkmin = 0.0f, fkmax = 0.0f;
    if (tf!=null) {
      fk = tf.sampleUniform2(SX,SX);
      fkmin = min(fk);
      fkmax = max(fk);
      plot("known",fkmin,fkmax,fk,x1,x2);
    }
    //SibsonInterpolator2.Method[] methods = {HL,BS,WS};
    SibsonInterpolator2.Method[] methods = {HL};
    for (SibsonInterpolator2.Method method:methods) {
      SibsonInterpolator2 si = new SibsonInterpolator2(method,f,x1,x2);
      si.setNullValue(1.0f);
      si.setBounds(SX,SX);
      si.setGradientPower(1.0);
      float[][] fi = si.interpolate(SX,SX);
      plot(method.toString()+" interpolated",fkmin,fkmax,fi,x1,x2);
      if (fk!=null) {
        float[][] fe = sub(fi,fk);
        plot(method.toString()+" interpolation error",0.0f,0.0f,fe,x1,x2);
      }
    }
  }

  private static void plot(final String method,
    final float cmin, final float cmax,
    final float[][] g, final float[] x1, final float[] x2) 
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        System.out.println("min="+ min(g)+" max="+ max(g));
        SimplePlot sp = new SimplePlot();
        sp.setTitle(method);
        sp.setSize(847,740);
        sp.addColorBar();
        sp.getPlotPanel().setColorBarWidthMinimum(100);
        PixelsView pv = sp.addPixels(SX,SX,g);
        pv.setColorModel(ColorMap.JET);
        pv.setInterpolation(PixelsView.Interpolation.LINEAR);
        if (cmin<cmax)
          pv.setClips(cmin,cmax);
        PointsView px = sp.addPoints(x1,x2);
        px.setLineStyle(PointsView.Line.NONE);
        px.setMarkStyle(PointsView.Mark.FILLED_CIRCLE);
        px.setMarkSize(6);
      }
    });
  }
}
