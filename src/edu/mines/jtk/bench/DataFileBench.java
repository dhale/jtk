/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import edu.mines.jtk.io.DataFile;
import edu.mines.jtk.util.Array;
import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark for {@link edu.mines.jtk.io.DataFile}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.17
 */
public class DataFileBench extends TestCase {
  public static void main(String[] args) {
    benchBigEndian();
    benchLittleEndian();
  }

  public static void benchBigEndian() {
    bench(DataFile.ByteOrder.BIG_ENDIAN);
  }

  public static void benchLittleEndian() {
    bench(DataFile.ByteOrder.LITTLE_ENDIAN);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void bench(DataFile.ByteOrder order) {
    System.out.println("order="+order);
    int n = 1000000;
    File file = null;
    DataFile df = null;
    try {
      file = File.createTempFile("junk","dat");
      df = new DataFile(file,"rw",order);
      benchFloat(df,n);
      benchDouble(df,n);
      if (df!=null)
        df.close();
      if (file!=null)
        file.delete();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static void benchFloat(DataFile df, int n) throws IOException {
    float[] a = Array.randfloat(n);
    float[] b = Array.zerofloat(n);
    int nio;
    Stopwatch sw = new Stopwatch();
    sw.start();
    for (nio=0; sw.time()<5.0; ++nio) {
      df.seek(0);
      df.writeFloats(a);
      df.seek(0);
      df.readFloats(b);
    }
    sw.stop();
    for (int i=0; i<n; ++i)
      if (a[i]!=b[i])
        throw new RuntimeException(" float: i/o failure");
    double time = sw.time();
    double rate = 2.0*4.0e-6*nio*n/time;
    System.out.println(" float: rate="+rate+" MB/s");
  }

  private static void benchDouble(DataFile df, int n) throws IOException {
    double[] a = Array.randdouble(n);
    double[] b = Array.zerodouble(n);
    int nio;
    Stopwatch sw = new Stopwatch();
    sw.start();
    for (nio=0; sw.time()<5.0; ++nio) {
      df.seek(0);
      df.writeDoubles(a);
      df.seek(0);
      df.readDoubles(b);
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
