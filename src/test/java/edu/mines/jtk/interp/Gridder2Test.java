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
 * Tests implementations of {@link edu.mines.jtk.interp.Gridder2}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.22
 */
public class Gridder2Test extends TestCase {

  public static void main(String[] args) {
    if (args.length>=1 && args[0].equals("demo")) {
      demo();
      return;
    }
    TestSuite suite = new TestSuite(Gridder2Test.class);
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

  ///////////////////////////////////////////////////////////////////////////
  // tests

  public void testNothing() {
    // just a placeholder 
  }

  ///////////////////////////////////////////////////////////////////////////
  // demos

  public static void demo() {
    TestFunction tfs = TestFunction.makeSine();
    TestFunction tfl = TestFunction.makeLinear();
    TestFunction tfq = TestFunction.makeSphericalQuadratic();
    //TestFunction[] tfa = {tfs,tfl,tfq};
    TestFunction[] tfa = {tfl,tfs};
    for (TestFunction tf:tfa) {
      doScattered(tf);
      doUniform(tf);
    }
  }
  private static void doScattered(TestFunction tf) {
    float[][] fx = tf.sampleScattered2(NS,XMIN,XMAX,XMIN,XMAX);
    doGridders(tf,fx);
  }

  private static void doUniform(TestFunction tf) {
    float[][] fx = tf.sampleUniform2(NS,XMIN,XMAX,XMIN,XMAX);
    doGridders(tf,fx);
  }

  private static void doGridders(TestFunction tf, float[][] fx) {
    float[] f = fx[0], x1 = fx[1], x2 = fx[2];
    float[][] fk = null;
    float fkmin = 0.0f, fkmax = 0.0f;
    if (tf!=null) {
      fk = tf.sampleUniform2(SX,SX);
      fkmin = min(fk);
      fkmax = max(fk);
      plot("known",fkmin,fkmax,fk,x1,x2);
    }
    SimpleGridder2 simple = new SimpleGridder2(f,x1,x2);
    NearestGridder2 nearest = new NearestGridder2(f,x1,x2);
    BlendedGridder2 blended = new BlendedGridder2(f,x1,x2);
    SibsonGridder2 sibson0 = new SibsonGridder2(f,x1,x2);
    SibsonGridder2 sibson1 = new SibsonGridder2(f,x1,x2);
    sibson1.setSmooth(true);
    Gridder2[] gridders = {simple,nearest,blended,sibson0,sibson1};
    for (Gridder2 gridder:gridders) {
      float[][] g = gridder.grid(SX,SX);
      plot(gridder.getClass().getName(),fkmin,fkmax,g,x1,x2);
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
