/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import static edu.mines.jtk.util.ArrayMath.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.dsp.LinearInterpolator}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2011.02.27
 */
public class LinearInterpolatorTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LinearInterpolatorTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testInterpolator1() {
    int nxu = 100;
    float dxu = 0.3f;
    float fxu = 3.3f;
    float xmin = fxu;
    float xmax = fxu+(nxu-1)*dxu;
    float[] yu = new float[nxu];
    for (int ixu=0; ixu<nxu; ++ixu)
      yu[ixu] = ramp(fxu+ixu*dxu);
    LinearInterpolator li = new LinearInterpolator();
    li.setExtrapolation(LinearInterpolator.Extrapolation.CONSTANT);
    li.setUniform(nxu,dxu,fxu,yu);
    int nx = 500;
    float dx = 1.02f*(xmax-xmin)/(nx-1);
    float fx = xmin-2.3f*dx;
    for (int ix=0; ix<nx; ++ix) {
      float x = fx+ix*dx;
      float yi = li.interpolate(x);
      float c = max(xmin,min(xmax,x));
      float yr = ramp(c);
      assertEquals(yi,yr,yr*0.00001f);
    }
  }

  public void testInterpolator2() {
    int nx1u = 100, nx2u = 200;
    float dx1u = 0.3f, dx2u = 0.4f;
    float fx1u = 3.3f, fx2u = 4.3f;
    float x1min = fx1u, x1max = fx1u+(nx1u-1)*dx1u;
    float x2min = fx2u, x2max = fx2u+(nx2u-1)*dx2u;
    float[][] yu = new float[nx2u][nx1u];
    for (int ix2u=0; ix2u<nx2u; ++ix2u)
      for (int ix1u=0; ix1u<nx1u; ++ix1u)
        yu[ix2u][ix1u] = ramp(fx1u+ix1u*dx1u,fx2u+ix2u*dx2u);
    LinearInterpolator li = new LinearInterpolator();
    li.setExtrapolation(LinearInterpolator.Extrapolation.CONSTANT);
    li.setUniform(nx1u,dx1u,fx1u,nx2u,dx2u,fx2u,yu);
    int nx1 = 300, nx2 = 400;
    float dx1 = 1.02f*(x1max-x1min)/(nx1-1);
    float dx2 = 1.01f*(x2max-x2min)/(nx2-1);
    float fx1 = x1min-3.9f*dx1, fx2 = x2min-4.1f*dx2;
    for (int ix2=0; ix2<nx2; ++ix2) {
      float x2 = fx2+ix2*dx2;
      float c2 = max(x2min,min(x2max,x2));
      for (int ix1=0; ix1<nx1; ++ix1) {
        float x1 = fx1+ix1*dx1;
        float yi = li.interpolate(x1,x2);
        float c1 = max(x1min,min(x1max,x1));
        float yr = ramp(c1,c2);
        assertEquals(yi,yr,yr*0.00001f);
      }
    }
  }

  public void testInterpolator3() {
    int nx1u = 110, nx2u = 220, nx3u = 330;
    float dx1u = 0.3f, dx2u = 0.4f, dx3u = 0.5f;
    float fx1u = 3.3f, fx2u = 4.3f, fx3u = 5.3f;
    float x1min = fx1u, x1max = fx1u+(nx1u-1)*dx1u;
    float x2min = fx2u, x2max = fx2u+(nx2u-1)*dx2u;
    float x3min = fx3u, x3max = fx3u+(nx3u-1)*dx3u;
    float[][][] yu = new float[nx3u][nx2u][nx1u];
    for (int ix3u=0; ix3u<nx3u; ++ix3u)
      for (int ix2u=0; ix2u<nx2u; ++ix2u)
        for (int ix1u=0; ix1u<nx1u; ++ix1u)
          yu[ix3u][ix2u][ix1u] = 
            ramp(fx1u+ix1u*dx1u,fx2u+ix2u*dx2u,fx3u+ix3u*dx3u);
    LinearInterpolator li = new LinearInterpolator();
    li.setExtrapolation(LinearInterpolator.Extrapolation.CONSTANT);
    li.setUniform(nx1u,dx1u,fx1u,nx2u,dx2u,fx2u,nx3u,dx3u,fx3u,yu);
    int nx1 = 30, nx2 = 40, nx3 = 20;
    float dx1 = 1.1f*(x1max-x1min)/(nx1-1);
    float dx2 = 1.2f*(x2max-x2min)/(nx2-1);
    float dx3 = 1.3f*(x3max-x3min)/(nx3-1);
    float fx1 = x1min-3.3f*dx1, fx2 = x2min-2.3f*dx2, fx3 = x3min-4.3f*dx3;
    for (int ix3=0; ix3<nx3; ++ix3) {
      float x3 = fx3+ix3*dx3;
      float c3 = max(x3min,min(x3max,x3));
      for (int ix2=0; ix2<nx2; ++ix2) {
        float x2 = fx2+ix2*dx2;
        float c2 = max(x2min,min(x2max,x2));
        for (int ix1=0; ix1<nx1; ++ix1) {
          float x1 = fx1+ix1*dx1;
          float c1 = max(x1min,min(x1max,x1));
          float yi = li.interpolate(x1,x2,x3);
          float yr = ramp(c1,c2,c3);
          assertEquals(yi,yr,yr*0.00001f);
        }
      }
    }
  }

  public void testInterpolator1Zero() {
    float[] yu = {1.0f};
    LinearInterpolator li = new LinearInterpolator();
    li.setExtrapolation(LinearInterpolator.Extrapolation.ZERO);
    li.setUniform(1,2.0,0.0,yu);
    assertEquals(0.0f,li.interpolate(-2.0),0.0);
    assertEquals(0.5f,li.interpolate(-1.0),0.0);
    assertEquals(1.0f,li.interpolate( 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 1.0),0.0);
    assertEquals(0.0f,li.interpolate( 2.0),0.0);
  }

  public void testInterpolator2Zero() {
    float[][] yu = {{1.0f}};
    LinearInterpolator li = new LinearInterpolator();
    li.setExtrapolation(LinearInterpolator.Extrapolation.ZERO);
    li.setUniform(1,2.0,0.0,1,1.0,0.0,yu);
    assertEquals(0.0f,li.interpolate(-2.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate(-1.0, 0.0),0.0);
    assertEquals(1.0f,li.interpolate( 0.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 1.0, 0.0),0.0);
    assertEquals(0.0f,li.interpolate( 2.0, 0.0),0.0);
    assertEquals(0.0f,li.interpolate( 0.0,-1.0),0.0);
    assertEquals(0.5f,li.interpolate( 0.0,-0.5),0.0);
    assertEquals(1.0f,li.interpolate( 0.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 0.0, 0.5),0.0);
    assertEquals(0.0f,li.interpolate( 0.0, 1.0),0.0);
  }

  public void testInterpolator3Zero() {
    float[][][] yu = {{{1.0f}}};
    LinearInterpolator li = new LinearInterpolator();
    li.setExtrapolation(LinearInterpolator.Extrapolation.ZERO);
    li.setUniform(1,2.0,0.0,1,1.0,0.0,1,4.0,0.0,yu);
    assertEquals(0.0f,li.interpolate(-2.0, 0.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate(-1.0, 0.0, 0.0),0.0);
    assertEquals(1.0f,li.interpolate( 0.0, 0.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 1.0, 0.0, 0.0),0.0);
    assertEquals(0.0f,li.interpolate( 2.0, 0.0, 0.0),0.0);
    assertEquals(0.0f,li.interpolate( 0.0,-1.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 0.0,-0.5, 0.0),0.0);
    assertEquals(1.0f,li.interpolate( 0.0, 0.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 0.0, 0.5, 0.0),0.0);
    assertEquals(0.0f,li.interpolate( 0.0, 1.0, 0.0),0.0);
    assertEquals(0.0f,li.interpolate( 0.0, 0.0,-4.0),0.0);
    assertEquals(0.5f,li.interpolate( 0.0, 0.0,-2.0),0.0);
    assertEquals(1.0f,li.interpolate( 0.0, 0.0, 0.0),0.0);
    assertEquals(0.5f,li.interpolate( 0.0, 0.0, 2.0),0.0);
    assertEquals(0.0f,li.interpolate( 0.0, 0.0, 4.0),0.0);
  }

  private static final float R0 = 3.0f;
  private static final float R1 = 4.0f;
  private static final float R2 = 5.0f;
  private static final float R3 = 6.0f;
  private static float ramp(float x1) {
    return R0+R1*x1;
  }
  private static float ramp(float x1, float x2) {
    return R0+R1*x1+R2*x2;
  }
  private static float ramp(float x1, float x2, float x3) {
    return R0+R1*x1+R2*x2+R3*x3;
  }

  private static void trace(String s) {
    //System.out.println(s);
  }
}
