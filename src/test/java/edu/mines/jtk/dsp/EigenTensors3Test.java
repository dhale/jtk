/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import java.io.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.dump;

/**
 * Tests {@link edu.mines.jtk.dsp.EigenTensors3}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.06.09
 */
public class EigenTensors3Test extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(EigenTensors3Test.class);
    junit.textui.TestRunner.run(suite);
  }

  public static void testRandom() {
    testRandom(true,1.0,1.0e-3,1.0e-2);
    testRandom(false,0.1,1.0e-6,1.0e-3);
  }

  private static void testRandom(
    boolean compressed, 
    double errorAngle, double errorValue, double errorTensor) 
  {
    int n1 = 19, n2 = 20, n3 = 21;
    EigenTensors3 et = new EigenTensors3(n1,n2,n3,compressed);
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float[] a = makeRandomEigenvalues();
          float[] u = makeRandomEigenvector();
          float[] w = makeOrthogonalVector(u);
          et.setEigenvalues(i1,i2,i3,a);
          et.setEigenvectorU(i1,i2,i3,u);
          et.setEigenvectorW(i1,i2,i3,w);
          float[] c;
          c = et.getEigenvectorU(i1,i2,i3); checkEigenvectors(u,c,errorAngle);
          c = et.getEigenvectorW(i1,i2,i3); checkEigenvectors(w,c,errorAngle);
          c = et.getEigenvalues(i1,i2,i3); checkEigenvalues(a,c,errorValue);
          float[] t1 = et.getTensor(i1,i2,i3);
          et.setTensor(i1,i2,i3,t1);
          float[] t2 = et.getTensor(i1,i2,i3);
          checkTensors(t1,t2,errorTensor);
        }
      }
    }
  }

  public void testIO() throws IOException,ClassNotFoundException {

    // Make random eigen-tensors.
    int n1 = 3, n2 = 4, n3 = 5;
    EigenTensors3 et1 = new EigenTensors3(n1,n2,n3,false);
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          float[] a = makeRandomEigenvalues();
          float[] u = makeRandomEigenvector();
          float[] w = makeOrthogonalVector(u);
          et1.setEigenvalues(i1,i2,i3,a);
          et1.setEigenvectorU(i1,i2,i3,u);
          et1.setEigenvectorW(i1,i2,i3,w);
        }
      }
    }

    // Write and read them.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(et1);
    baos.close();
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    EigenTensors3 et2 = (EigenTensors3)ois.readObject();
    bais.close();

    // Compare; should be exactly equal.
    float[] a1 = new float[3];
    float[] u1 = new float[3];
    float[] w1 = new float[3];
    float[] a2 = new float[3];
    float[] u2 = new float[3];
    float[] w2 = new float[3];
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          et1.getEigenvalues(i1,i2,i3,a1);
          et1.getEigenvectorU(i1,i2,i3,u1);
          et1.getEigenvectorW(i1,i2,i3,w1);
          et2.getEigenvalues(i1,i2,i3,a2);
          et2.getEigenvectorU(i1,i2,i3,u2);
          et2.getEigenvectorW(i1,i2,i3,w2);
          assertEqual(a1,a2);
          assertEqual(u1,u2);
          assertEqual(w1,w2);
        }
      }
    }
  }
  private void assertEqual(float[] e, float[] a) {
    for (int i=0; i<e.length; ++i)
      assertEquals(e[i],a[i],0.0);
  }

  private static void checkEigenvalues(float[] a, float[] b, double e) {
    assertEquals(a[0],b[0],e);
    assertEquals(a[1],b[1],e);
    assertEquals(a[2],b[2],e);
  }

  private static void checkEigenvectors(float[] u, float[] v, double e) {
    float uv = Math.abs(u[0]*v[0]+u[1]*v[1]+u[2]*v[2]);
    double ce = 1.0-Math.cos(Math.toRadians(e));
    boolean ok = Math.abs(1.0-uv)<ce;
    if (!ok) {
      System.out.println("uv="+uv+" ce="+ce);
      System.out.println("expect:"); dump(u);
      System.out.println("actual:"); dump(v);
    }
    assertEquals(1.0,uv,ce);
  }

  private static void checkTensors(float[] s, float[] t, double e) {
    for (int i=0; i<6; ++i)
      assertEquals(s[i],t[i],e);
  }

  private static java.util.Random r = new java.util.Random();

  // Random eigenvalues.
  private static float[] makeRandomEigenvalues() {
    float a1 = r.nextFloat();
    float a2 = r.nextFloat();
    float a3 = r.nextFloat();
    float au = Math.max(Math.max(a1,a2),a3);
    float aw = Math.min(Math.min(a1,a2),a3);
    float av = a1+a2+a3-au-aw;
    return new float[]{au,av,aw};
  }

  // Random unit vector with non-negative 3rd component.
  private static float[] makeRandomEigenvector() {
    float a = r.nextFloat()-0.5f;
    float b = r.nextFloat()-0.5f;
    float c = r.nextFloat()-0.5f;
    if (c<0.0f) {
      a = -a;
      b = -b;
      c = -c;
    }
    float s = 1.0f/(float)Math.sqrt(a*a+b*b+c*c);
    return new float[]{a*s,b*s,c*s};
  }

  // Random unit vector orthogonal to specified vector.
  private static float[] makeOrthogonalVector(float[] v1) {
    float a1 = v1[0];
    float b1 = v1[1];
    float c1 = v1[2];
    float a2 = r.nextFloat()-0.5f;
    float b2 = r.nextFloat()-0.5f;
    float c2 = r.nextFloat()-0.5f;
    float d11 = a1*a1+b1*b1+c1*c1;
    float d12 = a1*a2+b1*b2+c1*c2;
    float s = d12/d11;
    float a = a2-s*a1;
    float b = b2-s*b1;
    float c = c2-s*c1;
    if (c<0.0f) {
      a = -a;
      b = -b;
      c = -c;
    }
    s = 1.0f/(float)Math.sqrt(a*a+b*b+c*c);
    return new float[]{a*s,b*s,c*s};
  }
}
