/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.interp.CubicInterpolator}.
 * @author Dave Hale, Zachary Pember, Colorado School of Mines
 * @version 2000.02.21, 2006.08.22
 */
public class CubicInterpolatorTest extends TestCase {
  
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(CubicInterpolatorTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testArrayMethods() {
    int nc = 10;
    int ni = 100;
    float[] xc = randfloat(nc);
    float[] yc = randfloat(nc);
    quickSort(xc);
    CubicInterpolator ci = new CubicInterpolator(xc,yc);
    float[] xi = randfloat(ni);
    float[] yi = zerofloat(ni);
    ci.interpolate(xi,yi);
    for (int i=0; i<ni; ++i)
      assertEqual(yi[i],ci.interpolate(xi[i]));
    ci.interpolate1(xi,yi);
    for (int i=0; i<ni; ++i)
      assertEqual(yi[i],ci.interpolate1(xi[i]));
    ci.interpolate2(xi,yi);
    for (int i=0; i<ni; ++i)
      assertEqual(yi[i],ci.interpolate2(xi[i]));
    ci.interpolate3(xi,yi);
    for (int i=0; i<ni; ++i)
      assertEqual(yi[i],ci.interpolate3(xi[i]));
  }


  public void testLinearAndSpline() {
    //create set of data points
    int npoints = 100;
    Random generator = new Random(100);
    float[] x = randfloat(generator, npoints);
    float[] y = randfloat(generator, npoints);
    quickSort(x);
    
    //construct interpolators
    CubicInterpolator.Method linear = CubicInterpolator.Method.LINEAR;
    CubicInterpolator.Method spline = CubicInterpolator.Method.SPLINE;
    CubicInterpolator cl = new CubicInterpolator(linear,npoints,x,y);
    CubicInterpolator cs = new CubicInterpolator(spline,npoints,x,y);
    
    //check interpolation between each pair of knots
    for(int i=0; i<npoints-1; i++){
      //test linear
      float xpos = 0.5f*(x[i]+x[i+1]);
      float xi = x[i]+0.2f*(x[i+1]-x[i]);   //=x0
      float yi0 = cl.interpolate(xi);       //=y(x0)
      float yi1 = cl.interpolate1(xi);      //=y'(x0)
      float xj = x[i+1]-0.2f*(x[i+1]-x[i]); //=x1
      float yj0 = cl.interpolate(xj);       //=y(x1)
      float yj1 = cl.interpolate1(xj);      //=y'(x1)
      float[] abcd = computeCoefficients(xi, yi0, yi1, xj, yj0, yj1); 
      assertEqual(deriv0(abcd, xpos-xi), cl.interpolate(xpos));
      assertEqual(deriv1(abcd, xpos-xi), cl.interpolate1(xpos));
      assertEqual(0.0f, cl.interpolate2(xpos));
      assertEqual(0.0f, cl.interpolate3(xpos));
      //test spline
      yi0 = cs.interpolate(xi);       //=y(x0)
      yi1 = cs.interpolate1(xi);      //=y'(x0)
      yj0 = cs.interpolate(xj);       //=y(x1)
      yj1 = cs.interpolate1(xj);      //=y'(x1)
      abcd = computeCoefficients(xi, yi0, yi1, xj, yj0, yj1); 
      assertEqual(deriv0(abcd, xpos-xi), cs.interpolate(xpos));
      assertEqual(deriv1(abcd, xpos-xi), cs.interpolate1(xpos));
      assertEqual(deriv2(abcd, xpos-xi), cs.interpolate2(xpos));
      assertEqual(deriv3(abcd, xpos-xi), cs.interpolate3(xpos));
    }
    //Check extrapolation at ends
    float xinterval = 0.5f;
    checkExtrapolation(linear, cl, npoints, xinterval, x);
    checkExtrapolation(spline, cs, npoints, xinterval, x);
  }
  
  public void testMonotonic() {
    //create set of monotonic data points
    int npoints = 100;
    Random generator = new Random(100);
    float[] x = randfloat(generator, npoints);
    float[] y = randfloat(generator, npoints);
    quickSort(x);
    quickSort(y);
    
    //Construct interpolator
    CubicInterpolator.Method monoto = CubicInterpolator.Method.MONOTONIC;
    CubicInterpolator cm = new CubicInterpolator(monoto,npoints,x,y);
    
    //check interpolation between each pair of knots
    for(int i=0; i<npoints-1; i++){
      float xpos = 0.5f*(x[i]+x[i+1]); 
      float xi = x[i];                  //=x0
      float yi0 = cm.interpolate(xi);   //=y(x0)
      float yi1 = cm.interpolate1(xi);  //=y'(x0)
      float xj = x[i+1];                //=x1
      float yj0 = cm.interpolate(xj);   //=y(x1)
      float yj1 = cm.interpolate1(xj);  //=y'(x1)
      float[] abcd = computeCoefficients(xi, yi0, yi1, xj, yj0, yj1);
      assertEqual(deriv0(abcd, xpos-xi), cm.interpolate(xpos));
      assertEqual(deriv1(abcd, xpos-xi), cm.interpolate1(xpos));
      assertEqual(deriv2(abcd, xpos-xi), cm.interpolate2(xpos));
      assertEqual(deriv3(abcd, xpos-xi), cm.interpolate3(xpos));
    }    
    //Check extrapolation at ends
    float xinterval = 0.5f;
    checkExtrapolation(monoto, cm, npoints, xinterval, x);

  }

  //checks extrapolation at ends
  private static void checkExtrapolation(CubicInterpolator.Method type, 
      CubicInterpolator ci, int npoints, float xinterval, float[] x){
    //extrapolate beyond first knot
    float xpos = x[0]-0.5f*xinterval;
    float xi = x[0]-xinterval;          //=x0
    float yi0 = ci.interpolate(xi);     //=y(x0)
    float yi1 = ci.interpolate1(xi);    //=y'(x0)
    float xj = x[0];                    //=x1
    float yj0 = ci.interpolate(xj);     //=y(x1)
    float yj1 = ci.interpolate1(xj);    //=y'(x1)
    float[] abcd = computeCoefficients(xi, yi0, yi1, xj, yj0, yj1);
    assertEqual(deriv0(abcd, xpos-xi), ci.interpolate(xpos));
    assertEqual(deriv1(abcd, xpos-xi), ci.interpolate1(xpos));
    if(type==CubicInterpolator.Method.LINEAR){
      assertEqual(0.0f, ci.interpolate2(xpos));
      assertEqual(0.0f, ci.interpolate3(xpos));
    }else{
      assertEqual(deriv2(abcd, xpos-xi), ci.interpolate2(xpos));
      assertEqual(deriv3(abcd, xpos-xi), ci.interpolate3(xpos));
    }  
    //extrapolate beyond last knot
    xpos = x[npoints-1]+0.5f*xinterval;
    xi = x[npoints-1];            //=x0 
    yi0 = ci.interpolate(xi);     //=y(x0)
    yi1 = ci.interpolate1(xi);    //=y'(x0)
    xj = x[npoints-1]+xinterval;  //=x1
    yj0 = ci.interpolate(xj);     //=y(x1)
    yj1 = ci.interpolate1(xj);    //=y'(x1)
    abcd = computeCoefficients(xi, yi0, yi1, xj, yj0, yj1);
    assertEqual(deriv0(abcd, xpos-xi), ci.interpolate(xpos));
    assertEqual(deriv1(abcd, xpos-xi), ci.interpolate1(xpos));
    if(type==CubicInterpolator.Method.LINEAR){
      assertEqual(0.0f, ci.interpolate2(xpos));
      assertEqual(0.0f, ci.interpolate3(xpos));
    }else{
      assertEqual(deriv2(abcd, xpos-xi), ci.interpolate2(xpos));
      assertEqual(deriv3(abcd, xpos-xi), ci.interpolate3(xpos));
    }  
  }

  //determines coefficients for cubic polynomial
  private static float[] computeCoefficients(
      float xi, float yi0, float yi1,
      float xj, float yj0, float yj1){  
    //y(x)= ax^3 + bx^2 + cx + d
    float dx = xj-xi;
    float dydx = (yj0-yi0)/dx;
    float a = (yi1+yj1-2.0f*dydx)/(dx*dx);
    float b = (3.0f*dydx-2.0f*yi1-yj1)/dx;
    float c = yi1;
    float d = yi0;
    return new float[] {a, b, c, d};
  }

  //calculates y(x)
  private static float deriv0(float[] abcd, float dx){
    return(((abcd[0]*dx+abcd[1])*dx+abcd[2])*dx+abcd[3]);
  }

  //calculates y'(x)
  private static float deriv1(float[] abcd, float dx){
    return ((3.0f*abcd[0]*dx+2.0f*abcd[1])*dx+abcd[2]); 
  }

  //calculates y''(x)
  private static float deriv2(float[] abcd, float dx){
    return (6.0f*abcd[0]*dx+2.0f*abcd[1]);
  }

  //calculates y'''(x)
  private static float deriv3(float[] abcd, float dx){
    return (6.0f*abcd[0]);
  }

  private static void assertEqual(float x, float y) {
    assertTrue(x+" = "+y,almostEqual(x,y));
  }
  
  private static boolean almostEqual(float x, float y) {
    float ax = abs(x);
    float ay = abs(y);
    return abs(x-y)<=0.001f*max(ax,ay);
  }

}
