/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
package edu.mines.jtk.bench;

import java.io.*;
import java.nio.ByteOrder;

import edu.mines.jtk.io.*;
import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark for {@link edu.mines.jtk.io.ArrayFile}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.01.17
 */
public class ArrayFileBench {
  public static void main(String[] args) {
    benchEndian();
    benchStream();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void benchStream() {
    try {
      boolean[] b = {false,true};
      for (boolean buffered:b) {
        File file = File.createTempFile("junk","dat");
        file.deleteOnExit();
        //File file = new File("junk.dat");
        benchStream(file,buffered);
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
  private static void benchStream(File file, boolean buffered) 
    throws IOException 
  {
    System.out.println("buffered="+buffered);
    Stopwatch sw = new Stopwatch();
    int nh = 60; // like a trace header
    int na = 1000; // like trace samples
    int[] h = randint(nh);
    float[] a = randfloat(na);
    double maxtime=60.0,s;
    double mbytes = 4.0e-6*(nh+na);
    long nw,nr,rate;
    FileOutputStream fos = new FileOutputStream(file);
    ArrayOutputStream aos = buffered ?
      new ArrayOutputStream(new BufferedOutputStream(fos)) :
      new ArrayOutputStream(fos);
    sw.restart();
    for (nw=0,s=0.0; sw.time()<maxtime; ++nw) {
      s += sum(a);
      aos.writeInts(h);
      aos.writeFloats(a);
    }
    aos.close();
    sw.stop();
    rate = (int)(mbytes*nw/sw.time());
    System.out.println("ArrayOutputStream: sum="+s+" nw="+nw+" rate="+rate);
    FileInputStream fis = new FileInputStream(file);
    ArrayInputStream ais = buffered ?
      new ArrayInputStream(new BufferedInputStream(fis)) :
      new ArrayInputStream(fis);
    sw.restart();
    for (nr=0,s=0.0; nr<nw; ++nr) {
      ais.readInts(h);
      ais.readFloats(a);
      s += sum(a);
    }
    ais.close();
    sw.stop();
    rate = (int)(mbytes*nr/sw.time());
    System.out.println(" ArrayInputStream: sum="+s+" nr="+nr+" rate="+rate);
  }

  private static void benchEndian() {
    benchBigEndian();
    benchLittleEndian();
  }

  private static void benchBigEndian() {
    bench(ByteOrder.BIG_ENDIAN);
  }

  private static void benchLittleEndian() {
    bench(ByteOrder.LITTLE_ENDIAN);
  }

  private static void bench(ByteOrder order) {
    System.out.println("order="+order);
    int n = 1000000;
    try {
      File file = File.createTempFile("junk","dat");
      file.deleteOnExit();
      ArrayFile af = new ArrayFile(file,"rw",order,order);
      benchFloat(af,n);
      benchDouble(af,n);
      af.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static void benchFloat(
    ArrayFile af, int n) throws IOException 
  {
    float[] a = randfloat(n);
    float[] b = zerofloat(n);
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
    double[] a = randdouble(n);
    double[] b = zerodouble(n);
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
