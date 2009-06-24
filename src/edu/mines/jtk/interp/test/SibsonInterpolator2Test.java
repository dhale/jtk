/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import static java.lang.Math.*;
import javax.swing.SwingUtilities;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.interp.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;

/**
 * Tests {@link edu.mines.jtk.mesh.SibsonInterpolator2}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.13
 */
public class SibsonInterpolator2Test extends TestCase {
  public static void main(String[] args) {
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

  public void testAll() {
    doSkinnyTriangle();
    doSimpleTriangle();
    doSimpleSquare();
    TestFunction tf;
    tf = TestFunction.makeSine();
    doScattered(tf);
    doUniform(tf);
    tf = TestFunction.makeLinear();
    doScattered(tf);
    doUniform(tf);
  }

  private void doScattered(TestFunction tf) {
    float[][] fx = tf.sampleScattered2(NS,XMIN,XMAX,XMIN,XMAX);
    doMethods(tf,fx);
  }

  private void doUniform(TestFunction tf) {
    float[][] fx = tf.sampleUniform2(NS,XMIN,XMAX,XMIN,XMAX);
    doMethods(tf,fx);
  }

  private void doSkinnyTriangle() {
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

  private void doSimpleTriangle() {
    float xmin = XMIN+0.35f*(XMAX-XMIN);
    float xmax = XMAX-0.35f*(XMAX-XMIN);
    float xavg = 0.5f*(xmin+xmax);
    float[] x1 = {xmin,xmax,xavg};
    float[] x2 = {xmin,xmin,xmax};
    float[] f =  {xmin+xmin,xmax+xmin,xavg+xmax};
    float[][] fx = {f,x1,x2};
    doMethods(null,fx);
  }

  private void doSimpleSquare() {
    float xmin = XMIN+0.35f*(XMAX-XMIN);
    float xmax = XMAX-0.35f*(XMAX-XMIN);
    float[] x1 = {xmin,xmax,xmin,xmax};
    float[] x2 = {xmin,xmin,xmax,xmax};
    float[] f =  {xmin+xmin,xmax+xmin,xmin+xmax,xmax+xmax};
    float[][] fx = {f,x1,x2};
    doMethods(null,fx);
  }

  private void doMethods(TestFunction tf, float[][] fx) {
    float[] f = fx[0], x1 = fx[1], x2 = fx[2];
    //SibsonInterpolator2.Method[] methods = {HL,BS,WS};
    SibsonInterpolator2.Method[] methods = {HL};
    for (int i=0; i<methods.length; ++i) {
      SibsonInterpolator2.Method method = methods[i];
      SibsonInterpolator2 si = new SibsonInterpolator2(method,f,x1,x2);
      si.setNullValue(1.0f);
      si.setBounds(SX,SX);
      //si.setGradientPower(1.0);
      double tmin = Double.MAX_VALUE;
      float[][] g = null;
      for (int iter=0; iter<3; ++iter) {
        Stopwatch sw = new Stopwatch();
        sw.start();
        g = si.interpolate(SX,SX);
        sw.stop();
        tmin = min(tmin,sw.time());
      }
      System.out.println("method="+method+" time="+tmin);
      plot(method.toString(),g,x1,x2);
    }
  }

  private static void plot(final String method,
    final float[][] g, final float[] x1, final float[] x2) 
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        System.out.println("min="+Array.min(g)+" max="+Array.max(g));
        SimplePlot sp = new SimplePlot();
        sp.setTitle(method);
        sp.setSize(700,745);
        PixelsView pv = sp.addPixels(SX,SX,g);
        pv.setColorModel(ColorMap.JET);
        pv.setInterpolation(PixelsView.Interpolation.LINEAR);
        //pv.setClips(0.0f,2.0f);
        PointsView px = sp.addPoints(x1,x2);
        px.setLineStyle(PointsView.Line.NONE);
        px.setMarkStyle(PointsView.Mark.FILLED_CIRCLE);
        px.setMarkSize(6);
      }
    });
  }
}
