/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

import junit.framework.TestCase;

import edu.mines.jtk.io.ArrayFile;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark for {@link edu.mines.jtk.io.ArrayFile}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.17
 */
public class ArrayFileBench extends TestCase {
  public static void main(String[] args) {
    benchBigEndian();
    benchLittleEndian();
  }

  public static void benchBigEndian() {
    bench(ByteOrder.BIG_ENDIAN);
  }

  public static void benchLittleEndian() {
    bench(ByteOrder.LITTLE_ENDIAN);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void bench(ByteOrder order) {
    System.out.println("order="+order);
    int n = 1000000;
    File file = null;
    ArrayFile af = null;
    try {
      file = File.createTempFile("junk","dat");
      af = new ArrayFile(file,"rw",order,order);
      benchFloat(af,n);
      benchDouble(af,n);
      if (af!=null)
        af.close();
      if (file!=null)
        file.delete();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static void benchFloat(
    ArrayFile af, int n) throws IOException 
  {
    float[] a = Array.randfloat(n);
    float[] b = Array.zerofloat(n);
    int nio;
    Stopwatch sw = new Stopwatch();
    sw.start();
    for (nio=0; sw.time()<5.0; ++nio) {
      af.seek(0);
      af.writeFloats(a);
      af.seek(0);
      af.readFloats(b);
    }
    sw.stop();
    for (int i=0; i<n; ++i)
      if (a[i]!=b[i])
        throw new RuntimeException(" float: i/o failure");
    double time = sw.time();
    double rate = 2.0*4.0e-6*nio*n/time;
    System.out.println(" float: rate="+rate+" MB/s");
  }

  private static void benchDouble(
    ArrayFile af, int n) throws IOException 
  {
    double[] a = Array.randdouble(n);
    double[] b = Array.zerodouble(n);
    int nio;
    Stopwatch sw = new Stopwatch();
    sw.start();
    for (nio=0; sw.time()<5.0; ++nio) {
      af.seek(0);
      af.writeDoubles(a);
      af.seek(0);
      af.readDoubles(b);
    }
    sw.stop();
    for (int i=0; i<n; ++i)
      if (a[i]!=b[i])
        throw new RuntimeException("double: i/o failure");
    double time = sw.time();
    double rate = 2.0*8.0e-6*nio*n/time;
    System.out.println("double: rate="+rate+" MB/s");
  }
}
