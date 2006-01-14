/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.io.test;

import junit.framework.*;
import java.io.IOException;
import edu.mines.jtk.io.DataFile;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Stopwatch;

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

  public void test() throws IOException {
    int n = 100000;
    float[] af = Array.randfloat(n);
    float[] bf = Array.zerofloat(n);
    double[] ad = Array.randdouble(n);
    double[] bd = Array.zerodouble(n);
    for (int itest=0; itest<4; ++itest) {
      DataFile.ByteOrder order = (itest%2==0) ?
        DataFile.ByteOrder.BIG_ENDIAN :
        DataFile.ByteOrder.LITTLE_ENDIAN;
      System.out.println("byte order="+order);
      DataFile df = new DataFile("junk.dat","rw",order);
      testFloat(df,af,bf);
      testDouble(df,ad,bd);
      df.close();
    }
  }

  private void testFloat(
    DataFile df, float[] a, float[] b) throws IOException 
  {
    int n = a.length;
    int nio;
    double rate;
    Stopwatch sw = new Stopwatch();

    sw.restart();
    for (nio=0; sw.time()<1.0; ++nio) {
      df.seek(0);
      df.writeFloats(a);
      df.seek(0);
      df.readFloats(b);
    }
    sw.stop();
    rate = 2.0*4.0*1.0e-6*n*nio/sw.time();
    System.out.println("testFloat: fast rate="+rate+" MB/s");
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    sw.restart();
    for (nio=0; sw.time()<1.0; ++nio) {
      df.seek(0);
      for (int i=0; i<n; ++i)
        df.writeFloat(a[i]);
      df.seek(0);
      for (int i=0; i<n; ++i)
        b[i] = df.readFloat();
    }
    rate = 2.0*4.0*1.0e-6*n*nio/sw.time();
    System.out.println("testFloat: slow rate="+rate+" MB/s");
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    df.seek(0);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],df.readFloat());

    df.seek(0);
    for (int i=0; i<n; ++i)
      df.writeFloat(a[i]);
    df.seek(0);
    df.readFloats(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);
  }

  private void testDouble(
    DataFile df, double[] a, double[] b) throws IOException 
  {
    int n = a.length;
    int nio;
    double rate;
    Stopwatch sw = new Stopwatch();

    sw.restart();
    for (nio=0; sw.time()<1.0; ++nio) {
      df.seek(0);
      df.writeDoubles(a);
      df.seek(0);
      df.readDoubles(b);
    }
    sw.stop();
    rate = 2.0*8.0*1.0e-6*n*nio/sw.time();
    System.out.println("testDouble: fast rate="+rate+" MB/s");
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    sw.restart();
    for (nio=0; sw.time()<1.0; ++nio) {
      df.seek(0);
      for (int i=0; i<n; ++i)
        df.writeDouble(a[i]);
      df.seek(0);
      for (int i=0; i<n; ++i)
        b[i] = df.readDouble();
    }
    rate = 2.0*8.0*1.0e-6*n*nio/sw.time();
    System.out.println("testDouble: slow rate="+rate+" MB/s");
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);

    df.seek(0);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],df.readDouble());

    df.seek(0);
    for (int i=0; i<n; ++i)
      df.writeDouble(a[i]);
    df.seek(0);
    df.readDoubles(b);
    for (int i=0; i<n; ++i)
      assertEquals(a[i],b[i]);
  }
}
