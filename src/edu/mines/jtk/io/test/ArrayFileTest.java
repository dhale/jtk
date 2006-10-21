/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io.test;

import static java.lang.Math.min;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.io.ArrayFile;
import edu.mines.jtk.util.Array;

/**
 * Tests {@link edu.mines.jtk.io.ArrayFile}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.14
 */
public class ArrayFileTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ArrayFileTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testBigEndian() throws IOException {
    test(ByteOrder.BIG_ENDIAN);
  }

  public void testLittleEndian() throws IOException {
    test(ByteOrder.LITTLE_ENDIAN);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void test(ByteOrder order) throws IOException {
    int n = 10000;
    File file = null;
    ArrayFile af = null;
    try {
      file = File.createTempFile("junk","dat");
      af = new ArrayFile(file,"rw",order,order);
      testFloat(af,n);
      testDouble(af,n);
    } finally {
      if (af!=null)
        af.close();
      if (file!=null)
        file.delete();
    }
  }

  private static void testFloat(ArrayFile af, int n) 
    throws IOException 
  {
    float[] a = Array.randfloat(n);
    float[] b = Array.zerofloat(n);

    af.seek(0);
    af.writeFloats(a);
    af.seek(0);
    af.readFloats(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    Array.zero(b);
    af.seek(0);
    for (int i=0; i<n; ++i)
      af.writeFloat(a[i]);
    af.seek(0);
    for (int i=0; i<n; ++i)
      b[i] = af.readFloat();
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    af.seek(0);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],af.readFloat());

    af.seek(0);
    for (int i=0; i<n; ++i)
      af.writeFloat(a[i]);
    Array.zero(b);
    af.seek(0);
    af.readFloats(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    int mw = 3141;
    af.seek(0);
    for (int j=0; j<n; j+=mw)
      af.writeFloats(a,j,min(n-j,mw));
    Array.zero(b);
    af.seek(0);
    int mr = 2739;
    for (int j=0; j<n; j+=mr)
      af.readFloats(b,j,min(n-j,mr));
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);
  }

  private static void testDouble(ArrayFile af, int n) 
    throws IOException 
  {
    double[] a = Array.randdouble(n);
    double[] b = Array.zerodouble(n);

    af.seek(0);
    af.writeDoubles(a);
    af.seek(0);
    af.readDoubles(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    Array.zero(b);
    af.seek(0);
    for (int i=0; i<n; ++i)
      af.writeDouble(a[i]);
    af.seek(0);
    for (int i=0; i<n; ++i)
      b[i] = af.readDouble();
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    af.seek(0);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],af.readDouble());

    af.seek(0);
    for (int i=0; i<n; ++i)
      af.writeDouble(a[i]);
    Array.zero(b);
    af.seek(0);
    af.readDoubles(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    int mw = 3141;
    af.seek(0);
    for (int j=0; j<n; j+=mw)
      af.writeDoubles(a,j,min(n-j,mw));
    Array.zero(b);
    af.seek(0);
    int mr = 2739;
    for (int j=0; j<n; j+=mr)
      af.readDoubles(b,j,min(n-j,mr));
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);
  }
}
