/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dsp;

import junit.framework.*;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.Fft}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.12.10
 */
public class FftTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(FftTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test1() {
    for (boolean complex:_complex) {
      for (boolean overwrite:_overwrite) {
        for (boolean center:_center) {
          for (int padding:_padding) {
            for (int n1:_count) {
              for (double d1:_delta) {
                for (double f1:_first) {
                  test1(complex,overwrite,center,padding,n1,d1,f1);
                }
              }
            }
          }
        }
      }
    }
  }

  public void test2() {
    for (boolean complex:_complex) {
      for (boolean overwrite:_overwrite) {
        for (boolean center1:_center) {
        for (boolean center2:_center) {
          for (int padding1:_padding) {
          for (int padding2:_padding) {
            for (int n1:_count) {
            for (int n2:_count) {
              for (double d1:_delta) {
              for (double d2:_delta) {
                for (double f1:_first) {
                for (double f2:_first) {
                  test2(complex,overwrite,
                        center1,center2,
                        padding1,padding2,
                        n1,d1,f1,
                        n2,d2,f2);
                }}
              }}
            }}
          }}
        }}
      }
    }
  }

  public void test3() {
    for (boolean complex:_complex) {
      for (boolean overwrite:_overwrite) {
        for (boolean center:_center) {
          for (int padding:_padding) {
            for (int n1:_count) {
            for (int n2:_count) {
            for (int n3:_count) {
              for (double d1:_delta) {
              for (double d2:_delta) {
              for (double d3:_delta) {
                for (double f1:_first) {
                for (double f2:_first) {
                for (double f3:_first) {
                  test3(complex,overwrite,
                        center,center,center,
                        padding,padding,padding,
                        n1,d1,f1,
                        n2,d2,f2,
                        n3,d3,f3);
                }}}
              }}}
            }}}
          }
        }
      }
    }
  }

  public void xxtest3() { // too long for routine testing
    for (boolean complex:_complex) {
      for (boolean overwrite:_overwrite) {
        for (boolean center1:_center) {
        for (boolean center2:_center) {
        for (boolean center3:_center) {
          for (int padding1:_padding) {
          for (int padding2:_padding) {
          for (int padding3:_padding) {
            for (int n1:_count) {
            for (int n2:_count) {
            for (int n3:_count) {
              for (double d1:_delta) {
              for (double d2:_delta) {
              for (double d3:_delta) {
                for (double f1:_first) {
                for (double f2:_first) {
                for (double f3:_first) {
                  test3(complex,overwrite,
                        center1,center2,center3,
                        padding1,padding2,padding3,
                        n1,d1,f1,
                        n2,d2,f2,
                        n3,d3,f3);
                }}}
              }}}
            }}}
          }}}
        }}}
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static boolean[] _complex = {true,false};
  private static boolean[] _overwrite = {true};
  private static boolean[] _center = {true,false};
  private static int[] _padding = {0,4};
  private static int[] _count = {0,4,5};
  private static double[] _delta = {1.0,2.0};
  private static double[] _first = {0.0,1.0};

  private void test1(
    boolean complex, boolean overwrite, 
    boolean center, int padding, 
    int n1, double d1, double f1)
  {
    if (n1<=0) return;
    Sampling s1 = new Sampling(n1,d1,f1);
    Fft fft = new Fft(s1);
    fft.setComplex(complex);
    fft.setOverwrite(overwrite);
    fft.setCenter(center);
    fft.setPadding(padding);
    float[] f = (complex)?crandfloat(n1):randfloat(n1);
    float[] g = fft.applyForward(f);
    float[] h = fft.applyInverse(g);
    if (complex)
      assertComplexEqual(n1,f,h);
    else
      assertRealEqual(n1,f,h);
  }
  private void test2(
    boolean complex, boolean overwrite,
    boolean center1, boolean center2,
    int padding1, int padding2,
    int n1, double d1, double f1,
    int n2, double d2, double f2)
  {
    if (n1<=0 || n2<=0) return;
    Sampling s1 = new Sampling(n1,d1,f1);
    Sampling s2 = new Sampling(n2,d2,f2);
    Fft fft = new Fft(s1,s2);
    fft.setComplex(complex);
    fft.setOverwrite(overwrite);
    fft.setCenter1(center1);
    fft.setCenter2(center2);
    fft.setPadding1(padding1);
    fft.setPadding2(padding2);
    float[][] f = (complex)?crandfloat(n1,n2):randfloat(n1,n2);
    float[][] g = fft.applyForward(f);
    float[][] h = fft.applyInverse(g);
    if (complex)
      assertComplexEqual(n1,n2,f,h);
    else
      assertRealEqual(n1,n2,f,h);
  }
  private void test3(
    boolean complex, boolean overwrite, 
    boolean center1, boolean center2, boolean center3,
    int padding1, int padding2, int padding3,
    int n1, double d1, double f1,
    int n2, double d2, double f2,
    int n3, double d3, double f3)
  {
    if (n1<=0 || n2<=0 || n3<=0) return;
    Sampling s1 = new Sampling(n1,d1,f1);
    Sampling s2 = new Sampling(n2,d2,f2);
    Sampling s3 = new Sampling(n3,d3,f3);
    Fft fft = new Fft(s1,s2,s3);
    fft.setComplex(complex);
    fft.setOverwrite(overwrite);
    fft.setCenter1(center1);
    fft.setCenter2(center2);
    fft.setCenter3(center3);
    fft.setPadding1(padding1);
    fft.setPadding2(padding2);
    fft.setPadding3(padding3);
    float[][][] f = (complex)?crandfloat(n1,n2,n3):randfloat(n1,n2,n3);
    float[][][] g = fft.applyForward(f);
    float[][][] h = fft.applyInverse(g);
    if (complex)
      assertComplexEqual(n1,n2,n3,f,h);
    else
      assertRealEqual(n1,n2,n3,f,h);
  }

  private static void assertRealEqual(int n1, float[] re, float[] ra) {
    float tolerance = (float)(n1)*FLT_EPSILON;
    for (int i1=0; i1<n1; ++i1)
      assertEquals(re[i1],ra[i1],tolerance);
  }
  private static void assertRealEqual(
    int n1, int n2, float[][] re, float[][] ra) 
  {
    float tolerance = (float)(n1+n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1)
        try {
          assertEquals(re[i2][i1],ra[i2][i1],tolerance);
        } catch (AssertionFailedError e) {
          System.out.println("index i1="+i1+" i2="+i2);
          throw e;
        }
    }
  }
  private static void assertRealEqual(
    int n1, int n2, int n3, float[][][] re, float[][][] ra) 
  {
    float tolerance = (float)(n1+n2+n3)*FLT_EPSILON;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          try {
            assertEquals(re[i3][i2][i1],ra[i3][i2][i1],tolerance);
          } catch (AssertionFailedError e) {
            System.out.println("index i1="+i1+" i2="+i2+" i3="+i3);
            throw e;
          }
        }
      }
    }
  }
  private static void assertComplexEqual(int n1, float[] ce, float[] ca) {
    float tolerance = (float)(n1)*FLT_EPSILON;
    for (int i1=0; i1<n1; ++i1) {
      assertEquals(ce[2*i1  ],ca[2*i1  ],tolerance);
      assertEquals(ce[2*i1+1],ca[2*i1+1],tolerance);
    }
  }
  private static void assertComplexEqual(
    int n1, int n2, float[][] ce, float[][] ca) 
  {
    float tolerance = (float)(n1+n2)*FLT_EPSILON;
    for (int i2=0; i2<n2; ++i2) {
      for (int i1=0; i1<n1; ++i1) {
        try {
          assertEquals(ce[i2][2*i1  ],ca[i2][2*i1  ],tolerance);
          assertEquals(ce[i2][2*i1+1],ca[i2][2*i1+1],tolerance);
        } catch (AssertionFailedError e) {
          System.out.println("index i1="+i1+" i2="+i2);
          throw e;
        }
      }
    }
  }
  private static void assertComplexEqual(
    int n1, int n2, int n3, float[][][] ce, float[][][] ca) 
  {
    float tolerance = (float)(n1+n2+n3)*FLT_EPSILON;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          try {
            assertEquals(ce[i3][i2][2*i1  ],ca[i3][i2][2*i1  ],tolerance);
            assertEquals(ce[i3][i2][2*i1+1],ca[i3][i2][2*i1+1],tolerance);
          } catch (AssertionFailedError e) {
            System.out.println("index i1="+i1+" i2="+i2+" i3="+i3);
            throw e;
          }
        }
      }
    }
  }
  private static void trace(String s) {
    System.out.println(s);
  }
}
