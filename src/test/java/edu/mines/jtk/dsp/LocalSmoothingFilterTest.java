/****************************************************************************
Copyright 2012, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Random;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.LocalSmoothingFilter}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.09.23
 */
public class LocalSmoothingFilterTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LocalSmoothingFilterTest.class);
    junit.textui.TestRunner.run(suite);
  }

  ///////////////////////////////////////////////////////////////////////////
  // test

  public void testSpd2() {
    int n1 = 5;
    int n2 = 6;
    LocalSmoothingFilter lsf = new LocalSmoothingFilter(1.0e-6,1000);
    lsf.setPreconditioner(true);
    for (int iter=0; iter<10; ++iter) {
      float[][] s = randfloat(n1,n2);
      float[][] x = sub(randfloat(n1,n2),0.5f);
      float[][] y = sub(randfloat(n1,n2),0.5f);
      float[][] dx = zerofloat(n1,n2);
      float[][] dy = zerofloat(n1,n2);
      Tensors2 d = new RandomTensors2(n1,n2);
      float c = 10.0f*s[n2/2][n1/2];
      lsf.apply(d,c,s,x,dx);
      lsf.apply(d,c,s,y,dy);
      float xdx = dot(x,dx);
      float ydy = dot(y,dy);
      float ydx = dot(y,dx);
      float xdy = dot(x,dy);
      assertTrue(xdx>=0.0f);
      assertTrue(ydy>=0.0f);
      assertEquals(xdy,ydx,0.0001);
    }
  }

  private static float dot(float[][] x, float[][] y) {
    return sum(mul(x,y));
  }
  private static float dot(float[][][] x, float[][][] y) {
    return sum(mul(x,y));
  }

  private static class RandomTensors2 extends EigenTensors2 {
    RandomTensors2(int n1, int n2) {
      super(n1,n2);
      Random r = new Random();
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float a = 2.0f*FLT_PI*r.nextFloat();
          float u1 = cos(a);
          float u2 = sin(a);
          float du = 0.0001f+0.0009f*r.nextFloat();
          float dv = 0.0001f+0.9999f*r.nextFloat();
          setEigenvectorU(i1,i2,u1,u2);
          setEigenvalues(i1,i2,du,dv);
        }
      }
    }
  }
  private static class IdentityTensors2 implements Tensors2 {
    public void getTensor(int i1, int i2, float[] a) {
      a[0] = 1.0f;
      a[1] = 0.0f;
      a[2] = 1.0f;
    }
  }
  private static class IdentityTensors3 implements Tensors3 {
    public void getTensor(int i1, int i2, int i3, float[] a) {
      a[0] = 1.0f;
      a[1] = 0.0f;
      a[2] = 0.0f;
      a[3] = 1.0f;
      a[4] = 0.0f;
      a[5] = 1.0f;
    }
  }
}
