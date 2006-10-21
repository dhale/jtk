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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.io.DataFile;
import edu.mines.jtk.util.Array;

/**
 * Tests {@link edu.mines.jtk.io.DataFile}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.14
 */
public class DataFileTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(DataFileTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testBigEndian() throws IOException {
    test(DataFile.ByteOrder.BIG_ENDIAN);
  }

  public void testLittleEndian() throws IOException {
    test(DataFile.ByteOrder.LITTLE_ENDIAN);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void test(DataFile.ByteOrder order) throws IOException {
    int n = 10000;
    File file = null;
    DataFile df = null;
    try {
      file = File.createTempFile("junk","dat");
      df = new DataFile(file,"rw",order);
      testFloat(df,n);
      testDouble(df,n);
    } finally {
      if (df!=null)
        df.close();
      if (file!=null)
        file.delete();
    }
  }

  private static void testFloat(DataFile df, int n) throws IOException {
    float[] a = Array.randfloat(n);
    float[] b = Array.zerofloat(n);

    df.seek(0);
    df.writeFloats(a);
    df.seek(0);
    df.readFloats(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    Array.zero(b);
    df.seek(0);
    for (int i=0; i<n; ++i)
      df.writeFloat(a[i]);
    df.seek(0);
    for (int i=0; i<n; ++i)
      b[i] = df.readFloat();
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    df.seek(0);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],df.readFloat());

    df.seek(0);
    for (int i=0; i<n; ++i)
      df.writeFloat(a[i]);
    Array.zero(b);
    df.seek(0);
    df.readFloats(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    int mw = 3141;
    df.seek(0);
    for (int j=0; j<n; j+=mw)
      df.writeFloats(a,j,min(n-j,mw));
    Array.zero(b);
    df.seek(0);
    int mr = 2739;
    for (int j=0; j<n; j+=mr)
      df.readFloats(b,j,min(n-j,mr));
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);
  }

  private static void testDouble(DataFile df, int n) throws IOException {
    double[] a = Array.randdouble(n);
    double[] b = Array.zerodouble(n);

    df.seek(0);
    df.writeDoubles(a);
    df.seek(0);
    df.readDoubles(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    Array.zero(b);
    df.seek(0);
    for (int i=0; i<n; ++i)
      df.writeDouble(a[i]);
    df.seek(0);
    for (int i=0; i<n; ++i)
      b[i] = df.readDouble();
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    df.seek(0);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],df.readDouble());

    df.seek(0);
    for (int i=0; i<n; ++i)
      df.writeDouble(a[i]);
    Array.zero(b);
    df.seek(0);
    df.readDoubles(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    int mw = 3141;
    df.seek(0);
    for (int j=0; j<n; j+=mw)
      df.writeDoubles(a,j,min(n-j,mw));
    Array.zero(b);
    df.seek(0);
    int mr = 2739;
    for (int j=0; j<n; j+=mr)
      df.readDoubles(b,j,min(n-j,mr));
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);
  }
}
